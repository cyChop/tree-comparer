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
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

// XXX JAVADOC
/**
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class NodeTest {

    @Test
    public void testEqualityAndSortingMethods() {
        String value1 = UUID.randomUUID().toString();
        String value2 = UUID.randomUUID().toString();
        Node<String> node1 = new Node<>(value1);
        Node<String> node2 = new Node<>(value2);

        assertEquals(value1.hashCode(), node1.hashCode());
        assertEquals(value2.hashCode(), node2.hashCode());

        assertFalse(node1.equals(new Node<>(value1.toUpperCase())));
        assertFalse(node1.equals(value2));
        assertTrue(node1.equals(new Node<>(value1)));
        assertTrue(node1.equals(value1));

        assertEquals(value1.compareTo(value2), node1.compareTo(node2));
    }

    @Test
    public void testChildOrdering() {
        Node<String> node = new Node<>("Animals");
        node.addChild(new Node<>("cat"));
        node.addChild(new Node<>("dog"));
        node.addChild(new Node<>("cow"));
        node.addChild(new Node<>("bird"));

        List<Node<String>> children = node.getChildren();
        assertEquals(4, children.size());
        assertEquals("bird", children.get(0).getNodeInfo());
        assertEquals("cat", children.get(1).getNodeInfo());
        assertEquals("cow", children.get(2).getNodeInfo());
        assertEquals("dog", children.get(3).getNodeInfo());
    }

    @Test
    public void testParent() {
        Node<String> node = new Node<>("Parent");
        Node<String> node1 = new Node<>("Child 1");
        Node<String> node2 = new Node<>("Child 2");

        node.addChild(node1);
        node.addChild(node2);

        assertTrue(node1.getParent() == node2.getParent());
        assertTrue(node1.getParent() == node);
    }
}
