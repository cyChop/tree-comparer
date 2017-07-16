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
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An object containing the different versions for a given node in the compared versions.
 *
 * @param <T> the type of node
 * @author Cyrille Chopelet (https://keyboardplaying.org)
 */
public class Variations<T> implements Iterable<T> {

    private T[] array;
    private int index = 0;

    /**
     * Creates a new instance.
     *
     * @param size the number of variations being compared
     */
    @SuppressWarnings("unchecked")
    public Variations(int size) {
        this.array = (T[]) new Comparable[size];
    }

    /**
     * Creates a new instance.
     *
     * @param variations the variations
     */
    @SuppressWarnings("unchecked")
    public Variations(List<T> variations) {
        this.array = (T[]) variations.toArray();
    }

    /**
     * Sets the next variation.
     *
     * @param variation the variation to set
     */
    public void add(T variation) {
        if (index == array.length) {
            throw new IndexOutOfBoundsException("No new version could be added.");
        }
        this.set(index++, variation);
    }

    /**
     * Sets a variation.
     * <p/>
     * This does not move the index for {@link #add(Object)}.
     *
     * @param id        the compared version number
     * @param variation the corresponding variation
     */
    public void set(int id, T variation) {
        array[id] = variation;
    }

    /**
     * Returns a variation.
     *
     * @param id the compared version number
     * @return the corresponding variation
     */
    public T get(int id) {
        return array[id];
    }

    /**
     * Returns the number of versions being compared for this set of variations.
     *
     * @return the number of versions
     */
    public int size() {
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

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Variations" + Arrays.toString(array);
    }

    /**
     * An {@link Iterator} implementation for {@link Variations}.
     *
     * @param <T> the type of node
     * @author Cyrille Chopelet (https://keyboardplaying.org)
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
