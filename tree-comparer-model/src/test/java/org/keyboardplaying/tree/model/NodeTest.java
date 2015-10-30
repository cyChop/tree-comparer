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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link Node}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class NodeTest {

    /** Tests the class constructor. */
    @Test
    public void testConstructor() {
        Node<String> node = new Node<>("hello");
        assertEquals("hello", node.getContent());
        assertNull(node.getParent());
        assertTrue(node.isTreeRoot());
        assertTrue(node.getChildren().isEmpty());
    }

    /** Tests the class constructor when the content is null. */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullContent() {
        @SuppressWarnings("unused")
        Node<String> node = new Node<>(null);
    }

    /** Tests the behavior when adding children. */
    @Test
    public void testTreeStructure() {
        Node<String> root = new Node<>("root");
        Node<String> child1 = new Node<>("child1");
        Node<String> child2 = new Node<>("child2");
        Node<String> child11 = new Node<>("child11");
        Node<String> child12 = new Node<>("child12");

        child1.setChildren(Arrays.asList(child11, child12));
        root.addChild(child1);
        root.addChild(child2);

        assertTrue(root.isTreeRoot());
        assertFalse(child1.isTreeRoot());
        assertSame(root, child1.getParent());
        assertEquals(2, root.getChildren().size());
        assertSame(child1, root.getChildren().get(0));

        // setChildren removes previous additions
        root.setChildren(null);
        assertEquals(0, root.getChildren().size());
        root.setChildren(Arrays.asList(new Node<>("child1")));
        assertEquals(1, root.getChildren().size());
        root.addChild(new Node<>("child2"));
        assertEquals(2, root.getChildren().size());
    }

    /** Ensures adding a null child fails. */
    @Test(expected = NullPointerException.class)
    public void testAddNullChild() {
        new Node<>("root").addChild(null);
    }

    /** Tests {@link Node#equals(Object)} and {@link Node#hashCode()}. */
    @Test
    public void testEqualsAndHashcode() {
        Node<String> node1 = new Node<>("hello");
        Node<String> node2 = new Node<>("world");

        /* Equals */
        assertTrue(node1.equals(node1));
        assertFalse(node1.equals(node2));
        assertFalse(node2.equals(node1));

        /* Hashcode */
        assertTrue(node1.hashCode() == node1.hashCode());
    }
}
