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
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.keyboardplaying.tree.exception.PrintException;
import org.keyboardplaying.tree.file.model.DirectoryInfo;
import org.keyboardplaying.tree.file.model.FileInfo;
import org.keyboardplaying.tree.file.model.FileSystemElementInfo;
import org.keyboardplaying.tree.file.util.ByteSizeUtils;
import org.keyboardplaying.tree.reporter.NodePrinter;

/**
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
// TODO Javadoc
public class FileNodePrinter implements NodePrinter<String, FileSystemElementInfo> {

    /*
     * (non-Javadoc)
     * 
     * @see org.keyboardplaying.tree.reporter.NodePrinter#printId(java.lang.Comparable)
     */
    @Override
    public String printId(String path) {
        return path;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.keyboardplaying.tree.reporter.NodePrinter#isContentPrintable(java.lang.Comparable)
     */
    @Override
    public boolean isContentPrintable(FileSystemElementInfo content) {
        return content instanceof FileInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.keyboardplaying.tree.reporter.NodePrinter#printHeader(java.lang.Comparable,
     * java.lang.Comparable)
     */
    @Override
    public String printHeader(String id, FileSystemElementInfo content) throws PrintException {
        if (content == null) {
            return null;
        } else if (content instanceof DirectoryInfo) {
            return "<b>+ " + getPath(id, content.getPath()) + "</b>";
        } else {
            FileInfo info = (FileInfo) content;
            return String.format(
                    "&nbsp;&nbsp; %s <small>(%s, %3$tY-%3$tm-%3$te %3$tH:%3$tM:%3$tS, %s)</small>",
                    getPath(id, info.getPath()),
                    ByteSizeUtils.getHumanReadable(info.getFileSize(), false),
                    info.getLastModified(), info.getChecksum());
        }
    }

    private String getPath(String rootPath, String path) {
        int rootLength = rootPath.length();
        int pathLength = path.length();
        return pathLength > rootLength + 3 ? path.substring(rootLength + 3)
                : pathLength > rootLength + 1 ? path.substring(rootLength + 1) : path;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.keyboardplaying.tree.reporter.NodePrinter#printContent(java.lang.Comparable)
     */
    @Override
    public String printContent(FileSystemElementInfo content) throws PrintException {
        try {
            return content instanceof FileInfo ? FileUtils.readFileToString(new File(content
                    .getPath())) : null;
        } catch (IOException e) {
            throw new PrintException(e);
        }
    }
}
