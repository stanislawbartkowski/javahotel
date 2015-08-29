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

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

class HadoopFS implements IFileSystem {

	private final Path file;
	private final Configuration conf;

	HadoopFS(String filename, Configuration conf) {
		file = new Path(filename);
		this.conf = conf;
	}

	public void appendToFile(String filename, String content, boolean firstline) throws IOException {
		Path outFile = new Path(file, filename);
		FileSystem fs = FileSystem.get(conf);
		FSDataOutputStream fsout;
		if (!fs.exists(outFile))
			fsout = fs.create(outFile);
		else {
			if (firstline) {
//				fs.close();
				return;
			}
			fsout = fs.append(outFile);
		}
		PrintWriter writer = new PrintWriter(fsout);
		writer.append(content);
		writer.close();
		fsout.close();
//		fs.close();
	}

	public void clear() throws IOException {
		FileSystem fs = FileSystem.get(file.toUri(), conf);
		if (!fs.exists(file))
			fs.mkdirs(file);
		if (!fs.isDirectory(file))
			throw new IOException(file.getName() + " not directory");
		FileStatus[] pList = fs.listStatus(file);
		for (FileStatus ft : pList)
			fs.delete(ft.getPath());
	}

}
