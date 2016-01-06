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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Simple implementation for a tree.
 * <p/>
 * The tree is a root node with children nodes the same type.
 * <p/>
 * There is no difference of implementation between nodes and leaves. A leaf will simply be a childless node.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 *
 * @param <T>
 *            the type of nodes for this tree
 */
public class Node<T> {

    /* === The content part === */
    private final T content;

    /* === The tree part === */
    /** This node's children. */
    private final List<Node<T>> children = new ArrayList<>();

    /**
     * Creates a new node.
     *
     * @param content
     *            the node's content
     */
    public Node(T content) {
        Objects.requireNonNull(content, "A node content may not be null.");
        this.content = content;
    }

    /**
     * Returns the node's content.
     *
     * @return the content the node's content
     */
    public T getContent() {
        return content;
    }

    /**
     * Adds a child to this node.
     *
     * @param child
     *            the child to add to this node
     */
    public void addChild(Node<T> child) {
        Objects.requireNonNull(child, "The node cannot have null children");
        this.children.add(child);
    }

    /**
     * Sets the children for this node.
     *
     * @param children
     *            the children for this node
     */
    public void setChildren(List<Node<T>> children) {
        this.children.clear();
        if (children != null) {
            for (Node<T> child : children) {
                addChild(child);
            }
        }
    }

    /**
     * Returns this node's children.
     *
     * @return the children
     */
    public List<Node<T>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getContent().hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node && ((Node<T>) obj).getContent().equals(getContent());
    }
}
