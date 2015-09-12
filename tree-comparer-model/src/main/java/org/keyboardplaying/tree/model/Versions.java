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

// XXX JAVADOC
/**
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 *
 * @param <T>
 *            the type of node
 */
public class Versions<T extends Comparable<T>> implements Comparable<Versions<T>>, Iterable<T> {
    private Object[] versions;

    public Versions(int nbVersions) {
        versions = new Object[nbVersions];
    }

    public void set(int idx, T version) {
        versions[idx] = version;
    }

    @SuppressWarnings("unchecked")
    public T get(int idx) {
        return (T) versions[idx];
    }

    @SuppressWarnings("unchecked")
    protected T getFirstNonNullVersion() {
        for (Object e : versions) {
            if (e != null) {
                return (T) e;
            }
        }
        return null;
    }

    public int getNbVersions() {
        return versions.length;
    }

    public boolean isConstant() {
        boolean constant = true;
        @SuppressWarnings("unchecked")
        T ref = (T) versions[0];
        for (int i = 1; i < versions.length; i++) {
            if (ref == null ? versions[i] != null : !ref.equals(versions[i])) {
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
    public int compareTo(Versions<T> o) {
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
        return Arrays.hashCode(versions);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Versions && Arrays.equals(versions, ((Versions<?>) o).versions);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return new VersionIterator<>(versions);
    }

    public static class VersionIterator<T> implements Iterator<T> {

        private int i = 0;
        private Object[] array;

        public VersionIterator(Object[] array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return i < array.length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            return (T) array[i++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
