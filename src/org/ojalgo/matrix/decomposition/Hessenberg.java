/*
 * Copyright 1997-2015 Optimatika (www.optimatika.se)
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.ojalgo.matrix.decomposition;

import java.math.BigDecimal;

import org.ojalgo.access.Access2D;
import org.ojalgo.access.Structure2D;
import org.ojalgo.matrix.MatrixUtils;
import org.ojalgo.matrix.store.ElementsSupplier;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.scalar.ComplexNumber;

/**
 * Hessenberg: [A] = [Q][H][Q]<sup>T</sup> A general square matrix [A] can be decomposed by orthogonal
 * similarity transformations into the form [A]=[Q][H][Q]<sup>T</sup> where
 * <ul>
 * <li>[H] is upper (or lower) hessenberg matrix</li>
 * <li>[Q] is orthogonal/unitary</li>
 * </ul>
 *
 * @author apete
 */
public interface Hessenberg<N extends Number> extends MatrixDecomposition<N> {

    interface Factory<N extends Number> extends MatrixDecomposition.Factory<Hessenberg<N>> {

    }

    public static final Factory<BigDecimal> BIG = new Factory<BigDecimal>() {

        public Hessenberg<BigDecimal> make(final Structure2D typical) {
            return new HessenbergDecomposition.Big();
        }

    };

    public static final Factory<ComplexNumber> COMPLEX = new Factory<ComplexNumber>() {

        public Hessenberg<ComplexNumber> make(final Structure2D typical) {
            return new HessenbergDecomposition.Complex();
        }

    };

    public static final Factory<Double> PRIMITIVE = new Factory<Double>() {

        public Hessenberg<Double> make(final Structure2D typical) {
            return new HessenbergDecomposition.Primitive();
        }

    };

    @SuppressWarnings("unchecked")
    public static <N extends Number> Hessenberg<N> make(final Access2D<N> typical) {

        final N tmpNumber = typical.get(0, 0);

        if (tmpNumber instanceof BigDecimal) {
            return (Hessenberg<N>) BIG.make(typical);
        } else if (tmpNumber instanceof ComplexNumber) {
            return (Hessenberg<N>) COMPLEX.make(typical);
        } else if (tmpNumber instanceof Double) {
            return (Hessenberg<N>) PRIMITIVE.make(typical);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @deprecated v40 Use {@link #BIG}
     */
    @Deprecated
    public static Hessenberg<BigDecimal> makeBig() {
        return BIG.make();
    }

    /**
     * @deprecated v40 Use {@link #COMPLEX}
     */
    @Deprecated
    public static Hessenberg<ComplexNumber> makeComplex() {
        return COMPLEX.make();
    }

    /**
     * @deprecated v40 Use {@link #PRIMITIVE}
     */
    @Deprecated
    public static Hessenberg<Double> makePrimitive() {
        return PRIMITIVE.make();
    }

    boolean compute(ElementsSupplier<N> matrix, boolean upper);

    MatrixStore<N> getH();

    MatrixStore<N> getQ();

    boolean isUpper();

    default MatrixStore<N> reconstruct() {
        return MatrixUtils.reconstruct(this);
    }
}
