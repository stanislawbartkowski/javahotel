/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.storage.suggest.impl;

import com.google.inject.Inject;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ConvertTT;
import com.jythonui.server.storage.suggest.IRememberValue;
import com.jythonui.server.storage.suggest.ISuggestionStorage;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;

public class RememberValue implements IRememberValue {

	private final ISuggestionStorage iStor;

	@Inject
	public RememberValue(ISuggestionStorage iStor) {
		this.iStor = iStor;
	}

	@Override
	public void saveRemember(String key, FieldValue val) {
		String s = null;
		if (val != null)
			s = ConvertTT.toS(val.getValue(), val.getType(), val.getAfterdot());

		if (CUtil.EmptyS(s))
			iStor.removeRemember(key);
		else
			iStor.saveRemember(key, s);

	}

	@Override
	public FieldValue getRemember(String key, FieldItem t) {
		String s = iStor.getRemember(key);
		if (CUtil.EmptyS(s))
			return null;
		Object o = ConvertTT.toO(t.getFieldType(), s);
		FieldValue val = new FieldValue();
		val.setValue(t.getFieldType(), o, t.getAfterDot());
		return val;
	}

}
