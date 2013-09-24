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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.keyboardplaying.tree.diff.Comparer;
import org.keyboardplaying.tree.diff.model.Versions;
import org.keyboardplaying.tree.file.model.FileSystemElementInfo;
import org.keyboardplaying.tree.file.model.FileTree;
import org.keyboardplaying.tree.model.Tree;

// XXX JAVADOC
/**
 * @author cyChop (http://keyboardplaying.org/)
 */
public class ReportGenerator {

	private FileTreeBuilder builder = new FileTreeBuilder();
	private Comparer comparer = new Comparer();

	public void generateReport(String[] paths) throws FileNotFoundException,
			IOException {
		File[] files = new File[paths.length];
		for (int i = 0; i < paths.length; i++) {
			files[i] = new File(paths[i]);
		}
		generateReport(files);
	}

	public void generateReport(File[] files) throws FileNotFoundException,
			IOException {
		FileTree[] trees = new FileTree[files.length];
		for (int i = 0; i < files.length; i++) {
			trees[i] = builder.buildTree(files[i]);
		}
		Tree<Versions<String>, Versions<FileSystemElementInfo>> result = comparer
				.compare(trees);
		// FIXME generate the report
	}
}
