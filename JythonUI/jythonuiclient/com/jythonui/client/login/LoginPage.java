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
package com.jythonui.client.login;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.editw.FormLineContainer;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.login.ILoginDataView;
import com.gwtmodel.table.login.LoginDataModelFactory;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.login.LoginViewFactory;
import com.gwtmodel.table.mm.MM;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.validate.ValidateUtil;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.jythonui.client.M;
import com.jythonui.client.interfaces.ILoginPage;
import com.jythonui.shared.CustomSecurity;

public class LoginPage implements ILoginPage {

	static class DataValidate extends AbstractSlotContainer implements IDataValidateAction {

		private final ICommand ok;
		private final String shiroRealm;
		private final CustomSecurity iCustom;

		private class LoginValid extends CommonCallBack<String> {

			private final String user;

			LoginValid(String user) {
				this.user = user;
			}

			@Override
			public void onMySuccess(String arg) {
				if (arg == null) {
					IVField password = new LoginField(LoginField.F.PASSWORD);
					List<InvalidateMess> eList = new ArrayList<InvalidateMess>();
					InvalidateMess mess = new InvalidateMess(password, MM.getL().UserNameOrPasswordInvalid());
					eList.add(mess);
					publish(dType, DataActionEnum.ChangeViewFormToInvalidAction, new InvalidateFormContainer(eList));
					return;
				}
				M.setUserName(user);
				M.setSecToken(arg);
				IWebPanel wPanel = GwtGiniInjector.getI().getWebPanel();
				wPanel.setPaneText(IWebPanel.InfoType.USER, user);
				wPanel.setDCenter(null);
				ok.execute();
			}

		}

		private class LoginButton implements ISlotListener {

			private final LoginDataModelFactory logFactory = new LoginDataModelFactory();

			@Override
			public void signal(ISlotSignalContext slContext) {
				IVField login = new LoginField(LoginField.F.LOGINNAME);
				IVField password = new LoginField(LoginField.F.PASSWORD);
				IVModelData lData = logFactory.construct(dType);
				lData = getGetterIVModelData(dType, GetActionEnum.GetViewModelEdited, lData);
				List<InvalidateMess> eMess = ValidateUtil.checkEmpty(lData, login, password);
				if (eMess != null) {
					publish(dType, DataActionEnum.ChangeViewFormToInvalidAction, new InvalidateFormContainer(eMess));
					return;
				}
				String sLogin = (String) lData.getF(login);
				String sPass = (String) lData.getF(password);
				M.JR().login(shiroRealm, sLogin, sPass, iCustom, new LoginValid(sLogin));
			}

		}

		DataValidate(IDataType dType, String shiroRealm, CustomSecurity iCustom, ICommand ok) {
			this.dType = dType;
			this.ok = ok;
			this.iCustom = iCustom;
			this.shiroRealm = shiroRealm;
			this.getSlContainer().registerSubscriber(dType, ClickButtonType.StandClickEnum.ACCEPT, new LoginButton());
		}

	}

	static class GetWidget implements ISlotListener {

		@Override
		public void signal(ISlotSignalContext slContext) {
			IGWidget w = slContext.getGwtWidget();
			IWebPanel wPanel = GwtGiniInjector.getI().getWebPanel();
			wPanel.setDCenter(w.getGWidget());
		}
	}

	@Override
	public void login(String shiroRealm, CustomSecurity iCustom, ICommand ok) {
		IDataType dType = Empty.getDataType();
		CellId ce = new CellId(0);
		LoginDataModelFactory logFactory = new LoginDataModelFactory();
		FormLineContainer lForm = LoginViewFactory.construct();
		ILoginDataView lView = LoginViewFactory.contructView(ce, dType, lForm, logFactory,
				new DataValidate(dType, shiroRealm, iCustom, ok));
		SlU.registerWidgetListener0(dType, lView, new GetWidget());
		lView.startPublish(null);

	}

}
