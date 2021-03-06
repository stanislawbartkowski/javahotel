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
package com.jythonui.client.listmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType.IGetListValues;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IMapEntry;
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.TOptional;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.VModelData;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.chooselist.ChooseListFactory;
import com.gwtmodel.table.chooselist.ICallBackWidget;
import com.gwtmodel.table.chooselist.IChooseList;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistListAction;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.json.IJsonConvert;
import com.gwtmodel.table.listdataview.ChangeFieldEditSignal;
import com.gwtmodel.table.listdataview.ClickColumnImageSignal;
import com.gwtmodel.table.listdataview.DataIntegerSignal;
import com.gwtmodel.table.listdataview.DataIntegerVDataSignal;
import com.gwtmodel.table.listdataview.EditRowActionSignal;
import com.gwtmodel.table.listdataview.EditRowErrorSignal;
import com.gwtmodel.table.listdataview.EditRowsSignal;
import com.gwtmodel.table.listdataview.FinishEditRowSignal;
import com.gwtmodel.table.listdataview.GetImageColSignal;
import com.gwtmodel.table.listdataview.GetImageColSignalReturn;
import com.gwtmodel.table.listdataview.ReadChunkSignal;
import com.gwtmodel.table.listdataview.StartNextRowSignal;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.tabledef.IColumnImageSelect;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.table.ChangeEditableRowsParam;
import com.gwtmodel.table.view.util.AbstractDataModel;
import com.gwtmodel.table.view.util.ModalDialog;
import com.gwtmodel.table.view.util.SolidPos;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.M;
import com.jythonui.client.dialog.ICreateBackActionFactory;
import com.jythonui.client.dialog.IPerformClickAction;
import com.jythonui.client.dialog.VField;
import com.jythonui.client.injector.UIGiniInjector;
import com.jythonui.client.interfaces.IExecuteBackAction;
import com.jythonui.client.interfaces.IExecuteJS;
import com.jythonui.client.util.CreateForm;
import com.jythonui.client.util.CreateForm.IGetEnum;
import com.jythonui.client.util.CreateForm.ISelectFactory;
import com.jythonui.client.util.CreateSearchVar;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.client.util.GetImageList;
import com.jythonui.client.util.JUtils;
import com.jythonui.client.util.ListOfButt;
import com.jythonui.client.util.PerformVariableAction;
import com.jythonui.client.util.PerformVariableAction.VisitList;
import com.jythonui.client.util.PerformVariableAction.VisitList.IGetFooter;
import com.jythonui.client.util.ValidateForm;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.MapDialogVariable;
import com.jythonui.shared.TypedefDescr;

/**
 * @author hotel
 * 
 */
class ListControler {

	private ListControler() {

	}

	private static class DataModel extends AbstractDataModel {

		private final RowListDataManager rM;

		DataModel(RowListDataManager rM) {
			this.rM = rM;
		}

		@Override
		public IVModelData construct(IDataType dType) {
			return rM.contructE(dType);
		}
	}

	private static String getFormHeader(RowListDataManager rM, IDataType da) {
		ListFormat fo = rM.getFormat(da);
		String tName = null;
		if (fo.getfElem() != null) {
			tName = fo.getfElem().getDisplayName();
		}
		if (tName == null) {
			tName = fo.getDisplayName();
		}
		return tName;
	}

	private static SolidPos constructSolidPos(RowListDataManager rM, IDataType da) {
		ListFormat fo = rM.getFormat(da);
		if (fo.getfElem() == null)
			return null;
		return JUtils.constructSolidPos(fo.getfElem());
	}

	private static class DataListPersistAction extends AbstractSlotContainer implements IDataPersistListAction {

		private final RowListDataManager rM;
		private final IVariablesContainer iCon;
		private final ICreateBackActionFactory bFactory;
		private final IVModelData vFooter = new VModelData();
		private final IJsonConvert iJson;
		private final IExecuteJS executeJS;
		private final IExecuteBackAction executeBack;

		private int alastRowNum;
		private int afterRowNum = -1;
		private PersistTypeEnum lastPersistAction;
		private ChangeFieldEditSignal lastC = null;
		private FinishEditRowSignal lastF = null;
		private MutableInteger lastI = null;

		private int getLastRow() {
			if (afterRowNum != -1)
				return afterRowNum;
			return alastRowNum;
		}

		private void setLastRow(int r) {
			alastRowNum = r;
		}

