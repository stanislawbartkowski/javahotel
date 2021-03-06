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
import com.polymerui.client.IConsts;
import com.polymerui.client.eventbus.ButtonEvent;
import com.polymerui.client.eventbus.ChangeEvent;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.util.Utils;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperCheckbox;
import com.vaadin.polymer.paper.widget.PaperInput;
import com.vaadin.polymer.vaadin.widget.VaadinDatePicker;
import com.vaadin.polymer.vaadin.widget.VaadinDatePickerLight;

class BiWidget {

	private final Widget w;
	private final String fieldid;
	private final IGetStandardMessage iGet;

	private String regNumberExpr(FieldItem i) {
		switch (i.getAfterDot()) {
		case 0:
			return "[-+]?[0-9]*";
		case 1:
			return "[-+]?[0-9]*(\\.[0-9])?";
		case 2:
			return "[-+]?[0-9]+(\\.[0-9][0-9]?)?";
		case 3:
			return "[-+]?[0-9]+(\\.[0-9][0-9]?[0-9]?)?";
		case 4:
			return "[-+]?[0-9]+(\\.[0-9][0-9]?[0-9]?[0-9]?)?";
		}
		return null;
	}

	private static final Map<Class, TT> widgetType = new HashMap<Class, TT>();

	static {
		widgetType.put(PaperInput.class, TT.STRING);
		widgetType.put(PaperCheckbox.class, TT.BOOLEAN);
		widgetType.put(VaadinDatePickerLight.class, TT.DATE);
		widgetType.put(VaadinDatePicker.class, TT.DATE);
	}

	BiWidget(Widget w, String fieldid) {
		this.w = w;
		this.fieldid = fieldid;
		iGet = UIGiniInjector.getI().getGetStandardMessage();
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

	void setButtonSubscriber(IEventBus iBus, ButtonItem b) {
		PaperButton b1 = U.castP(w, PaperButton.class);
		if (b1 != null) {
			if (b == null) {
				b = new ButtonItem();
				b.setId(fieldid);
			}
			ButtonItem bb = b;
			b1.addClickHandler(p -> iBus.publish(new ButtonEvent(), bb));
		}
	}

	VaadinDatePicker getVaadinDatePicker() {
		return U.castP(w, VaadinDatePicker.class);
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

	private void setErrorMessage(String err) {
		VaadinDatePicker d1 = getVaadinDatePicker();
		if (d1 != null) {
			d1.setErrorMessage(err);
			d1.setInvalid(err != null);
		}
		VaadinDatePickerLight d2 = getVaadinDatePickerLight();
		if (d2 != null) {
			d2.setInvalid(err != null);
		}
		if (w instanceof PaperInput) {
			PaperInput p = (PaperInput) w;
			p.setErrorMessage(err);
			p.setInvalid(err != null);
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
		Optional<String> o = Optional.empty();
		PaperInput p = U.castP(w, PaperInput.class);
		if (p != null)
			o = Optional.of(p.getValue());
		if (o.isPresent())
			return new V(o.get());
		else
			return null;
	}

	private V checkForBoolean() {
		Optional<Boolean> o = Optional.empty();
		PaperCheckbox p = U.castP(w, PaperCheckbox.class);
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

	void setToVar(DialogVariables v, FieldItem i) {
		V va = getV();
		if (va == null)
			return;
		switch (va.t) {
		case STRING:
			if (i != null) {
				FieldValue val = new FieldValue();
				Object o = ConvertTT.toO(i.getFieldType(), va.v);
				val.setValue(i.getFieldType(), o, i.getAfterDot());
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
		PaperInput p1 = U.castP(w, PaperInput.class);
		if (p1 != null)
			if (!p1.validate())
				return false;

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

	void setValue(IReadDialog iR, FieldValue val, FieldItem def) {

		assert val.getType() == def.getFieldType();

		TT t = getWidgetType();
		assert t != null;
		boolean ok = false;

		// check if widget matches value
		switch (t) {
		case STRING:
			switch (val.getType()) {
			case STRING:
			case INT:
			case LONG:
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
			Utils.errAlertB(M.M().DialogField(iR.getD().getDialog().getId(), def.getId()),
					M.M().WidgetTypeAndValueDoesNotMatch(w.getClass().getName(), val.getType().name()));

		String sval = ConvertTT.toS(val.getValue(), val.getType(), def.getAfterDot());

		if (t == TT.BOOLEAN && val.getValue() == null)
			Utils.errAlertB(M.M().DialogField(iR.getD().getDialog().getId(), def.getId()),
					M.M().BooleanValueCannotBeNull());

		if (w instanceof PaperInput) {
			PaperInput p = (PaperInput) w;
			p.setValue(sval);
			return;
		}
		if (w instanceof PaperCheckbox) {
			PaperCheckbox c = (PaperCheckbox) w;
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

	void setInputPattern(FieldItem i) {
		if (i == null)
			return;
		TT t = i.getFieldType();
		assert t != null;
		if (t == TT.BIGDECIMAL || t == TT.INT || t == TT.LONG)
			if (w instanceof PaperInput) {
				PaperInput p = (PaperInput) w;
				p.setPattern(regNumberExpr(i));
				return;
			}
	}

	boolean isEmpty(FieldItem f) {
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

	private void publishChange(IEventBus iBus) {
		iBus.publish(new ChangeEvent(), fieldid);
	}

	void setSignalChange(IEventBus iBus, FieldItem i) {
		if (i == null)
			return;
		if (!i.isSignalChange())
			return;
		if (w instanceof PaperInput) {
			PaperInput f = (PaperInput) w;
			f.addChangeHandler(e -> {
				publishChange(iBus);
			});
		}
	}

}
