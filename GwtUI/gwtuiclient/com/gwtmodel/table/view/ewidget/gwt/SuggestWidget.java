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
package com.gwtmodel.table.view.ewidget.gwt;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IGetDataListCallBack;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.factories.IGetCustomValues;

class SuggestWidget extends AbstractField {

	private final SuggestBox box;
	private final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();

	private class SetValues implements IGetDataListCallBack {

		@Override
		public void set(IDataListType dataList) {
			oracle.clear();
			IVField displayV = dataList.displayComboField();
			for (IVModelData i : dataList.getList()) {
				String name = (String) i.getF(displayV);
				oracle.add(name);
			}
		}

	}

	private class CHandler implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
			runOnChange(SuggestWidget.this, true);
		}
	}

	SuggestWidget(IVField v, IFormFieldProperties pr, IGetDataList iGet) {
		super(v, pr);
		box = new SuggestBox(oracle);
		box.getValueBox().addChangeHandler(new CHandler());
		if (iGet != null)
			iGet.call(v, new SetValues());
		initWidget(box);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.setReadOnly(readOnly);

	}

	@Override
	public Object getValObj() {
		return box.getValueBox().getValue();
	}

	@Override
	public void setValObj(Object o) {
		box.getValueBox().setValue((String) o);
	}

	@Override
	public void setSuggestList(List<String> list) {
		oracle.clear();
		for (String s : list)
			oracle.add(s);
	}

	@Override
	public void setFocus(boolean focus) {
		box.setFocus(focus);

	}

}
