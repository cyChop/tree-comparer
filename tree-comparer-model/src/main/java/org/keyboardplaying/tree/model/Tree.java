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

/**
 * Simple class for a tree structure.
 * <p/>
 * A tree is an ID and a root node. Each node will reference its parent and children.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 * @param <R>
 *            the type of the tree's ID
 * @param <T>
 *            the type of the tree's nodes' characteristics
 * @see Node
 */
public class Tree<R extends Comparable<R>, T extends Comparable<T>> {

    /** This tree's ID. */
    private R id;
    /** This tree's root. */
    private Node<T> root;

    /**
     * Creates a new instance.
     *
     * @param id
     *            this tree's ID
     * @param root
     *            this tree's root
     */
    public Tree(R id, Node<T> root) {
        this.id = id;
        this.root = root;
    }

    /**
     * Returns this tree's ID
     *
     * @return this tree's ID
     */
    public R getId() {
        return id;
    }

    /**
     * Returns this tree's root.
     *
     * @return this tree's root
     */
    public Node<T> getRoot() {
        return root;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // useful when debugging
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(id)).append(':');
        appendNode(root, sb, 0);
        return sb.toString();
    }

    /**
     * Appends a node to the supplied {@link StringBuilder}.
     *
     * @param node
     *            the node to append
     * @param sb
     *            the {@link StringBuilder}
     * @param indent
     *            the indent level of this node (or the number of parent level)
     */
    private void appendNode(Node<T> node, StringBuilder sb, int indent) {
        sb.append('\n');
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
        sb.append(String.valueOf(node));
        for (Node<T> child : node.getChildren()) {
            appendNode(child, sb, indent + 1);
        }
    }
}
