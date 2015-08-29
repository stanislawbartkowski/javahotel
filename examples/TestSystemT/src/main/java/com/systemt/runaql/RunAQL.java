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
package com.systemt.runaql;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import org.apache.hadoop.conf.Configuration;

import com.ibm.avatar.api.OperatorGraph;
import com.systemt.aql.ExecuteAQL;
import com.systemt.aql.GetOperatorGraph;
import com.systemt.aql.Utils;
import com.systemt.aql.ExecuteAQL.OutputCollection;
import com.systemt.fs.FSFactory;
import com.systemt.fs.IFileSystem;

public class RunAQL {
	
	private static Logger log = Logger.getLogger(RunAQL.class.getName());
	
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.out.println("Invalid number of argument");
			System.out.println("<inputdata> <outputdata> <conffile>");
			System.exit(4);
		}
		boolean isTab = false;
		Utils.StartParam par = Utils.runSQL(args[2]);
		boolean ismulti = Utils.isMulti(par.par.getIsMulti());
		String tamdir = par.tempdir.getPath();
		OperatorGraph og = GetOperatorGraph.createS(tamdir, par.par.getListOfModules(), ismulti,
				par.par.getDicNameList(), par.par.getDicFileList());
		log.info("Annotating documents ...");
		Map<String, OutputCollection> result = ExecuteAQL.execute(og, args[0], par.par.getLangCode());
		System.out.println("Copy output to " + args[1]);
		Configuration conf = new Configuration();
		IFileSystem FS = FSFactory.produce(conf, args[1]);
		FS.clear();
		for (Entry<String, OutputCollection> e : result.entrySet()) {
			String outFile = Utils.createFirstLine(e.getValue(), isTab);
			String viewName = e.getKey();
			System.out.println(viewName);
			outFile += System.lineSeparator() + Utils.toCVS(e.getValue(), isTab);
			// Utils.appendTextFile(outDir, conf, viewName, outFile, false);
			FS.appendToFile(viewName, outFile, false);
		}
	}
}
