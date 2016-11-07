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
package com.gwtmodel.table.binder;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.map.XMap;

public class BinderWidget extends XMap {

	public final static String FIELDID = "fieldid";

	private static final long serialVersionUID = 1L;

	private WidgetTypes type;

	private String contentHtml;

	public static class StyleClass extends XMap {
		private static final long serialVersionUID = 1L;
		private String content;

		public void setContent(String content) {
			this.content = content;
		}

		public String getContent() {
			return content;
		}

	}

	private List<StyleClass> styleList = new ArrayList<StyleClass>();

	private String id;

	private boolean idDropId = false;
	private boolean classDropDownContent = false;

	// recursive
	private List<BinderWidget> wList = new ArrayList<BinderWidget>();

	public WidgetTypes getType() {
		return type;
	}

	public void setType(WidgetTypes type) {
		this.type = type;
	}

	public String getContentHtml() {
		return contentHtml;
	}

	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<BinderWidget> getwList() {
		return wList;
	}

	public void setwList(List<BinderWidget> wList) {
		this.wList = wList;
	}

	public String getFieldId() {
		return getAttr(FIELDID);
	}

	public boolean isFieldId() {
		return isAttr(FIELDID);
	}

	public List<StyleClass> getStyleList() {
		return styleList;
	}

	public boolean isIdDropId() {
		return idDropId;
	}

	public void setIdDropId(boolean idDropId) {
		this.idDropId = idDropId;
	}

	public boolean isClassDropDownContent() {
		return classDropDownContent;
	}

	public void setClassDropDownContent(boolean classDropDownContent) {
		this.classDropDownContent = classDropDownContent;
	}

}
