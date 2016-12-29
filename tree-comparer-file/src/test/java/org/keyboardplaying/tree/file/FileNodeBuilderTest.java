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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;
import org.keyboardplaying.tree.file.comparator.FileSystemElementComparator;
import org.keyboardplaying.tree.file.filter.DirectoryFilter;
import org.keyboardplaying.tree.file.filter.HiddenFileFilter;
import org.keyboardplaying.tree.file.model.FileSystemElement;
import org.keyboardplaying.tree.file.model.FileSystemElementType;
import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.sort.NodeSorter;

/**
 * Test class for {@link FileNodeBuilder}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class FileNodeBuilderTest {

    private FileNodeBuilder builder = new FileNodeBuilder();
    private NodeSorter<FileSystemElement> sorter = new NodeSorter<>(new FileSystemElementComparator());

    /**
     * Tests the tree building.
     */
    @SuppressWarnings("javadoc")
    @Test
    public void testTreeBuilding() throws IOException {
        /* Prepare */
        File file = new File("src/test/resources/version1");

        /* Execute */
        Node<FileSystemElement> tree = builder.buildTree(file);
        sorter.sort(tree);

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
        assertEquals("directory", child.getContent().getName());
        assertEquals(FileSystemElementType.DIRECTORY, child.getContent().getType());
        assertNull(child.getContent().getChecksum());

        // 2nd child
        child = iter.next();
        assertEquals(".htaccess", child.getContent().getName());
        assertEquals(FileSystemElementType.TEXT, child.getContent().getType());
        assertEquals("0576fea54f83abd3fb459336e1dcf278", child.getContent().getChecksum());

        // 3rd child
        child = iter.next();
        assertEquals("empty.log", child.getContent().getName());
        assertEquals(FileSystemElementType.TEXT, child.getContent().getType());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", child.getContent().getChecksum());

        // 4rd child
        child = iter.next();
        assertEquals("hello.properties", child.getContent().getName());
        assertEquals(FileSystemElementType.TEXT, child.getContent().getType());
        assertEquals("9e60e9c13569a9ece7ae20fd5798e0cd", child.getContent().getChecksum());

        // no more
        assertFalse(iter.hasNext());
    }

    /**
     * Tests the tree building with a filter.
     */
    @SuppressWarnings("javadoc")
    @Test
    public void testTreeBuildingWithFilter() throws IOException {
        /* Prepare */
        File file = new File("src/test/resources/version1");
        builder.setFileFilters(new DirectoryFilter(), new HiddenFileFilter());

        /* Execute */
        Node<FileSystemElement> tree = builder.buildTree(file);
        sorter.sort(tree);

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

    /**
     * Tests the tree building when the supplied file is not a directory.
     */
    @SuppressWarnings("javadoc")
    @Test
    public void testTreeBuildingForFile() throws IOException {
        /* Prepare */
        File file = new File("src/test/resources/version1/empty.log");

        /* Execute */
        Node<FileSystemElement> tree = builder.buildTree(file);
        sorter.sort(tree);

        /* Assert */
        // Root
        assertEquals("empty.log", tree.getContent().getName());
        assertEquals(FileSystemElementType.TEXT, tree.getContent().getType());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", tree.getContent().getChecksum());

        assertTrue(tree.getChildren().isEmpty());
    }

    /**
     * Tests the tree building when the supplied file does not exist.
     */
    @SuppressWarnings("javadoc")
    @Test(expected = FileNotFoundException.class)
    public void testTreeBuildingForNotExistingElement() throws IOException {
        /* Prepare */
        File file = new File("src/test/resources/harry-potter/invisibility-cloak.cache");

        /* Execute */
        builder.buildTree(file);
    }
}
