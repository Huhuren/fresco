/*******************************************************************************
 * Copyright (c) 2017 FRESCO (http://github.com/aicis/fresco).
 *
 * This file is part of the FRESCO project.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * FRESCO uses SCAPI - http://crypto.biu.ac.il/SCAPI, Crypto++, Miracl, NTL, and Bouncy Castle.
 * Please see these projects for any further licensing issues.
 *******************************************************************************/

package dk.alexandra.fresco.suite.dummy.arithmetic;

import dk.alexandra.fresco.framework.Computation;
import dk.alexandra.fresco.framework.network.SCENetwork;
import dk.alexandra.fresco.framework.value.SInt;
import java.math.BigInteger;

public class DummyArithmeticSubtractProtocol extends DummyArithmeticNativeProtocol<SInt> {

  private Computation<SInt> left;
  private Computation<SInt> right;
  private DummyArithmeticSInt out;

  /**
   * Constructs a native subtraction protocol for the Dummy Arithmetic suite.
   * 
   * <p>
   * Lets the caller specify where to store the output. This is for backward compatibility.
   * </p>
   * 
   * @param left the left operand
   * @param right the right operand
   * @param out the {@link SInt} in which to store the output
   */
  public DummyArithmeticSubtractProtocol(Computation<SInt> left, Computation<SInt> right,
      SInt out) {
    super();
    this.left = left;
    this.right = right;
    this.out = (DummyArithmeticSInt) out;
  }

  /**
   * Constructs a native subtraction protocol for the Dummy Arithmetic suite.
   * 
   * @param left the left operand
   * @param right the right operand
   * 
   */
  public DummyArithmeticSubtractProtocol(Computation<SInt> left, Computation<SInt> right) {
    super();
    this.left = left;
    this.right = right;
    this.out = null;
  }

  @Override
  public EvaluationStatus evaluate(int round, DummyArithmeticResourcePool rp, SCENetwork network) {
    BigInteger l = ((DummyArithmeticSInt) left.out()).getValue();
    BigInteger r = ((DummyArithmeticSInt) right.out()).getValue();
    BigInteger sub = l.subtract(r).mod(rp.getModulus());
    out = (out == null) ? new DummyArithmeticSInt() : out;
    out.setValue(sub);
    return EvaluationStatus.IS_DONE;
  }

  @Override
  public SInt out() {
    return out;
  }
}
