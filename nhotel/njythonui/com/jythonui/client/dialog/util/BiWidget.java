/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jythonui.client.dialog.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ConvertTT;
import com.gwtmodel.table.common.DateFormat;
import com.gwtmodel.table.common.TT;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.gini.UIGiniInjector;
import com.jythonui.client.smessage.IGetStandardMessage;
import com.jythonui.client.util.U;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.polymerui.client.IConsts;
import com.polymerui.client.eventbus.ButtonEvent;
import com.polymerui.client.eventbus.ChangeEvent;
import com.polymerui.client.eventbus.ClickHelperEvent;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.util.Utils;
import com.polymerui.client.view.util.PolymerUtil;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperCheckbox;
import com.vaadin.polymer.paper.widget.PaperInput;
import com.vaadin.polymer.paper.widget.PaperTextarea;
import com.vaadin.polymer.vaadin.widget.VaadinDatePicker;
import com.vaadin.polymer.vaadin.widget.VaadinDatePickerLight;

class BiWidget {

	private final Widget w;
	private final String fieldid;
	private final IGetStandardMessage iGet;
	private final FieldItem fi;
	private final IReadDialog iR;
	private final IEventBus iBus;

	private static String regNumberExpr(FieldItem i) {
		switch (i.getAfterDot()) {
		case 0:
			return "[-+]?[0-9]*";
		case 1:
			return "[-+]?[0-9]*(\\.[0-9]{0,1}){0,1}";
		case 2:
			return "[-+]?[0-9]*(\\.[0-9]{0,2}){0,1}";
		case 3:
			return "[-+]?[0-9]*(\\.[0-9]{0,3}){0,1}";
		case 4:
			return "[-+]?[0-9]*(\\.[0-9]{0,4}){0,1}";
		}
		return null;
	}

	private static final Map<Class, TT> widgetType = new HashMap<Class, TT>();

	static {
		widgetType.put(PaperInput.class, TT.STRING);
		widgetType.put(PaperCheckbox.class, TT.BOOLEAN);
		widgetType.put(VaadinDatePickerLight.class, TT.DATE);
		widgetType.put(VaadinDatePicker.class, TT.DATE);
		widgetType.put(PaperTextarea.class, TT.STRING);
	}

	private enum OPTYPE {
		SIGNALCHANGE, HELPER,
	}

	private static final Map<OPTYPE, Class[]> opWidget = new HashMap<OPTYPE, Class[]>();

	static {
		opWidget.put(OPTYPE.SIGNALCHANGE, new Class[] { PaperInput.class });
		opWidget.put(OPTYPE.HELPER, new Class[] { PaperInput.class });
	}

	private void verifyOp(OPTYPE o) {
		String desc = null;
		switch (o) {
		case SIGNALCHANGE:
			desc = M.M().DialogAttributenotSupported(iR.getD().getDialog().getId(), fieldid,
					ICommonConsts.SIGNALCHANGE);
			break;
		case HELPER:
			desc = M.M().DialogAttributenotSupported(iR.getD().getDialog().getId(), fieldid, ICommonConsts.HELPER);
			break;

		}
		PolymerUtil.verifyWidgetType(desc, w, opWidget.get(o));
	}

	BiWidget(IEventBus iBus, Widget w, String fieldid, FieldItem fi) {
		this.fi = fi;
		this.w = w;
		this.fieldid = fieldid;
		iGet = UIGiniInjector.getI().getGetStandardMessage();
		this.iBus = iBus;
		iR = U.getIDialog(iBus);
	}

	void setI18() {
		JavaScriptObject oo = (JavaScriptObject) Utils.geti18N("");
		if (w instanceof VaadinDatePickerLight)
			getVaadinDatePickerLight().setI18n(oo);
		if (w instanceof VaadinDatePicker)
			getVaadinDatePicker().setI18n(oo);
	}

	VaadinDatePickerLight getVaadinDatePickerLight() {
		return U.castP(w, VaadinDatePickerLight.class);
	}

	VaadinDatePicker getVaadinDatePicker() {
		return U.castP(w, VaadinDatePicker.class);
	}

	PaperInput getPaperInput() {
		return U.castP(w, PaperInput.class);
	}

	PaperButton getPaperButton() {
		return U.castP(w, PaperButton.class);
	}

	PaperCheckbox getPaperCheckbox() {
		return U.castP(w, PaperCheckbox.class);
	}

