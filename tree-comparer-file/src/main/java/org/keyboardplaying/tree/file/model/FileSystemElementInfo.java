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
 * Describes an element on the file system, be it file or directory.
 * <p/>
 * This class is {@code abstract}, subclasses must be used to instantiate objects.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public abstract class FileSystemElementInfo implements Comparable<FileSystemElementInfo> {

    /**
     * The {@link File} representation of the file system element this object represents.
     */
    private File file;

    /**
     * Creates a new instance.
     *
     * @param file
     *            the {@link File} representation of the file system element this object represents
     */
    protected FileSystemElementInfo(File file) {
        this.file = file;
    }

    /**
     * Returns the {@link File} representation of the file system element this object represents
     *
     * @return the {@link File} representation of the file system element this object represents
     */
    protected File getFile() {
        return this.file;
    }

    /**
     * Returns the name of the file or directory denoted by this abstract pathname. This is just the last name in the
     * pathname's name sequence. If the pathname's name sequence is empty, then the empty string is returned.
     *
     * @return the name of the file or directory denoted by this abstract pathname, or the empty string if this
     *         pathname's name sequence is empty
     */
    public String getName() {
        return file.getName();
    }

    /**
     * Returns the absolute pathname string of this abstract pathname.
     *
     * @return the absolute pathname string denoting the same file or directory as this abstract pathname
     */
    public String getPath() {
        return file.getAbsolutePath();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(FileSystemElementInfo o) {
        int result = this.getClass().getSimpleName().compareTo(o.getClass().getSimpleName());
        if (result == 0) {
            result = this.getName().compareTo(o.getName());
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return file.getName();
    }
}
