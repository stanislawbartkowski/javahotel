/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.columndialog;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.factories.ILaunchPropertyDialogColumn;
import com.gwtmodel.table.json.IJsonConvert;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.dialog.ICustomClickAction;
import com.jythonui.client.dialog.VField;
import com.jythonui.client.dialog.run.RunAction;
import com.jythonui.client.interfaces.IGenCookieName;
import com.jythonui.client.interfaces.IVariableContainerFactory;
import com.jythonui.client.util.JUtils;
import com.jythonui.client.util.ParseJ;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class PropertyColumnDialog implements ILaunchPropertyDialogColumn {

	private final IVariableContainerFactory vFactory;
	private final IGenCookieName iCookie;
	private final IJsonConvert iJson;

	@Inject
	public PropertyColumnDialog(IVariableContainerFactory vFactory,
			IGenCookieName iCookie, IJsonConvert iJson) {
		this.vFactory = vFactory;
		this.iCookie = iCookie;
		this.iJson = iJson;
	}

	private class Click implements ICustomClickAction {

		private final ISlotable publishSlo;
		private final IDataType dType;

		Click(ISlotable publishSlo, IDataType dType) {
			this.publishSlo = publishSlo;
			this.dType = dType;
		}

		private class CreateH {

			private final VListHeaderContainer vHeader = SlU.getHeaderList(
					dType, publishSlo);
			private final List<VListHeaderDesc> vL = vHeader.getAllHeList();
			private final List<VListHeaderDesc> nL = new ArrayList<VListHeaderDesc>();

			void addHeader(String id, boolean hidden, String columnName) {
				IVField vF = VField.construct(id);
				for (VListHeaderDesc vv : vL) {
					if (vv.getFie().eq(vF)) {
						vv.setHidden(hidden);
						vv.setHeaderString(columnName);
						nL.add(vv);
						break;
					}
				}
			}
		}

		@Override
		public boolean click(String id, WSize w, DialogFormat d,
				DialogVariables v) {

			CreateH cH = new CreateH();
			boolean refresh = false;
			String cName = iCookie.genCookieName(dType,
					IUIConsts.COOKIEPROPERTYLIST);
			if (id.equals("defa")) {
				Utils.RemoveCookie(cName);
				refresh = true;
				ListFormat li = JUtils.getL(dType);
				for (FieldItem f : li.getColumns()) {
					String idS = f.getId();
					boolean hidden = f.isHidden();
					String columnName = f.getDisplayName();
					cH.addHeader(idS, hidden, columnName);
				}
			}
			if (id.equals("accept")) {
				refresh = true;
				ListOfRows rL = v.getList(IUIConsts.LISTPROPERTY);
				for (RowContent r : rL.getRowList()) {
					boolean hidden = !r.getRow(0).getValueB();
					String idS = r.getRow(1).getValueS();
					String header = r.getRow(2).getValueS();
					cH.addHeader(idS, hidden, header);
				}
				RowIndex ri = ParseJ.constructProp();
				IDataListType iList = JUtils.constructList(ri, rL, null, null);
				String jSon = iJson.construct(iList);
				Utils.SetCookie(cName, jSon);

			}
			if (refresh) {
				cH.vL.clear();
				cH.vL.addAll(cH.nL);
				publishSlo.getSlContainer().publish(dType, cH.vHeader);
				publishSlo.getSlContainer().publish(dType,
						DataActionEnum.RefreshListAction);

			}
			return id.equals("accept");
		}
	}

	@Override
	public void doDialog(ISlotable publishSlo, IDataType dType, WSize w) {
		IVariablesContainer iCon = vFactory.construct();
		VListHeaderContainer vHeader = SlU.getHeaderList(dType, publishSlo);
		DialogVariables var = new DialogVariables();
		ListOfRows li = new ListOfRows();
		RowIndex rI = ParseJ.constructProp();
		for (VListHeaderDesc v : vHeader.getAllHeList()) {
			RowContent ro = rI.constructRow();
			FieldValue val = new FieldValue();
			val.setValue(!v.isHidden());
			rI.setRowField(ro, IUIConsts.PROPVISIBLE, val);
			val = new FieldValue();
			val.setValue(v.getFie().getId());
			rI.setRowField(ro, IUIConsts.PROPID, val);
			val = new FieldValue();
			val.setValue(v.getHeaderString());
			rI.setRowField(ro, IUIConsts.PROPCOLUMNNAME, val);
			li.addRow(ro);
		}
		var.getRowList().put(IUIConsts.LISTPROPERTY, li);
		new RunAction().upDialog(IUIConsts.COLUMNDIALOG, w, iCon, null, null,
				null, var, new Click(publishSlo, dType));
	}

}
