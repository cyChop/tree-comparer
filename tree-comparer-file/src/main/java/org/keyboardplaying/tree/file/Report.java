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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.keyboardplaying.tree.diff.model.Versions;
import org.keyboardplaying.tree.file.model.DirectoryInfo;
import org.keyboardplaying.tree.file.model.FileInfo;
import org.keyboardplaying.tree.file.model.FileSystemElementInfo;
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
		writer.write("<head>");
		writer.write("<meta charset=\"UTF-8\">");
		writer.write("<title>Comparison report</title>");
		writer.write("<style>");
		InputStream style = this.getClass().getClassLoader()
				.getResourceAsStream("bootstrap.min.css");
		IOUtils.copy(style, writer);
		writer.write("</style>");
		writer.write("</head>");

		// write the body
		writer.write("<body style=\"padding: 10px\">");
		printHeader(diff, writer, colWidth);
		print(diff.getRoot(), writer, colWidth);
		writer.write("</body>");

		// end HTML
		writer.write("</html>");

		IOUtils.closeQuietly(writer);
	}

	private void printHeader(
			Tree<Versions<String>, Versions<FileSystemElementInfo>> result,
			BufferedWriter writer, int colWidth) throws IOException {
		writer.write("<div class=\"row\">");
		for (String path : result.getId()) {
			writer.write("<div class=\"col-md-" + colWidth + "\"><h4>" + path
					+ "</h4></div>");
		}
		writer.write("</div>");
	}

	private void print(Node<Versions<FileSystemElementInfo>> node,
			Writer writer, int colWidth) throws IOException {
		boolean constant = node.getNodeInfo().isConstant();
		writer.write("<div class=\"row\">");
		if (constant) {
			writer.write("<span class=\"text-muted\">");
		}
		for (int i = 0; i < node.getNodeInfo().getNbVersions(); i++) {
			writer.write("<div class=\"col-md-" + colWidth + "\">");
			if (node.getNodeInfo().get(i) instanceof DirectoryInfo) {
				writer.write("<b>" + getPath(node, i) + "</b>");
			} else {
				FileInfo info = (FileInfo) node.getNodeInfo().get(i);
				writer.write(String
						.format("%s <small>(%s, %3$tY-%3$tm-%3$te %3$tH:%3$tM:%3$tS, %s)</small>",
								getPath(node, i),
								FileSizeUtils.humanReadableFileSize(
										info.getFileSize(), false),
								info.getLastModified(), info.getChecksum()));
			}
			if (constant) {
				writer.write("</span>");
			}
			writer.write("</div>");
		}
		writer.write("</div>");
		for (Node<Versions<FileSystemElementInfo>> child : node.getChildren()) {
			print(child, writer, colWidth);
		}
	}

	private String getPath(Node<Versions<FileSystemElementInfo>> node,
			int version) {
		return node.getParent() == null ? node.getNodeInfo().get(version)
				.getName() : getPath(node.getParent(), version) + '/'
				+ node.getNodeInfo().get(version).getName();
	}
}
