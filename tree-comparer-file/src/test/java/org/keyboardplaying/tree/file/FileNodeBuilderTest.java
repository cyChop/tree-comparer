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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;
import org.keyboardplaying.tree.file.model.FileSystemElement;
import org.keyboardplaying.tree.file.model.FileSystemElementType;
import org.keyboardplaying.tree.model.Node;

/**
 * Test class for {@link FileNodeBuilder}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class FileNodeBuilderTest {

    private static FileNodeBuilder builder = new FileNodeBuilder();

    /** Tests the tree building. */
    @SuppressWarnings("javadoc")
    @Test
    public void testTreeBuilding() throws IOException {
        /* Prepare */
        File file = new File("src/test/resources/version1");

        /* Execute */
        Node<FileSystemElement> tree = builder.buildTree(file);

        /* Assert */
        // Root
        assertEquals(new FileSystemElement(new File(file, "."), FileSystemElementType.DIRECTORY, null),
                tree.getContent());
        assertEquals(".", tree.getContent().getName());
        assertEquals(FileSystemElementType.DIRECTORY, tree.getContent().getType());

        // children
        Iterator<Node<FileSystemElement>> iter = tree.getChildren().iterator();
        Node<FileSystemElement> child;

        // 1st child
        child = iter.next();
        assertEquals(".htaccess", child.getContent().getName());
        assertEquals(FileSystemElementType.TEXT, child.getContent().getType());
        assertEquals("0576fea54f83abd3fb459336e1dcf278", child.getContent().getChecksum());

        // 2nf child
        child = iter.next();
        assertEquals("directory", child.getContent().getName());
        assertEquals(FileSystemElementType.DIRECTORY, child.getContent().getType());
        assertNull(child.getContent().getChecksum());

        // 3rd child
        child = iter.next();
        assertEquals("empty.log", child.getContent().getName());
        assertEquals(FileSystemElementType.TEXT, child.getContent().getType());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", child.getContent().getChecksum());

        // 4rd child
        child = iter.next();
        assertEquals("hello.properties", child.getContent().getName());
        assertEquals(FileSystemElementType.TEXT, child.getContent().getType());
        assertEquals("0571f792f6c7d1848e3ae008695bccf7", child.getContent().getChecksum());

        // no more
        assertFalse(iter.hasNext());
    }

    /** Tests the tree building with a filter. */
    @SuppressWarnings("javadoc")
    @Test
    public void testTreeBuildingWithFilter() throws IOException {
        /* Prepare */
        File file = new File("src/test/resources/version1");
        FileFilter fileFilter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                // remove directories
                return !pathname.isDirectory();
            }
        };
        FileFilter hiddenFileRemover = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !pathname.getName().matches("^\\..+$");
            }
        };
        builder.setFileFilters(fileFilter, hiddenFileRemover);

        /* Execute */
        Node<FileSystemElement> tree = builder.buildTree(file);

        /* Assert */
        // Root
        assertEquals(new FileSystemElement(new File(file, "."), FileSystemElementType.DIRECTORY, null),
                tree.getContent());
        assertEquals(".", tree.getContent().getName());
        assertEquals(FileSystemElementType.DIRECTORY, tree.getContent().getType());

        // children
        Iterator<Node<FileSystemElement>> iter = tree.getChildren().iterator();
        assertEquals("empty.log", iter.next().getContent().getName());
        assertEquals("hello.properties", iter.next().getContent().getName());
        assertFalse(iter.hasNext());
    }
}
