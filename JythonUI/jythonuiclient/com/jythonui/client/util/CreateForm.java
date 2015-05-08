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
package com.jythonui.client.util;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ClickButtonType.StandClickEnum;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.tabledef.IColumnImageSelect;
import com.gwtmodel.table.tabledef.IGetSpinnerRange;
import com.gwtmodel.table.tabledef.VFooterDesc;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IEnumTypesList;
import com.jythonui.client.dialog.VField;
import com.jythonui.client.injector.UIGiniInjector;
import com.jythonui.client.interfaces.IGenCookieName;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;
import com.jythonui.shared.TypedefDescr;

/**
 * @author hotel
 * 
 */
public class CreateForm {

	private CreateForm() {
	}

	private static String getDisplayName(FieldItem f) {
		String name = f.getDisplayName();
		if (name == null) {
			name = M.J().DefaStringName();
		}
		return name;
	}

	public static FormLineContainer construct(DialogInfo dInfo,
			IGetDataList iGet, IEnumTypesList eList,
			IRequestForGWidget iHelper, IConstructCustomDataType fType) {
		DialogFormat d = dInfo.getDialog();
		List<FieldItem> iList = d.getFieldList();
		EditWidgetFactory eFactory = GwtGiniInjector.getI()
				.getEditWidgetFactory();
		List<FormField> fList = new ArrayList<FormField>();
		IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
		for (FieldItem f : iList) {
			// if (!CUtil.EmptyS(f.getFrom()))
			// continue;
			IVField vf = VField.construct(f);
			IFormLineView v;
			String htmlId = f.getHtmlId();
			if (f.isEmailType())
				v = eFactory.constructEmail(vf, htmlId);
			else if (f.isSpinner())
				v = eFactory.constructSpinner(vf, htmlId, f.getSpinnerMin(),
						f.getSpinnerMax());
			else if (f.isUploadType())
				v = eFactory.constructEditFileName(vf, htmlId);
			else if (f.isDownloadType())
				v = eFactory.constructAnchorField(vf);
			else if (f.isHtmlType())
				v = eFactory.constructHTMLField(vf);
			else if (f.isLabel())
				v = eFactory.constructLabelField(vf,
						iMess.getMessage(f.getDisplayName()));
			else if (!CUtil.EmptyS(f.getCustom())) {
				TypedefDescr te = d.findCustomType(f.getCustom());
				if (te == null) {
					Utils.errAlert(f.getCustom(),
							M.M().CannotFindCustomType(f.getTypeName()));
				}
				if (te.isComboType()) {
					eList.add(vf, f.getCustom());
					v = eFactory.constructListValuesCombo(vf, iGet,
							!f.isNotEmpty(), htmlId);
				} else {
					IDataType dType = fType.construct(f.getTypeName());
					v = eFactory.constructHelperList(vf, dType,
							f.isHelperRefresh(), htmlId);
				}
			} else {
				if (f.isPassword()) {
					v = eFactory.constructPasswordField(vf, htmlId);
				} else if (f.isHelper() || f.isTextArea() || f.isRichText()) {
					if (f.isHelper() && (f.getFieldType() != TT.STRING)
							&& f.getFieldType() != TT.DATE) {
						String mess = M.M().HelperOnlyForStringType(f.getId());
						Utils.errAlertB(mess);
					}
					if (f.getFieldType() == TT.STRING)
						v = eFactory.constructTextField(vf, null,
								f.isHelper() ? iHelper : null, f.isTextArea(),
								f.isRichText(), f.isHelperRefresh(), htmlId);
					else
						v = eFactory.constructDateBoxCalendarWithHelper(vf,
								iHelper, true, htmlId);
				} else {
					v = eFactory.constructEditWidget(vf, htmlId);
				}

			}
			boolean modeSetAlready = false;
			if (f.isHidden()) {
				v.setHidden(true);
				modeSetAlready = true;
			}
			if (f.isReadOnly()) {
				v.setReadOnly(true);
				modeSetAlready = true;
			}
			// 2014/09/06
			if (!CUtil.EmptyS(f.getWidth())) {
				v.getGWidget().setWidth(f.getWidth());
			}
			if (!CUtil.EmptyS(f.getVisLines())) {
				v.setAttr("rows", f.getVisLines());
			}

			String name = null;
			IVField fRange = null;

			if (!CUtil.EmptyS(f.getFrom())) {
				if (CUtil.EmptyS(f.getDisplayName()))
					name = MM.getL().BetweenFieldsRange();
				FieldItem ff = d.findFieldItem(f.getFrom());
				if (ff == null)
					Utils.errAlert(M.M().CannotFindFromField(
							ICommonConsts.FROM, f.getFrom()));
				else
					fRange = VField.construct(ff);
			}
			if (name == null)
				name = getDisplayName(f);

			if (!CUtil.EmptyS(f.getCellTitle()))
				v.setCellTitle(f.getCellTitle());

			FormField fie = new FormField(name, v, vf, fRange,
					f.isReadOnlyChange(), f.isReadOnlyAdd(), modeSetAlready,
					f.isLabel() || f.isHtmlType());
			fList.add(fie);
		}
		return new FormLineContainer(fList);
	}

