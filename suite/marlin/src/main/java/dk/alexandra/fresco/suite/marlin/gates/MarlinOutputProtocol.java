package dk.alexandra.fresco.suite.marlin.gates;

import dk.alexandra.fresco.framework.DRes;
import dk.alexandra.fresco.framework.network.Network;
import dk.alexandra.fresco.framework.util.ByteAndBitConverter;
import dk.alexandra.fresco.framework.value.SInt;
import dk.alexandra.fresco.suite.marlin.datatypes.BigUInt;
import dk.alexandra.fresco.suite.marlin.datatypes.BigUIntFactory;
import dk.alexandra.fresco.suite.marlin.datatypes.MarlinSInt;
import dk.alexandra.fresco.suite.marlin.resource.MarlinResourcePool;
import dk.alexandra.fresco.suite.marlin.resource.storage.MarlinOpenedValueStore;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class MarlinOutputProtocol<T extends BigUInt<T>> extends
    MarlinNativeProtocol<BigInteger, T> {

  private final DRes<SInt> share;
  private BigInteger opened;

  public MarlinOutputProtocol(DRes<SInt> share) {
    this.share = share;
  }

  @Override
  public EvaluationStatus evaluate(int round, MarlinResourcePool<T> resourcePool,
      Network network) {
    MarlinOpenedValueStore<T> openedValueStore = resourcePool.getOpenedValueStore();
    BigUIntFactory<T> factory = resourcePool.getFactory();
    if (round == 0) {
      MarlinSInt<T> out = (MarlinSInt<T>) share.out();
      // TODO clean up--only sending lower k bits
      network.sendToAll(ByteAndBitConverter.toByteArray(out.getShare().getLow()));
      return EvaluationStatus.HAS_MORE_ROUNDS;
    } else {
      // TODO probably want a serializer
      List<T> shares = network.receiveFromAll().stream().map(factory::createFromBytes).collect(
          Collectors.toList());
      // TODO make sure that arithmetic before storing is still mod 2^k
      T openedNotConverted = BigUInt.sum(shares);
      openedValueStore.pushOpenedValue(
          (MarlinSInt<T>) share.out(),
          openedNotConverted);
      this.opened = resourcePool
          .convertRepresentation(BigInteger.valueOf(openedNotConverted.getLow()));
      return EvaluationStatus.IS_DONE;
    }
  }

  @Override
  public BigInteger out() {
    return opened;
  }

}