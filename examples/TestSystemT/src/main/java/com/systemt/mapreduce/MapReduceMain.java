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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import com.systemt.aql.CopyToHDFS;
import com.systemt.aql.ITextParameters;
import com.systemt.aql.Utils;

public class MapReduceMain {

	private static Logger log = Logger.getLogger(MapReduceMain.class.getName());

	static class RunMapReduce extends Configured implements Tool {

		public int run(String[] args) throws Exception {

			if (args.length != 3) {
				System.out.println("Invalid number of argument");
				System.out.println("<inputdata> <outputdata> <conffile>");
				return 4;
			}
			Utils.StartParam par = Utils.runSQL(args[2]);
			log.info("Copy tam files to HDFS " + par.par.getOutTamDir());
			CopyToHDFS.copy(par.tempdir, par.par.getOutTamDir());
			log.info("Start M/R Job");

			Configuration conf = getConf();
			conf.set(ITextParameters.OUTDIR, par.par.getOutTamDir());
			conf.set(ITextParameters.LISTMODULES, par.par.getListOfModules());
			conf.set(ITextParameters.EXDICTIONARYLISTDICT, par.par.getDicNameList());
			conf.set(ITextParameters.EXDICTIONARYLISTFILES, par.par.getDicFileList());
			conf.set(ITextParameters.OUTPUTDIRECTORY, args[1]);
			conf.set(ITextParameters.LANGCODE, par.par.getLangCode());
			conf.set(ITextParameters.ISMULTI, par.par.getIsMulti());
			Job job = Job.getInstance(conf);
			job.setJarByClass(MapReduceMain.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			// job.setOutputFormatClass(ResultOutputFormat.class);

			job.setMapperClass(MapTextAnalytics.class);
			job.setReducerClass(ReduceTextAnalytics.class);

			// job.setInputFormatClass(TextInputFormat.class);
			job.setInputFormatClass(SingleFileInputFormat.class);
			job.setOutputFormatClass(ResultOutputFormat.class);

			FileInputFormat.addInputPath(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));

			job.waitForCompletion(true);
			return 0;
		}

	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		int res = ToolRunner.run(conf, new RunMapReduce(), args);
		System.exit(res);
	}

}
