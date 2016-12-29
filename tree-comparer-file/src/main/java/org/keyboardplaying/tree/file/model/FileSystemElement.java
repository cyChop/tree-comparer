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
import java.util.Objects;

/**
 * A representation of a file system element for comparison.
 *
 * @author Cyrille Chopelet (https://keyboardplaying.org)
 */
public class FileSystemElement {

    /** The {@link File} representation of this file system element. */
    private final File file;

    /** The type of this element. */
    private final FileSystemElementType type;

    /** A checksum for the file. */
    private final String checksum;

    /**
     * Creates a new instance representing a file.
     *
     * @param file
     *            the {@link File} representation of this file system element
     * @param type
     *            the type of this element
     * @param checksum
     *            a checksum for the file; expected to be {@code null} for directories
     */
    public FileSystemElement(File file, FileSystemElementType type, String checksum) {
        Objects.requireNonNull(file, "The file cannot be null.");
        Objects.requireNonNull(type, "The type cannot be null.");
        this.file = file;
        this.type = type;
        this.checksum = checksum;
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

    /**
     * Returns the length of the file denoted by this abstract pathname.
     *
     * @return the length, in bytes, of the file denoted by this abstract pathname, or {@code 0L} if the file does not
     *         exist. Some operating systems may return {@code 0L} for pathnames denoting system-dependent entities such
     *         as devices or pipes.
     */
    public long getFileSize() {
        return file.length();
    }

    /**
     * Returns the time that the file denoted by this abstract pathname was last modified.
     *
     * @return a {@code long} value representing the time the file was last modified, measured in milliseconds since the
     *         epoch (00:00:00 GMT, January 1, 1970), or {@code 0L} if the file does not exist or if an I/O error occurs
     */
    public long getLastModified() {
        return file.lastModified();
    }

    /**
     * Returns the type for this file system element.
     *
     * @return the type
     */
    public FileSystemElementType getType() {
        return type;
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
        return file.hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof FileSystemElement && ((FileSystemElement) obj).file.equals(this.file);
    }
}
