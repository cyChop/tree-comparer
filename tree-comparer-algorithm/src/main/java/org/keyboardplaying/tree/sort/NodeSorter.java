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
package org.keyboardplaying.tree.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.keyboardplaying.tree.model.Node;

/**
 * This class recursively sorts all children from a tree using a provided {@link Comparator}.
 *
 * @param <T> the type of content for the nodes being processed here
 * @author Cyrille Chopelet (https://keyboardplaying.org)
 */
public class NodeSorter<T> {

    private Comparator<Node<T>> comparator;

    /**
     * Creates a new instance.
     *
     * @param comparator the comparator
     */
    public NodeSorter(Comparator<Node<T>> comparator) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        this.comparator = comparator;
    }

    /**
     * Recursively sorts the children of this tree using the instance's comparator.
     *
     * @param node the tree which should be sorted
     */
    public void sort(Node<T> node) {
        Objects.requireNonNull(comparator, "No comparator has been set, sorting cannot be performed.");

        if (node.getChildren().isEmpty()) {
            return;
        }

        List<Node<T>> children = new ArrayList<>(node.getChildren());
        Collections.sort(children, comparator);
        node.setChildren(children);

        for (Node<T> child : children) {
            sort(child);
        }
    }
}
