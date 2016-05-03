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
package com.jythonui.client.util;

import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.json.IJsonConvert;
import com.jythonui.client.M;
import com.jythonui.shared.FieldItem;

public class GetImageList {

	private GetImageList() {

	}

	public interface IGetVData {
		IVModelData get();
	}

	public static String[] getList(FieldItem fi, IGetVData iGet) {
		String s = fi.getImageList();
		String js = Utils.getJS(s);
		if (!CUtil.EmptyS(js)) {
			IVModelData va = iGet.get();
			IJsonConvert iJson = GwtGiniInjector.getI().getJsonConvert();

			String jSon = iJson.construct(va);
			s = Utils.callJsStringFun(js, jSon);
		}

		if (CUtil.EmptyS(s)) {
			String mess = M.M().ListOfImagesCannotBeEmpty(fi.getId(), fi.getImageList());
			Utils.errAlert(mess);
			return null;
		}

		String[] o = s.split(",");
		if (o.length != fi.getImageColumn()) {
			String mess = M.M().ListOfImagesDoNotComply(fi.getId(), fi.getImageList(), s, fi.getImageColumn());
			Utils.errAlert(mess);
			return null;
		}
		return o;
	}

}
