/*
 * Copyright (c) 2015, 2016 FRESCO (http://github.com/aicis/fresco).
 *
 * This file is part of the FRESCO project.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * FRESCO uses SCAPI - http://crypto.biu.ac.il/SCAPI, Crypto++, Miracl, NTL,
 * and Bouncy Castle. Please see these projects for any further licensing issues.
 */
package dk.alexandra.fresco.lib.math.integer.exp;

import dk.alexandra.fresco.framework.Computation;
import dk.alexandra.fresco.framework.builder.ComputationBuilder;
import dk.alexandra.fresco.framework.builder.numeric.NumericBuilder;
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric;
import dk.alexandra.fresco.framework.value.SInt;
import java.math.BigInteger;

public class Exponentiation implements ComputationBuilder<SInt, ProtocolBuilderNumeric> {

  private final Computation<SInt> input;
  private final Computation<SInt> exponent;
  private final int maxExponentBitLength;

  public Exponentiation(Computation<SInt> input, Computation<SInt> exponent,
      int maxExponentBitLength) {
    this.input = input;
    this.exponent = exponent;
    this.maxExponentBitLength = maxExponentBitLength;
  }

  @Override
  public Computation<SInt> build(ProtocolBuilderNumeric builder) {
    return builder.seq((seq) ->
        seq.advancedNumeric().toBits(exponent, maxExponentBitLength)
    ).seq((bits, seq) -> {
      Computation<SInt> e = input;
      Computation<SInt> result = seq.numeric().known(BigInteger.valueOf(1));
      NumericBuilder numeric = seq.numeric();
      for (SInt bit : bits) {
        /*
         * result += bits[i] * (result * r - r) + r
				 *
				 *  aka.
				 *
				 *            result       if bits[i] = 0
				 * result = {
				 *            result * e   if bits[i] = 1
				 */
        result = numeric
            .add(numeric.mult(() -> bit, numeric.sub(numeric.mult(result, e), result)), result);
        e = numeric.mult(e, e);
      }
      return result;
    });
  }
}