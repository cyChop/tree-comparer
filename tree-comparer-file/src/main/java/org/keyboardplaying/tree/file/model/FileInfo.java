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
 * Describes a file on the file system.
 * <p/>
 * This {@link FileSystemElementInfo} describes a file (as opposed to a directory) on the file
 * system. Two files are deemed equal if they have the same name (case-sensitive), same weight and
 * same MD5 checksum.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class FileInfo extends FileSystemElementInfo {

    /** The MD5 checksum of the file. */
    private String checksum;

    /**
     * Creates a new instance.
     *
     * @param file
     *            a {@link File} representation of the file this node describes
     * @param checksum
     *            the MD5 checksum of the file
     */
    public FileInfo(File file, String checksum) {
        super(file);
        assert !file.isDirectory();
        this.checksum = checksum;
    }

    /**
     * Returns the length of the file denoted by this abstract pathname.
     *
     * @return the length, in bytes, of the file denoted by this abstract pathname, or {@code 0L} if
     *         the file does not exist. Some operating systems may return {@code 0L} for pathnames
     *         denoting system-dependent entities such as devices or pipes.
     */
    public long getFileSize() {
        return getFile().length();
    }

    /**
     * Returns the time that the file denoted by this abstract pathname was last modified.
     *
     * @return a {@code long} value representing the time the file was last modified, measured in
     *         milliseconds since the epoch (00:00:00 GMT, January 1, 1970), or {@code 0L} if the
     *         file does not exist or if an I/O error occurs
     */
    public long getLastModified() {
        return getFile().lastModified();
    }

    /**
     * Returns the MD5 checksum for this file.
     *
     * @return the MD5 checksum for this file
     */
    public String getChecksum() {
        return checksum;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getName().hashCode() + checksum.hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if (o instanceof FileInfo) {
            FileInfo fi = (FileInfo) o;
            equal = getFileSize() == fi.getFileSize() && getName().equals(fi.getName())
                    && getChecksum().equals(fi.getChecksum());
        }
        return equal;
    }
}
