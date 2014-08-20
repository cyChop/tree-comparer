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
package org.keyboardplaying.tree.file.util;

/**
 * Utility class for byte sizes.
 * 
 * @author cyChop (http://keyboardplaying.org/)
 */
public final class ByteSizeUtils {

	/** Private constructor to avoid instantiation. */
	private ByteSizeUtils() {
	}

	/**
	 * Turns a {@code long} byte size into a human-readable {@link String}.
	 * 
	 * @param size
	 *            the byte size to display
	 * @param si
	 *            {@code true} to use the units of the international system
	 *            (kb), {@code false} to use the IEC units (KiB, MiB, ...)
	 * @return a human-readable byte size
	 */
	public static String getHumanReadable(long size, boolean si) {
		/*
		 * Provided by aioobe
		 * 
		 * http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into
		 * -human-readable-format-in-java
		 */
		int unit = si ? 1000 : 1024;
		if (size < unit) {
			return size + " B";
		}
		int exp = (int) (Math.log(size) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
				+ (si ? "" : "i");
		return String.format("%.1f %sB", size / Math.pow(unit, exp), pre);
	}
}