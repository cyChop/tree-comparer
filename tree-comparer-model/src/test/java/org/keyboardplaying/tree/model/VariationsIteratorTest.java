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
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.keyboardplaying.tree.model.Variations.VariationsIterator;

/**
 * Test class for the VariationsIterator.
 *
 * @author Cyrille Chopelet (https://keyboardplaying.org)
 */
public class VariationsIteratorTest {

    private Iterator<String> iter;

    /**
     * Prepares the iterator for testing.
     */
    @Before
    public void init() {
        String[] array = {"hello", "world"};
        iter = new VariationsIterator<>(array);
    }

    /**
     * Tests the {@link Iterator#next()} and {@link Iterator#hasNext()} methods.
     */
    @Test(expected = NoSuchElementException.class)
    public void testNextAndHasNext() {
        assertTrue(iter.hasNext());
        assertEquals("hello", iter.next());

        assertTrue(iter.hasNext());
        assertEquals("world", iter.next());

        assertFalse(iter.hasNext());
        // No more element, will throw an exception
        iter.next();
    }

    /**
     * Tests the {@link Iterator#remove()} method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        iter.next();
        iter.remove();
    }
}
