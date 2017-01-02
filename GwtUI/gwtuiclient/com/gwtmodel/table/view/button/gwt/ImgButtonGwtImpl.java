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
package com.gwtmodel.table.view.button.gwt;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.gwtmodel.table.GFocusWidgetFactory;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.button.IImgButton;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ImgButtonGwtImpl implements IImgButton {

	private final IGetStandardMessage iMess;

	@Inject
	public ImgButtonGwtImpl(IGetStandardMessage iMess) {
		this.iMess = iMess;
	}

	@Override
	public IGFocusWidget getButton(String bId, String bName, String img) {
		Button but;
		IGFocusWidget w;
		if (img != null) {
			String h = Utils.getImageHTML(img);
			but = new NamedButton(bId);
			but.setHTML(h);
			w = GFocusWidgetFactory.construct(but, iMess.getMessage(bName));
		} else {
			but = new NamedButton(iMess.getMessage(bName), bId);
			w = GFocusWidgetFactory.construct(but);
		}
		return w;
	}

	private static class NamedButton extends Button {

		NamedButton(String bId) {
			super();
			if (bId != null)
				getButtonElement().setName(bId);
		}

		NamedButton(String text, String bId) {
			super(text);
			if (bId != null)
				getButtonElement().setName(bId);
		}

	}

	@Override
	public IGFocusWidget getButtonTextImage(String bId, String bName, String img) {
		String ht = "<table><tr>";
		String h = Utils.getImageHTML(img);
		ht += h;
		Label la = new Label(bName);
		ht += "<td>" + la.getElement().getInnerHTML() + "</td>";
		ht += "</tr></table>";
		Button b = new NamedButton(bId);
		b.setHTML(ht);
		return GFocusWidgetFactory.construct(b);
	}
}
