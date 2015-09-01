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

import java.io.File;

/**
 * Describes a directory on the file system.
 * <p/>
 * This {@link FileSystemElementInfo} describes a directory on the file system. Two directories are deemed equal if they
 * have the same name (case-sensitive).
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class DirectoryInfo extends FileSystemElementInfo {

    /**
     * Creates a new instance.
     *
     * @param file
     *            a {@link File} representation of the directory this node describes
     */
    public DirectoryInfo(File file) {
        super(file);
        assert file.isDirectory();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof DirectoryInfo && getName().equals(((DirectoryInfo) o).getName());
    }
}
