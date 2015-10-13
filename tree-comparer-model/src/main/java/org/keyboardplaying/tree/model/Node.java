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
 * Simple implementation for a tree node.
 * <p/>
 * A node references its parent and children.
 * <p/>
 * There is no difference of implementation between nodes and leaves. A leaf will simply be a childless node.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 *
 * @param <T>
 *            the type of node content
 */
public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

    /** This node's characteristics. */
    private T content;
    /** A reference to this node's parent. */
    private Node<T> parent;
    /**
     * This node's children.
     * <p/>
     * This list should be kept sorted at all times.
     */
    private List<Node<T>> children = new ArrayList<>();

    /**
     * Creates a new instance.
     *
     * @param content
     *            this node's characteristics
     */
    public Node(T content) {
        this.content = content;
    }

    /**
     * Returns this node's content.
     *
     * @return this node's content
     */
    public T getContent() {
        return content;
    }

    /**
     * Returns this node's parent, or {@code null} if this node is root.
     *
     * @return this node's parent
     */
    public Node<T> getParent() {
        return parent;
    }

    /**
     * Adds a child to this node.
     * <p>
     * The children are kept sorted at all times.
     *
     * @param child
     *            the child to add to this node
     */
    public void addChild(Node<T> child) {
        /*
         * Keeping children sorted is a condition for the comparison algorithm to work as expected.
         */
        int i = 0;
        int size = children.size();
        while (i < size && child.compareTo(children.get(i)) > 0) {
            i++;
        }
        children.add(i, child);
        child.parent = this;
    }

    /**
     * Returns this node's children.
     * <p/>
     * The list is sorted and unmodifiable.
     *
     * @return this node's children
     */
    public List<Node<T>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Node<T> o) {
        return getContent().compareTo(o.getContent());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getContent());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node && Objects.equals(getContent(), ((Node<?>) obj).getContent());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.valueOf(getContent());
    }
}
