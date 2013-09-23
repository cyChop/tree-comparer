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

import org.junit.Test;

import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Tree;
import org.keyboardplaying.tree.model.Versions;

// XXX JAVADOC
/**
 * @author cyChop (http://keyboardplaying.org/)
 */
public class ComparerTest {

	@Test
	public void testComparison() {
		/* Build test trees; */
		Node<String> root, node;

		// Tree 1
		root = new Node<String>("A   ");
		node = new Node<String>("A1  ");
		node.addChild(new Node("A11 "));
		node.addChild(new Node("A12 "));
		node.addChild(new Node("A14 "));
		root.addChild(node);
		node = new Node<String>("A2  ");
		node.addChild(new Node<String>("A21 "));
		root.addChild(node);
		root.addChild(new Node<String>("A4  "));
		Tree<String, String> tree1 = new Tree<String, String>("Tree1", root);

		// Tree 2
		root = new Node<String>("A   ");
		node = new Node<String>("A1  ");
		node.addChild(new Node("A12 "));
		node.addChild(new Node("A13 "));
		root.addChild(node);
		root.addChild(new Node<String>("A3  "));
		root.addChild(new Node<String>("A4  "));
		Tree<String, String> tree2 = new Tree<String, String>("Tree2", root);

		// Tree 3
		root = new Node<String>("A   ");
		node = new Node<String>("A1  ");
		node.addChild(new Node("A11 "));
		node.addChild(new Node("A12 "));
		node.addChild(new Node("A14 "));
		root.addChild(node);
		root.addChild(new Node<String>("A3  "));
		root.addChild(new Node<String>("A4  "));
		Tree<String, String> tree3 = new Tree<String, String>("Tree3", root);

		// Now compare
		Comparer comparer = new Comparer();
		Tree<Versions<String>, Versions<String>> result =
			comparer.compare(tree1, tree2, tree3);
		print(result);
	}

	public void print(Tree<Versions<String>, Versions<String>> tree) {
		print(tree.getRoot(), 0);
	}

	private void print(Node<Versions<String>> node, int indent) {
		for (int i = 0; i < indent; i++) {
			System.out.print("+");
		}
		System.out.print("\t");
		Versions<String> versions = node.getNodeInfo();
		for (int i = 0; i < versions.getNbVersions(); i++) {
			System.out.print(versions.getVersion(i) + ",\t");
		}
		System.out.println();
		for (Node<Versions<String>> child : node.getChildren()) {
			print(child, indent + 1);
		}
	}
}