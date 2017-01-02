/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.view.ewidget.polymer;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.view.ewidget.widgets.EmptyBinderWidget;
import com.gwtmodel.table.view.ewidget.widgets.HTMLBinderWidget;
import com.gwtmodel.table.view.ewidget.widgets.IBinderWidget;
import com.gwtmodel.table.view.ewidget.widgets.LabelBinderWidget;
import com.vaadin.polymer.paper.widget.PaperSlider;
import com.vaadin.polymer.paper.widget.event.ValueChangeEvent;
import com.vaadin.polymer.paper.widget.event.ValueChangeEventHandler;

class BinderWidget extends AbstractWField {

	private IBinderWidget b = new EmptyBinderWidget();

	class PaperSliderBinder implements IBinderWidget {

		private final PaperSlider p;

		PaperSliderBinder(PaperSlider p) {
			this.p = p;
			p.addValueChangeHandler(new ValueChangeEventHandler() {

				@Override
				public void onValueChange(ValueChangeEvent event) {
					onChangeEdit(true);
				}

			});
		}

		@Override
		public void setReadOnly(boolean readOnly) {
		}

		@Override
		public void setHidden(boolean hidden) {
		}

		@Override
		public void setInvalidMess(String errmess) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setGStyleName(String styleMess, boolean set) {
			if (set)
				p.setStyleName(styleMess);
			else
				p.removeStyleName(styleMess);

		}

		@Override
		public void setCellTitle(String title) {
			p.setTitle(title);
		}

		@Override
		public void setFocus(boolean focus) {
			p.setFocused(focus);
		}

		@Override
		public boolean isInvalid() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object getValObj() {
			return new BigDecimal(p.getValue());
		}

		@Override
		public void setValObj(Object o) {
			p.setValue((double) o);
		}

		@Override
		public Widget getGWidget() {
			return p;
		}

	}

	protected BinderWidget(IVField v, IFormFieldProperties pr) {
		super(v, pr, null);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		b.setReadOnly(readOnly);
	}

	@Override
	public void setHidden(boolean hidden) {
		b.setHidden(hidden);
	}

	@Override
	public void setInvalidMess(String errmess) {
		b.setInvalidMess(errmess);
	}

	@Override
	public void setGStyleName(String styleMess, boolean set) {
		b.setGStyleName(styleMess, set);
	}

	@Override
	public void setCellTitle(String title) {
		b.setCellTitle(title);
	}

	@Override
	public void setSuggestList(List<String> list) {
	}

	@Override
	public void setFocus(boolean focus) {
		b.setFocus(focus);
	}

	@Override
	public boolean isInvalid() {
		return b.isInvalid();
	}

	@Override
	public Object getValObj() {
		return b.getValObj();
	}

	@Override
	public void setValObj(Object o) {
		b.setValObj(o);
	}

	@Override
	public Widget getGWidget() {
		return b.getGWidget();
	}

	@Override
	public void replaceWidget(Widget wi) {
		// only few implemented, to do later
		if (wi instanceof PaperSlider)
			b = new PaperSliderBinder((PaperSlider) wi);
		// important: HTML before Label
		else if (wi instanceof HTML)
			b = new HTMLBinderWidget((HTML) wi);
		// before Label
		else if (wi instanceof InlineLabel)
			// the same as Label, the difference is constructor only
			b = new LabelBinderWidget((InlineLabel) wi);
		else if (wi instanceof Label)
			b = new LabelBinderWidget((Label) wi);
		else {
			String mess = LogT.getT().BinderReplaceWidget(v.getId(), wi.getClass().getName());
			Utils.errAlertB(mess);
		}
	}

}
