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

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;

public class FormField {

	private final String pLabel;
	private final IFormLineView eLine;
	private final IVField fRange;

	private final IFormFieldProperties formProp;

	public FormField(final String p, final IFormLineView e, final IVField fie, IFormFieldProperties prop,
			IVField fRange) {
		this.pLabel = p;
		formProp = prop == null ? new FormFieldProperties() : prop;
		if (e == null) {
			assert fie != null : LogT.getT().cannotBeNull();
			this.eLine = EditWidgetFactory.constructEditWidget(fie, null);
		} else {
			this.eLine = e;
		}
		this.fRange = fRange;
	}

	public FormField(final String p, final IVField fie) {
		this(p, null, fie, null, null);
	}

	public FormField(final String p, final IFormLineView e) {
		this(p, e, null, null, null);
	}

	public FormField(final String p, final IFormLineView e, final IVField fie, IVField fRange) {
		this(p, e, fie, null, fRange);
	}

	public FormField(final String p, final IFormLineView e, final IVField fie) {
		this(p, e, fie, null, null);
	}

	/**
	 * @return the pLabel
	 */
	public String getPLabel() {
		return pLabel;
	}

	/**
	 * @return the eLine
	 */
	public IFormLineView getELine() {
		return eLine;
	}

	/**
	 * @return the fie
	 */
	public IVField getFie() {
		return eLine.getV();
	}

	public boolean isRange() {
		return fRange != null;
	}

	public IVField getFRange() {
		return fRange;

	}

	public IFormFieldProperties getFormProp() {
		return formProp;
	}

}
