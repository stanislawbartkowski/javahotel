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
package com.jythonui.client.gini;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.dialog.impl.ReadDialog;
import com.jythonui.client.requestcontext.RequestContextFactory;
import com.jythonui.client.smessage.IGetDisplayMess;
import com.jythonui.client.smessage.IGetStandardMessage;
import com.jythonui.client.smessage.impl.GetDisplayMess;
import com.jythonui.client.start.IJythonClientStart;
import com.jythonui.client.start.impl.JythonClientStart;
import com.jythonui.shared.RequestContext;
import com.polymerui.client.binder.ICreateBinderWidget;
import com.polymerui.client.binder.ISetWidgetAttribute;
import com.polymerui.client.binder.impl.CreateBinderWidget;
import com.polymerui.client.binder.impl.SetWidgetAttribute;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.eventbus.impl.EventBus;
import com.polymerui.client.view.panel.MainPanelFactory;
import com.polymerui.client.view.util.CreatePolymerMenu;

public class UIInjectModule extends AbstractGinModule {

	@Override
	protected void configure() {

		bind(IJythonClientStart.class).to(JythonClientStart.class).in(Singleton.class);
		bind(RequestContext.class).toProvider(RequestContextFactory.class);
		bind(ICreateBinderWidget.class).to(CreateBinderWidget.class).in(Singleton.class);
		bind(ISetWidgetAttribute.class).to(SetWidgetAttribute.class).in(Singleton.class);
		bind(MainPanelFactory.class);
		bind(IGetDisplayMess.class).to(GetDisplayMess.class).in(Singleton.class);
		bind(IEventBus.class).to(EventBus.class);
		bind(IReadDialog.class).to(ReadDialog.class);
		requestStaticInjection(M.class, CreatePolymerMenu.class);
	}

	@Provides
	@Singleton
	IGetStandardMessage getStandardMessage() {
		return new IGetStandardMessage() {

			@Override
			public String getMessage(String sou) {
				return M.getClientProp().getAttr(sou);
			}

		};
	}

}