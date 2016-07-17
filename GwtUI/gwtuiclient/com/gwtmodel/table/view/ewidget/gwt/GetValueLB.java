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

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.TOptional;
import com.gwtmodel.table.editw.IFormChangeListener;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.view.ewidget.comboutil.IValueLB;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class GetValueLB extends AbstractField implements IValueLB {

	final protected ListBox lB = new ListBox();
	private TOptional<String> beforeVal = null;
	private final boolean addEmpty;
	private List<String> idList = null;

	GetValueLB(IVField v, IFormFieldProperties pr,boolean addEmpty) {
		super(v, pr);
		initWidget(lB);
		lB.setName(getHtmlName());
		this.addEmpty = addEmpty;
	}

	GetValueLB(IVField v,IFormFieldProperties pr) {
		this(v, pr, false /*cValues.addEmptyAsDefault()*/);
	}

	protected String getValS() {
		int i = lB.getSelectedIndex();
		String s;
		if (i == -1) {
			// CHANGE: 2010/09/22
			// return null;
			s = beforeVal == null ? null : beforeVal.orNull();
		} else {
			if (idList != null) {
				s = idList.get(i);
			} else {
				s = lB.getItemText(i);
			}
		}
		return s;
	}

	@Override
	public Object getValObj() {
		String s = getValS();
		return FUtils.getValue(v, s);
	}

	private void setEmpty() {
		if (lB.getItemCount() == 0) {
			return;
		}
		lB.setItemSelected(0, true);
	}

	private void setV(final String s) {
		if (lB.getItemCount() == 0) {
			beforeVal = TOptional.fromNullable(s);
			return;
		}
		if (s == null) {
			setEmpty();
			return;
		}
		if (idList != null) {
			for (int i = 0; i < idList.size(); i++) {
				String val = idList.get(i);
				// TODO: experience
				if (val == null)
					continue;
				if (val.equals(s)) {
					lB.setSelectedIndex(i);
					return;
				}
			}
		} else
			for (int i = 0; i < lB.getItemCount(); i++) {
				String val = lB.getItemText(i);
				if (val.equals(s)) {
					lB.setSelectedIndex(i);
					return;
				}
			}
		setEmpty();
	}

	@Override
	public void setValObj(Object o) {
		setV((String) o);
		runOnChange(this, false);
	}

	@Override
	public void setReadOnly(final boolean readOnly) {
		lB.setEnabled(!readOnly);
	}

	public boolean validateField() {
		return true;
	}

	@Override
	public void addChangeListener(final IFormChangeListener l) {
		super.addChangeListener(l);
		ChangeListener le = new ChangeListener() {

			@Override
			public void onChange(Widget sender) {
				onTouch();
				runOnChange(GetValueLB.this, true);
			}
		};
		lB.addChangeListener(le);
		// added: 2011/08/12 : in case of immediate setting the list (before
		// AddChangeListener)
		if (!addEmpty) {
			runOnChange(GetValueLB.this, false);
		}
		//
	}

	/**
	 * @return the beforeVal
	 */
	@Override
	public TOptional<String> getBeforeVal() {
		return beforeVal;
	}

	@Override
	public void setList(List<String> li) {
		lB.clear();
		if (addEmpty) {
			lB.addItem(""); // add empty
		}
		for (String s : li) {
			lB.addItem(s);
		}
	}

	@Override
	public Widget getGWidget() {
		return this;
	}

	@Override
	public void setIdList(List<String> li) {
		this.idList = li;
	}

	@Override
	public boolean addEmpty() {
		return addEmpty;
	}

	@Override
	public void setFocus(boolean focus) {
		lB.setFocus(focus);

	}
}
