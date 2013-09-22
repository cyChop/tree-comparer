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

// XXX JAVADOC
/**
 * @author cyChop (http://keyboardplaying.org/)
 */
public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

	private T nodeInfo;
	private List<Node<T>> children = new ArrayList<Node<T>>();

	public Node(T nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public T getNodeInfo() {
		return nodeInfo;
	}

	public void addChild(T child) {
		this.addChild(new Node<T>(child));
	}

	public void addChild(Node<T> child) {
		/*
		 * Keeping children sorted is a condition for the comparison algorithm
		 * to work as expected.
		 */
		int i = 0;
		int size = children.size();
		while (i < size && child.compareTo(children.get(i)) > 0) {
			i++;
		}
		children.add(i, child);
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

	public List<Node<T>> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public int compareTo(Node<T> o) {
		return getNodeInfo().compareTo(o.getNodeInfo());
	}

	@Override
	public int hashCode() {
		return getNodeInfo().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Node
				&& getNodeInfo().equals(((Node<?>) obj).getNodeInfo())
				|| getNodeInfo().equals(obj);
	}

	@Override
	public String toString() {
		return nodeInfo.toString();
	}
}