	PaperTextarea getPaperTextarea() {
		return U.castP(w, PaperTextarea.class);
	}

	void setButtonSubscriber(ButtonItem b) {
		PaperButton b1 = getPaperButton();
		if (b1 != null) {
			if (b == null) {
				b = new ButtonItem();
				b.setId(fieldid);
			}
			ButtonItem bb = b;
			b1.addClickHandler(p -> iBus.publish(new ButtonEvent(), bb));
		}
	}

	String getFieldid() {
		return fieldid;
	}

	class V {
		final String v;
		final boolean b;
		final TT t;
		final int afterdot;

		V(boolean b) {
			this.b = b;
			t = TT.BOOLEAN;
			v = null;
			afterdot = 0;
		}

		V(String v) {
			this(v, TT.STRING);
		}

		V(String v, TT t) {
			this.v = v;
			this.t = t;
			afterdot = 0;
			b = false;
		}
	}

	private String toPicker(String s) {
		if (CUtil.EmptyS(s))
			return null;
		return s.replace('-', '/');
	}

	void setErrorMessage(String err) {
		VaadinDatePicker d1 = getVaadinDatePicker();
		if (d1 != null) {
			d1.setErrorMessage(err);
			d1.setInvalid(err != null);
		}
		VaadinDatePickerLight d2 = getVaadinDatePickerLight();
		if (d2 != null) {
			d2.setInvalid(err != null);
		}
		PaperInput p = getPaperInput();
		if (p != null) {
			p.setErrorMessage(err);
			p.setInvalid(err != null);
			return;
		}
		PaperTextarea a = getPaperTextarea();
		if (a != null) {
			a.setErrorMessage(err);
			a.setInvalid(err != null);
			return;
		}
	}

	private V checkForDate() {

		Optional<String> o = Optional.empty();
		VaadinDatePicker d1 = getVaadinDatePicker();
		if (d1 != null)
			o = Optional.of(d1.getValue());
		VaadinDatePickerLight d2 = getVaadinDatePickerLight();
		if (d2 != null)
			o = Optional.of(d2.getValue());
		if (o.isPresent())
			return new V(toPicker(o.get()), TT.DATE);
		else
			return null;
	}

	private V checkForString() {
		Optional<String> o = null;
		PaperInput p = getPaperInput();
		if (p != null)
			o = Optional.ofNullable(p.getValue());
		// PaperTextarea can return null
		PaperTextarea a = getPaperTextarea();
		if (a != null)
			o = Optional.ofNullable(a.getValue());
		if (o != null)
			return new V(o.orElse(null));
		else
			return null;
	}

	private V checkForBoolean() {
		Optional<Boolean> o = Optional.empty();
		PaperCheckbox p = getPaperCheckbox();
		if (p != null)
			o = Optional.of(p.getChecked());
		if (o.isPresent())
			return new V(o.get());
		else
			return null;
	}

	private V getV() {
		V v = checkForDate();
		if (v != null)
			return v;
		v = checkForString();
		if (v != null)
			return v;
		return checkForBoolean();
	}

	void setToVar(DialogVariables v) {
		V va = getV();
		if (va == null)
			return;
		switch (va.t) {
		case STRING:
			if (fi != null) {
				FieldValue val = new FieldValue();
				Object o = ConvertTT.toO(fi.getFieldType(), va.v);
				val.setValue(fi.getFieldType(), o, fi.getAfterDot());
				v.setValue(fieldid, val);
			} else
				v.setValueS(fieldid, va.v);
			break;
		case DATE:
			Date d = (Date) ConvertTT.toO(TT.DATE, va.v);
			v.setValue(fieldid, d);
			break;
		case BOOLEAN:
			v.setValueB(fieldid, va.b);
			break;
		default:
			break;
		}
	}

	boolean validate() {
		PaperInput p1 = getPaperInput();
		if (p1 != null)
			if (!p1.validate())
				return false;

		PaperTextarea a = getPaperTextarea();
		if (a != null) {
			if (!a.validate())
				return false;
		}
		V va = getV();
		if (va == null || CUtil.EmptyS(va.v))
			return true;
		String mess = null;
		switch (va.t) {
		case DATE:
			Date d = DateFormat.toD(va.v, false);
			if (d == null)
				mess = iGet.getMessage("dateformatnotvalid");
			break;
		default:
			break;
		}
		if (mess != null)
			setErrorMessage(mess);
		return mess == null;
	}

