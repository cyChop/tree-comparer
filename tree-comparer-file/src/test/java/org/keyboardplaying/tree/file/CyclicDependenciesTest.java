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

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;

/**
 * Ensures that there is no package dependency cycle.
 *
 * @see <a href="http://blog.mafr.de/2010/10/02/java-finding-package-cycles/">The inspirational blog post</a>
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class CyclicDependenciesTest {

    /** The {@link JDepend} instance used to run tests. */
    private JDepend jdepend;

    /**
     * Initializes {@link JDepend}.
     *
     * @throws IOException
     *             when initialization fails
     */
    @Before
    public void initJdepend() throws IOException {
        jdepend = new JDepend();
        jdepend.addDirectory("target/classes");

        jdepend.analyze();
    }

    /** Ensure there is no package cycle. */
    @Test
    public void testCycles() {
        if (jdepend.containsCycles()) {
            StringBuilder sb = new StringBuilder("The following packages contain cycles which should be removed.");

            for (Object element : jdepend.getPackages()) {
                JavaPackage pack = (JavaPackage) element;
                if (pack.containsCycle()) {
                    /*
                     * Append chars to avoid instantiating strings.
                     *
                     * Micro-optimisation in a test class _is_ ridiculous, don't you think? You are allowed to laugh at
                     * me on this one.
                     */
                    sb.append('\n').append('\t').append(pack.getName());
                }
            }
            fail(sb.toString());
        }
    }
}
