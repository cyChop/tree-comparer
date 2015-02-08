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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.keyboardplaying.diff.plaintext.Diff;
import org.keyboardplaying.diff.plaintext.Operation;
import org.keyboardplaying.diff.plaintext.PlaintextDiff;
import org.keyboardplaying.tree.diff.model.Versions;
import org.keyboardplaying.tree.file.model.DirectoryInfo;
import org.keyboardplaying.tree.file.model.FileInfo;
import org.keyboardplaying.tree.file.model.FileSystemElementInfo;
import org.keyboardplaying.tree.file.util.ByteSizeUtils;
import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Tree;

/**
 * Prints the result of the comparison of up to twelve
 * {@link org.keyboardplaying.tree.file.model.FileTree} to an HTML page.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class Report {

    /**
     * The result of a {@link org.keyboardplaying.tree.file.model.FileTree} comparison to display as
     * a report.
     */
    private Tree<Versions<String>, Versions<FileSystemElementInfo>> diff;
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
     *            the result of a {@link org.keyboardplaying.tree.file.model.FileTree} comparison to
     *            display as a report
     */
    public Report(Tree<Versions<String>, Versions<FileSystemElementInfo>> diff) {
        this.diff = diff;
        /* We use Bootstrap, which is a 12-column based grid system. */
        this.colWidth = 12 / diff.getId().getNbVersions();
    }

    /**
     * Generates the report as an HTML page.
     *
     * @throws IOException
     *             if an I/0 exception occurs
     */
    public void generate() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("report.html"));

        writer.write("<!DOCTYPE html><html>");
        // write header with bootstrap style
        printHtmlHeaders(writer);

        // write the body
        writer.write("<body style=\"padding: 10px\">");
        printHeader(diff.getId(), writer);
        print(diff.getRoot(), writer);
        writer.write("</body>");

        // end HTML
        writer.write("</html>");

        IOUtils.closeQuietly(writer);
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
        writer.write("<head>");
        writer.write("<meta charset=\"UTF-8\">");
        writer.write("<title>Comparison report</title>");
        writer.write("<style>");
        InputStream style = this.getClass().getClassLoader()
                .getResourceAsStream("bootstrap.min.css");
        IOUtils.copy(style, writer);
        IOUtils.closeQuietly(style);
        // enhance diff style a bit
        writer.write(".text-success{background-color:#ccffcc}"
                + ".text-danger{background-color:#ffcccc}"
                + "pre{white-space:pre;max-height: 500px;overflow-x:auto;overflow-y:auto}");
        writer.write("</style>");
        writer.write("</head>");
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
     */
    private void printHeader(Versions<String> ids, BufferedWriter writer) throws IOException {
        writer.write("<div class=\"row\">");
        for (String path : ids) {
            openCellDiv(writer);
            writer.write("<h4>" + path + "</h4></div>");
        }
        writer.write("</div>");
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
     */
    private void print(Node<Versions<FileSystemElementInfo>> node, Writer writer)
            throws IOException {
        boolean constant = node.getNodeInfo().isConstant();
        boolean diff = false;
        if (constant) {
            writer.write("<div class=\"row text-muted\">");
        } else {
            writer.write("<div class=\"row\">");
        }
        for (int v = 0; v < node.getNodeInfo().getNbVersions(); v++) {
            openCellDiv(writer);
            if (node.getNodeInfo().get(v) != null) {
                if (node.getNodeInfo().get(v) instanceof DirectoryInfo) {
                    printDirLine(node, writer, v);
                } else {
                    diff = !constant;
                    printFileLine(node, writer, v);
                }
            }
            writer.write("</div>");
        }
        writer.write("</div>");
        if (diff) {
            printDiff(writer, node.getNodeInfo());
        }
        for (Node<Versions<FileSystemElementInfo>> child : node.getChildren()) {
            print(child, writer);
        }
    }

    /**
     * Prints information about a directory.
     * <p/>
     * Printed line is the relative path in bold.
     *
     * @param node
     *            the node containing the version which should be printed
     * @param writer
     *            the {@link Writer} used to generate the report
     * @param version
     *            the index of the version to print
     * @throws IOException
     *             if an I/O error occurs
     */
    private void printDirLine(Node<Versions<FileSystemElementInfo>> node, Writer writer, int version)
            throws IOException {
        writer.write("<b>+ " + getPath(node, version) + "</b>");
    }

    /**
     * Prints information about a file.
     * <p/>
     * Printed information contain:
     * <ul>
     * <li>the relative path;</li>
     * <li>the size;</li>
     * <li>the last modification time;</li>
     * <li>the checksum.</li>
     * </ul>
     *
     * @param node
     *            the node containing the version which should be printed
     * @param writer
     *            the {@link Writer} used to generate the report
     * @param version
     *            the index of the version to print
     * @throws IOException
     *             if an I/O error occurs
     */
    private void printFileLine(Node<Versions<FileSystemElementInfo>> node, Writer writer,
            int version) throws IOException {
        FileInfo info = (FileInfo) node.getNodeInfo().get(version);
        writer.write(String.format(
                "&nbsp;&nbsp; %s <small>(%s, %3$tY-%3$tm-%3$te %3$tH:%3$tM:%3$tS, %s)</small>",
                getPath(node, version), ByteSizeUtils.getHumanReadable(info.getFileSize(), false),
                info.getLastModified(), info.getChecksum()));
    }

    /**
     * Returns the path to the file.
     * <p/>
     * The path is relative to the corresponding
     * {@link org.keyboardplaying.tree.file.model.FileTree}'s root.
     *
     * @param node
     *            the node containing the version whose path should be returned
     * @param version
     *            the index of the version to return
     * @return the path to the file, relative to the corresponding
     *         {@link org.keyboardplaying.tree.file.model.FileTree}'s root
     */
    private String getPath(Node<Versions<FileSystemElementInfo>> node, int version) {
        int rootLength = this.diff.getId().get(version).length();
        String path = node.getNodeInfo().get(version).getPath();
        return path.length() > rootLength + 3 ? path.substring(rootLength + 3)
                : path.length() > rootLength + 1 ? path.substring(rootLength + 1) : path;
    }

    /**
     * Prints the content of the files of this {@link Versions} instance.
     * <p/>
     * The first non-null version will be displayed as is for reference. Next versions will be
     * displayed and differences with reference file will be highlighted.
     *
     * @param writer
     *            the {@link Writer} used to generate the report
     * @param versions
     *            the versions of the file to print
     * @throws IOException
     *             if an I/O error occurs
     */
    private void printDiff(Writer writer, Versions<FileSystemElementInfo> versions)
            throws IOException {
        String ref = null;
        writer.write("<div class=\"row\">");
        for (int i = 0; i < versions.getNbVersions(); i++) {
            openCellDiv(writer);
            FileSystemElementInfo version = versions.get(i);
            if (version != null) {
                writer.write("<pre>");
                if (ref == null) {
                    ref = prepFileForHtml(version.getPath());
                    writer.write(ref);
                } else {
                    String file = prepFileForHtml(version.getPath());
                    PlaintextDiff diff = new PlaintextDiff();
                    LinkedList<Diff> delta = diff.diff(ref, file);
                    diff.cleanupSemantic(delta);
                    if (delta.size() == 1 && delta.get(0).operation == Operation.EQUAL) {
                        writer.write("No difference");
                    } else {
                        for (Diff deltum : delta) {
                            switch (deltum.operation) {
                            case INSERT:
                                writer.write("<span class=\"text-success\">");
                                writer.write(deltum.text);
                                writer.write("</span>");
                                break;
                            case DELETE:
                                writer.write("<s class=\"text-danger\">");
                                writer.write(deltum.text);
                                writer.write("</s>");
                                break;
                            case EQUAL:
                            default:
                                writer.write(deltum.text);
                                break;
                            }
                        }
                    }
                }
                writer.write("</pre>");
            }
            writer.write("</div>");
        }
        writer.write("</div>");
    }

    /**
     * Returns the content of a file as a {@link String}. Special characters will be escaped to
     * display in an HTML page.
     *
     * @param path
     *            the path of the file to render
     * @return an HTML-escaped representation of the file
     * @throws IOException
     *             if an I/O error occurs
     */
    private String prepFileForHtml(String path) throws IOException {
        // XXX if the file is binary, should display "Binary" instead of content
        return StringEscapeUtils.escapeHtml(FileUtils.readFileToString(new File(path)));
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
        writer.write("<div class=\"col-xs-" + colWidth + "\">");
    }
}
