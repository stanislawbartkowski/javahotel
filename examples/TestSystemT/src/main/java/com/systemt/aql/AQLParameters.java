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
package com.systemt.aql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

public class AQLParameters {

	private final String[] moduleFiles;
	private final String outTamDir;
	private final String listOfModules;
	private final String isMulti;
	private final String dicNameList;
	private final String dicFileList;
	private final String langCode;

	private AQLParameters(String[] moduleFiles, String outTamDir, String listOfModules, String dicNameList,
			String dicFileList, String isMulti, String langCode) {
		this.moduleFiles = moduleFiles;
		this.outTamDir = outTamDir;
		this.listOfModules = listOfModules;
		this.dicNameList = dicNameList;
		this.dicFileList = dicFileList;
		this.isMulti = isMulti;
		this.langCode = langCode;
	}

	public String[] getModuleFiles() {
		return moduleFiles;
	}

	public String getOutTamDir() {
		return outTamDir;
	}

	public static AQLParameters readAQLParameters(String propFile) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(propFile));

		String outTamDir = properties.getProperty(ITextParameters.OUTDIR);
		if (outTamDir == null)
			throw new IOException("No " + ITextParameters.OUTDIR + " property");

		List<String> moduleList = new ArrayList<String>();
		for (Entry<Object, Object> o : properties.entrySet()) {
			String key = (String) o.getKey();
			if (key.startsWith(ITextParameters.INMODULE))
				moduleList.add((String) o.getValue());
		}

		List<String> dicNameList = new ArrayList<String>();
		List<String> dicFileList = new ArrayList<String>();
		for (Entry<Object, Object> o : properties.entrySet()) {
			String key = (String) o.getKey();
			if (key.startsWith(ITextParameters.EXDICTIONARY)) {
				String dic = (String) o.getValue();
				String[] dicL = dic.split(",");
				if (dicL.length != 2)
					throw new IOException(key + ": " + dic + " Expected two entries separated by ,");
				dicNameList.add(dicL[0]);
				dicFileList.add(dicL[1]);
			}
		}

		if (moduleList.isEmpty())
			throw new IOException("No single " + ITextParameters.INMODULE + " property");
		String[] inModule = new String[moduleList.size()];
		for (int i = 0; i < moduleList.size(); i++)
			inModule[i] = moduleList.get(i);
		String modules = properties.getProperty(ITextParameters.LISTMODULES);
		if (modules == null)
			throw new IOException("No " + ITextParameters.LISTMODULES + " property");
		return new AQLParameters(inModule, outTamDir, modules, Utils.toComaLine(dicNameList),
				Utils.toComaLine(dicFileList), properties.getProperty(ITextParameters.ISMULTI),
				properties.getProperty(ITextParameters.LANGCODE));
	}

	public String getListOfModules() {
		return listOfModules;
	}

	public String getIsMulti() {
		return isMulti;
	}

	public String getDicNameList() {
		return dicNameList;
	}

	public String getDicFileList() {
		return dicFileList;
	}

	public String getLangCode() {
		return langCode;
	}

}
