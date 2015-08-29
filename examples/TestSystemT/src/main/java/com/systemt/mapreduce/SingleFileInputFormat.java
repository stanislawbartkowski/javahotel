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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.log4j.Logger;

public class SingleFileInputFormat extends FileInputFormat<Text, Text> {

	private static Logger log = Logger.getLogger(MapReduceMain.class.getName());

	@Override
	public RecordReader<Text, Text> createRecordReader(InputSplit arg0, TaskAttemptContext arg1)
			throws IOException, InterruptedException {
		log.info("createReader");
		return new MyRecordReader();
	}

	@Override
	protected boolean isSplitable(JobContext context, Path file) {
		// System.out.println("isSplitable");
		return false;
	}

	class MyRecordReader extends RecordReader<Text, Text> {

		private FileSplit split;
		private Configuration conf;

		private final Text currText = new Text();
		private boolean fileProcessed = false;

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public Text getCurrentKey() throws IOException, InterruptedException {
			return new Text(split.getPath().getName());
		}

		@Override
		public Text getCurrentValue() throws IOException, InterruptedException {
			return currText;
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void initialize(InputSplit arg0, TaskAttemptContext arg1) throws IOException, InterruptedException {
			this.split = (FileSplit) arg0;
			this.conf = arg1.getConfiguration();

		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			System.out.println("Next key value");
			if (fileProcessed)
				return false;
			int fileLength = (int) split.getLength();
			byte[] result = new byte[fileLength];
			FileSystem fs = FileSystem.get(conf);
			FSDataInputStream in = null;
			in = fs.open(split.getPath());
			IOUtils.readFully(in, result, 0, fileLength);
			currText.set(result, 0, fileLength);
			IOUtils.closeStream(in);
			this.fileProcessed = true;
			return true;
		}

	}

}
