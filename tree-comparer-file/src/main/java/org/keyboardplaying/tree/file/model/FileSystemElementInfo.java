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

import java.io.File;

//XXX JAVADOC
/**
 * @author cyChop (http://keyboardplaying.org/)
 */
public abstract class FileSystemElementInfo implements
		Comparable<FileSystemElementInfo> {

	private File file;

	protected FileSystemElementInfo(File file) {
		this.file = file;
	}

	protected File getFile() {
		return this.file;
	}

	public String getName() {
		return file.getName();
	}

	public String getPath() {
		return file.getAbsolutePath();
	}

	@Override
	public int compareTo(FileSystemElementInfo o) {
		int result = this.getClass().getSimpleName()
				.compareTo(o.getClass().getSimpleName());
		if (result == 0) {
			result = this.getName().compareTo(o.getName());
		}
		return result;
	}

	@Override
	public String toString() {
		return file.toString();
	}
}
