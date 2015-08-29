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

import org.apache.hadoop.conf.Configuration;

public class FSFactory {

	private FSFactory() {
	}

	public static boolean isHDFS(String filename) {
		if (filename.startsWith("hdfs"))
			return true;
		return false;
	}

	public static IFileSystem produce(Configuration conf, String filename) {
		if (isHDFS(filename))
			return new HadoopFS(filename, conf);
		return new LocalFS(filename);
	}

	public static IFileSystem produceHadoop(Configuration conf, String filename) {
		return new HadoopFS(filename, conf);
	}

}