		private void setAfterRowNum(int r) {
			afterRowNum = r;
		}

		private void drawFooter(List<IGetFooter> fList) {
			// persist current footer value in class variable
			for (IGetFooter i : fList) {
				vFooter.setF(VField.construct(i.getFie()), i.getV().getValue());
			}
			getSlContainer().publish(dType, DataActionEnum.DrawFooterAction, vFooter);
		}

		private void changeToEdit(VisitList.EditListMode eMode) {
			SlotType sl = EditRowsSignal.constructEditRowSignal(dType);
			EditRowsSignal sig = new EditRowsSignal(ChangeEditableRowsParam.ALLROWS, true, eMode.getMode(),
					eMode.geteList());
			getSlContainer().publish(sl, sig);
		}

		private class RowActionOkListener implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				if (lastPersistAction == null)
					return;
				ICustomObject i = slContext.getCustom();
				RowActionOk r = (RowActionOk) i;
				assert lastPersistAction != null : LogT.getT().cannotBeNull();
				if (lastPersistAction == PersistTypeEnum.ADD || lastPersistAction == PersistTypeEnum.ADDBEFORE) {
					CustomStringSlot sl = DataIntegerVDataSignal.constructSlotAddRowSignal(dType);
					DataIntegerVDataSignal sig = new DataIntegerVDataSignal(getLastRow(), r.getValue(),
							lastPersistAction == PersistTypeEnum.ADD);
					getSlContainer().publish(sl, sig);
				}
				if (lastPersistAction == PersistTypeEnum.REMOVE) {
					CustomStringSlot sl = DataIntegerVDataSignal.constructSlotRemoveVSignal(dType);
					DataIntegerSignal sig = new DataIntegerSignal(getLastRow());
					getSlContainer().publish(sl, sig);

				}
				lastPersistAction = null;
			}

		}

		private class DrawFooter implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				DrawFooterSignal sig = (DrawFooterSignal) i;
				drawFooter(sig.getValue());
			}
		}

		private class ChangeToEdit implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				ChangeToEditSignal sig = (ChangeToEditSignal) i;
				changeToEdit(sig.getValue());
			}

		}

		private int getI() {
			if (lastF != null) {
				return lastF.getValue().getChoosedLine();
			}
			if (lastC != null) {
				return lastC.getValue();
			}
			if (lastI != null) {
				int i = lastI.intValue();
				// 2014/10/02 : do not null here
				// lastI = null;
				return i;
			}
			return -1;
		}

		private IVModelData getV() {
			IVModelData vData;
			if (lastF != null)
				// set by FinishRowEdit, only for that purpose
				vData = SlU.getVDataByI(dType, DataListPersistAction.this, lastF.getValue().getChoosedLine());
			else if (lastC != null)
				vData = SlU.getVDataByI(dType, DataListPersistAction.this, lastC.getValue());
			else if (lastI != null) {
				vData = SlU.getVDataByI(dType, DataListPersistAction.this, lastI.intValue());
				// 2014/10/02 : do not null here
				// lastI = null;
			} else
				vData = getSlContainer().getGetterIVModelData(dType, GetActionEnum.GetListLineChecked);

			return vData;
		}

		private class IsRowSelected implements ISlotCallerListener {

			@Override
			public ISlotSignalContext call(ISlotSignalContext slContext) {
				IVModelData vData = getV();
				boolean setLine = vData != null;
				GetRowSelected g = new GetRowSelected(setLine);
				return slContextFactory.construct(slContext.getSlType(), g);
			}

		}

		private class AddVarListener implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ListFormat fo = rM.getFormat(dType);
				ICustomObject i = slContext.getCustom();
				AddVarList sig = (AddVarList) i;
				String buttonId = sig.getAction();
				DialogVariables var = sig.getValue();
				IVModelData vData = getV();
				boolean setLine = vData != null;
				FieldValue val = new FieldValue();
				val.setValue(setLine);
				var.setValue(rM.getLId(dType) + ICommonConsts.LINESET, val);
				JUtils.setVariables(var, vData);

				var.setValueL(ICommonConsts.JEDITLISTROWNO + fo.getId(), getLastRow());
				var.setValueS(ICommonConsts.JEDITLISTACTION + fo.getId(),
						lastPersistAction == null ? null : lastPersistAction.toString());
				// set footer value
				for (FieldItem co : fo.getColumns()) {
					if (!co.isFooter())
						continue;
					String footervar = ICommonConsts.JFOOTER + fo.getId() + "_" + co.getId();
					FieldValue footerval = new FieldValue();
					Object fval = null;
					VField fie = VField.construct(co);
					if (vFooter != null) {
						fval = vFooter.getF(fie);
					}
					footerval.setValue(co.getFooterType(), fval, co.getFooterAfterDot());
					var.setValue(footervar, footerval);
				}
				// list of values
				String lList = fo.getListButtonsWithList();
				if (!CUtil.EmptyS(buttonId) && !CUtil.EmptyS(lList)) {
					String vList[] = lList.split(",");
					for (String s : vList)
						if (s.equals(buttonId))
							JUtils.prepareListOfRows(var, dType, DataListPersistAction.this, fo.getId(),
									fo.getColumns());
				}

			}

		}

		private class Synch extends SynchronizeList {

			ListOfRows lOfRows;

			Synch() {
				super(2);
			}

			@Override
			protected void doTask() {
				ListFormat fo = rM.getFormat(dType);
				if (lOfRows == null) {
					// Utils.errAlert(M.M().NoInfoOnList(fo.getId()));
					return;
				}
				if (fo.isChunked()) {
					int size = lOfRows.getSize();
					CustomStringSlot slot = DataIntegerSignal.constructSlotSetTableSize(dType);
					DataIntegerSignal sig = new DataIntegerSignal(size);
					getSlContainer().publish(slot, sig);
				} else {
					IDataListType dList = ListUtils.constructList(dType, rM, lOfRows);
					getSlContainer().publish(dType, DataActionEnum.ListReadSuccessSignal, dList);
				}
			}

		}

		private final Synch sy = new Synch();

		private void executeAction(DialogVariables v, final ListFormat li, final WSize w, String doAction,
				final ICommand iLast) {
			ListUtils.addListName(v, li);
			final MapDialogVariable addV = new MapDialogVariable();
			ListUtils.addListName(addV, li);
			DialogFormat dF = rM.getDialogInfo().getDialog();
			ButtonItem bu = DialogFormat.findE(dF.getActionList(), doAction);
			IExecuteBackAction.IBackFactory backFactory = new IExecuteBackAction.IBackFactory() {

				@Override
				public CommonCallBack<DialogVariables> construct() {
					return bFactory.construct(li.getId(), w, addV, iLast);
				}

			};
			executeBack.execute(backFactory, v, bu, rM.getDialogName(), doAction);
		}

		private class ReadList implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				if (sy.signalledAlready()) {
					DialogVariables v = iCon.getVariables(IUIConsts.CRUD_READLIST);
					ListFormat li = rM.getFormat(dType);
					IOkModelData iOk = SlU.getOkModelData(dType, DataListPersistAction.this);
					CreateSearchVar.addSearchVar(v, li, iOk);
					executeAction(v, li, null, IUIConsts.CRUD_READLIST, null);
				} else {
					sy.signalDone();
				}
			}

		}

		private class FormBeforeCompleted implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				FormBeforeCompletedSignal sig = (FormBeforeCompletedSignal) i;
				sy.lOfRows = sig.getValue();
				sy.signalDone();
			}

		}

		private void focusFinalSignal() {
			if (lastC != null) {
				lastC.signalFinishChangeSignal(DataListPersistAction.this);
			}
			lastC = null;
		}

		private class GetListSize implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				IOkModelData iOk = slContext.getIOkModelData();
				DialogVariables v = iCon.getVariables(IUIConsts.JLIST_GETSIZE);
				ListFormat li = rM.getFormat(dType);
				CreateSearchVar.addSearchVar(v, li, iOk);
				executeAction(v, li, null, IUIConsts.JLIST_GETSIZE, null);
			}

		}

		private class NextRowListener implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				StartNextRowSignal si = (StartNextRowSignal) i;
			}

		}

		private class EditRowAction implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				EditRowActionSignal e = (EditRowActionSignal) i;
				setLastRow(e.getRownum());
				lastPersistAction = e.getE();
				DialogVariables v = iCon.getVariables(ICommonConsts.JEDITLISTROWACTION);
				ListFormat li = rM.getFormat(dType);
				executeAction(v, li, e.getW(), ICommonConsts.JEDITLISTROWACTION, null);
			}

		}

		private class ChangeColumnEdit implements ISlotListener {

			class KeyTable {
				int row;
				IVField v;

				public @Override int hashCode() {
					return v.hashCode() + row;
				}

				public @Override boolean equals(Object o) {
					KeyTable c = (KeyTable) o;
					return c.row == row && v.eq(c.v);
				}
			}

			private final Map<KeyTable, TOptional<Object>> valsBefore = new HashMap<KeyTable, TOptional<Object>>();

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				ChangeFieldEditSignal c = (ChangeFieldEditSignal) i;
				ListFormat li = rM.getFormat(dType);
				IVField vv = c.getV();
				FieldItem col = li.getColumn(vv.getId());
				assert col != null : LogT.getT().cannotBeNull();
				IVModelData iD = SlU.getVDataByI(dType, DataListPersistAction.this, c.getValue());
				Object o = iD.getF(vv);
				KeyTable key = new KeyTable();
				key.row = c.getValue();
				setLastRow(c.getValue());
				key.v = vv;
				TOptional<Object> beforeD = null;
				focusFinalSignal();
				lastC = c;
				if (c.isBefore()) {
					key.v = vv;
					valsBefore.put(key, TOptional.fromNullable(o));
				} else {
					// Important: before does not work boolean, set value
					// manually
					if (vv.getType().isBoolean()) {
						Boolean b = (Boolean) iD.getF(vv);
						assert b != null;
						// negate previous value
						Object oo = new Boolean(!b.booleanValue());
						beforeD = TOptional.of(oo);
					} else {
						beforeD = valsBefore.get(key);
						valsBefore.remove(key);
					}

				}
				if (c.isBefore() && !col.isSignalBefore()) {
					focusFinalSignal();
					return;
				}
				if (!c.isBefore() && !col.isSignalChange()) {
					focusFinalSignal();
					return;
				}

				Object prevO = null;
				if (!c.isBefore()) {
					if (beforeD == null) {
						String mess = M.M().BeforeValueNotFound(c.getValue(), vv.getId());
						Utils.errAlert(LogT.getT().InternalError(), mess);
						return;
					}
					prevO = beforeD.orNull();
				}
				DialogVariables v = iCon.getVariables(ICommonConsts.SIGNALCOLUMNCHANGE);
				v.setValueB(IUIConsts.JCHANGESIGNALBEFORE, c.isBefore());
				v.setValueS(ICommonConsts.SIGNALCHANGEFIELD, vv.getId());
				FieldValue prev = new FieldValue();
				prev.setValue(col.getFieldType(), prevO, col.getAfterDot());
				v.setValue(ICommonConsts.JVALBEFORE, prev);
				executeAction(v, li, c.getW(), ICommonConsts.SIGNALCOLUMNCHANGE, null);
			}

		}

		private class ErrorsInfo implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				SendErrorsInfo e = (SendErrorsInfo) i;
				List<InvalidateMess> eList = e.getValue();
				if (eList.isEmpty()) {
					focusFinalSignal();
					return;
				}
				boolean remove = false;
				for (InvalidateMess m : eList)
					if (CUtil.EmptyS(m.getErrmess()))
						remove = true;
				if (remove) {
					eList = null;
					focusFinalSignal();
				}

				EditRowErrorSignal eSignal = new EditRowErrorSignal(getLastRow(), new InvalidateFormContainer(eList));
				getSlContainer().publish(EditRowErrorSignal.constructSlotLineErrorSignal(dType), eSignal);
			}

		}

		private void sendFinishSignal(boolean ok) {
			if (lastF != null) {
				if (!ok)
					lastF.setDoNotChange();
				getSlContainer().publish(FinishEditRowSignal.constructSlotFinishEditRowReturnSignal(dType), lastF);
			}
			lastF = null;
		}

		private class FinishRowEdit implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				FinishEditRowSignal f = (FinishEditRowSignal) i;
				lastF = f;
				ListFormat fo = rM.getFormat(dType);
				IVModelData vData = getV();
				List<InvalidateMess> err = ValidateForm.createErrList(vData, fo.getColumns(), fo.getValList(), null);
				if (err != null) {
					EditRowErrorSignal eSignal = new EditRowErrorSignal(f.getValue().getChoosedLine(),
							new InvalidateFormContainer(err));
					getSlContainer().publish(EditRowErrorSignal.constructSlotLineErrorSignal(dType), eSignal);
					sendFinishSignal(false);
				} else {
					if (!fo.isAfterRowSignal()) {
						sendFinishSignal(true);
						return;
					}
					// next getV will use lastF to set current row
					setAfterRowNum(lastF.getValue().getChoosedLine());
					DialogVariables v = iCon.getVariables(ICommonConsts.SIGNALAFTERROW);
					executeAction(v, fo, lastF.getValue().getwSize(), ICommonConsts.SIGNALAFTERROW, new ICommand() {

						@Override
						public void execute() {
							setAfterRowNum(-1);
						}
					});
				}
			}
		}

		private class AfterRowActionOk implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				AfterRowOk f = (AfterRowOk) i;
				sendFinishSignal(f.getValue());
			}

		}

		private class ColumnHelper implements IColumnImageSelect {

			private final IVField v;
			private final String helperAction;

			ColumnHelper(IVField v, FieldItem fi) {
				this.v = v;
				String h = fi.getAttr(ICommonConsts.HELPER);
				if (CUtil.EmptyS(h))
					h = ICommonConsts.HELPER;
				this.helperAction = h;
			}

			@Override
			public String getImage() {
				return null;
			}

			@Override
			public String getImageHint() {
				return null;
			}

			@Override
			public void executeImage(String val, int row, WSize w, IExecuteSetString i) {
				DialogVariables var = iCon.getVariables(helperAction);
				ListFormat fo = rM.getFormat(dType);
				var.setValueS(ICommonConsts.SIGNALCHANGEFIELD, v.getId());
				executeAction(var, fo, w, helperAction, null);
			}

		}

		private class SelectTypeHelper implements IColumnImageSelect {

			private class ChooseD implements ICallBackWidget<IVModelData> {

				private final IExecuteSetString i;
				private HelperDialog dia = null;

				private class HelperDialog extends ModalDialog {

					private final IGWidget w;

					HelperDialog(IGWidget w) {
						// super(type.getDisplayName());
						super("");
						this.w = w;
						create();
					}

					@Override
					protected void addVP(VerticalPanel vp) {
						vp.add(w.getGWidget());
					}
				}

				ChooseD(IExecuteSetString i) {
					this.i = i;
				}

				@Override
				public void setWidget(WSize ws, IGWidget w) {
					dia = new HelperDialog(w);
					dia.show(ws);
				}

				@Override
				public void setChoosed(IVModelData vData, IVField comboFie) {
					String sy = FUtils.getValueS(vData, comboFie);
					i.setString(sy);
					dia.hide();
				}

				@Override
				public void setResign() {
					dia.hide();
				}

			}

			private final IVField v;
			private final FieldItem f;
			private final TypedefDescr type;

			SelectTypeHelper(FieldItem f, IVField v) {
				this.f = f;
				this.v = v;
				type = rM.getDialogInfo().getDialog().findCustomType(f.getCustom());

			}

			@Override
			public String getImage() {
				return null;
			}

			@Override
			public String getImageHint() {
				return null;
			}

			@Override
			public void executeImage(String val, int row, WSize w, IExecuteSetString i) {
				IDataType d = rM.gettConstruct().construct(f.getTypeName());
				IChooseList iC = ChooseListFactory.constructChooseList(d, w, new ChooseD(i));
			}

		}

		private class SelectFactory implements ISelectFactory {

			@Override
			public IColumnImageSelect construct(IVField v, FieldItem f) {
				if (!CUtil.EmptyS(f.getCustom())) {
					return new SelectTypeHelper(f, v);
				}
				return new ColumnHelper(v, f);
			}

		}

		ISelectFactory construct() {
			return new SelectFactory();
		}

		private class ChangeValues implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				SetNewValues vals = (SetNewValues) i;
				VModelData row = vals.getValue();
				List<IGetSetVField> eList = SlU.getVListFromEditTable(dType, DataListPersistAction.this, getI());
				// IVModelData vD = getV();
				IVModelData vD = SlU.getVDataByI(dType, DataListPersistAction.this, getLastRow());

				if (vD == null)
					Utils.internalErrorAlert(M.M().RowReferenceIsNull(dType.toString()));
				boolean refreshlist = false;
				for (IVField f : row.getF()) {
					vD.setF(f, row.getF(f));
					boolean isset = false;
					if (eList != null)
						for (IGetSetVField ii : eList) {
							if (ii.getV().eq(f)) {
								ii.setValObj(row.getF(f));
								isset = true;
								break;
							}
						}
					if (!isset)
						refreshlist = true;
				}
				// added 2014/03/02 : for refreshing values not related to edit
				if (refreshlist) {
					// publish(dType, DataActionEnum.RefreshListAction);
					CustomStringSlot sl = DataIntegerSignal.constructSlotRedrawRow(dType);
					publish(sl, new DataIntegerSignal(getLastRow()));
				}
			}

		}

		private class SetEditRow implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				SetListEditRow s = (SetListEditRow) i;
				setLastRow(s.getValue());
			}

		}

		private class ClickImageCol implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				ClickColumnImageSignal sig = (ClickColumnImageSignal) i;
				// important: before getVariable()
				lastI = new MutableInteger(sig.getValue());
				DialogVariables v = iCon.getVariables(ICommonConsts.JCLICKIMAGEACTION);
				// 2014/10/02 : set null at this moment
				lastI = null;
				ListFormat li = rM.getFormat(dType);
				v.setValueS(ICommonConsts.SIGNALCHANGEFIELD, sig.getV().getId());
				v.setValueL(ICommonConsts.IMAGECOLUMN, sig.getImno());
				executeAction(v, li, sig.getW(), ICommonConsts.JCLICKIMAGEACTION, null);
			}

		}

		private class GetImageCol implements ISlotCallerListener {

			@Override
			public ISlotSignalContext call(ISlotSignalContext slContext) {
				final GetImageColSignal sig = (GetImageColSignal) slContext.getCustom();
				VField v = (VField) sig.getV();
				ListFormat fo = rM.getFormat(dType);
				FieldItem fi = fo.getColumn(v.getId());
				String[] res = GetImageList.getList(fi, new GetImageList.IGetVData() {
					@Override
					public IVModelData get() {
						IVModelData va = SlU.getVDataByI(dType, DataListPersistAction.this, sig.getValue());
						return va;
					}
				});

				GetImageColSignalReturn sl = new GetImageColSignalReturn(TOptional.fromNullable(res));
				return slContextFactory.construct(slContext.getSlType(), sl);
			}

		}

		DataListPersistAction(IDataType d, RowListDataManager rM, IVariablesContainer iCon,
				ICreateBackActionFactory bFactory) {
			this.dType = d;
			this.rM = rM;
			this.bFactory = bFactory;
			this.iCon = iCon;
			iJson = GwtGiniInjector.getI().getJsonConvert();
			executeJS = UIGiniInjector.getI().getExecuteJS();
			executeBack = UIGiniInjector.getI().getExecuteBackAction();
			registerSubscriber(dType, DataActionEnum.ReadListAction, new ReadList());
			registerSubscriber(FormBeforeCompletedSignal.constructSignal(d), new FormBeforeCompleted());
			registerSubscriber(dType, DataActionEnum.GetListSize, new GetListSize());
			registerSubscriber(DrawFooterSignal.constructSignal(d), new DrawFooter());
			registerSubscriber(ChangeToEditSignal.constructSignal(d), new ChangeToEdit());
			registerSubscriber(ChangeFieldEditSignal.constructSlotChangeEditSignal(d), new ChangeColumnEdit());
			registerSubscriber(EditRowActionSignal.constructSlotEditActionSignal(d), new EditRowAction());
			registerSubscriber(AddVarList.constructSignal(d), new AddVarListener());
			registerSubscriber(RowActionOk.constructSignal(d), new RowActionOkListener());
			registerSubscriber(SendErrorsInfo.constructSignal(d), new ErrorsInfo());
			registerSubscriber(FinishEditRowSignal.constructSlotFinishEditRowSignal(d), new FinishRowEdit());
			registerSubscriber(StartNextRowSignal.constructSlotStartNextRowSignal(d), new NextRowListener());
			registerSubscriber(AfterRowOk.constructSignal(d), new AfterRowActionOk());
			registerSubscriber(SetNewValues.constructSignal(d), new ChangeValues());
			registerSubscriber(SetListEditRow.constructSignal(d), new SetEditRow());
			registerCaller(GetImageColSignal.constructSlotGetImageCol(d), new GetImageCol());
			registerCaller(GetRowSelected.constructSignal(d), new IsRowSelected());
			registerSubscriber(ClickColumnImageSignal.constructSlotClickColumnSignal(d), new ClickImageCol());
		}
	}

	private static class HeaderList extends AbstractSlotContainer implements IHeaderListContainer {

		private final ISelectFactory iSelect;
		private final ListFormat fo;
		private final Sy sy;

		private final Map<String, IDataListType> enumMap = new HashMap<String, IDataListType>();

		private class Sy extends SynchronizeList {

			Sy(int no) {
				super(no);
			}

			@Override
			protected void doTask() {
				IGetEnum iGet = new IGetEnum() {

					@Override
					public IGetListValues getEnum(String customT, final boolean addEmpty) {
						final IDataListType dList = enumMap.get(customT);
						if (dList == null)
							return null;
						return new IGetListValues() {

							@Override
							public List<IMapEntry> getList() {
								List<IMapEntry> eList = new ArrayList<IMapEntry>();

								if (addEmpty) {
									IMapEntry eEmpty = new IMapEntry() {

										@Override
										public String getKey() {
											return "";
										}

										@Override
										public String getValue() {
											return "";
										}

									};
									eList.add(eEmpty);
								}

								for (final IVModelData v : dList.getList()) {
									IMapEntry e = new IMapEntry() {

										@Override
										public String getKey() {
											return (String) v.getF(dList.comboField());
										}

										@Override
										public String getValue() {
											return (String) v.getF(dList.displayComboField());
										}

									};
									eList.add(e);
								}
								return eList;
							}

						};
					}

				};
				VListHeaderContainer vHeader = CreateForm.constructColumns(dType, fo, iSelect, iGet);
				publish(dType, vHeader);
			}

		}

		HeaderList(IDataType dType, RowListDataManager rM, ISelectFactory iSelect) {
			this.dType = dType;
			this.iSelect = iSelect;
			fo = rM.getFormat(dType);
			registerSubscriber(SendEnumToList.constructSignal(dType), new GetEnumList());
			int no = 1;
			for (FieldItem f : fo.getColumns()) {
				if (!CUtil.EmptyS(f.getCustom())) {
					TypedefDescr type = rM.getDialogInfo().getDialog().findCustomType(f.getCustom());
					if (type == null) {
						String mess = M.M().CustomTypeIsNull(f.getCustom());
						Utils.errAlertB(mess);
						assert type != null : mess;
					}
					if (type.isComboType())
						no++;
				}
			}
			sy = new Sy(no);
		}

		private class GetEnumList implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				ICustomObject i = slContext.getCustom();
				SendEnumToList s = (SendEnumToList) i;
				enumMap.put(s.getCustomT(), s.getValue());
				sy.signalDone();
			}

		}

		@Override
		public void startPublish(CellId cellId) {
			sy.signalDone();
		}

	}

	private static DataListParam getParam(final RowListDataManager rM, final IDataType da, IVariablesContainer iCon,
			ICreateBackActionFactory bFactory) {

		// create factories
		DataListPersistAction iPersist = new DataListPersistAction(da, rM, iCon, bFactory);
		IHeaderListContainer heList = new HeaderList(da, rM, iPersist.construct());
		IDataModelFactory dataFactory = new DataModel(rM);
		IFormTitleFactory formFactory = new IFormTitleFactory() {

			@Override
			public String getFormTitle(ICallContext iContext) {
				return getFormHeader(rM, da);
			}

			@Override
			public SolidPos getSolidPos(ICallContext iContext) {
				return constructSolidPos(rM, da);
			}

		};

		IGetViewControllerFactory fControler = new GetViewController(rM, dataFactory, iCon);

		return new DataListParam(da, iPersist, heList, dataFactory, formFactory, fControler, null);
	}

	private static class ActionClicked implements ISlotListener {

		private final IPerformClickAction iClick;
		private final IVariablesContainer iCon;
		private final ISlotable iS;
		private final IDataType dType;

		ActionClicked(IPerformClickAction iClick, IVariablesContainer iCon, ISlotable i, IDataType dType) {
			this.iClick = iClick;
			this.iCon = iCon;
			this.iS = i;
			this.dType = dType;
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			ICustomObject i = slContext.getCustom();
			WChoosedLine wC = (WChoosedLine) i;
			// 2015/12/25
			iS.getSlContainer().publish(SetListEditRow.constructSignal(dType), new SetListEditRow(wC.getChoosedLine()));
			// setLastRow(1);

			IVField fie = wC.getvField();
			WSize w = wC.getwSize();
			iClick.click(fie.getId(), w);
		}

	}

	private static class ReadInChunk implements ISlotListener {

		private final IDataType dType;
		private final IVariablesContainer iCon;
		private final RowListDataManager rM;

		ReadInChunk(IDataType dType, IVariablesContainer iCon, RowListDataManager rM) {
			this.dType = dType;
			this.iCon = iCon;
			this.rM = rM;
		}

		private class ReadRowsChunk extends CommonCallBack<DialogVariables> {

			private final ReadChunkSignal r;

			ReadRowsChunk(ReadChunkSignal r) {
				this.r = r;
			}

			@Override
			public void onMySuccess(DialogVariables arg) {
				PerformVariableAction.VisitList vis = new PerformVariableAction.VisitList() {

					@Override
					public void accept(IDataType da, ListOfRows lRows) {
						if (da.eq(dType)) {
							IDataListType dList = ListUtils.constructList(dType, rM, lRows);
							r.signalRowsRead(dList.getList());
						}
					}

					@Override
					public void acceptTypes(String typeName, ListOfRows lRows) {
						// do nothing, not expected here
					}

					@Override
					public void acceptFooter(IDataType da, List<IGetFooter> fList) {
						// TODO: do something

					}

					@Override
					public void acceptEditListMode(IDataType da, EditListMode e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void acceptChartValues(IDataType da, ListOfRows lRows) {
						// TODO Auto-generated method stub

					}
				};
				PerformVariableAction.perform(null, null, arg, iCon, rM, null, vis, null, null);

			}
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			ICustomObject i = slContext.getCustom();
			ReadChunkSignal r = (ReadChunkSignal) i;
			int start = r.getStart();
			int size = r.getSize();
			DialogVariables v = iCon.getVariables(IUIConsts.JLIST_READCHUNK);
			v.setValueL(IUIConsts.JLIST_READCHUNKSTART, start);
			v.setValueL(IUIConsts.JLIST_READCHUNKLENGTH, size);
			IVField fSort = r.getfSort();
			if (fSort == null)
				v.setValueS(IUIConsts.JLIST_SORTLIST, null);

			else
				v.setValueS(IUIConsts.JLIST_SORTLIST, fSort.getId());
			v.setValueB(IUIConsts.JLIST_SORTASC, r.isAsc());
			ListFormat li = rM.getFormat(dType);
			CreateSearchVar.addSearchVar(v, li, r.getOkData());
			ListUtils.addListName(v, li);
			ExecuteAction.action(v, rM.getDialogName(), IUIConsts.JLIST_READCHUNK, new ReadRowsChunk(r));
		}

	}

	private static class CustomClick implements ISlotListener {

		private final IPerformClickAction custClick;

		CustomClick(IPerformClickAction custClick) {
			this.custClick = custClick;
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			WSize w = new WSize(slContext.getGwtWidget());
			String s = slContext.getSlType().getButtonClick().getCustomButt();
			custClick.click(s, w);
		}

	}

	static ISlotable contruct(RowListDataManager rM, IDataType da, CellId panelId, IVariablesContainer iCon,
			IPerformClickAction iClick, ICreateBackActionFactory backFactory, IPerformClickAction custClick) {
		TableDataControlerFactory tFactory = GwtGiniInjector.getI().getTableDataControlerFactory();
		ListFormat li = rM.getFormat(da);
		List<ControlButtonDesc> cList;
		List<ControlButtonDesc> customList = new ArrayList<ControlButtonDesc>();
		if (!CUtil.EmptyS(li.getStandButt())) {
			ListOfButt.IGetButtons i = ListOfButt.constructList(rM.getDialogInfo().getDialog(), li.getStandButt());
			cList = i.getList();
			customList = i.getCustomList();
		} else
			cList = ListOfButt.getStandList(li.getToolBarType());

		ListOfControlDesc cButton = new ListOfControlDesc(cList);
		DisplayListControlerParam dList = tFactory.constructParam(cButton, panelId, getParam(rM, da, iCon, backFactory),
				null, /*
						 * TODO: important to run edit
						 */true, li.isChunked());
		ISlotable i = tFactory.constructDataControler(dList);
		CustomStringSlot sl = ReadChunkSignal.constructReadChunkSignal(da);
		i.getSlContainer().registerSubscriber(sl, new ReadInChunk(da, iCon, rM));
		i.getSlContainer().registerSubscriber(da, DataActionEnum.TableCellClicked,
				new ActionClicked(iClick, iCon, i, da));
		for (ControlButtonDesc b : customList)
			i.getSlContainer().registerSubscriber(da, b.getActionId(), new CustomClick(custClick));

		return i;
	}

}
