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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.google.common.base.Strings;
import com.ibm.avatar.algebra.datamodel.AbstractTupleSchema;
import com.ibm.avatar.algebra.datamodel.FieldType;
import com.ibm.avatar.algebra.datamodel.Span;
import com.ibm.avatar.algebra.datamodel.Text;
import com.ibm.avatar.algebra.datamodel.TextSetter;
import com.ibm.avatar.algebra.datamodel.Tuple;
import com.ibm.avatar.algebra.datamodel.TupleList;
import com.ibm.avatar.algebra.datamodel.TupleSchema;
import com.ibm.avatar.api.DocReader;
import com.ibm.avatar.api.OperatorGraph;
import com.ibm.avatar.api.exceptions.TextAnalyticsException;
import com.systemt.fs.FSFactory;

public class ExecuteAQL {

	public static class OutputCollection {
		private final String[] colNames;
		private final FieldType[] fTypes;
		private final List<String[]> vals;

		private OutputCollection(String[] colNames, FieldType[] fTypes, List<String[]> vals) {
			this.colNames = colNames;
			this.fTypes = fTypes;
			this.vals = vals;
		}

		private void add(OutputCollection o) {
			this.vals.addAll(o.vals);
		}

		public String[] getColNames() {
			return colNames;
		}

		public FieldType[] getfTypes() {
			return fTypes;
		}

		public List<String[]> getVals() {
			return vals;
		}

	}

	private static Map<String, OutputCollection> executeTuple(OperatorGraph og, Tuple docTuple)
			throws TextAnalyticsException {
		Map<String, OutputCollection> outList = new HashMap<String, OutputCollection>();
		Map<String, TupleList> results = og.execute(docTuple, null, null);
		for (Entry<String, TupleList> e : results.entrySet()) {
			String outputView = e.getKey();
			TupleList li = e.getValue();
			AbstractTupleSchema s = li.getSchema();
			String[] names = s.getFieldNames();
			FieldType[] types = s.getFieldTypes();

			List<String[]> cList = new ArrayList<String[]>();
			for (int i = 0; i < li.size(); i++) {
				Tuple tu = li.getElemAtIndex(i);
				String[] vals = new String[tu.size()];
				for (int k = 0; k < tu.size(); k++) {
					Object val = s.getCol(tu, k);
					String valS = null;
					if (val != null)
						if (types[k] == FieldType.SPAN_TYPE) {
							Span sp = (Span) val;
							valS = sp.getText();
						} else if (types[k] == FieldType.TEXT_TYPE) {
							Text te = (Text) val;
							valS = te.getText();
						} else
							valS = val.toString();
					vals[k] = valS;
				} // for
				cList.add(vals);
			} // for
			outList.put(outputView, new OutputCollection(names, types, cList));
		} // for

		return outList;

	}

	public static Map<String, OutputCollection> executeText(OperatorGraph og, String docName, String docContent,
			String langCode) throws TextAnalyticsException {

		TupleSchema tup = new TupleSchema(new String[] { "docname", "doccontent" },
				new FieldType[] { FieldType.TEXT_TYPE, FieldType.TEXT_TYPE });
		TextSetter docNameSet = tup.textSetter("docname");
		TextSetter contentNameSet = tup.textSetter("doccontent");
		Tuple docTuple = tup.createTup();
		docNameSet.setVal(docTuple, docName, Utils.toLangCode(langCode));
		contentNameSet.setVal(docTuple, docContent, Utils.toLangCode(langCode));
		return executeTuple(og, docTuple);
	}

	private static void addResult(Map<String, OutputCollection> outList, Map<String, OutputCollection> ouList) {
		for (Entry<String, OutputCollection> e : ouList.entrySet()) {
			if (outList.get(e.getKey()) == null)
				outList.put(e.getKey(), e.getValue());
			else
				outList.get(e.getKey()).add(e.getValue());
		}
	}

	public static Map<String, OutputCollection> executeHDFS(OperatorGraph og, String dataPath, String langCode)
			throws TextAnalyticsException, IOException {
		Map<String, OutputCollection> outList = new HashMap<String, OutputCollection>();
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dataPath), conf);
		Path pa = new Path(dataPath);
		if (!fs.isDirectory(pa))
			throw new IOException(dataPath + " not a directory");
		FileStatus[] pList = fs.listStatus(pa);
		for (FileStatus f : pList) {
			Path fPath = f.getPath();
			InputStream stream = fs.open(fPath);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			IOUtils.copy(stream, out);
			String content = out.toString();
			Map<String, OutputCollection> ouList = executeText(og, f.getPath().getName(), content, langCode);
			addResult(outList, ouList);
		}
		return outList;
	}

	public static Map<String, OutputCollection> execute(OperatorGraph og, String dataPath, String langCode)
			throws TextAnalyticsException, IOException {
		if (FSFactory.isHDFS(dataPath))
			return executeHDFS(og, dataPath, langCode);
		TupleSchema docSchema = og.getDocumentSchema();
		DocReader docs = new DocReader(new File(dataPath), docSchema, null);
		if (!Strings.isNullOrEmpty(langCode))
			docs.overrideLanguage(Utils.toLangCode(langCode));
		Map<String, OutputCollection> outList = new HashMap<String, OutputCollection>();
		while (docs.hasNext()) {
			Tuple docTuple = docs.next();
			Map<String, OutputCollection> ouList = executeTuple(og, docTuple);
			addResult(outList, ouList);
		}
		return outList;
	}

}
