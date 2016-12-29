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
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.keyboardplaying.tree.file.filter.CompositeFileFilter;
import org.keyboardplaying.tree.file.model.FileSystemElement;
import org.keyboardplaying.tree.model.Node;

/**
 * This class contains the algorithm to build a node from a root.
 *
 * @author Cyrille Chopelet (https://keyboardplaying.org)
 */
public class FileNodeBuilder {

    private static final String ROOT_DIR_NAME = ".";

    private FileSystemElementBuilder builder = new FileSystemElementBuilder();

    private FileFilter filter;

    /**
     * Sets the {@link FileFilter} to use to determine which files should or should not be included in the tree.
     *
     * @param filter the filter to use
     */
    public void setFileFilter(FileFilter filter) {
        this.filter = filter;
    }

    /**
     * Sets the {@link FileFilter} to use to determine which files should or should not be included in the tree.
     *
     * @param filters the filters to use
     */
    public void setFileFilters(FileFilter... filters) {
        setFileFilters(Arrays.asList(filters));
    }

    /**
     * Sets the {@link FileFilter} to use to determine which files should or should not be included in the tree.
     *
     * @param filters the filters to use
     */
    public void setFileFilters(Collection<FileFilter> filters) {
        setFileFilter(new CompositeFileFilter(filters));
    }

    /**
     * Builds a {@link Node} for the supplied file or directory and all children files and directories.
     *
     * @param root the root file or directory for the tree to build
     * @return a {@link Node} representing the file information as {@link FileSystemElement}
     * @throws IOException if a file cannot be read
     */
    public Node<FileSystemElement> buildTree(File root) throws IOException {
        // File is required to exist.
        Objects.requireNonNull(root, "An existing file must be supplied.");
        if (!root.exists()) {
            throw new FileNotFoundException("File " + root.getPath() + " could not be found.");
        }

        // If directory, use . as a name to ease later comparison.
        return buildNode(root.isDirectory() ? new File(root, ROOT_DIR_NAME) : root);
    }

    /**
     * Recursively builds a node with its children.
     *
     * @param file the file or directory to build a node for
     * @return the node for the supplied {@link File} with all children
     * @throws IOException if a file cannot be read
     */
    private Node<FileSystemElement> buildNode(File file) throws IOException {
        Node<FileSystemElement> node;

        if (file.isDirectory()) {
            node = new Node<>(builder.buildDirectoryElement(file));

            File[] childFiles = file.listFiles(filter);
            if (childFiles != null) {
                for (File childFile : childFiles) {
                    node.addChild(buildNode(childFile));
                }
            }

        } else {

            node = new Node<>(builder.buildFileElement(file));
        }

        return node;
    }
}
