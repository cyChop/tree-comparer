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
import java.io.IOException;

import org.junit.Test;
import org.keyboardplaying.tree.file.filter.FilenameAntiMaskFilter;
import org.keyboardplaying.tree.file.filter.FilenameMaskFilter;
import org.keyboardplaying.tree.file.model.DirectoryInfo;
import org.keyboardplaying.tree.file.model.FileInfo;
import org.keyboardplaying.tree.file.model.FileSystemElementInfo;
import org.keyboardplaying.tree.file.model.FileTree;
import org.keyboardplaying.tree.model.Node;

/**
 * Tests the {@link FileTreeBuilder} class.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
@SuppressWarnings("javadoc")
public class FileTreeBuilderTest {

    /** Tests the standard tree building. */
    @Test
    public void testTreeBuilding() throws IOException {
        File file = new File("src/test/resources/version1");
        FileTreeBuilder builder = new FileTreeBuilder();
        FileTree tree = builder.buildTree(file);

        assertEquals(file.getAbsolutePath(), tree.getId());

        Node<FileSystemElementInfo> root = tree.getRoot();
        assertEquals(".", root.getContent().getName());
        assertTrue(root.getContent() instanceof DirectoryInfo);

        int i = 0;
        if (!"directory".equals(root.getChildren().get(i).getContent().getName())) {
            i++;
        }

        assertEquals("directory", root.getChildren().get(i).getContent().getName());
        assertTrue(root.getChildren().get(i).getContent() instanceof DirectoryInfo);

        i++;
        assertEquals(".htaccess", root.getChildren().get(i).getContent().getName());

        i++;
        assertEquals("hello.properties", root.getChildren().get(i).getContent().getName());
        assertTrue(root.getChildren().get(i).getContent() instanceof FileInfo);
    }

    /** Tests the tree building with a positive mask filter. */
    @Test
    public void testTreeBuildingWithFilter() throws IOException {
        File file = new File("src/test/resources/version1");
        FileTreeBuilder builder = new FileTreeBuilder();
        FileTree tree = builder.buildTree(file, new FilenameMaskFilter("[^.].+"));

        Node<FileSystemElementInfo> root = tree.getRoot();
        assertTrue(root.getContent() instanceof DirectoryInfo);
        assertEquals("directory", root.getChildren().get(0).getContent().getName());
        assertEquals("hello.properties", root.getChildren().get(1).getContent().getName());
    }

    /** Tests the tree building with a negative mask filter. */
    @Test
    public void testTreeBuildingWithAntiMaskFilter() throws IOException {
        File file = new File("src/test/resources/version1");
        FileTreeBuilder builder = new FileTreeBuilder();
        FileTree tree = builder.buildTree(file, new FilenameAntiMaskFilter("\\..+"));

        Node<FileSystemElementInfo> root = tree.getRoot();
        assertTrue(root.getContent() instanceof DirectoryInfo);
        assertEquals("directory", root.getChildren().get(0).getContent().getName());
        assertEquals("hello.properties", root.getChildren().get(1).getContent().getName());
    }
}
