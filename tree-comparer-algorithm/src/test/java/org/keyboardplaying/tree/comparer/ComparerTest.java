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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.keyboardplaying.tree.diff.Comparer;
import org.keyboardplaying.tree.diff.model.Versions;
import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Tree;

/**
 * Tests the {@link Comparer}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class ComparerTest {

    /** Tests the comparison algorithm. */
    @Test
    public void testComparison() {
        /* Build test trees; */
        Node<String> root, node;

        // Tree 1
        root = new Node<String>("A");
        node = new Node<String>("A1");
        node.addChild(new Node<String>("A11"));
        node.addChild(new Node<String>("A12"));
        node.addChild(new Node<String>("A14"));
        root.addChild(node);
        node = new Node<String>("A2");
        node.addChild(new Node<String>("A21"));
        root.addChild(node);
        root.addChild(new Node<String>("A4"));
        Tree<String, String> tree1 = new Tree<String, String>("Tree1", root);

        // Tree 2
        root = new Node<String>("A");
        node = new Node<String>("A1");
        node.addChild(new Node<String>("A12"));
        node.addChild(new Node<String>("A13"));
        root.addChild(node);
        root.addChild(new Node<String>("A3"));
        root.addChild(new Node<String>("A4"));
        Tree<String, String> tree2 = new Tree<String, String>("Tree2", root);

        // Tree 3
        root = new Node<String>("A");
        node = new Node<String>("A1");
        node.addChild(new Node<String>("A11"));
        node.addChild(new Node<String>("A12"));
        node.addChild(new Node<String>("A14"));
        root.addChild(node);
        root.addChild(new Node<String>("A3"));
        root.addChild(new Node<String>("A4"));
        Tree<String, String> tree3 = new Tree<String, String>("Tree3", root);

        // Now compare
        Comparer comparer = new Comparer();
        @SuppressWarnings("unchecked") Tree<Versions<String>, Versions<String>> result = comparer
                .compare(new Tree[] { tree1, tree2, tree3 });

        // Ensure the result is as expected
        Versions<String> versions = result.getId();
        assertEquals("Tree1", versions.get(0));
        assertEquals("Tree2", versions.get(1));
        Node<Versions<String>> nod = result.getRoot();
        versions = new Versions<String>(3);
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
