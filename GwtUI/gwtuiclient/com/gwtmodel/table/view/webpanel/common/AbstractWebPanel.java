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
package com.gwtmodel.table.view.webpanel.common;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.IWebPanel.InfoType;

abstract public class AbstractWebPanel implements IWebPanel {

	private final CallBackProgress callBack = new CallBackProgress();

	protected Label productName; // product name
	protected Label ownerName; // owner name
	protected Label userName;
	protected Label hotelName;
	protected Label upInfo;
	protected final IWebPanelResources pResources;
	protected final ICommand logOut;
	private boolean autologoutmode = true;
	protected ISignal centreHideSignal = null;

	protected AbstractWebPanel(IWebPanelResources pResources, ICommand logOut) {
		this.pResources = pResources;
		this.logOut = logOut;
	}

	protected void setLabels(Label productName, Label ownerName, Label userName, Label hotelName, Label upInfo) {
		this.productName = productName;
		this.ownerName = ownerName;
		this.userName = userName;
		this.hotelName = hotelName;
		this.upInfo = upInfo;
		Window.setTitle(pResources.getRes(IWebPanelResources.TITLE));
		productName.setText(pResources.getRes(IWebPanelResources.PRODUCTNAME));
		ownerName.setText(pResources.getRes(IWebPanelResources.OWNERNAME));
	}

	class CallBackProgress {

		private int coL = 0;

		void IncDecL(final boolean inc) {
			if (inc) {
				coL++;
			} else {
				coL--;
			}
			setReplay(coL);
		}
	}

	@Override
	public void IncDecCounter(boolean inc) {
		callBack.IncDecL(inc);
	}

	@Override
	public void setPaneText(InfoType t, String te) {
		IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
		String text = iMess.getMessage(te);
		switch (t) {
		case USER:
			userName.setText(text);
			// if (autologoutmode)
			// setOut(text != null ? true : false);
			break;
		case DATA:
			hotelName.setText(text);
			break;
		case OWNER:
			ownerName.setText(text);
			break;
		case TITLE:
			Window.setTitle(text);
			break;
		case PRODUCT:
			productName.setText(text);
			break;
		case UPINFO:
			if (text == null) {
				upInfo.setVisible(false);
			} else {
				upInfo.setVisible(true);
				upInfo.setText(text);
			}
			break;
		}
	}

	@Override
	public void setCentreHideSignal(ISignal iSig) {
		centreHideSignal = iSig;
	}

	@Override
	public void setLogOutMode(boolean logout) {
		autologoutmode = logout;
	}

}
