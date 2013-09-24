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
package org.keyboardplaying.tree.diff.model;

import java.util.Arrays;

// XXX JAVADOC
/**
 * @author cyChop (http://keyboardplaying.org/)
 */
public class Versions<T extends Comparable<T>> implements
		Comparable<Versions<T>> {
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

	@Override
	public int compareTo(Versions<T> o) {
		return getFirstNonNullVersion().compareTo(o.getFirstNonNullVersion());
	}

	@Override
	public int hashCode() {
		return versions.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Versions
				&& Arrays.equals(versions, ((Versions<?>) o).versions);
	}
}
