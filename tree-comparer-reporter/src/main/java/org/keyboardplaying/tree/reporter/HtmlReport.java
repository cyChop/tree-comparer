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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.LinkedList;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.keyboardplaying.diff.plaintext.Diff;
import org.keyboardplaying.diff.plaintext.Operation;
import org.keyboardplaying.diff.plaintext.PlaintextDiff;
import org.keyboardplaying.tree.exception.PrintException;
import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Tree;
import org.keyboardplaying.tree.model.Versions;

/**
 * A tool to report a diffTree tree as an HTML page, based on Bootstrap style.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 *
 * @param <T>
 *            the type of the tree's ID
 * @param <U>
 *            the type of the tree's nodes' characteristics
 */
public class HtmlReport<T extends Comparable<T>, U extends Comparable<U>> {

    /** The result of a comparison to display as a report. */
    private Tree<Versions<T>, Versions<U>> diffTree;
    private NodePrinter<T, U> printer;
    /**
     * The width of each column in the report.
     * <p/>
     * This is based on Bootstrap's 12-column grid system.
     */
    private int colWidth;

    /**
     * Creates a new instance.
     *
     * @param diff
     *            the result of a comparison to print as a report
     * @param printer
     *            the printer for this report
     *
     */
    public HtmlReport(Tree<Versions<T>, Versions<U>> diff, NodePrinter<T, U> printer) {
        this.diffTree = diff;
        this.printer = printer;
        /* We use Bootstrap, which is a 12-column based grid system. */
        this.colWidth = 12 / diff.getId().getNbVersions();
    }

    /**
     * Generates the report as an HTML page.
     *
     * @throws IOException
     *             if an I/0 exception occurs
     * @throws PrintException
     *             if printing fails
     */
    public void generate() throws IOException, PrintException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.html"))) {

            IOUtils.write("<!DOCTYPE html><html>", writer);
            // write header with bootstrap style
            printHtmlHeaders(writer);

            // write the body
            IOUtils.write("<body style=\"padding: 10px\">", writer);
            printHeader(diffTree.getId(), writer);
            print(diffTree.getRoot(), writer);
            IOUtils.write("</body>", writer);

            // end HTML
            IOUtils.write("</html>", writer);
        }
    }

    /**
     * Prints the {@code &lt;head/&gt;} section of the HTML page.
     *
     * @param writer
     *            the {@link Writer} used to generate the report
     * @throws IOException
     *             if an I/O error occurs
     */
    private void printHtmlHeaders(BufferedWriter writer) throws IOException {
        IOUtils.write("<head>", writer);
        IOUtils.write("<meta charset=\"UTF-8\">", writer);
        IOUtils.write("<title>Comparison report</title>", writer);
        IOUtils.write("<style>", writer);

        // Copy stylesheet
        try (InputStream style = this.getClass().getClassLoader().getResourceAsStream("bootstrap.min.css")) {
            IOUtils.copy(style, writer);
        }

        // enhance diffTree style a bit
        writer.write(".text-success{background-color:#ccffcc}" + ".text-danger{background-color:#ffcccc}"
                + "pre{white-space:pre;max-height: 500px;overflow-x:auto;overflow-y:auto}");
        IOUtils.write("</style>", writer);
        IOUtils.write("</head>", writer);
    }

    /**
     * Prints the versions of the tree IDs as a header line in the report.
     *
     * @param ids
     *            the versions of the tree IDs to print
     * @param writer
     *            the {@link Writer} used to generate the report
     * @throws IOException
     *             if an I/0 error occurs
     * @throws PrintException
     *             if printing fails
     */
    private void printHeader(Versions<T> ids, BufferedWriter writer) throws IOException, PrintException {
        IOUtils.write("<div class=\"row\">", writer);
        for (T id : ids) {
            openCellDiv(writer);
            IOUtils.write("<h4>" + printer.printId(id) + "</h4></div>", writer);
        }
        IOUtils.write("</div>", writer);
    }

    /**
     * Prints all the versions of a node on a line.
     *
     * @param node
     *            the node containing the various versions to print
     * @param writer
     *            the {@link Writer} used to generate the report
     * @throws IOException
     *             if an I/O exception error
     * @throws PrintException
     *             if printing fails
     */
    private void print(Node<Versions<U>> node, Writer writer) throws IOException, PrintException {
        boolean constant = node.getNodeInfo().isConstant();
        if (constant) {
            IOUtils.write("<div class=\"row text-muted\">", writer);
        } else {
            IOUtils.write("<div class=\"row\">", writer);
        }
        for (int v = 0; v < node.getNodeInfo().getNbVersions(); v++) {
            U version = node.getNodeInfo().get(v);
            T id = this.diffTree.getId().get(v);
            openCellDiv(writer);
            IOUtils.write(printer.printHeader(id, version), writer);
            IOUtils.write("</div>", writer);
        }
        IOUtils.write("</div>", writer);
        if (!constant) {
            printDiff(writer, node.getNodeInfo());
        }
        for (Node<Versions<U>> child : node.getChildren()) {
            print(child, writer);
        }
    }

    /**
     * Prints the content of the files of this {@link Versions} instance.
     * <p/>
     * The first non-null version will be displayed as is for reference. Next versions will be displayed and differences
     * with reference file will be highlighted.
     *
     * @param writer
     *            the {@link Writer} used to generate the report
     * @param versions
     *            the versions of the file to print
     * @throws IOException
     *             if an I/O error occurs
     * @throws PrintException
     *             if printing fails
     */
    private void printDiff(Writer writer, Versions<U> versions) throws IOException, PrintException {
        String ref = null;
        IOUtils.write("<div class=\"row\">", writer);
        for (U version : versions) {
            openCellDiv(writer);
            if (version != null && printer.isContentPrintable(version)) {
                IOUtils.write("<pre>", writer);
                if (ref == null) {
                    ref = escapeHtml(printer.printContent(version));
                    IOUtils.write(ref, writer);
                } else {
                    String content = escapeHtml(printer.printContent(version));
                    PlaintextDiff diff = new PlaintextDiff();
                    LinkedList<Diff> delta = diff.diff(ref, content);
                    diff.cleanupSemantic(delta);
                    if (delta.size() == 1 && delta.get(0).operation == Operation.EQUAL) {
                        IOUtils.write("<em>&lt;no difference&gt;</em>", writer);
                    } else {
                        for (Diff deltum : delta) {
                            switch (deltum.operation) {
                            case INSERT:
                                IOUtils.write("<span class=\"text-success\">", writer);
                                IOUtils.write(escapeHtml(deltum.text), writer);
                                IOUtils.write("</span>", writer);
                                break;
                            case DELETE:
                                IOUtils.write("<s class=\"text-danger\">", writer);
                                IOUtils.write(escapeHtml(deltum.text), writer);
                                IOUtils.write("</s>", writer);
                                break;
                            case EQUAL:
                            default:
                                IOUtils.write(escapeHtml(deltum.text), writer);
                                break;
                            }
                        }
                    }
                }
                IOUtils.write("</pre>", writer);
            }
            IOUtils.write("</div>", writer);
        }
        IOUtils.write("</div>", writer);
    }

    private String escapeHtml(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }

    /**
     * Opens a div to display the result comparison for a determined file or directory.
     *
     * @param writer
     *            the {@link Writer} used to generate the report
     * @throws IOException
     *             if an I/O error occurs
     */
    private void openCellDiv(Writer writer) throws IOException {
        IOUtils.write("<div class=\"col-xs-" + colWidth + "\">", writer);
    }
}
