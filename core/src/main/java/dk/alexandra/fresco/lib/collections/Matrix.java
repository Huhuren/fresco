/*
 * Copyright (c) 2015, 2016 FRESCO (http://github.com/aicis/fresco).
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
 */

package dk.alexandra.fresco.lib.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class Matrix<T> {

  private final int width;
  private final int height;
  private final ArrayList<ArrayList<T>> matrix;

  /**
   * Creates an matrix from a row building function.
   * 
   * @param height height of the matrix
   * @param width width of the matrix
   * @param rowBuilder the function for building rows
   */
  public Matrix(int height, int width, IntFunction<ArrayList<T>> rowBuilder) {
    this.width = width;
    this.matrix = new ArrayList<>(height);
    this.height = height;
    for (int i = 0; i < height; i++) {
      this.matrix.add(rowBuilder.apply(i));
    }
  }

  /**
   * Clones matrix.
   * 
   * @param other
   */
  public Matrix(Matrix<T> other) {
    this.width = other.getWidth();
    this.height = other.getHeight();
    this.matrix = other.getRows().stream().map(row -> new ArrayList<>(row))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Creates a matrix directly from an ArrayList of ArrayLists.
   * 
   * @param height height of the matrix
   * @param width width of the matrix
   * @param matrix the array data
   */
  public Matrix(int height, int width, ArrayList<ArrayList<T>> matrix) {
    this.width = width;
    this.height = height;
    this.matrix = matrix;
  }

  /**
   * Gets all rows of the matrix.
   * 
   * Convenience method for iterating over the rows of the matrix.
   * 
   * @return
   */
  public ArrayList<ArrayList<T>> getRows() {
    return this.matrix;
  }

  public ArrayList<T> getRow(int i) {
    return matrix.get(i);
  }

  public void setRow(int i, ArrayList<T> row) {
    matrix.set(i, row);
  }

  /**
   * Gets the width of the matrix.
   * 
   * @return the width of the matrix
   */
  public int getWidth() {
    return width;
  }


  /**
   * Gets the height of the matrix.
   * 
   * @return the height of the matrix
   */
  public int getHeight() {
    return height;
  }

  public List<T> getColumn(int i) {
    return this.matrix.stream().map(row -> row.get(i)).collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return "Matrix{" + "width=" + width + ", height=" + height + ", matrix=" + matrix + '}';
  }

}