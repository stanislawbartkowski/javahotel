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
package com.jythonui.client.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.datalisttype.DataListTypeFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.mm.MM;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.util.SolidPos;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.M;
import com.jythonui.client.dialog.DataType;
import com.jythonui.client.dialog.IDialogContainer;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

/**
 * @author hotel
 * 
 */
public class JUtils {

	private JUtils() {
	}

	public interface IVisitor {
		void action(String fie, String field);
	};

	public static void visitListOfFields(DialogVariables var, String prefix, IVisitor i) {
		for (String key : var.getFields())
			if (key.startsWith(prefix)) {
				String fie = key.substring(prefix.length());
				i.action(fie, key);
			}
	}

	public static IDataListType constructFromList(final List<String> li) {
		DataListTypeFactory lFactory = GwtGiniInjector.getI().getDataListTypeFactory();
		FieldItem item = new FieldItem();
		item.setId(ICommonConsts.DISPLAYNAME);
		item.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
		List<FieldItem> listI = new ArrayList<FieldItem>();
		listI.add(item);
		RowIndex rI = new RowIndex(listI);
		IVField vDisp = VField.construct(item);
		List<IVModelData> dList = new ArrayList<IVModelData>();
		for (String val : li) {
			IVModelData vData = new RowVModelData(rI);
			vData.setF(vDisp, val);
			dList.add(vData);
		}
		return lFactory.construct(dList, null, vDisp);
	}

	public static IDataListType constructList(RowIndex rI, ListOfRows rL, IVField comboField, IVField displayFie) {
		DataListTypeFactory lFactory = GwtGiniInjector.getI().getDataListTypeFactory();

		List<IVModelData> rList = new ArrayList<IVModelData>();
		if (rL != null)
			for (RowContent t : rL.getRowList()) {
				RowVModelData r = new RowVModelData(rI, t);
				rList.add(r);
			}
		return lFactory.construct(rList, comboField, displayFie);
	}

	public static void setVariables(DialogVariables v, IVModelData vData) {
		if (vData == null)
			return;
		for (IVField fie : vData.getF()) {
			Object o = vData.getF(fie);
			// pass empty as null (None)
			FieldValue fVal = new FieldValue();
			fVal.setValue(fie.getType().getType(), o, fie.getType().getAfterdot());
			v.setValue(fie.getId(), fVal);
		}
	}

	public interface IFieldVisit {
		void setField(VField v, FieldValue val, boolean global);
	}

	public static void VisitVariable(final DialogVariables var, String listid, final IFieldVisit i) {
		JUtils.IVisitor vis = new JUtils.IVisitor() {

			@Override
			public void action(String fie, String field) {
				boolean global = false;
				if (fie.startsWith(IUIConsts.JGLOBAL)) {
					global = true;
					fie = fie.substring(IUIConsts.JGLOBAL.length());
				}
				FieldValue val = var.getValue(fie);
				if (val == null) {
					Utils.errAlert(M.M().ErrorNoValue(fie), field);
					return;
				}
				VField v = VField.construct(fie, val.getType());
				i.setField(v, val, global);
			}
		};
		String prefix = ICommonConsts.JCOPY;
		if (listid != null)
			prefix = IUIConsts.JROWCOPY + listid + "_";
		JUtils.visitListOfFields(var, prefix, vis);
	}

	public static SolidPos constructSolidPos(DialogFormat d) {
		SolidPos pos = new SolidPos(d.getTop(), d.getLeft(), d.getMaxTop(), d.getMaxLeft());
		return pos;
	}

	public static ListFormat getL(IDataType dType) {
		DataType d = (DataType) dType;
		String listId = d.getId();
		IDialogContainer dC = d.getD();
		DialogFormat dForm = dC.getD();
		ListFormat li = dForm.findList(listId);
		return li;
	}

	public static class Names {
		private final String dialogName;
		private final String listName;

		public Names(String dialogName, String listName) {
			this.dialogName = dialogName;
			this.listName = listName;
		}

		public String getDialogName() {
			return dialogName;
		}

		public String getListName() {
			return listName;
		}

	}

	public static Names getNames(IDataType dType) {
		DataType d = (DataType) dType;
		String dName = d.getD().getInfo().getDialog().getId();
		String fName = d.getId();
		return new Names(dName, fName);
	}

	public static boolean verifyType(String fName, FieldValue val, TT expectedType) {
		if (val.getType() == expectedType)
			return true;
		String mess = M.M().InvalidFieldType(fName, val.getType().toString(), expectedType.toString());
		Utils.errAlert(mess);
		return false;
	}

	public static String getValueS(DialogVariables var, String field) {
		FieldValue val = var.getValue(field);
		if (!JUtils.verifyType(field, val, TT.STRING))
			return null;
		return val.getValueS();
	}

	public static boolean getValueB(DialogVariables var, String field) {
		FieldValue val = var.getValue(field);
		if (!JUtils.verifyType(field, val, TT.BOOLEAN))
			return false;
		return val.getValueB();
	}

	public static void prepareListOfRows(DialogVariables var, IDataType dType, ISlotable sLo, String listId,
			List<FieldItem> colList) {
		RowIndex rI = new RowIndex(colList);
		ListOfRows li = new ListOfRows();
		var.getRowList().put(listId, li);
		IDataListType dList = SlU.getIDataListType(dType, sLo);
		for (IVModelData vD : dList.getList()) {
			RowContent row = rI.constructRow();
			li.addRow(row);
			for (IVField v : vD.getF()) {
				String id = v.getId();
				FieldItem item = DialogFormat.findE(colList, id);
				if (item == null)
					continue;
				FieldValue vali = new FieldValue();
				vali.setValue(item.getFieldType(), vD.getF(v), item.getAfterDot());
				rI.setRowField(row, id, vali);
			}
		}
	}

	public static boolean isPolymerD(DialogFormat d) {
		return d.isPolymer() && MM.isPolymer();
	}

	public static String[] toA(List<String> l) {
		String[] aout = new String[l.size()];
		for (int i = 0; i < l.size(); i++)
			aout[i] = l.get(i);
		return aout;
	}

	public static String getDisplayName(FieldItem f) {
		String name = f.getDisplayName();
		if (name == null) {
			name = MM.getL().DefaStringName();
		}
		return name;
	}

	public static Widget findWidgetByFieldId(HasWidgets ha, String fieldid) {
		Iterator<Widget> iW = ha.iterator();
		// iterate through widget
		while (iW.hasNext()) {
			Widget ww = iW.next();
			String id = Utils.getWidgetAttribute(ww, BinderWidget.FIELDID);
			// look for fieldid attribute
			if (CUtil.EqNS(id, fieldid))
				return ww;
		}
		// not found
		return null;
	}
}
