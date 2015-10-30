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
package org.keyboardplaying.tree.util;

import java.util.ArrayList;
import java.util.List;

import org.keyboardplaying.tree.model.Node;

/**
 * A utility class for testing.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class NodeTestUtil {

    /**
     * Creates a list of {@link Node} instances from a set of content.
     *
     * @param children
     *            the contents
     * @return the contents in nodes
     */
    @SafeVarargs
    public static <T> List<Node<T>> asNodes(T... children) {
        List<Node<T>> nodes = new ArrayList<>();

        for (T child : children) {
            nodes.add(new Node<>(child));
        }

        return nodes;
    }
}
