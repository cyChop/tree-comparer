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
package org.keyboardplaying.tree.file.model;

import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Tree;

/**
 * A specific use of {@link Tree} to represent a file system structure.
 * 
 * @author cyChop (http://keyboardplaying.org/)
 */
public class FileTree extends Tree<String, FileSystemElementInfo> {

	/**
	 * Creates a new instance.
	 * 
	 * @param rootPath
	 *            the path to the root element of this tree
	 * @param root
	 *            the root element of this tree
	 */
	public FileTree(String rootPath, Node<FileSystemElementInfo> root) {
		super(rootPath, root);
	}
}
