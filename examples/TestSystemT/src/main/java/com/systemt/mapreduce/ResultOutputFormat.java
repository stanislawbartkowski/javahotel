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
package com.systemt.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import com.systemt.aql.ITextParameters;
import com.systemt.fs.FSFactory;
import com.systemt.fs.IFileSystem;


public class ResultOutputFormat extends FileOutputFormat<Text, Text> {
	
	private static Logger log = Logger.getLogger(MapReduceMain.class.getName());
	
	public static class MyRecordWriter extends RecordWriter<Text, Text> {

		private final TaskAttemptContext arg0;
		private IFileSystem FS;

		public MyRecordWriter(TaskAttemptContext arg0) {
			this.arg0 = arg0;
			String outDir = arg0.getConfiguration().get(ITextParameters.OUTPUTDIRECTORY);
			// outP = new Path(outDir);
			FS = FSFactory.produceHadoop(arg0.getConfiguration(), outDir);

		}

		@Override
		public void close(TaskAttemptContext arg0) throws IOException,
				InterruptedException {
		}

		@Override
		public void write(Text key, Text value) throws IOException,
				InterruptedException {
			log.info("write " + arg0.toString() + " " + value.toString());
			FS.appendToFile(key.toString(), value.toString(), false);
		}

	}

	
	@Override
	public RecordWriter<Text, Text> getRecordWriter(TaskAttemptContext arg0)
			throws IOException, InterruptedException {
		// get the current path
		// Path path = FileOutputFormat.getOutputPath(arg0);
		// create the full path with the output directory plus our filename
		// Path fullPath = new Path(path, "result.txt");

		// create the file in the file system
		// FileSystem fs = path.getFileSystem(arg0.getConfiguration());
		// FSDataOutputStream fileOut = fs.create(fullPath, arg0);

		// create our record writer with the new file
		return new MyRecordWriter(arg0);
	}
}
