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
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.ibm.avatar.api.OperatorGraph;
import com.ibm.avatar.api.exceptions.TextAnalyticsException;
import com.systemt.aql.ExecuteAQL;
import com.systemt.aql.GetOperatorGraph;
import com.systemt.aql.ITextParameters;
import com.systemt.aql.Utils;
import com.systemt.aql.ExecuteAQL.OutputCollection;

public class MapTextAnalytics extends Mapper<Text, Text, Text, Text> {

	private String tamoutput;
	private String listOfModules;
	private OperatorGraph graph = null;
	private boolean isTab;
	private boolean ismulti;
	private String langCode;
	private static Logger log = Logger.getLogger(MapReduceMain.class.getName());
	private static String dicList;
	private static String fileList;

	@Override
	public void setup(Context context) {
		tamoutput = context.getConfiguration().get(ITextParameters.OUTDIR);
		listOfModules = context.getConfiguration().get(ITextParameters.LISTMODULES);
		isTab = Utils.isTabCSV(context.getConfiguration().get(ITextParameters.OUTPUTFOMAT));
		ismulti = Utils.isMulti(context.getConfiguration().get(ITextParameters.ISMULTI));
		langCode = context.getConfiguration().get(ITextParameters.LANGCODE);
		dicList = context.getConfiguration().get(ITextParameters.EXDICTIONARYLISTDICT);
		fileList = context.getConfiguration().get(ITextParameters.EXDICTIONARYLISTFILES);
	}

	public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
		String docName = key.toString();
		String docContent = value.toString();
		log.info("docname=" + docName);
		log.debug("content=" + docContent);
		log.info("Create OG graph");
		if (Strings.isNullOrEmpty(dicList))
			log.info("No external dictionaries");
		else
			log.info(dicList + " " + fileList);
		if (Strings.isNullOrEmpty(langCode))
			log.info("No language");
		else
			log.info("Language code: " + langCode);
		Map<String, OutputCollection> result = null;
		try {
			if (graph == null)
				graph = GetOperatorGraph.createS(tamoutput, listOfModules, ismulti, dicList, fileList);
			result = ExecuteAQL.executeText(graph, docName, docContent, langCode);
		} catch (TextAnalyticsException e) {
			log.fatal(e.getMessage(), e);
			throw new InterruptedException(e.getMessage());
		}
		for (Entry<String, OutputCollection> e : result.entrySet()) {
			String viewName = e.getKey();
			String outLineS = Utils.toCVS(e.getValue(), isTab);
			if (outLineS != null)
				context.write(new Text(viewName), new Text(outLineS));
		}

	}
}
