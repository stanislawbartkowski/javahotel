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

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.ibm.avatar.algebra.util.lang.LangCode;
import com.ibm.avatar.algebra.util.tokenize.TokenizerConfig;
import com.ibm.avatar.api.exceptions.TextAnalyticsException;
import com.systemt.aql.ExecuteAQL.OutputCollection;

public class Utils {

	private static Logger log = Logger.getLogger(Utils.class.getName());

	public static String[] toListOf(String mlist) {
		if (Strings.isNullOrEmpty(mlist))
			return new String[] {};
		return mlist.split(",");
	}

	public static String toComaLine(List<String> list) {
		String outS = "";
		for (String s : list)
			if (outS.equals(""))
				outS = s;
			else
				outS += "," + s;
		return outS;
	}

	private static String linetoCVS(String[] s, boolean tab) {
		String outLine = null;
		char sep = ',';
		if (tab)
			sep = '\t';
		for (String ss : s) {
			String valS = ss;
			if (!tab)
				valS = '"' + ss + '"';
			if (outLine == null)
				outLine = valS;
			else
				outLine += sep + valS;
		}
		return outLine;
	}

	public static String createFirstLine(OutputCollection col, boolean tab) {
		return linetoCVS(col.getColNames(), tab);
	}

	public static String toCVS(OutputCollection col, boolean tab) {
		String outLineS = null;
		for (String[] s : col.getVals()) {
			String outLine = linetoCVS(s, tab);
			if (outLineS == null)
				outLineS = outLine;
			else
				outLineS += System.lineSeparator() + outLine;
		}
		return outLineS;
	}

	public static boolean isTabCSV(String format) {
		if (format == null)
			return false;
		return ITextParameters.TABCVS.equals(format);
	}

	public static boolean isMulti(String multi) {
		if (multi == null)
			return false;
		return true;
	}

	public static class StartParam {
		public File tempdir;
		public AQLParameters par;
	}

	public static StartParam runSQL(String confFile) throws Exception {
		StartParam start = new StartParam();
		log.info("Reading properties from " + confFile);
		start.par = AQLParameters.readAQLParameters(confFile);
		start.tempdir = File.createTempFile("tempdir", "tam");
		log.info("Compiling AQL source file to " + start.tempdir.getPath());
		start.tempdir.delete();
		start.tempdir.mkdir();
		boolean ismulti = Utils.isMulti(start.par.getIsMulti());
		if (ismulti)
			log.info("Multilingual tokenizer");
		else
			log.info("Standard tokenizer");
		if (Strings.isNullOrEmpty(start.par.getLangCode()))
			log.info("No language");
		else
			log.info("Language: " + start.par.getLangCode());
		CompileAQLModule.compile(start.par.getModuleFiles(), start.tempdir.getPath(), ismulti);
		return start;
	}

	public static TokenizerConfig getTokenizer(boolean multi) throws TextAnalyticsException {
		if (multi)
			return new TokenizerConfig.Multilingual();
		return new TokenizerConfig.Standard();
	}

	public static LangCode toLangCode(String s) {
		if (Strings.isNullOrEmpty(s))
			return null;
		return LangCode.valueOf(s);
	}

}
