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
import java.util.Date;

import org.keyboardplaying.tree.diff.Comparer;
import org.keyboardplaying.tree.exception.PrintException;
import org.keyboardplaying.tree.file.filter.FilenameMaskFilter;
import org.keyboardplaying.tree.file.model.FileSystemElementInfo;
import org.keyboardplaying.tree.file.model.FileTree;
import org.keyboardplaying.tree.model.Tree;
import org.keyboardplaying.tree.model.Versions;
import org.keyboardplaying.tree.reporter.HtmlReport;

/**
 * A sample implementation to show a use example of the file tree comparison mechanism.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
// FIXME convert into a unit test and remove class
public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException, PrintException {
        System.out.println(String.format("[%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1$tS] Starting", new Date()));

        File[] files = new File[2];
        files[0] = new File("src/test/resources/version1");
        files[1] = new File("src/test/resources/version2");

        // Build trees
        System.out.println(String.format("[%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1$tS] Building file trees", new Date()));
        FileTree[] trees = new FileTree[files.length];
        for (int i = 0; i < files.length; i++) {
            trees[i] = new FileTreeBuilder().buildTree(files[i], new FilenameMaskFilter("[^.].+"));
        }

        // Compare
        System.out.println(String.format("[%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1$tS] Calculating diff", new Date()));
        Tree<Versions<String>, Versions<FileSystemElementInfo>> result = new Comparer().compare(trees);

        // Generate the report
        System.out.println(String.format("[%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1$tS] Generating report", new Date()));
        new HtmlReport<String, FileSystemElementInfo>(result, new FileNodePrinter()).generate();

        System.out.println(String.format("[%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1$tS] Done", new Date()));
    }
}
