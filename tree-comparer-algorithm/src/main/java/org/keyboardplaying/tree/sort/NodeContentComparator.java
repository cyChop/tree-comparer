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
package org.keyboardplaying.tree.sort;

import java.util.Comparator;

import org.keyboardplaying.tree.model.Node;

/**
 * A simple comparator for nodes using {@link Comparable} content types.
 *
 * @param <T> the type of content for the node
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class NodeContentComparator<T extends Comparable<T>> implements Comparator<Node<T>> {

    /*
     * (non-Javadoc)
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Node<T> o1, Node<T> o2) {
        return o1.getContent().compareTo(o2.getContent());
    }
}
