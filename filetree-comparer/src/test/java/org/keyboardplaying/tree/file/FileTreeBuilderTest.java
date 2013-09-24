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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.keyboardplaying.tree.file.model.DirectoryInfo;
import org.keyboardplaying.tree.file.model.FileInfo;
import org.keyboardplaying.tree.file.model.FileSystemElementInfo;
import org.keyboardplaying.tree.file.model.FileTree;
import org.keyboardplaying.tree.model.Node;

// XXX JAVADOC
/**
 * @author cyChop (http://keyboardplaying.org/)
 */
public class FileTreeBuilderTest {

	@Test
	public void testTreeBuilding() throws FileNotFoundException, IOException {
		File file = new File("src/test/resources/version1");
		FileTreeBuilder builder = new FileTreeBuilder();
		FileTree tree = builder.buildTree(file);

		assertEquals(file.getAbsolutePath(), tree.getId());

		Node<FileSystemElementInfo> root = tree.getRoot();
		assertEquals(".", root.getNodeInfo().getName());
		assertTrue(root.getNodeInfo() instanceof DirectoryInfo);
		assertEquals("directory", root.getChildren().get(0).getNodeInfo()
				.getName());
		assertTrue(root.getChildren().get(0).getNodeInfo() instanceof DirectoryInfo);
		assertEquals(1, root.getChildren().get(0).getChildren().size());
		assertEquals("hello.properties", root.getChildren().get(2)
				.getNodeInfo().getName());
		assertTrue(root.getChildren().get(2).getNodeInfo() instanceof FileInfo);
	}
}
