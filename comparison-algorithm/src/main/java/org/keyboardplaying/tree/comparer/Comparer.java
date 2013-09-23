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
package org.keyboardplaying.tree.comparer;

import java.util.ArrayList;
import java.util.List;

import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Tree;
import org.keyboardplaying.tree.model.Versions;

// XXX JAVADOC
/**
 * @author cyChop (http://keyboardplaying.org/)
 */
public class Comparer {

	public <R extends Comparable<R>, T extends Comparable<T>> Tree<Versions<R>, Versions<T>> compare(
			Tree<R, T>... trees) {
		int nbVersions = trees.length;

		if (nbVersions < 2) {
			throw new IllegalArgumentException(
					"The comparator works if there are at least two trees to compare.");
		}

		Versions<R> roots = new Versions<R>(nbVersions);
		Versions<T> rootVersions = new Versions<T>(nbVersions);
		@SuppressWarnings("unchecked")
		List<Node<T>>[] children = new List[nbVersions];

		for (int i = 0; i < nbVersions; i++) {
			roots.setVersion(i, trees[i].getRootInfo());
			rootVersions.setVersion(i, trees[i].getRoot().getNodeInfo());
			children[i] = trees[i].getRoot().getChildren();
		}

		Node<Versions<T>> rootNode = new Node<Versions<T>>(rootVersions);
		addChildrenVersions(rootNode, children);

		return new Tree<Versions<R>, Versions<T>>(roots, rootNode);
	}

	private <T extends Comparable<T>> void addChildrenVersions(
			Node<Versions<T>> rootNode, List<Node<T>>[] children) {
		int nbVersions = children.length;

		assert rootNode.getNodeInfo().getNbVersions() == nbVersions;

		/* Init indices. */
		boolean iterate = false;
		int[] indices = new int[nbVersions];
		int[] maxIdcs = new int[nbVersions];
		for (int i = 0; i < nbVersions; i++) {
			indices[i] = 0;
			maxIdcs[i] = children[i].size();
			iterate |= maxIdcs[i] > 0;
		}

		/* Prepare versions. */
		// loop over all children
		while (iterate) {
			// compute this line
			T min = null;
			Versions<T> versions = new Versions<T>(nbVersions);
			List<Node<T>>[] nextChildren = new List[nbVersions];
			for (int i = 0; i < nbVersions; i++) {
				// build list
				if (indices[i] < maxIdcs[i]) {
					Node<T> currentChildNode = children[i].get(indices[i]);
					T currentChild = currentChildNode.getNodeInfo();
					if (min == null) {
						// first non null version found for this item
						min = currentChild;
						versions.setVersion(i, currentChild);
						indices[i]++;
					} else {
						int comparison = min.compareTo(currentChild);
						if (comparison == 0) {
							// save version
							versions.setVersion(i, currentChild);
							indices[i]++;
						} else if (comparison > 0) {
							// save version
							min = currentChild;
							versions.setVersion(i, currentChild);
							indices[i]++;
							// previous were not the real minimum, revert them.
							for (int j = 0; j < i; j++) {
								versions.setVersion(j, null);
								indices[j]--;
								nextChildren[j] = new ArrayList<Node<T>>();
							}
						}
						// case comparison < 0: just skip that one for now
					}
					nextChildren[i] = currentChildNode.getChildren();
				} else {
					nextChildren[i] = new ArrayList<Node<T>>();
				}
			}
			// Build and add node
			Node<Versions<T>> versionsNode = new Node<Versions<T>>(versions);
			addChildrenVersions(versionsNode, nextChildren);
			rootNode.addChild(versionsNode);
			// Was this the last iteration?
			iterate = false;
			for (int i = 0; i < nbVersions; i++) {
				maxIdcs[i] = children[i].size();
				iterate |= maxIdcs[i] > indices[i];
			}
		}
	}
}
