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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Tree;
import org.keyboardplaying.tree.model.Variations;

/**
 * Tests the {@link Comparer}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class ComparerTest {

    private Comparer comparer;

    @Before
    public void initComparer() {
        this.comparer = new Comparer();
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unchecked")
    public void testNoArgComparer() {
        comparer.compare();
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unchecked")
    public void testSingleArgComparer() {
        Node<String> root = new Node<>("A");
        Tree<String, String> tree = new Tree<>("Tree1", root);
        comparer.compare(tree);
    }

    /** Tests the comparison algorithm. */
    @Test
    public void testComparison() {
        /* Build test trees; */
        Node<String> root, node;

        // Tree 1
        root = new Node<>("A");
        node = new Node<>("A1");
        node.addChild(new Node<>("A11"));
        node.addChild(new Node<>("A12"));
        node.addChild(new Node<>("A14"));
        root.addChild(node);
        node = new Node<>("A2");
        node.addChild(new Node<>("A21"));
        root.addChild(node);
        root.addChild(new Node<>("A4"));
        Tree<String, String> tree1 = new Tree<>("Tree1", root);

        // Tree 2
        root = new Node<>("A");
        node = new Node<>("A1");
        node.addChild(new Node<>("A12"));
        node.addChild(new Node<>("A13"));
        root.addChild(node);
        root.addChild(new Node<>("A3"));
        root.addChild(new Node<>("A4"));
        Tree<String, String> tree2 = new Tree<>("Tree2", root);

        // Tree 3
        root = new Node<>("A");
        node = new Node<>("A1");
        node.addChild(new Node<>("A11"));
        node.addChild(new Node<>("A12"));
        node.addChild(new Node<>("A14"));
        root.addChild(node);
        root.addChild(new Node<>("A3"));
        root.addChild(new Node<>("A4"));
        Tree<String, String> tree3 = new Tree<>("Tree3", root);

        // Now compare
        @SuppressWarnings("unchecked")
        Tree<Variations<String>, Variations<String>> result = comparer.compare(new Tree[] { tree1, tree2, tree3 });

        // Ensure the result is as expected
        Variations<String> versions = result.getId();
        assertEquals("Tree1", versions.get(0));
        assertEquals("Tree2", versions.get(1));
        Node<Variations<String>> nod = result.getRoot();
        versions = new Variations<>(3);
        versions.set(0, "A1");
        versions.set(1, "A1");
        versions.set(2, "A1");
        assertEquals(versions, nod.getChildren().get(0).getNodeInfo());
        versions.set(0, "A11");
        versions.set(1, null);
        versions.set(2, "A11");
        assertEquals(versions, nod.getChildren().get(0).getChildren().get(0).getNodeInfo());
        versions.set(0, null);
        versions.set(1, "A13");
        versions.set(2, null);
        assertEquals(versions, nod.getChildren().get(0).getChildren().get(2).getNodeInfo());
        versions.set(0, "A12");
        versions.set(1, "A12");
        versions.set(2, "A12");
        assertEquals(versions, nod.getChildren().get(0).getChildren().get(1).getNodeInfo());
        versions.set(0, "A2");
        versions.set(1, null);
        versions.set(2, null);
        assertEquals(versions, nod.getChildren().get(1).getNodeInfo());
        // make sure there is no null pointer exception
        versions.set(0, "A21");
        assertEquals(versions, nod.getChildren().get(1).getChildren().get(0).getNodeInfo());
        versions.set(0, null);
        versions.set(1, "A3");
        versions.set(2, "A3");
        assertEquals(versions, nod.getChildren().get(2).getNodeInfo());
        versions.set(0, "A4");
        versions.set(1, "A4");
        versions.set(2, "A4");
        assertEquals(versions, nod.getChildren().get(3).getNodeInfo());
    }
}
