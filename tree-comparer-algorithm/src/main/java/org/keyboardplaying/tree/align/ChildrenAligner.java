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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Variations;

/**
 * A class to align the children of a node in a collection of trees.
 * <p/>
 * This is package-visible only because only the tree aligner should be able to use it.
 * <p/>
 * This class recursively calls itself rather than calling recursively a method. This makes reading the scope and
 * maintaining the code easier.
 *
 * @param <T> the type of node content for the trees being aligned
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 * @see TreeAligner
 */
// Package visible only
class ChildrenAligner<T> {

    private Comparator<Node<T>> comparator;
    private List<ListIterator<Node<T>>> iterators;
    private int nbTrees;

    /**
     * Creates a new instance.
     *
     * @param comparator the comparator to use when aligning the trees
     * @param nbTrees    the number of trees being aligned (avoid recounting)
     * @param variations the lists of children to align
     */
    public ChildrenAligner(Comparator<Node<T>> comparator, int nbTrees, List<List<Node<T>>> variations) {
        this.comparator = comparator;

        this.nbTrees = nbTrees;
        this.iterators = new ArrayList<>();
        for (List<Node<T>> children : variations) {
            this.iterators.add(children.listIterator());
        }
    }

    /**
     * Produces a list of aligned nodes.
     *
     * @return the aligned children
     */
    public List<Node<Variations<T>>> alignChildren() {
        List<Node<Variations<T>>> aligned = new ArrayList<>();
        int size = iterators.size();

        while (hasNext()) {
            aligned.add(makeNextLine(size));
        }

        return aligned;
    }

    private Node<Variations<T>> makeNextLine(int size) {
        List<T> line = new ArrayList<>(size);
        List<List<Node<T>>> lineChildren = new ArrayList<>(size);

        Node<T> minimum = null;
        for (ListIterator<Node<T>> iter : iterators) {
            minimum = alignChild(minimum, iter, line, lineChildren);
        }

        Variations<T> content = new Variations<>(line);
        Node<Variations<T>> node = new Node<>(content);
        node.setChildren(new ChildrenAligner<>(comparator, nbTrees, lineChildren).alignChildren());
        return node;
    }

    private Node<T> alignChild(Node<T> min, ListIterator<Node<T>> iter, List<T> line,
                               List<List<Node<T>>> lineChildren) {
        Node<T> minimum = min;
        Node<T> current = null;

        if (iter.hasNext()) {
            current = iter.next();
            if (minimum == null) {
                minimum = current;
            } else {
                int delta = comparator.compare(minimum, current);

                if (delta > 0) {
                    // Previous minimum was not the minimum.
                    minimum = current;
                    // Reset what was already saved for this line.
                    resetCurrentLine(line, lineChildren, iter);
                } else if (delta < 0) {
                    // This child cannot be added, revert the iterator one step
                    current = null;
                    iter.previous();
                }
            }
        }

        addCurrentToLine(current, line, lineChildren);

        return minimum;
    }

    /**
     * Replaces all children already added to this line with {@code null} and moves all iterators one step back.
     *
     * @param line         the current line
     * @param lineChildren the children to be aligned for the nodes of the current line
     * @param iter         the iterator of the new minimum for the line
     */
    private void resetCurrentLine(List<T> line, List<List<Node<T>>> lineChildren, ListIterator<Node<T>> iter) {
        line.clear();
        lineChildren.clear();
        for (ListIterator<Node<T>> it : iterators) {
            if (it == iter) {
                break;
            }
            // The child is null for this one
            line.add(null);
            lineChildren.add(new ArrayList<Node<T>>());
            // Revert the iterator one step
            it.previous();
        }
    }

    private void addCurrentToLine(Node<T> current, List<T> line, List<List<Node<T>>> lineChildren) {
        if (current != null) {
            line.add(current.getContent());
            lineChildren.add(current.getChildren());
        } else {
            line.add(null);
            lineChildren.add(new ArrayList<Node<T>>());
        }
    }

    /**
     * Returns {@code true} if there is still any child to be aligned.
     *
     * @return {@code true} if not all children have been processed, {@code false} otherwise
     */
    private boolean hasNext() {
        for (ListIterator<Node<T>> iter : iterators) {
            if (iter.hasNext()) {
                return true;
            }
        }
        return false;
    }
}
