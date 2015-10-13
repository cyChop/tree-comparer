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
package org.keyboardplaying.tree.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test class for {@link Variations}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class VariationsTest {

    /**
     * Tests {@link Variations#getNbVersions()}, {@link Variations#get(int)}, {@link Variations#set(int, Comparable)}
     * and Variations#getFirstNonNullVersion().
     */
    @Test
    public void testGetSet() {
        Variations<String> v = new Variations<>(2);
        assertEquals(2, v.getNbVersions());

        assertNull(v.get(0));
        assertNull(v.get(1));
        assertNull(v.getFirstNonNullVersion());

        v.set(1, "hello");
        assertNull(v.get(0));
        assertEquals("hello", v.get(1));
        assertEquals("hello", v.getFirstNonNullVersion());

        try {
            v.get(2);
            fail("An exception should have been thrown.");
        } catch (IndexOutOfBoundsException e) {
            // that's expected
        }
    }

    /** Tests {@link Variations#isConstant()}. */
    @Test
    public void testIsConstant() {
        Variations<String> v = new Variations<>(2);
        assertTrue(v.isConstant());

        v.set(0, "hello");
        assertFalse(v.isConstant());

        v.set(1, "world");
        assertFalse(v.isConstant());

        v.set(0, null);
        assertFalse(v.isConstant());

        v.set(0, "world");
        assertTrue(v.isConstant());
    }

    /** Tests {@link Variations#equals(Object)} and {@link Variations#compareTo(Variations)}. */
    @Test
    public void testEqualsAndCompareTo() {
        Variations<String> v1 = new Variations<>(2);
        Variations<String> v2 = new Variations<>(2);

        assertEquals(0, v1.compareTo(v2));
        assertFalse(v1.equals(null));
        assertFalse(v2.equals(null));
        assertTrue(v1.equals(v1));
        assertTrue(v2.equals(v2));
        assertTrue(v1.equals(v2));
        assertTrue(v2.equals(v1));

        v1.set(1, "hello");
        assertEquals(1, v1.compareTo(v2));
        assertEquals(-1, v2.compareTo(v1));
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));

        v2.set(0, "hello");
        assertEquals(0, v1.compareTo(v2));
        assertEquals(0, v2.compareTo(v1));
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));

        v1.set(1, null);
        assertEquals(-1, v1.compareTo(v2));
        assertEquals(1, v2.compareTo(v1));
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));

        v1.set(1, "world");
        assertEquals("world".compareTo("hello"), v1.compareTo(v2));
        assertEquals("hello".compareTo("world"), v2.compareTo(v1));
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));

        v1.set(0, "hello");
        assertEquals(0, v1.compareTo(v2));
        assertEquals(0, v2.compareTo(v1));
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));

        v1.set(1, null);
        assertTrue(v1.equals(v2));
        assertTrue(v2.equals(v1));
    }
}
