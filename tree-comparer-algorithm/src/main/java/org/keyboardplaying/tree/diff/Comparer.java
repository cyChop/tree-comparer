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
package org.keyboardplaying.tree.diff;

import java.util.ArrayList;
import java.util.List;

import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Tree;
import org.keyboardplaying.tree.model.Versions;

/**
 * Compares a variable number of trees and compiles the results in a single tree containing each diff as a node.
 * <p/>
 * The diff tree respects the hierarchical structure of the compared trees and stores the various versions of each node
 * in a {@link Versions} object. If a node is missing from a tree, it will be stored as {@code null} at that position in
 * the corresponding {@link Versions} instance.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class Comparer {

    /**
     * Compares a variable number of trees.
     * <p/>
     * See the class description for more information.
     *
     * @param trees
     *            the trees to compare; at least two should be provided
     * @return a {@link Tree} containing all the versions of each node in each original {@link Tree}
     */
    public <R extends Comparable<R>, T extends Comparable<T>> Tree<Versions<R>, Versions<T>> compare(
            @SuppressWarnings("unchecked") Tree<R, T>... trees) {
        int nbVersions = trees.length;

        if (nbVersions < 2) {
            throw new IllegalArgumentException("The comparator works if there are at least two trees to compare.");
        }

        Versions<R> roots = new Versions<>(nbVersions);
        Versions<T> rootVersions = new Versions<>(nbVersions);
        @SuppressWarnings("unchecked")
        List<Node<T>>[] children = new List[nbVersions];

        for (int i = 0; i < nbVersions; i++) {
            roots.set(i, trees[i].getId());
            rootVersions.set(i, trees[i].getRoot().getNodeInfo());
            children[i] = trees[i].getRoot().getChildren();
        }

        Node<Versions<T>> rootNode = new Node<>(rootVersions);
        addChildrenVersions(rootNode, children);

        return new Tree<>(roots, rootNode);
    }

    /**
     * Compares several lists of children and appends the diff result to the supplied root node.
     * <p/>
     * This method adds the children one at a time. An array is used to save the position in each children list.
     *
     * @param rootNode
     *            the diff root node which should be parent to the newly generated diff children
     * @param children
     *            the various versions of children lists to compare
     *
     * @see #addChildrenAtCurrentIndices(Node, List[], int, int[], int[])
     */
    private <T extends Comparable<T>> void addChildrenVersions(Node<Versions<T>> rootNode, List<Node<T>>[] children) {
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
            addChildrenAtCurrentIndices(rootNode, children, nbVersions, indices, maxIdcs);
            // Was this the last iteration?
            iterate = false;
            for (int i = 0; i < nbVersions; i++) {
                maxIdcs[i] = children[i].size();
                iterate |= maxIdcs[i] > indices[i];
            }
        }
    }

    /**
     * Adds the children at the current indices.
     *
     * @param rootNode
     *            the diff root node which should be parent to the newly generated diff children
     * @param children
     *            the various versions of children lists to compare
     * @param nbVersions
     *            the number of versions being compared
     * @param indices
     *            the current position of iteration in each children list
     * @param maxIdcs
     *            the size of each children list; passing them as a parameter avoids counting the elements in the list
     *            on each iteration
     */
    private <T extends Comparable<T>> void addChildrenAtCurrentIndices(Node<Versions<T>> rootNode,
            List<Node<T>>[] children, int nbVersions, int[] indices, int[] maxIdcs) {
        T min = null;
        Versions<T> versions = new Versions<>(nbVersions);
        @SuppressWarnings("unchecked")
        List<Node<T>>[] nextChildren = new List[nbVersions];
        for (int i = 0; i < nbVersions; i++) {
            // build list
            if (indices[i] < maxIdcs[i]) {
                Node<T> currentChildNode = children[i].get(indices[i]);
                T currentChild = currentChildNode.getNodeInfo();
                if (min == null) {
                    // first non null version found for this item
                    min = currentChild;
                    versions.set(i, currentChild);
                    indices[i]++;
                    nextChildren[i] = currentChildNode.getChildren();
                } else {
                    int comparison = min.compareTo(currentChild);
                    if (comparison == 0) {
                        // save version
                        versions.set(i, currentChild);
                        indices[i]++;
                        nextChildren[i] = currentChildNode.getChildren();
                    } else if (comparison > 0) {
                        // save version
                        min = currentChild;
                        versions.set(i, currentChild);
                        indices[i]++;
                        nextChildren[i] = currentChildNode.getChildren();
                        // previous were not the real minimum, revert them.
                        for (int j = 0; j < i; j++) {
                            versions.set(j, null);
                            indices[j] = Math.max(indices[j] - 1, 0);
                            nextChildren[j] = new ArrayList<>();
                        }
                    } else {
                        // higher than minimal
                        // do not save version
                        nextChildren[i] = new ArrayList<>();
                    }
                }
            } else {
                nextChildren[i] = new ArrayList<>();
            }
        }
        // Build and add node
        Node<Versions<T>> versionsNode = new Node<>(versions);
        addChildrenVersions(versionsNode, nextChildren);
        rootNode.addChild(versionsNode);
    }
}
