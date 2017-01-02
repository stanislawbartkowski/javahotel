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
package com.gwtmodel.table.editw;

class FormFieldProperties implements IFormFieldProperties {

	private final boolean readOnlyIfModif;
	private final boolean readOnlyIfAdd;
	private final boolean modeSetAlready;
	private final boolean label;
	private final boolean polymer;
	private final boolean hidden;
	private final String htmlId;
	private final boolean notEmpty;
	private final String displayName;
	private final boolean menu;
	private final int visLines;
	private final boolean multi;

	public boolean isMulti() {
		return multi;
	}

	FormFieldProperties() {
		this(false, false, false, false, false, false, false, null, null, false, -1, false);
	}

	FormFieldProperties(boolean readOnlyIfModif, boolean readOnlyIfAdd, boolean modeSetAlready, boolean label,
			boolean polymer, boolean hidden, boolean notEmpty, String htmlId, String displayName, boolean menu,
			int visLines, boolean multi) {
		this.readOnlyIfModif = readOnlyIfModif;
		this.readOnlyIfAdd = readOnlyIfAdd;
		this.modeSetAlready = modeSetAlready;
		this.label = label;
		this.polymer = polymer;
		this.hidden = hidden;
		this.htmlId = htmlId;
		this.notEmpty = notEmpty;
		this.displayName = displayName;
		this.menu = menu;
		this.visLines = visLines;
		this.multi = multi;
	}

	@Override
	public boolean isReadOnlyIfModif() {
		return readOnlyIfModif;
	}

	@Override
	public boolean isReadOnlyIfAdd() {
		return readOnlyIfAdd;
	}

	@Override
	public boolean isModeSetAlready() {
		return modeSetAlready;
	}

	@Override
	public boolean isLabel() {
		return label;
	}

	@Override
	public boolean isPolymer() {
		return polymer;
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public String getHtmlId() {
		return htmlId;
	}

	@Override
	public boolean isNotEmpty() {
		return notEmpty;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public boolean isMenu() {
		return menu;
	}

	@Override
	public int getVisLines() {
		return visLines;
	}

}
