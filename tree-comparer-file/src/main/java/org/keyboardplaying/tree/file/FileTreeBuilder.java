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
import org.keyboardplaying.tree.file.filter.Filter;
import org.keyboardplaying.tree.file.model.DirectoryInfo;
import org.keyboardplaying.tree.file.model.FileInfo;
import org.keyboardplaying.tree.file.model.FileSystemElementInfo;
import org.keyboardplaying.tree.file.model.FileTree;
import org.keyboardplaying.tree.model.Node;

/**
 * A facility to build a {@link FileTree} to represent a file system hierarchy.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class FileTreeBuilder {

    /**
     * Recursively builds a {@link FileTree} from a root {@link File} (preferably a directory).
     *
     * @param file
     *            the root file or directory
     * @param filters
     *            the filters to apply when building the tree; a file should match <em>all</em> filters to be included
     *            in the comparison
     * @return the generated {@link FileTree}
     * @throws IOException
     *             when a file could not be found or read
     */
    public FileTree buildTree(File file, Filter... filters) throws IOException {
        // ensure file exists
        if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getPath() + " was not found.");
        }

        FileTree tree;
        String path = file.getAbsolutePath();
        if (file.isDirectory() && !".".equals(file.getName())) {
            // The directories to be compared may have different names.
            // Use '.' as name for the root directory.
            tree = new FileTree(path, buildNode(new File(file, "."), filters));
        } else {
            tree = new FileTree(path, buildNode(file, filters));
        }
        return tree;
    }

    /**
     * Recursively builds a node with its children.
     *
     * @param file
     *            the file to build a node for
     * @param filters
     *            the filters to exclude files if need be
     * @return the root node, containing all its children
     * @throws IOException
     *             when the file cannot be read
     */
    private Node<FileSystemElementInfo> buildNode(File file, Filter... filters) throws IOException {
        Node<FileSystemElementInfo> root;
        if (file.isDirectory()) {
            root = createDirectoryNode(file);

            File[] list = file.listFiles();
            if (list != null) {
                for (File subFile : list) {
                    if (matchFilters(subFile, filters)) {
                        root.addChild(buildNode(subFile, filters));
                    }
                }
            }
        } else {
            root = createFileNode(file);
        }
        return root;
    }

    /**
     * Ensures a file matches all supplied filters.
     * <p/>
     * If no filter is supplied, this method will return {@code true}.
     *
     * @param file
     *            the {@link File} to test
     * @param filters
     *            the filters to test the file against
     * @return {@code true} if no filter was supplied or if {@link Filter#include(File)} returned {@code true} for all
     *         filters, {@code false} otherwise
     */
    private boolean matchFilters(File file, Filter... filters) {
        boolean include = true;
        for (Filter filter : filters) {
            if (!filter.include(file)) {
                include = false;
                break;
            }
        }
        return include;
    }

    /**
     * Creates a node with {@link DirectoryInfo} characteristics.
     *
     * @param directory
     *            the directory to create a node for
     * @return the node
     */
    private Node<FileSystemElementInfo> createDirectoryNode(File directory) {
        assert directory.isDirectory();
        return new Node<FileSystemElementInfo>(new DirectoryInfo(directory));
    }

    /**
     * Creates a node with {@link FileInfo} characteristics.
     *
     * @param file
     *            the file to create a node for
     * @return the node
     * @throws IOException
     *             if the file was not found or could not be read
     */
    private Node<FileSystemElementInfo> createFileNode(File file) throws IOException {
        assert file.isFile();

        try (FileInputStream fileIS = new FileInputStream(file)) {
            // calculate the MD5 checksum for this file
            String checksum = DigestUtils.md5Hex(fileIS);

            // create and return node
            FileSystemElementInfo fileInfo = new FileInfo(file, checksum);
            return new Node<>(fileInfo);
        }
    }
}
