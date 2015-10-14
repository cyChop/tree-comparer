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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Tree}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class TreeTest {

    private Tree<String, String> tree;
    private Node<String> root;

    /** Prepares a tree for testing. */
    @Before
    public void buildTree() {
        root = new Node<>("root");
        root.addChild(new Node<>("node1"));
        Node<String> node2 = new Node<>("node2");
        root.addChild(node2);
        node2.addChild(new Node<>("node21"));
        node2.addChild(new Node<>("node22"));
        node2.addChild(new Node<>("node23"));
        root.addChild(new Node<>("node3"));

        tree = new Tree<>("tree", root);
    }

    /** Tests {@link Tree#Tree(Comparable, Node)}, {@link Tree#getId()} and {@link Tree#getRoot()} */
    @Test
    public void testAccessors() {
        assertEquals("tree", tree.getId());
        assertSame(root, tree.getRoot());
    }

    /** Tests {@link #toString()}, though it is mainly a debug method. */
    @Test
    public void testToString() {
        String str = "tree:" + "\nroot" + "\n  node1" + "\n  node2" + "\n    node21" + "\n    node22" + "\n    node23"
                + "\n  node3";
        assertEquals(str, tree.toString());
    }
}
