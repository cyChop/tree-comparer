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
package org.keyboardplaying.tree.file.comparator;

import java.util.Comparator;

import org.keyboardplaying.tree.file.model.FileSystemElement;
import org.keyboardplaying.tree.file.model.FileSystemElementType;
import org.keyboardplaying.tree.model.Node;

/**
 * A comparator for {@link FileSystemElement} nodes.
 *
 * @author Cyrille Chopelet (https://keyboardplaying.org)
 */
public class FileSystemElementComparator implements Comparator<Node<FileSystemElement>> {

    /*
     * (non-Javadoc)
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Node<FileSystemElement> node1, Node<FileSystemElement> node2) {
        FileSystemElement file1 = node1.getContent();
        FileSystemElement file2 = node2.getContent();

        if (file1.getType() == FileSystemElementType.DIRECTORY) {
            if (file2.getType() != FileSystemElementType.DIRECTORY) {
                return -1;
            }
        } else if (file2.getType() == FileSystemElementType.DIRECTORY) {
            return 1;
        }

        return file1.getName().compareTo(file2.getName());
    }
}
