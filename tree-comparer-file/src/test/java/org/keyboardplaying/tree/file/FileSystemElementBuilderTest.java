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
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.keyboardplaying.tree.file.model.FileSystemElement;
import org.keyboardplaying.tree.file.model.FileSystemElementType;

/**
 * Test class for {@link FileSystemElementBuilder}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class FileSystemElementBuilderTest {

    private FileSystemElementBuilder builder = new FileSystemElementBuilder();

    /**
     * Tests {@link FileSystemElementBuilder#buildDirectoryElement(File)}.
     */
    @Test
    public void testBuildDirectoryElement() {
        /* Prepare */
        File file = new File("src/test/resources/version1");

        /* Execute */
        FileSystemElement fse = builder.buildDirectoryElement(file);

        /* Assert */
        assertEquals(FileSystemElementType.DIRECTORY, fse.getType());
        assertNull(fse.getChecksum());
    }

    /**
     * Tests {@link FileSystemElementBuilder#buildFileElement(File)} with a text file.
     */
    @SuppressWarnings("javadoc")
    @Test
    public void testBuildTextFileElement() throws IOException {
        /* Prepare */
        File file = new File("src/test/resources/version1/hello.properties");

        /* Execute */
        FileSystemElement fse = builder.buildFileElement(file);

        /* Assert */
        assertEquals(FileSystemElementType.TEXT, fse.getType());
        assertEquals("9e60e9c13569a9ece7ae20fd5798e0cd", fse.getChecksum());
    }

    /**
     * Tests {@link FileSystemElementBuilder#buildFileElement(File)} with a binary file.
     */
    @SuppressWarnings("javadoc")
    @Test
    public void testBuildBinaryFileElement() throws IOException {
        /* Prepare */
        File file = new File("src/test/resources/version1/directory/clouded-lava.jpg");

        /* Execute */
        FileSystemElement fse = builder.buildFileElement(file);

        /* Assert */
        assertEquals(FileSystemElementType.BINARY, fse.getType());
        assertEquals("454a02dcd0e797bd93737b92cad0652d", fse.getChecksum());
    }

    /**
     * Tests {@link FileSystemElementBuilder#buildFileElement(File)} with an empty file.
     */
    @SuppressWarnings("javadoc")
    @Test
    public void testBuildEmptyFileElement() throws IOException {
        /* Prepare */
        File file = new File("src/test/resources/version1/empty.log");

        /* Execute */
        FileSystemElement fse = builder.buildFileElement(file);

        /* Assert */
        assertEquals(FileSystemElementType.TEXT, fse.getType());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", fse.getChecksum());
    }

    /**
     * Tests {@link FileSystemElementBuilder#buildFileElement(File)} with SHA-1 as checksum algorithm.
     */
    @SuppressWarnings("javadoc")
    @Test
    public void testSha1Algorithm() throws IOException, NoSuchAlgorithmException {
        /* Prepare */
        File file = new File("src/test/resources/version1/hello.properties");

        /* Execute */
        builder.setChecksumAlgorithm(FileSystemElementBuilder.CHECKSUM_ALGORITHM_SHA1);
        FileSystemElement fse = builder.buildFileElement(file);

        /* Assert */
        assertEquals("140b3e00577c52dfc94c728da93fbd3ba1e0ee1e", fse.getChecksum());
    }

    /**
     * Tests {@link FileSystemElementBuilder#buildFileElement(File)} with SHA-256 as checksum algorithm.
     */
    @SuppressWarnings("javadoc")
    @Test
    public void testSha256Algorithm() throws IOException, NoSuchAlgorithmException {
        /* Prepare */
        File file = new File("src/test/resources/version1/hello.properties");

        /* Execute */
        builder.setChecksumAlgorithm(FileSystemElementBuilder.CHECKSUM_ALGORITHM_SHA256);
        FileSystemElement fse = builder.buildFileElement(file);

        /* Assert */
        assertEquals("03a414fafc012133e3ed48953590ae76feb50a6e4143decc3dc086ed9e0fe71c", fse.getChecksum());
    }

    /**
     * Tests {@link FileSystemElementBuilder#buildFileElement(File)} with an unknown checksum algorithm.
     */
    @SuppressWarnings("javadoc")
    @Test(expected = NoSuchAlgorithmException.class)
    public void testUnknownAlgorithm() throws NoSuchAlgorithmException {
        /* Execute */
        builder.setChecksumAlgorithm("theBestAlgorithmEver");
    }
}