	public static class ColumnsDesc {
		public List<VListHeaderDesc> hList = new ArrayList<VListHeaderDesc>();
		public List<VFooterDesc> footList = new ArrayList<VFooterDesc>();
		public int colvisNo = 0;
	}

	public interface ISelectFactory {

		IColumnImageSelect construct(IVField v, FieldItem f);
	}

	public interface IGetEnum {
		FieldDataType.IGetListValues getEnum(String customT, boolean addempty);
	}

	private static VListHeaderDesc.ColAlign getA(String a) {
		VListHeaderDesc.ColAlign al = null;
		if (CUtil.EqNS(a, IUIConsts.ALIGNL))
			al = VListHeaderDesc.ColAlign.LEFT;

		if (CUtil.EqNS(a, IUIConsts.ALIGNR))
			al = VListHeaderDesc.ColAlign.RIGHT;

		if (CUtil.EqNS(a, IUIConsts.ALIGNC))
			al = VListHeaderDesc.ColAlign.CENTER;
		return al;
	}

	public static ColumnsDesc constructColumns(List<FieldItem> fList,
			ISelectFactory sFactory, IGetEnum iGet) {
		ColumnsDesc desc = new ColumnsDesc();
		List<VListHeaderDesc> heList = desc.hList;
		for (final FieldItem f : fList) {
			IVField vf;
			boolean isSelect = false;
			IGetSpinnerRange iSpinner = null;
			if (!CUtil.EmptyS(f.getCustom()))
				if (iGet.getEnum(f.getCustom(), !f.isNotEmpty()) != null)
					vf = VField.construct(f.getId(),
							iGet.getEnum(f.getCustom(), !f.isNotEmpty()));
				else {
					isSelect = true;
					vf = VField.construct(f);
				}
			else
				vf = VField.construct(f);
			VListHeaderDesc.ColAlign al = getA(f.getAlign());
			// TODO: can be null for combo, check it later
			IColumnImageSelect iHelper = null;
			int colNo = 0;
			if (f.isSpinner()) {
				iSpinner = new IGetSpinnerRange() {

					@Override
					public int min() {
						return f.getSpinnerMin();
					}

					@Override
					public int max() {
						return f.getSpinnerMax();
					}

				};
			}
			if ((f.isHelper() || isSelect || f.isImageColumn())
					&& sFactory != null) {
				iHelper = sFactory.construct(vf, f);
				if (f.isImageColumn()) {
					if (vf.getType().getType() != TT.STRING) {
						String mess = M.M().OnlyStringColumnImage(f.getId(),
								f.getTypeName(), ICommonConsts.STRINGTYPE,
								ICommonConsts.IMAGECOLUMN);
						Utils.errAlert(mess);
					}
					colNo = f.getImageColumn();
				}
			}
			VListHeaderDesc v = new VListHeaderDesc(getDisplayName(f), vf,
					f.isHidden(), f.getActionId(), f.isColumnEditable(), al,
					f.getWidth(), f.getEditClass(), f.getEditCss(), iHelper,
					colNo, f.getColumnClass(), f.getHeaderClass(), iSpinner);
			heList.add(v);
			if (!f.isHidden())
				desc.colvisNo++;
			if (f.isFooter()) {
				if (desc.footList == null)
					desc.footList = new ArrayList<VFooterDesc>();
				VFooterDesc foot = new VFooterDesc(vf,
						getA(f.getFooterAlign()), VField.getFieldType(
								f.getFooterType(), f.getFooterAfterDot()));
				desc.footList.add(foot);
			}
		}
		return desc;
	}

