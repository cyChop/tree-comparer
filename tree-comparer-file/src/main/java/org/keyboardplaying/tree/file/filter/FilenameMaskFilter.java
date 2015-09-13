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
package org.keyboardplaying.tree.file.filter;

import java.io.File;

/**
 * A {@link Filter} that will include only files and directories whose name matches the supplied mask (regex).
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class FilenameMaskFilter implements Filter {

    private String mask;

    /**
     * Creates a new instance.
     *
     * @param mask
     *            the regex to test files against
     */
    public FilenameMaskFilter(String mask) {
        this.mask = mask;
    }

    /** return {@code true} if the directory or file's name matches the filter's mask */
    @Override
    public boolean include(File file) {
        return file.getName().matches(mask);
    }
}
