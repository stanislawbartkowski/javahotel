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
package com.jythonui.client.util;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IDialogContainer;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;

/**
 * @author hotel
 * 
 */
public class VerifyJError {

	private VerifyJError() {

	}

	public interface IOkFieldName {
		boolean okField(String s);
	}

	public static List<InvalidateMess> constructErrors(final DialogVariables v, final IOkFieldName ok) {
		final List<InvalidateMess> err = new ArrayList<InvalidateMess>();
		JUtils.IVisitor vis = new JUtils.IVisitor() {

			@Override
			public void action(String fie, String field) {
				if (field.equals(ICommonConsts.JERRORMESSAGE)) {
					return;
				}
				if (!ok.okField(fie))
					return;
				String errS = v.getValue(field).getValueS();
				FieldValue val = v.getValue(fie);
				if (val == null) {
					Utils.errAlert(M.M().NoFieldRelatedToError(fie, field), errS == null ? "" : errS);
					return;
				}
				VField vv = VField.construct(fie, val);
				err.add(new InvalidateMess(vv, errS));
			}
		};
		// important: add underline (JERROR does not have underline)
		JUtils.visitListOfFields(v, ICommonConsts.JERROR + "_", vis);
		return err;
	}

	public static boolean isError(final IDialogContainer d, IDataType dType, final DialogVariables v, ISlotable iSlo) {

		IOkFieldName iOk = new IOkFieldName() {

			@Override
			public boolean okField(String s) {
				return d.getInfo().getDialog().findFieldItem(s) != null;
			}

		};
		List<InvalidateMess> err = constructErrors(v, iOk);
		if (err.isEmpty()) {
			if (d == null)
				return false;
			if (!d.okCheckListError(v))
				return true;
			return false;
		}
		iSlo.getSlContainer().publish(dType,
				// d == null ? DataActionEnum.ChangeViewFormToInvalidAction
				// : DataActionEnum.InvalidSignal,
				DataActionEnum.ChangeViewFormToInvalidAction, new InvalidateFormContainer(err));
		return true;
	}

}