	public static VListHeaderContainer constructColumns(IDataType dType,
			ListFormat l, ISelectFactory sFactory, IGetEnum iGet) {
		ColumnsDesc desc = constructColumns(l.getColumns(), sFactory, iGet);
		String lName = l.getDisplayName();
		IGenCookieName iCookie = UIGiniInjector.getI().getGenCookieName();
		String cookieName = iCookie.genCookieName(dType,
				IUIConsts.COOKIEPAGESIZE);
		String pSize = Utils.getCookie(cookieName);
		int pageSize;
		if (CUtil.EmptyS(pSize))
			pageSize = l.getPageSize();
		else
			pageSize = Utils.getNum(pSize);
		String colCookieName = iCookie.genCookieName(dType,
				IUIConsts.COOKIEPROPERTYLIST);
		String jSon = Utils.getCookie(colCookieName);
		if (!CUtil.EmptyS(jSon)) {
			ListOfRows ro = ParseJ.toProp(jSon);
			RowIndex rI = ParseJ.constructProp();
			List<VListHeaderDesc> newH = new ArrayList<VListHeaderDesc>();
			for (RowContent r : ro.getRowList()) {
				String id = rI.get(r, IUIConsts.PROPID).getValueS();
				boolean visible = rI.get(r, IUIConsts.PROPVISIBLE).getValueB()
						.booleanValue();
				String headerS = rI.get(r, IUIConsts.PROPCOLUMNNAME)
						.getValueS();
				IVField vv = VField.construct(id);
				for (VListHeaderDesc v : desc.hList) {
					if (v.getFie().eq(vv)) {
						if (CUtil.EmptyS(headerS))
							headerS = "";
						v.setHeaderString(headerS);
						v.setHidden(!visible);
						newH.add(v);
						break;
					}
				}
			}
			desc.hList.clear();
			desc.hList.addAll(newH);
		}
		return new VListHeaderContainer(desc.hList, lName, pageSize,
				l.getJSModifRow(), l.getWidth(), null, desc.footList,
				l.getPageSize(), l.isNoPropertyColumn());
	}

	public static ControlButtonDesc constructButton(ButtonItem b,
			boolean enabled, boolean hidden) {

		String id = b.getId();
		String dName = b.getDisplayName();
		String imageName = b.getImageButton();
		if (b.isHeaderButton())
			return new ControlButtonDesc(imageName, dName, new ClickButtonType(
					StandClickEnum.MENUTITLE), enabled, hidden);
		else
			return new ControlButtonDesc(imageName, dName, new ClickButtonType(
					id), enabled, hidden);
	}

	public static List<ControlButtonDesc> constructBList(List<ButtonItem> iList) {
		List<ControlButtonDesc> bList = new ArrayList<ControlButtonDesc>();
		for (ButtonItem b : iList) {
			bList.add(constructButton(b, !b.isReadOnly(), b.isHidden()));
		}
		return bList;
	}

}