	TT getWidgetType() {
		return widgetType.get(w.getClass());
	}

	void setValue(IReadDialog iR, FieldValue val, FieldItem fi) {

		assert fi != null;
		assert val.getType() == fi.getFieldType();

		TT t = getWidgetType();
		assert t != null;
		boolean ok = false;

		// check if widget matches value
		switch (t) {
		case STRING:
			switch (val.getType()) {
			case STRING:
			case INT:
			case BIGDECIMAL:
				ok = true;
			default:
				break;
			}
			break;
		case BOOLEAN:
			ok = val.getType() == TT.BOOLEAN;
			break;
		case DATE:
			ok = val.getType() == TT.DATE;
			break;
		default:
			break;
		}

		if (!ok)
			Utils.errAlertB(M.M().DialogField(iR.getD().getDialog().getId(), fi.getId()),
					M.M().WidgetTypeAndValueDoesNotMatch(w.getClass().getName(), val.getType().name()));

		String sval = ConvertTT.toS(val.getValue(), val.getType(), fi.getAfterDot());

		if (t == TT.BOOLEAN && val.getValue() == null)
			Utils.errAlertB(M.M().DialogField(iR.getD().getDialog().getId(), fi.getId()),
					M.M().BooleanValueCannotBeNull());

		PaperInput p = getPaperInput();
		if (p != null) {
			p.setValue(sval);
			return;
		}
		PaperTextarea a = getPaperTextarea();
		if (a != null) {
			a.setValue(sval);
			return;
		}
		PaperCheckbox c = getPaperCheckbox();
		if (c != null) {
			if (val != null)
				c.setChecked(val.getValueB());
			return;
		}

		if (t == TT.DATE) {
			sval = sval.replace('/', '-');
			if (w instanceof VaadinDatePickerLight)
				getVaadinDatePickerLight().setValue(sval);
			if (w instanceof VaadinDatePicker)
				getVaadinDatePicker().setValue(sval);
			return;
		}

	}

	private String errorMessageForNumbers(PaperInput w) {
		// integerformatnotvalid
		// decimalformatnotvalid
		if (fi.getFieldType() == TT.INT)
			return iGet.getMessage("integerformatnotvalid");
		return iGet.getMessage("decimalformatnotvalid").replaceAll("%%", CUtil.NumbToS(fi.getAfterDot()));

	}

	void setInputPattern() {
		if (fi == null)
			return;
		TT t = fi.getFieldType();
		assert t != null;
		if (t == TT.BIGDECIMAL || t == TT.INT)
			if (w instanceof PaperInput) {
				PaperInput p = (PaperInput) w;
				p.setPattern(regNumberExpr(fi));
				p.setAllowedPattern(regNumberExpr(fi));
				p.setAutoValidate(true);
				p.setErrorMessage(errorMessageForNumbers(p));
				return;
			}
	}

	boolean isEmpty() {
		V va = getV();
		if (va == null)
			return false;
		if (va.t == TT.BOOLEAN)
			return false;
		if (!CUtil.EmptyS(va.v))
			return false;
		String errmess = iGet.getMessage(IConsts.EMPTYFIELDMESSAGE);
		setErrorMessage(errmess);
		return true;
	}

	private void publishChange() {
		iBus.publish(new ChangeEvent(), fieldid);
	}

	void setSignalChange() {
		if (fi == null || !fi.isSignalChange())
			return;
		verifyOp(OPTYPE.SIGNALCHANGE);
		PaperInput f = getPaperInput();
		if (f != null) {
			f.addChangeHandler(e -> {
				publishChange();
			});
		}
	}

	void addHelperButton() {
		if (fi == null || !fi.isHelper())
			return;
		verifyOp(OPTYPE.HELPER);
		PaperInput i = getPaperInput();
		String he = fi.getHelper();
		Element ele = i.getElementById(he);
		if (ele == null)
			Utils.errAlertB(iR.getD().getDialog().getId(),
					M.M().CannotFindElementById(ICommonConsts.HELPER, fi.getId(), he));
		Event.sinkEvents(ele, Event.ONCLICK);
		Event.setEventListener(ele, e -> {
			if (e.getTypeInt() == Event.ONCLICK)
				iBus.publish(new ClickHelperEvent(), fi);
		});
	}

}
