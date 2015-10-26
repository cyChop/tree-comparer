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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

/**
 * Test class for {@link FileSystemElement}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class FileSystemElementTest {

    /**
     * Tests {@link FileSystemElement#FileSystemElement(File, FileSystemElementType, String)} without providing a
     * {@link File}.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithoutFile() {
        @SuppressWarnings("unused")
        FileSystemElement elm = new FileSystemElement(null, FileSystemElementType.BINARY, "my5up3rch3ck5um");
    }

    /**
     * Tests {@link FileSystemElement#FileSystemElement(File, FileSystemElementType, String)} without providing a
     * {@link FileSystemElementType}.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithoutType() {
        @SuppressWarnings("unused")
        FileSystemElement elm = new FileSystemElement(
                new File("src/test/resources/version1/directory/something-in-here.txt"), null, "my5up3rch3ck5um");
    }

    /**
     * Tests {@link FileSystemElement#FileSystemElement(File, FileSystemElementType, String)} and getters when no
     * checksum was supplied.
     */
    @Test
    public void testFullConstructor() {
        /* Prepare */
        File file = new File("src/test/resources/version1");

        /* Execute */
        FileSystemElement elm = new FileSystemElement(file, FileSystemElementType.DIRECTORY, null);

        /* Assert */
        assertEquals(file, elm.getFile());
        assertEquals("version1", elm.getName());
        assertEquals(file.getAbsolutePath(), elm.getPath());
        assertEquals(file.length(), elm.getFileSize());
        assertEquals(file.lastModified(), elm.getLastModified());
        assertEquals(FileSystemElementType.DIRECTORY, elm.getType());
        assertNull(elm.getChecksum());
    }

    /**
     * Tests {@link FileSystemElement#FileSystemElement(File, FileSystemElementType, String)} and getters when all
     * parameters were supplied.
     */
    @Test
    public void testConstructorWithoutChecksum() {
        /* Prepare */
        File file = new File("src/test/resources/version1/directory/clouded-lava.jpg");
        String md5 = "454a02dcd0e797bd93737b92cad0652d";

        /* Execute */
        FileSystemElement elm = new FileSystemElement(file, FileSystemElementType.BINARY, md5);

        /* Assert */
        assertEquals(file, elm.getFile());
        assertEquals("clouded-lava.jpg", elm.getName());
        assertEquals(file.getAbsolutePath(), elm.getPath());
        assertEquals(file.length(), elm.getFileSize());
        assertEquals(file.lastModified(), elm.getLastModified());
        assertEquals(FileSystemElementType.BINARY, elm.getType());
        assertEquals(md5, elm.getChecksum());
    }

    /** Tests {@link FileSystemElement#equals(Object)} and {@link FileSystemElement#hashCode()}. */
    @Test
    public void testEqualsAndHashCode() {
        /* Prepare */
        File file1 = new File("src/test/resources/version1");
        File file2 = new File("src/test/resources/version1/directory/clouded-lava.jpg");

        /* Execute */
        FileSystemElement elm1 = new FileSystemElement(file1, FileSystemElementType.DIRECTORY, null);
        FileSystemElement elm2 = new FileSystemElement(file2, FileSystemElementType.BINARY,
                "454a02dcd0e797bd93737b92cad0652d");
        FileSystemElement elm3 = new FileSystemElement(file2, FileSystemElementType.TEXT, "my5up3rch3ck5um");

        /* Assert */
        // equals
        assertFalse(elm1.equals(file1));
        assertFalse(elm1.equals(elm2));
        assertFalse(elm2.equals(elm1));
        assertTrue(elm1.equals(elm1));
        assertTrue(elm2.equals(elm2));
        assertTrue(elm3.equals(elm2));
        assertTrue(elm2.equals(elm3));
        // hashcode
        assertTrue(elm2.hashCode() == elm3.hashCode());
    }
}
