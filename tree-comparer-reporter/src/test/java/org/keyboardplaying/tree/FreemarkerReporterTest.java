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
package org.keyboardplaying.tree;

import org.junit.Test;
import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Variations;
import org.keyboardplaying.tree.reporter.FreemarkerReporter;
import org.keyboardplaying.tree.reporter.ReporterException;

import java.io.StringWriter;
import java.util.Arrays;

/**
 * Test class for {@link FreemarkerReporter}.
 *
 * @author Cyrille Chopelet (https://keyboardplaying.org)
 */
// TODO make some more relevant tests
public class FreemarkerReporterTest {

    /**
     * Ensures the reporter loads correctly.
     */
    @Test
    @SuppressWarnings("javadoc")
    public void testInit() throws ReporterException {
        Variations<String> v = new Variations<>(Arrays.asList("hello", "world"));
        Node<Variations<String>> node = new Node<>(v);

        FreemarkerReporter reporter = new FreemarkerReporter();
        StringWriter writer = new StringWriter();
        reporter.generateReport(FreemarkerReporter.TPL_HTML_BOOTSTRAP, node, v, writer);
        System.out.println(writer.toString());
    }
}
