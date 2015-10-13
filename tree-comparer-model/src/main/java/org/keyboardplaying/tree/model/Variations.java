/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.keyboardplaying.tree.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An object containing the different versions for a given node in the compared versions.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 *
 * @param <T>
 *            the type of node
 */
public class Variations<T extends Comparable<T>> implements Comparable<Variations<T>>, Iterable<T> {

    private T[] array;

    /**
     * Creates a new instance.
     *
     * @param nbVersions
     *            the number of versions being compared
     */
    @SuppressWarnings("unchecked")
    public Variations(int nbVersions) {
        this.array = (T[]) new Comparable[nbVersions];
    }

    /**
     * Sets a variation.
     *
     * @param id
     *            the compared version number
     * @param variation
     *            the corresponding variation
     */
    public void set(int id, T variation) {
        array[id] = variation;
    }

    /**
     * Returns a variation.
     *
     * @param id
     *            the compared version number
     * @return the corresponding variation
     */
    public T get(int id) {
        return array[id];
    }

    protected T getFirstNonNullVersion() {
        for (T e : array) {
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    /**
     * Returns the number of versions being compared for this set of variations.
     *
     * @return the number of versions
     */
    public int getNbVersions() {
        return array.length;
    }

    /**
     * Returns {@code true} if all variations are equal (using the {@link Object#equals(Object)} method).
     *
     * @return {@code true} if all variations are equal, {@code false} otherwise
     */
    public boolean isConstant() {
        boolean constant = true;
        T ref = array[0];
        for (int i = 1; i < array.length; i++) {
            if (ref == null ? array[i] != null : !ref.equals(array[i])) {
                constant = false;
                break;
            }
        }
        return constant;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Variations<T> o) {
        T tnnv = getFirstNonNullVersion();
        T onnv = o.getFirstNonNullVersion();

        int result;
        if (tnnv == null) {
            result = onnv == null ? 0 : -1;
        } else if (onnv == null) {
            result = 1;
        } else {
            result = tnnv.compareTo(onnv);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Variations && Arrays.equals(array, ((Variations<?>) o).array);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return new VariationsIterator<>(array);
    }

    /**
     * An {@link Iterator} implementation for {@link Variations}.
     *
     * @author Cyrille Chopelet (http://keyboardplaying.org)
     *
     * @param <T>
     *            the type of node
     */
    static class VariationsIterator<T> implements Iterator<T> {

        private int i = 0;
        private T[] array;

        public VariationsIterator(T[] array) {
            this.array = array;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return i < array.length;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.util.Iterator#next()
         */
        @Override
        public T next() {
            if (hasNext()) {
                return array[i++];
            }
            throw new NoSuchElementException();
        }

        /*
         * (non-Javadoc)
         *
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
