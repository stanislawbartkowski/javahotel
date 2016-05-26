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
package com.jythonui.client.dialog;

import com.gwtmodel.table.AbstractVField;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.mm.LogT;
import com.jythonui.client.util.JUtils;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;

/**
 * @author hotel
 * 
 */
public class VField extends AbstractVField {

	private final String id;
	private final TT type;
	private final int afterdot;
	private final FieldDataType.IGetListValues eList;
	private final String label;

	private VField(String id, TT type, int afterdot, FieldDataType.IGetListValues eList, String label) {
		super(id, eList != null ? FieldDataType.constructStringList(eList) : getFieldType(type, afterdot), label);
		assert id != null : LogT.getT().cannotBeNull();
		this.id = id;
		this.type = type;
		this.afterdot = afterdot;
		this.eList = eList;
		this.label = label;
	}

	public static VField construct(String id, TT type) {
		return new VField(id, type, ICommonConsts.DEFAULTAFTERDOT, null, null);
	}

	public static VField construct(String id, FieldDataType.IGetListValues eList) {
		return new VField(id, TT.STRING, 0, eList, null);
	}

	public static VField construct(FieldItem f) {
		return new VField(f.getId(), f.getFieldType(), f.getAfterDot(), null, JUtils.getDisplayName(f));
	}

	public static VField construct(String fie, FieldValue f) {
		return new VField(fie, f.getType(), f.getAfterdot(), null, null);
	}

	public static VField construct(String id) {
		return new VField(id, TT.STRING, 0, null, null);
	}

	@Override
	public boolean eq(IVField o) {
		VField v = (VField) o;
		return id.equals(v.id);
	}

	public static FieldDataType getFieldType(TT type, int afterdot) {
		if (type == TT.BIGDECIMAL) {
			return FieldDataType.constructBigDecimal(afterdot);
		}
		return FieldDataType.construct(type);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		VField v = (VField) o;
		return eq(v);
	}
}
