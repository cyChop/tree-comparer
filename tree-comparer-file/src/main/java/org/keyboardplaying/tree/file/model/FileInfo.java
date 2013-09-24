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
package org.keyboardplaying.tree.file.model;

import org.keyboardplaying.tree.file.util.FileSizeUtils;

// XXX JAVADOC
/**
 * @author cyChop (http://keyboardplaying.org/)
 */
public class FileInfo extends FileSystemElementInfo {

	private long size;
	private long lastModified;
	private String checksum;

	public FileInfo(String name, long size, long lastModified, String checksum) {
		super(name);
		this.size = size;
		this.lastModified = lastModified;
		this.checksum = checksum;
	}

	public long getFileSize() {
		return size;
	}

	public long getLastModified() {
		return lastModified;
	}

	public String getChecksum() {
		return checksum;
	}

	@Override
	public int hashCode() {
		return getName().hashCode() + checksum.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		boolean equal = false;
		if (o instanceof FileInfo) {
			FileInfo fi = (FileInfo) o;
			equal = getFileSize() == fi.getFileSize()
					&& getName().equals(fi.getName())
					&& getChecksum().equals(fi.getChecksum());
		}
		return equal;
	}

	@Override
	public String toString() {
		return String.format(
				"%s (%s, %3$tY-%3$tm-%3$te %3$tH:%3$tM:%3$tS, %s)", getName(),
				FileSizeUtils.humanReadableFileSize(getFileSize(), false),
				getLastModified(), getChecksum());
	}
}
