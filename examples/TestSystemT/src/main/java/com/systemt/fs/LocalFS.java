/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.systemt.fs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

class LocalFS implements IFileSystem {

	private final File file;

	LocalFS(String filename) {
		file = new File(filename);
	}

	public void appendToFile(String filename, String content, boolean firstline) throws IOException {
		File fileappend = new File(file, filename);
		if (fileappend.exists() && firstline)
			return;
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileappend, true)));
		out.println(content);
		out.close();
	}

	public void clear() throws IOException {
		FileUtils.deleteDirectory(file);
		file.mkdir();
	}

}
