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
import java.io.IOException;
import java.io.InputStream;
import java.rmi.UnexpectedException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.keyboardplaying.tree.file.model.FileSystemElement;
import org.keyboardplaying.tree.file.model.FileSystemElementType;

/**
 * A utility to create {@link FileSystemElement} representations of {@link File} instances.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class FileSystemElementBuilder {

    private static final String CHECKSUM_ALGORITHM = "MD5";
    private static final int CHUNK_SIZE = 1024;
    private static final float TXT_ASCII_DENSITY = 0.95F;

    /**
     * Builds a {@link FileSystemElement} for a directory.
     *
     * @param directory
     *            the {@link File} representation of the directory
     * @return the {@link FileSystemElement} representation of the directory
     */
    public FileSystemElement buildDirectoryElement(File directory) {
        return new FileSystemElement(directory, FileSystemElementType.DIRECTORY, null);
    }

    /**
     * Builds a {@link FileSystemElement} for a file.
     * <p/>
     * This includes binary vs. text detection and MD5 checksum computation.
     *
     * @param file
     *            the {@link File} representation of the file
     * @return the {@link FileSystemElement} representation of the directory
     * @throws IOException
     */
    public FileSystemElement buildFileElement(File file) throws IOException {
        try {
            return doBuildFileElement(file);
        } catch (NoSuchAlgorithmException e) {
            // will not happen
            throw new UnexpectedException(e.getMessage(), e);
        }
    }

    private FileSystemElement doBuildFileElement(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(CHECKSUM_ALGORITHM);

        FileSystemElementType type;
        try (InputStream is = new FileInputStream(file); DigestInputStream dis = new DigestInputStream(is, md)) {
            type = isAsciiFile(dis) ? FileSystemElementType.TEXT : FileSystemElementType.BINARY;
        }
        String digest = digestToHexString(md.digest());

        return new FileSystemElement(file, type, new String(digest));
    }

    private boolean isAsciiFile(InputStream is) throws IOException {
        long asciiChars = 0;
        long otherChars = 0;
        int size;

        do {
            /* How many bytes we read for this chunk. */
            size = is.available();
            if (size > CHUNK_SIZE) {
                size = CHUNK_SIZE;
            }

            /* Do read. */
            byte[] data = new byte[size];
            is.read(data);

            /* Count ASCII/binary characters. */
            for (byte b : data) {
                if (Character.isWhitespace((char) b) || b >= 0x20 && b <= 0x7E) {
                    asciiChars++;
                } else {
                    otherChars++;
                }
            }
        } while (size == CHUNK_SIZE);

        /* Text if the density of ASCII characters is over the threshold. */
        return otherChars == 0 || (float) asciiChars / (otherChars + asciiChars) > TXT_ASCII_DENSITY;
    }

    private String digestToHexString(byte[] digest) {
        return new HexBinaryAdapter().marshal(digest).toLowerCase();
    }
}
