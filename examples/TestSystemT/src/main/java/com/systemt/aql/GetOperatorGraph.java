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

import com.ibm.avatar.algebra.util.tokenize.TokenizerConfig;
import com.ibm.avatar.api.ExternalTypeInfo;
import com.ibm.avatar.api.ExternalTypeInfoFactory;
import com.ibm.avatar.api.OperatorGraph;
import com.ibm.avatar.api.exceptions.TextAnalyticsException;

public class GetOperatorGraph {

	public static OperatorGraph create(String dirTam, String[] listOfModules, boolean ismulti, String exDicNames,
			String exDicFiles) throws TextAnalyticsException {
		TokenizerConfig tokenizer = Utils.getTokenizer(ismulti);
		ExternalTypeInfo externalTypeInfo = ExternalTypeInfoFactory.createInstance();

		String[] dicEx = Utils.toListOf(exDicNames);
		String[] dicFiles = Utils.toListOf(exDicFiles);

		for (int i = 0; i < dicEx.length; i++)
			// remove trailing spaces
			externalTypeInfo.addDictionary(dicEx[i].trim(), dicFiles[i].trim());

		OperatorGraph.validateOG(listOfModules, dirTam, tokenizer);
		OperatorGraph extractor = OperatorGraph.createOG(listOfModules, dirTam, externalTypeInfo, tokenizer);
		return extractor;
	}

	public static OperatorGraph createS(String dirTam, String listOfModulesS, boolean multi, String exDicNames,
			String exDicFiles) throws TextAnalyticsException {
		return create(dirTam, Utils.toListOf(listOfModulesS), multi, exDicNames, exDicFiles);
	}

}
