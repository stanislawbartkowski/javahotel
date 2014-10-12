/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.dict.impl;

import java.util.List;
import java.util.Map;

import com.jythonui.server.IGetResourceMap;
import com.jythonui.server.dict.DictEntry;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.ReadResourceFactory;

public class ReadDict {

	private static final String dictName = "dict";
	private static IReadResource iRead = new ReadResourceFactory()
			.constructLoader(ReadDict.class.getClassLoader());

	public static List<DictEntry> getList(IGetResourceMap iGet, String resName) {
		Map<String, String> mess = iGet
				.getResourceMap(iRead, dictName, resName);
		return MapToDict.toDict(mess);
	}
}