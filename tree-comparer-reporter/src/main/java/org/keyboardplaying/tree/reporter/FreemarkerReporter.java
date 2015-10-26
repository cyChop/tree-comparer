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
package org.keyboardplaying.tree.reporter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;

import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Variations;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

/**
 * A utility class to print an aligned tree to a report using a Freemarker template.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class FreemarkerReporter {

    /** The template for an HTML */
    public static final String TPL_HTML_BOOTSTRAP = "bootstrap/template.ftl";

    private static final String ENCODING_UTF8 = "UTF-8";

    private static Configuration cfg;

    protected static Configuration getConfiguration() {
        if (cfg == null) {
            cfg = new Configuration(Configuration.VERSION_2_3_23);
            try {
                cfg.setDirectoryForTemplateLoading(new File(FreemarkerReporter.class.getResource("templates").toURI()));
            } catch (URISyntaxException | IOException e) {
                // should never happen
                // TODO log
            }
            cfg.setDefaultEncoding(ENCODING_UTF8);
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        }
        return cfg;
    }

    /**
     * Generates a report from a template and an aligned tree.
     *
     * @param template
     *            the template to use
     * @param aligned
     *            the tree of aligned nodes
     * @param writer
     *            the writer to write the report to
     *
     * @throws TemplateNotFoundException
     *             if the template cannot be found.
     * @throws MalformedTemplateNameException
     *             if the template name is incorrect.
     * @throws ParseException
     * @throws IOException
     *             if an I/O exception occurs during writing to the writer.
     * @throws TemplateException
     *             if an exception occurs during template processing.
     */
    // FIXME finish implementing, add node wrapper to display content
    public <T> void generateReport(String template, Node<Variations<T>> aligned, Writer writer)
            throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException,
            IOException {
        Template tpl = getConfiguration().getTemplate(template);
        tpl.process(aligned, writer);
    }
}