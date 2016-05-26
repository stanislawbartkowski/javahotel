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
package com.gwtmodel.table;

public abstract class AbstractVField implements IVField {

	private final String id;

	private final String label;

	private final FieldDataType dType;

	protected AbstractVField(String id, FieldDataType dType, String label) {
		this.id = id;
		this.label = label;
		this.dType = dType;
	}

	protected AbstractVField(String id) {
		this(id, null, null);
	}

	protected AbstractVField(String id, FieldDataType dType) {
		this(id, dType, null);
	}

	protected AbstractVField() {
		this(null, null, null);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public FieldDataType getType() {
		return dType;
	}

}
