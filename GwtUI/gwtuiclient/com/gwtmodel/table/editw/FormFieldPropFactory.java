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
package com.gwtmodel.table.editw;

public class FormFieldPropFactory {

	private FormFieldPropFactory() {
	}

	static public IFormFieldProperties construct() {
		return new FormFieldProperties();
	}

	static public IFormFieldProperties construct(boolean readOnlyIfModif, boolean readOnlyIfAdd, boolean modeSetAlready,
			boolean label, boolean polymer, boolean hidden, boolean notEmpty, String htmlId, String displayName) {
		return new FormFieldProperties(readOnlyIfModif, readOnlyIfAdd, modeSetAlready, label, polymer, hidden, notEmpty,
				htmlId, displayName);
	}

	static public IFormFieldProperties construct(String htmlId) {
		return new FormFieldProperties(false, false, false, false, false, false, false, htmlId, null);
	}

	static public IFormFieldProperties constructNotEmpty() {
		return new FormFieldProperties(false, false, false, false, false, false, true, null, null);
	}

	static public IFormFieldProperties constructDisplayName(String displayName) {
		return new FormFieldProperties(false, false, false, false, false, false, true, null, displayName);
	}

}
