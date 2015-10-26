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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.keyboardplaying.tree.model.Variations.VariationsIterator;

/**
 * Test class for {@link Variations}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class VariationsTest {

    /** Tests {@link Variations#size()}, {@link Variations#get(int)} and {@link Variations#set(int, Object)}. */
    @Test
    public void testGetSet() {
        Variations<String> v = new Variations<>(2);
        assertEquals(2, v.size());

        assertNull(v.get(0));
        assertNull(v.get(1));

        v.set(1, "hello");
        assertNull(v.get(0));
        assertEquals("hello", v.get(1));

        try {
            v.get(2);
            fail("An exception should have been thrown.");
        } catch (IndexOutOfBoundsException e) {
            // that's expected
        }
    }

    /** Tests {@link Variations#Variations(java.util.List)}. */
    @Test
    public void testListConstructor() {
        Variations<String> v = new Variations<>(Arrays.asList("hello", "world"));
        assertEquals(2, v.size());
        assertEquals("hello", v.get(0));
        assertEquals("world", v.get(1));
        assertEquals("Variations[hello, world]", v.toString());

        List<String> list = new ArrayList<>(2);
        list.add("hello");
        list.add(null);
        v = new Variations<>(list);
        assertEquals(2, v.size());
        assertEquals("hello", v.get(0));
        assertNull(v.get(1));
    }

    /** Tests {@link Variations#add(Object)}. */
    @Test
    public void testAdd() {
        Variations<String> v = new Variations<>(3);
        v.add("id0");
        v.add("id1");
        assertEquals("id1", v.get(1));
        v.set(2, "id2");
        assertEquals("id2", v.get(2));
        v.add("id21");
        assertEquals("id21", v.get(2));

        try {
            v.add("id3");
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

    /** Tests {@link Variations#equals(Object)} and {@link Variations#hashCode()}. */
    @Test
    public void testEqualsAndHashCode() {
        Variations<String> v1 = new Variations<>(2);
        Variations<String> v2 = new Variations<>(2);

        assertFalse(v1.equals(null));
        assertFalse(v2.equals(null));
        assertTrue(v1.equals(v1));
        assertTrue(v2.equals(v2));
        assertTrue(v1.equals(v2));
        assertTrue(v2.equals(v1));

        v1.set(1, "hello");
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));

        v2.set(0, "hello");
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));

        v1.set(1, null);
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));

        v1.set(1, "world");
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));

        v1.set(0, "hello");
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));

        v1.set(1, null);
        assertTrue(v1.equals(v2));
        assertTrue(v2.equals(v1));
        assertTrue(v1.hashCode() == v2.hashCode());
    }

    /** Tests {@link Variations#iterator()}. */
    @Test
    public void testIterator() {
        assertTrue(new Variations<String>(2).iterator() instanceof VariationsIterator);
    }
}
