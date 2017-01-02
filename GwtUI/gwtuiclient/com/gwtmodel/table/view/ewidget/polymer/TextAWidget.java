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

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.polymer.elemental.EventListener;
import com.vaadin.polymer.paper.widget.PaperInput;
import com.vaadin.polymer.paper.widget.PaperTextarea;
import com.vaadin.polymer.paper.widget.event.ChangeEventHandler;

class TextAWidget {

	private PaperInput in = null;
	private PaperTextarea te = null;

	void setP(PaperInput in) {
		this.in = in;
	}

	void setP(PaperTextarea te) {
		this.te = te;
	}

	boolean isPaperInput() {
		return in != null;
	}

	String getLabel() {
		if (in != null)
			return in.getLabel();
		return te.getLabel();
	}

	void setLabel(String s) {
		if (in != null)
			in.setLabel(s);
		else
			te.setLabel(s);
	}

	void addChangeHandler(ChangeEventHandler ha) {
		if (in != null)
			in.addChangeHandler(ha);
		else
			te.addChangeHandler(ha);
	}

	void addEventListener(String type, EventListener listener) {
		if (in != null)
			in.getPolymerElement().addEventListener(type, listener);
		else
			te.getPolymerElement().addEventListener(type, listener);

	}

	void setRequired(boolean b) {
		if (in != null)
			in.setRequired(b);
		else
			te.setRequired(b);

	}

	void setPattern(String pattern) {
		if (in != null)
			in.setPattern(pattern);
		else
			te.setPattern(pattern);

	}

	void setAutoValidate(boolean b) {
		if (in != null)
			in.setAutoValidate(b);
		else
			te.setAutoValidate(b);

	}

	void setErrorMessage(String standErrMess) {
		if (in != null)
			in.setErrorMessage(standErrMess);
		else
			te.setErrorMessage(standErrMess);

	}

	void setType(String string) {
		if (in != null)
			in.setType(string);
		else
			te.setType(string);
	}

	String getValue() {
		if (in != null)
			return in.getValue();
		return te.getValue();
	}

	void setValue(String s) {
		if (in != null)
			in.setValue(s);
		else
			te.setValue(s);
	}

	Widget getGWidget() {
		if (in != null)
			return in;
		return te;
	}

	void setReadonly(boolean readOnly) {
		if (in != null)
			in.setReadonly(readOnly);
		else
			te.setReadonly(readOnly);
	}

	void setVisible(boolean b) {
		if (in != null)
			in.setVisible(b);
		else
			te.setVisible(b);

	}

	void setInvalid(boolean b) {
		if (in != null)
			in.setInvalid(b);
		else
			te.setInvalid(b);
	}

	void setTitle(String title) {
		if (in != null)
			in.setTitle(title);
		else
			te.setTitle(title);
	}

	void setFocused(boolean focus) {
		if (in != null)
			in.setFocused(focus);
		else
			te.setFocused(focus);

	}

	void validate() {
		if (in != null)
			in.validate();
		else
			te.validate();
	}

	boolean getInvalid() {
		if (in != null)
			return in.getInvalid();
		return te.getInvalid();
	}

}
