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
package com.jythonui.client.start.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.inject.Inject;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.WebPanelHolder;
import com.gwtmodel.table.mm.MM;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.WebPanelFactory;
import com.jythonui.client.IJythonUIClient;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.M;
import com.jythonui.client.injector.UIGiniInjector;
import com.jythonui.client.interfaces.ILoginPage;
import com.jythonui.client.interfaces.IRegisterCustom;
import com.jythonui.client.interfaces.IWebPanelResourcesFactory;
import com.jythonui.client.start.IJythonClientStart;
import com.jythonui.client.webpanel.BinderPanelFactory;
import com.jythonui.shared.ClientProp;
import com.jythonui.shared.CustomSecurity;
import com.jythonui.shared.ICommonConsts;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.elemental.Function;
import com.vaadin.polymer.iron.widget.IronDropdown;
import com.vaadin.polymer.paper.widget.PaperDialog;

public class JythonClientStart implements IJythonClientStart {
	private static final String START = "start.xml";

	private final IJythonUIClient iClient;
	private final ILoginPage loginPage;
	private final IWebPanelResourcesFactory iResFactory;
	private final WebPanelFactory wFactory;
	private final ITableAbstractFactories tFactories;
	private final IRegisterCustom iRegister;

	@Inject
	public JythonClientStart(IJythonUIClient iClient, ILoginPage loginPage, IWebPanelResourcesFactory iResFactory,
			WebPanelFactory wFactory, ITableAbstractFactories tFactories, IRegisterCustom iRegister) {
		this.iClient = iClient;
		this.loginPage = loginPage;
		this.iResFactory = iResFactory;
		this.wFactory = wFactory;
		this.tFactories = tFactories;
		this.iRegister = iRegister;
	}

	private class Sync extends SynchronizeList {

		Sync() {
			super(2);
		}

		private ClientProp prop;
		private GoStart go;

		@Override
		protected void doTask() {
			go.go(prop);
		}

	}

	private class ResBack implements AsyncCallback<ClientProp> {

		private final Sync sy;

		ResBack(Sync sy) {
			this.sy = sy;
		}

		private void drawError(Throwable caught) {
			String html = "<H1>" + M.M().cannotLoadClientResource() + "</H1>";
			if (caught != null) {
				html = html + "<p>" + caught.getLocalizedMessage();
			}
			RootPanel.get().add(new HTML(html));
		}

		@Override
		public void onFailure(Throwable caught) {
			drawError(caught);
		}

		@Override
		public void onSuccess(ClientProp result) {
			if (result == null) {
				drawError(null);
				return;
			}
			sy.prop = result;
			sy.signalDone();
		}
	}

	private class GoStart {

		private String startX;
		private String shiroRealm;
		private final CustomSecurity iCustom;

		private class AfterLogin implements ICommand {

			@Override
			public void execute() {
				iClient.start(startX);
			}
		}

		GoStart(String startX, CustomSecurity iCustom) {
			this.startX = startX;
			this.iCustom = iCustom;
		}

		private class LogoutBack implements AsyncCallback<Void> {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub

			}

		}

		private class LogOut implements ICommand {

			private final boolean auth;

			LogOut(boolean auth) {
				this.auth = auth;
			}

			@Override
			public void execute() {
				// do nothing in async back
				M.JR().logout(UIGiniInjector.getI().getRequestContext(), new LogoutBack());
				IWebPanel wPanel = GwtGiniInjector.getI().getWebPanel();
				wPanel.setWest(null);
				wPanel.setPaneText(IWebPanel.InfoType.USER, null);
				wPanel.setPaneText(IWebPanel.InfoType.DATA, null);
				wPanel.setMenuPanel(null, null);
				startBegin(auth);
			}

		}

		private class WithoutLogin extends CommonCallBack<String> {

			private final ICommand co;

			WithoutLogin(ICommand co) {
				this.co = co;
			}

			@Override
			public void onMySuccess(String arg) {
				M.setSecToken(arg);
				co.execute();
			}
		}

		private void startBegin(boolean auth) {
			ICommand co = new AfterLogin();
			if (auth) {
				loginPage.login(shiroRealm, iCustom, co);
			} else
				M.JR().withoutlogin(iCustom, new WithoutLogin(co));
		}

		private void setWebPanel(IWebPanel wPan, ClientProp result) {
			wPan.setLogOutMode(result.addLogOut());
			WebPanelHolder.setWebPanel(wPan);
			RootPanel.get().add(wPan.getWidget());

		}

