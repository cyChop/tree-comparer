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
package org.keyboardplaying.tree.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.keyboardplaying.tree.file.model.DirectoryInfo;
import org.keyboardplaying.tree.file.model.FileInfo;
import org.keyboardplaying.tree.file.model.FileSystemElementInfo;
import org.keyboardplaying.tree.file.model.FileTree;
import org.keyboardplaying.tree.model.Node;

// XXX JAVADOC
/**
 * @author cyChop (http://keyboardplaying.org/)
 */
public class FileTreeBuilder {

	public FileTree buildTree(File file) throws FileNotFoundException,
			IOException {
		// ensure file exists
		if (!file.exists()) {
			throw new FileNotFoundException("File " + file.getPath()
					+ " was not found.");
		}

		FileTree tree;
		String path = file.getAbsolutePath();
		if (file.isDirectory() && !".".equals(file.getName())) {
			// The directories to be compared may have different names.
			// Use '.' as name for the root directory.
			tree = new FileTree(path, buildNode(new File(file, ".")));
		} else {
			tree = new FileTree(path, buildNode(file));
		}
		return tree;
	}

	private Node<FileSystemElementInfo> buildNode(File file)
			throws FileNotFoundException, IOException {
		Node<FileSystemElementInfo> root;
		if (file.isDirectory()) {
			root = createDirectoryNode(file);
			for (File subFile : file.listFiles()) {
				root.addChild(buildNode(subFile));
			}
		} else {
			root = createFileNode(file);
		}
		return root;
	}

	private Node<FileSystemElementInfo> createDirectoryNode(File file) {
		assert file.isDirectory();
		return new Node<FileSystemElementInfo>(
				new DirectoryInfo(file.getName()));
	}

	private Node<FileSystemElementInfo> createFileNode(File file)
			throws FileNotFoundException, IOException {
		assert file.isFile();
		String checksum = DigestUtils.md5Hex(new FileInputStream(file));
		FileSystemElementInfo fileInfo = new FileInfo(file.getName(),
				file.length(), file.lastModified(), checksum);
		return new Node<FileSystemElementInfo>(fileInfo);
	}
}
