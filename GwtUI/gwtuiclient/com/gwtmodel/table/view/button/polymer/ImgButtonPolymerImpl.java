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
package com.gwtmodel.table.view.button.polymer;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.view.button.IImgButton;
import com.vaadin.polymer.paper.widget.PaperButton;

public class ImgButtonPolymerImpl implements IImgButton {

	private class PolymerWidget implements IGFocusWidget {

		private final PaperButton pa;

		PolymerWidget(String bId, String bName, String img) {
			pa = new PaperButton(bName);
			pa.setRaised(true);
		}

		@Override
		public Widget getGWidget() {
			return pa;
		}

		@Override
		public void addClickHandler(ClickHandler h) {
			pa.addClickHandler(h);
		}

		@Override
		public void setEnabled(boolean enabled) {
			pa.setDisabled(!enabled);

		}

		@Override
		public void setHidden(boolean hidden) {
			pa.setVisible(!hidden);

		}

		@Override
		public boolean isEnabled() {
			return !pa.getDisabled();
		}

	}

	@Override
	public IGFocusWidget getButton(String bId, String bName, String img) {
		return new PolymerWidget(bId, bName, img);
	}

	@Override
	public IGFocusWidget getButtonTextImage(String bId, String bName, String img) {
		return getButton(bId, bName, img);
	}

}