		public void go(ClientProp result) {
			M.setCached(result.isCached());
			// resolve root dialog
			if (CUtil.EmptyS(startX)) {
				startX = Utils.getURLParam(IUIConsts.STARTPAGEQUERY);
			}
			if (CUtil.EmptyS(startX)) {
				startX = result.getAttr(IUIConsts.STARTPAGE);
			}
			if (CUtil.EmptyS(startX)) {
				startX = START;
			}
			String startPages = result.getAttr(IUIConsts.STARTPAGES);
			boolean okStart = false;
			if (!CUtil.EmptyS(startPages)) {
				String[] listS = startPages.split(IUIConsts.LOGINDELIMITER);
				for (String s : listS) {
					if (s.equals(startX))
						okStart = true;
				}
			}
			String shiroPage = ICommonConsts.SHIROREALM + "-" + startX;
			shiroRealm = result.getAttr(shiroPage);
			if (CUtil.EmptyS(shiroRealm))
				shiroRealm = result.getAttr(ICommonConsts.SHIROREALM);
			// resolve if authentication is required
			boolean auth = result.isAuthenticate();
			if (!auth) {
				// not global authentication
				String authPages = result.getLoginPage();
				if (!CUtil.EmptyS(authPages)) {
					// verify if starting page on the list of pages requiring
					// authentication
					String[] pList = authPages.split(IUIConsts.LOGINDELIMITER);
					for (String s : pList) {
						if (s.equals(startX)) {
							auth = true;
						}
					}
				}
			}
			if (!okStart && !auth) {
				String mess = M.M().CannotStartWithThisPage(startX);
				Utils.errAlert(mess);
				return;
			}

			// initialize several factories
			tFactories.registerWebPanelResources(iResFactory.construct(result));
			iRegister.registerCustom(result);

			recognizePolymer(result);

			// construct WebPanel handler
			// start running
			if (MM.isPolymer()) {
				// for some reason this empty declarations are necessary here
				// for Polymer to work properly
				// otherwise a message "open is not a function"
				PaperDialog p = new PaperDialog();
				IronDropdown pi = new IronDropdown();
				// end of empty declaration

				final boolean fauth = auth;
				List<String> imp = new ArrayList<String>();
				imp.add("iron-icons/iron-icons.html");
				imp.add("vaadin-icons/vaadin-icons.html");
				final boolean pauth = auth;
				Polymer.importHref(imp, new Function() {
					public Object call(Object arg) {
						// The app is executed when all imports succeed.
						BinderPanelFactory.construct(new BinderPanelFactory.ISetWebPanel() {

							@Override
							public void set(IWebPanel t) {
								setWebPanel(wFactory.construct(new LogOut(pauth), MM.isPolymer()), result);
							}

						}, "mainpanel.xml", new LogOut(pauth));
						// setWebPanel(wFactory.construct(new LogOut(pauth),
						// MM.isPolymer()), result);
						startBegin(fauth);
						return null;
					}
				});
			} else {
				setWebPanel(wFactory.construct(new LogOut(auth), MM.isPolymer()), result);
				startBegin(auth);
			}
		}

	}

	private void recognizeNoCharts() {
		String noCharts = Utils.getURLParam(IUIConsts.NOCHARTSQUERY);
		if (CUtil.EqNS(noCharts, IUIConsts.ANSWERYES)) {
			M.setNocharts(true);
		}
	}

	private void recognizePolymer(ClientProp result) {
		MM.setPolymer(result.isPolymer());
		String polymerS = Utils.getURLParam(ICommonConsts.POLYMER);
		if (CUtil.EmptyS(polymerS))
			return;
		MM.setPolymer(CUtil.EqNS(polymerS, IUIConsts.ANSWERYES));
	}

	@Override
	public void start(CustomSecurity iCustom) {
		recognizeNoCharts();
		final Sync sy = new Sync();
		sy.go = new GoStart(null, iCustom);

		M.JR().getClientRes(UIGiniInjector.getI().getRequestContext(), new ResBack(sy));

		Runnable onLoadCallback = new Runnable() {
			public void run() {
				sy.signalDone();
			}
		};

		if (M.isNocharts())
			sy.signalDone();
		else
			VisualizationUtils.loadVisualizationApi(onLoadCallback, CoreChart.PACKAGE);
	}
}
