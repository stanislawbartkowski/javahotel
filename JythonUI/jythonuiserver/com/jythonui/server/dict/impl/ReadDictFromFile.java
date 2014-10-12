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

import com.google.inject.Inject;
import com.jythonui.server.BUtil;
import com.jythonui.server.IConsts;
import com.jythonui.server.IGetResourceMap;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.dict.DictEntry;
import com.jythonui.server.dict.IReadDictFromFile;

public class ReadDictFromFile implements IReadDictFromFile {

	private final IGetResourceMap iGet;
	private final IJythonUIServerProperties p;

	@Inject
	public ReadDictFromFile(IGetResourceMap iGet, IJythonUIServerProperties p) {
		this.iGet = iGet;
		this.p = p;
	}

	@Override
	public List<DictEntry> getDict(String dir, String dicName) {
		String dirName = BUtil.addNameToPath(IConsts.DIALOGDIR, dir);
		Map<String, String> ma = iGet.getResourceMap(p.getResource(), true,
				dirName, dicName);
		return MapToDict.toDict(ma);
	}

}
