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

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.view.util.PopupTip;

/**
 * 
 * @author perseus
 */
public class GFocusWidgetFactory {

	private GFocusWidgetFactory() {
	}

	private static class BDecorator implements IGFocusWidget {
		private final FocusWidget f;
		private FocusWidget ff = null;
		private ClickHandler h;

		BDecorator(FocusWidget f) {
			this.f = f;
		}

		@Override
		public Widget getGWidget() {
			if (ff != null)
				return ff;
			return f;
		}

		private FocusWidget getF() {
			return (FocusWidget) getGWidget();
		}

		@Override
		public void replaceButtonWidget(Widget w) {
			if (!(w instanceof FocusWidget))
				Utils.errAlertB(LogT.getT().BinderButtonShouldBeFocusWidget());
			ff = (FocusWidget) w;
			if (h != null)
				ff.addClickHandler(h);
			ff.setEnabled(f.isEnabled());
			ff.setVisible(f.isVisible());

		}

		@Override
		public void addClickHandler(ClickHandler h) {
			this.h = h;
			getF().addClickHandler(h);
		}

		@Override
		public void setEnabled(boolean enabled) {
			getF().setEnabled(enabled);
		}

		@Override
		public void setHidden(boolean hidden) {
			getF().setVisible(!hidden);
		}
	}

	private static class F implements IGWidget, IGFocusWidget {

		private final BDecorator b;

		F(FocusWidget f) {
			b = new BDecorator(f);
		}

		@Override
		public void addClickHandler(ClickHandler h) {
			b.addClickHandler(h);
		}

		@Override
		public void setEnabled(boolean enabled) {
			b.setEnabled(enabled);
		}

		@Override
		public void setHidden(boolean hidden) {
			b.setHidden(hidden);
		}

		@Override
		public void replaceButtonWidget(Widget w) {
			b.replaceButtonWidget(w);
		}

		@Override
		public Widget getGWidget() {
			return b.getGWidget();
		}
	}

	private static class FTip extends PopupTip implements IGFocusWidget {

		private final BDecorator b;

		FTip(FocusWidget f, String mess) {
			initWidget(f);
			setMessage(mess);
			b = new BDecorator(f);
		}

		@Override
		public void addClickHandler(ClickHandler h) {
			b.addClickHandler(h);
		}

		@Override
		public Widget getGWidget() {
			// replaced: 2016/07/03
			return this;
		}

		@Override
		public void setEnabled(boolean enabled) {
			b.setEnabled(enabled);
		}

		@Override
		public void setHidden(boolean hidden) {
			b.setHidden(hidden);
		}

		@Override
		public void replaceButtonWidget(Widget w) {
			b.replaceButtonWidget(w);
		}
	}

	public static IGFocusWidget construct(FocusWidget w, String mess) {
		return new FTip(w, mess);
	}

	public static IGFocusWidget construct(FocusWidget w) {
		return new F(w);
	}
}
