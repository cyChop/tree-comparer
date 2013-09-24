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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.keyboardplaying.tree.diff.model.Versions;
import org.keyboardplaying.tree.file.model.DirectoryInfo;
import org.keyboardplaying.tree.file.model.FileInfo;
import org.keyboardplaying.tree.file.model.FileSystemElementInfo;
import org.keyboardplaying.tree.file.util.DiffMatchPatch;
import org.keyboardplaying.tree.file.util.DiffMatchPatch.Diff;
import org.keyboardplaying.tree.file.util.FileSizeUtils;
import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Tree;

// XXX JAVADOC
/**
 * @author cyChop (http://keyboardplaying.org/)
 */
public class Report {

	private Tree<Versions<String>, Versions<FileSystemElementInfo>> diff;
	private int colWidth;

	public Report(Tree<Versions<String>, Versions<FileSystemElementInfo>> diff) {
		this.diff = diff;
		this.colWidth = 12 / diff.getId().getNbVersions();
	}

	public void generate() throws FileNotFoundException, IOException {
		BufferedWriter writer = new BufferedWriter(
				new FileWriter("report.html"));

		writer.write("<html>");
		// write header with bootstrap style
		printHtmlHeaders(writer);

		// write the body
		writer.write("<body style=\"padding: 10px\">");
		printHeader(diff, writer);
		print(diff.getRoot(), writer);
		writer.write("</body>");

		// end HTML
		writer.write("</html>");

		IOUtils.closeQuietly(writer);
	}

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
				+ "pre{white-space:nowrap;overflow-x:auto}");
		writer.write("</style>");
		writer.write("</head>");
	}

	private void printHeader(
			Tree<Versions<String>, Versions<FileSystemElementInfo>> result,
			BufferedWriter writer) throws IOException {
		writer.write("<div class=\"row\">");
		for (String path : result.getId()) {
			openCellDiv(writer);
			writer.write("<h4>" + path + "</h4></div>");
		}
		writer.write("</div>");
	}

	private void print(Node<Versions<FileSystemElementInfo>> node, Writer writer)
			throws IOException {
		boolean constant = node.getNodeInfo().isConstant();
		boolean diff = false;
		writer.write("<div class=\"row\">");
		if (constant) {
			writer.write("<span class=\"text-muted\">");
		}
		for (int i = 0; i < node.getNodeInfo().getNbVersions(); i++) {
			openCellDiv(writer);
			if (node.getNodeInfo().get(i) != null) {
				if (node.getNodeInfo().get(i) instanceof DirectoryInfo) {
					printDirLine(node, writer, i);
				} else {
					diff = !constant;
					printFileLine(node, writer, i);
				}
			}
			if (constant) {
				writer.write("</span>");
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

	private void printDirLine(Node<Versions<FileSystemElementInfo>> node,
			Writer writer, int version) throws IOException {
		writer.write("<b>+ " + getPath(node, version) + "</b>");
	}

	private void printFileLine(Node<Versions<FileSystemElementInfo>> node,
			Writer writer, int version) throws IOException {
		FileInfo info = (FileInfo) node.getNodeInfo().get(version);
		writer.write(String
				.format("&nbsp;&nbsp; %s <small>(%s, %3$tY-%3$tm-%3$te %3$tH:%3$tM:%3$tS, %s)</small>",
						getPath(node, version), FileSizeUtils
								.humanReadableFileSize(info.getFileSize(),
										false), info.getLastModified(), info
								.getChecksum()));
	}

	private String getPath(Node<Versions<FileSystemElementInfo>> node,
			int version) {
		return node.getParent() == null ? node.getNodeInfo().get(version)
				.getName() : getPath(node.getParent(), version) + '/'
				+ node.getNodeInfo().get(version).getName();
	}

	private void printDiff(Writer writer,
			Versions<FileSystemElementInfo> versions) throws IOException {
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
					DiffMatchPatch diff = new DiffMatchPatch();
					LinkedList<Diff> delta = diff.diff_main(ref, file);
					diff.diff_cleanupSemantic(delta);
					for (Diff deltum : delta) {
						switch (deltum.operation) {
						case INSERT:
							writer.write("<span class=\"text-success\">");
							writer.write(deltum.text);
							writer.write("</span>");
							break;
						case DELETE:
							writer.write("<strike class=\"text-danger\">");
							writer.write(deltum.text);
							writer.write("</strike>");
							break;
						case EQUAL:
						default:
							writer.write(deltum.text);
							break;
						}
					}
				}
				writer.write("</pre>");
			}
			writer.write("</div>");
		}
		writer.write("</div>");
	}

	private String prepFileForHtml(String path) throws IOException {
		return StringEscapeUtils.escapeHtml(FileUtils
				.readFileToString(new File(path)));
	}

	private void openCellDiv(Writer writer) throws IOException {
		writer.write("<div class=\"col-xs-" + colWidth + "\">");
	}
}
