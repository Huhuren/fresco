package dk.alexandra.fresco.suite.spdz2k.protocols.computations;

import dk.alexandra.fresco.commitment.HashBasedCommitment;
import dk.alexandra.fresco.framework.DRes;
import dk.alexandra.fresco.framework.builder.ComputationParallel;
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric;
import dk.alexandra.fresco.framework.network.serializers.ByteSerializer;
import dk.alexandra.fresco.framework.util.AesCtrDrbg;
import dk.alexandra.fresco.suite.spdz2k.protocols.natives.AllBroadcastProtocol;
import java.util.ArrayList;
import java.util.List;

public class Spdz2kCommitmentComputation implements
    ComputationParallel<List<byte[]>, ProtocolBuilderNumeric> {

  private final ByteSerializer<HashBasedCommitment> commitmentSerializer;
  private final byte[] value;
  private final int noOfParties;

  public Spdz2kCommitmentComputation(ByteSerializer<HashBasedCommitment> commitmentSerializer,
      byte[] value, int noOfParties) {
    this.commitmentSerializer = commitmentSerializer;
    this.value = value;
    this.noOfParties = noOfParties;
  }

  @Override
  public DRes<List<byte[]>> buildComputation(ProtocolBuilderNumeric builder) {
    HashBasedCommitment ownCommitment = new HashBasedCommitment();
    byte[] ownOpening = ownCommitment.commit(new AesCtrDrbg(), value);
    return builder.seq(new BroadcastComputation<>(
        commitmentSerializer.serialize(ownCommitment)
    )).seq((seq, rawCommitments) -> {
      DRes<List<byte[]>> openingsDRes = seq.append(new AllBroadcastProtocol<>(ownOpening));
      List<HashBasedCommitment> commitments = commitmentSerializer.deserializeList(rawCommitments);
      return () -> open(commitments, openingsDRes.out(), noOfParties);
    });
  }

  private List<byte[]> open(List<HashBasedCommitment> commitments, List<byte[]> openings,
      int noOfParties) {
    List<byte[]> result = new ArrayList<>(commitments.size());
    for (int i = 0; i < noOfParties; i++) {
      HashBasedCommitment commitment = commitments.get(i);
      byte[] opening = openings.get(i);
      result.add(commitment.open(opening));
    }
    return result;
  }

}