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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.util.NodeTestUtil;

/**
 * Test class for {@link NodeSorter}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class NodeSorterTest {

    /** Tests the sorting without specifying a comparator. */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNull() {
        @SuppressWarnings("unused")
        NodeSorter<String> sorter = new NodeSorter<>(null);
    }

    /** Tests a sorting with a custom comparator. */
    @Test
    public void testSort() {
        Node<String> living = buildTree();
        Node<String> expected = buildNameSortedTree();

        NodeSorter<String> sorter = new NodeSorter<>(new NodeContentComparator<String>());
        sorter.sort(living);
        assertOrder(expected, living);
    }

    private void assertOrder(Node<String> expected, Node<String> actual) {
        assertEquals(expected, actual);

        int size = expected.getChildren().size();
        assertEquals(size, actual.getChildren().size());
        for (int i = 0; i < size; i++) {
            assertOrder(expected.getChildren().get(i), actual.getChildren().get(i));
        }
    }

    private Node<String> buildTree() {
        Node<String> living = new Node<>("Phylogeny");

        Node<String> vertebrates = new Node<>("Vertebrates");
        vertebrates.setChildren(NodeTestUtil.asNodes("Wolf", "Cat", "Human", "Dog"));

        Node<String> invertebrates = new Node<>("Invertebrates");
        invertebrates.addChild(new Node<>("Ant"));

        living.setChildren(Arrays.asList(vertebrates, invertebrates));

        return living;
    }

    private Node<String> buildNameSortedTree() {
        Node<String> living = new Node<>("Phylogeny");

        Node<String> invertebrates = new Node<>("Invertebrates");
        invertebrates.addChild(new Node<>("Ant"));

        Node<String> vertebrates = new Node<>("Vertebrates");
        vertebrates.setChildren(NodeTestUtil.asNodes("Cat", "Dog", "Human", "Wolf"));

        living.setChildren(Arrays.asList(invertebrates, vertebrates));

        return living;
    }
}
