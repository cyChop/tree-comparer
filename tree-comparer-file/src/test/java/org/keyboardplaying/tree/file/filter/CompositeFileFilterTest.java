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
package org.keyboardplaying.tree.file.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link CompositeFileFilter}.
 *
 * @author Cyrille Chopelet (https://keyboardplaying.org)
 */
public class CompositeFileFilterTest {

    private FileFilter filter = new CompositeFileFilter(Arrays.asList(new DirectoryFilter(), new HiddenFileFilter()));

    /**
     * Tests file rejection from first filter.
     */
    @Test
    public void testDirectoryFiltering() {
        /* Prepare */
        File file = new File("src/test/resources/version1/directory");

        /* Execute and assert */
        assertFalse(filter.accept(file));
    }

    /**
     * Tests file rejection from non-first filter.
     */
    @Test
    public void testHiddenFileFiltering() {
        /* Prepare */
        File file = new File("src/test/resources/version1/.htaccess");

        /* Execute and assert */
        assertFalse(filter.accept(file));
    }

    /**
     * Tests file acceptance from all filters.
     */
    @Test
    public void testNormalFileFiltering() {
        /* Prepare */
        File file = new File("src/test/resources/version1/empty.log");

        /* Execute and assert */
        assertTrue(filter.accept(file));
    }

    /**
     * Tests file acceptance if the composite is empty.
     */
    @Test
    public void testEmptyCompositeFilter() {
        /* Prepare */
        File file = new File("src/test/resources/version1/empty.log");

        /* Execute and assert */
        assertTrue(new CompositeFileFilter(new ArrayList<>()).accept(file));
    }
}
