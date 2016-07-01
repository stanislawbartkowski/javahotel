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
package com.jythonui.client.dialog;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.mm.MM;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.stackpanelcontroller.IStackPanelController;
import com.gwtmodel.table.stackpanelcontroller.StackPanelControllerFactory;
import com.gwtmodel.table.view.util.polymer.PolymerUtil;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.util.CreateForm;
import com.jythonui.shared.ButtonItem;

/**
 * @author hotel
 * 
 */
public class LeftMenu {

	private final static IDataType leftMenuD = Empty.getDataType();

	public enum MenuType {
		LEFTPANEL, UPPANELMENU, LEFTSTACK;
	}

	private class Click implements ISignal {

		ISignal close;

		@Override
		public void signal() {
			if (close != null)
				close.signal();
		}

	}

	private class GetWidget implements ISlotListener {

		private final MenuType mType;
		private final Click click;

		GetWidget(MenuType mType, Click click) {
			this.mType = mType;
			this.click = click;
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			final Widget w = slContext.getGwtWidget().getGWidget();
			IWebPanel i = GwtGiniInjector.getI().getWebPanel();
			if (mType == MenuType.UPPANELMENU)
				if (MM.isPolymer())
					i.setMenuPanel(null, new IWebPanel.IStatusMenuIcon() {

						@Override
						public void click(WSize ws) {
							// very complicated to close menu if item hit
							click.close = PolymerUtil.popupPolymer(ws, w, null);
						}
					});
				else
					i.setMenuPanel(w, null);
			else
				i.setWest(w);
		}
	}

	public void createLeftButton(ISlotListener clickButton, List<ButtonItem> buttList, MenuType mType, String html) {
		if (!buttList.isEmpty()) {
			List<ControlButtonDesc> bList = CreateForm.constructBList(buttList);
			StackPanelControllerFactory sFactory = GwtGiniInjector.getI().getStackPanelControllerFactory();
			IStackPanelController iSlo = null;
			Click click = new Click();
			switch (mType) {
			case LEFTPANEL:
				iSlo = sFactory.construct(leftMenuD, bList, html);
				break;
			case UPPANELMENU:
				IWebPanelResources iW = GwtGiniInjector.getI().getWebPanelResources();
				String iName = iW.getRes(IWebPanelResources.PANELMENU);
				String iHtml = Utils.getImageHTML(iName, IUIConsts.PANELMENUDEFAW, IUIConsts.PANELMENUDEFAH,
						IUIConsts.PANELMENUNAME);
				iSlo = sFactory.constructDownMenu(leftMenuD, iHtml, bList, click);
				break;
			case LEFTSTACK:
				iSlo = sFactory.constructStackMenu(leftMenuD, bList);
				break;

			}
			SlU.registerWidgetListener0(leftMenuD, iSlo, new GetWidget(mType, click));
			iSlo.getSlContainer().registerSubscriber(leftMenuD, ClickButtonType.StandClickEnum.ALL, clickButton);
			CellId cId = new CellId(0);
			iSlo.startPublish(cId);
		}
	}
}