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
import java.io.FileFilter;
import java.util.Collection;
import java.util.Collections;

/**
 * A {@link FileFilter} to compose multiple filters into one.
 * <p/>
 * It will refuse a file as soon as one of the members of the composite refuses it, and accept any other.
 *
 * @author Cyrille Chopelet (https://keyboardplaying.org)
 */
public class CompositeFileFilter implements FileFilter {

    private final Collection<FileFilter> filters;

    /**
     * Creates a new instance.
     *
     * @param filters
     *            the filters to be composed
     */
    public CompositeFileFilter(Collection<FileFilter> filters) {
        this.filters = Collections.unmodifiableCollection(filters);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(File file) {
        for (FileFilter filter : filters) {
            if (!filter.accept(file)) {
                return false;
            }
        }
        return true;
    }
}
