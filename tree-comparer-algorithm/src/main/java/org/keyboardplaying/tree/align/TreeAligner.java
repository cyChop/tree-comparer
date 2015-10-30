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
package org.keyboardplaying.tree.align;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Variations;
import org.keyboardplaying.tree.sort.NodeSorter;

/**
 * A class to produce a tree allowing for easy comparison of several trees.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 *
 * @param <T>
 *            the type of node content for the trees being aligned
 */
public class TreeAligner<T> {

    private Comparator<Node<T>> comparator;

    /**
     * Creates a new instance.
     *
     * @param comparator
     *            the comparator to use when aligning the trees
     */
    public TreeAligner(Comparator<Node<T>> comparator) {
        this.comparator = comparator;
    }

    /**
     * Creates a tree allowing to visualize the differences between several trees by aligning equivalent between trees.
     *
     * @param trees
     *            the trees to align
     * @return the single tree showing the aligned children
     */
    @SafeVarargs
    public final Node<Variations<T>> alignTrees(Node<T>... trees) {
        return alignTrees(Arrays.asList(trees));
    }

    /**
     * Creates a tree allowing to visualize the differences between several trees by aligning equivalent between trees.
     *
     * @param trees
     *            the trees to align
     * @return the single tree showing the aligned children
     */
    public Node<Variations<T>> alignTrees(List<Node<T>> trees) {
        if (trees == null) {
            return null;
        }

        sortAllTrees(trees);

        int nbTrees = trees.size();

        Variations<T> root = new Variations<>(nbTrees);
        List<List<Node<T>>> childrenVariations = new ArrayList<>(nbTrees);

        for (Node<T> tree : trees) {
            root.add(tree.getContent());
            childrenVariations.add(tree.getChildren());
        }

        Node<Variations<T>> result = new Node<>(root);
        result.setChildren(new ChildrenAligner<>(comparator, nbTrees, childrenVariations).alignChildren());
        return result;
    }

    /**
     * Orders all trees using the same sorter.
     *
     * @param trees
     *            the trees to sort
     */
    private void sortAllTrees(List<Node<T>> trees) {
        NodeSorter<T> sorter = new NodeSorter<>(comparator);
        for (Node<T> tree : trees) {
            sorter.sort(tree);
        }
    }
}
