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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Variations;
import org.keyboardplaying.tree.sort.NodeContentComparator;
import org.keyboardplaying.tree.util.NodeTestUtil;

/**
 * Test class for {@link TreeAligner}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class TreeAlignerTest {

    private TreeAligner<String> aligner;

    /** Initializes the aligner. */
    @Before
    public void init() {
        aligner = new TreeAligner<>(new NodeContentComparator<String>());
    }

    /** Makes sure the aligner doesn't crash when no tree is provided. */
    @Test
    public void testEmptyAlignment() {
        Node<Variations<String>> aligned = aligner.alignTrees();
        assertNotNull(aligned);
        assertEquals(0, aligned.getContent().size());
    }

    /** Makes sure the aligner returns null when passed a null list. */
    @Test
    public void testNullAlignment() {
        Node<Variations<String>> aligned = aligner.alignTrees((List<Node<String>>) null);
        assertNull(aligned);
    }

    /** Tests the alignment. */
    @Test
    public void testAlignment() {
        /* Build test trees */
        Node<String> node;

        // Tree 1
        Node<String> tree1 = new Node<>("A");
        node = new Node<>("A1");
        node.setChildren(NodeTestUtil.asNodes("A11", "A12", "A14"));
        tree1.addChild(node);
        node = new Node<>("A2");
        node.addChild(new Node<>("A21"));
        tree1.addChild(node);
        tree1.addChild(new Node<>("A4"));

        // Tree 2
        Node<String> tree2 = new Node<>("A'");
        node = new Node<>("A1");
        node.setChildren(NodeTestUtil.asNodes("A12", "A13"));
        tree2.addChild(node);
        tree2.addChild(new Node<>("A3"));
        tree2.addChild(new Node<>("A4"));

        // Tree 3
        Node<String> tree3 = new Node<>("A''");
        node = new Node<>("A1");
        node.setChildren(NodeTestUtil.asNodes("A11", "A12", "A14"));
        tree3.addChild(node);
        tree3.addChild(new Node<>("A3"));
        tree3.addChild(new Node<>("A4"));

        // Now compare
        Node<Variations<String>> aligned = aligner.alignTrees(tree1, tree2, tree3);

        // Ensure the result is as expected
        Variations<String> versions = aligned.getContent();
        assertEquals(new Variations<>(Arrays.asList("A", "A'", "A''")), versions);

        Node<Variations<String>> alignedA1 = aligned.getChildren().get(0);
        assertEquals(new Variations<>(Arrays.asList("A1", "A1", "A1")), alignedA1.getContent());
        assertEquals(new Variations<>(Arrays.asList("A11", null, "A11")),
                alignedA1.getChildren().get(0).getContent());
        assertEquals(new Variations<>(Arrays.asList("A12", "A12", "A12")),
                alignedA1.getChildren().get(1).getContent());
        assertEquals(new Variations<>(Arrays.asList(null, "A13", null)),
                alignedA1.getChildren().get(2).getContent());

        Node<Variations<String>> alignedA2 = aligned.getChildren().get(1);
        assertEquals(new Variations<>(Arrays.asList("A2", null, null)), alignedA2.getContent());
        assertEquals(new Variations<>(Arrays.asList("A21", null, null)),
                alignedA2.getChildren().get(0).getContent());

        Node<Variations<String>> alignedA3 = aligned.getChildren().get(2);
        assertEquals(new Variations<>(Arrays.asList(null, "A3", "A3")), alignedA3.getContent());

        Node<Variations<String>> alignedA4 = aligned.getChildren().get(3);
        assertEquals(new Variations<>(Arrays.asList("A4", "A4", "A4")), alignedA4.getContent());
    }
}
