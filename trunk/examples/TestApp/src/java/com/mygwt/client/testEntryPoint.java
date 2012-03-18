/*
 *  Copyright 2011 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.mygwt.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Table;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.IJavaMailAction;
import com.gwtmodel.table.factories.IJavaMailActionFactory;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.WebPanelHolder;

/**
 * Main entry point.
 * 
 * @author hotel
 */
public class testEntryPoint implements EntryPoint {

	public interface IGetWidget {

		Widget getW();

	}

	/**
	 * Creates a new instance of testEntryPoint
	 */
	public testEntryPoint() {
	}

	@Override
	public void onModuleLoad() {

		class FormFactory implements IDataFormConstructorAbstractFactory {

			public CType construct(ICallContext iContext) {
				return new IDataFormConstructorAbstractFactory.CType();
			}
		}

		ITableAbstractFactories tFactories = GwtGiniInjector.getI()
				.getITableAbstractFactories();
		tFactories.registerGetCustomValues(new CustomFactory());
		tFactories
				.registerDataFormConstructorAbstractFactory(new FormFactory());
		tFactories.registerJavaMailActionFactory(new IJavaMailActionFactory() {

			public IJavaMailAction contruct() {
				return (IJavaMailAction) new MailOp();
			}
		});
		final WebPanelHolder.TableType tType = Utils.getParamTable();
		Runnable onLoadCallback = new Runnable() {

			@Override
			public void run() {
				start(tType);
			}
		};
		if (tType == WebPanelHolder.TableType.GOOGLETABLE) {
			VisualizationUtils.loadVisualizationApi(onLoadCallback,
					Table.PACKAGE);
		} else {
			start(tType);
		}
	}

	class CustomFactory implements IGetCustomValues {

		@Override
		public IVField getSymForCombo() {
			return null;
		}

		@Override
		public boolean compareComboByInt() {
			return false;
		}

		@Override
		public String getCustomValue(String key) {
			return null;
		}

        @Override
        public boolean addEmptyAsDefault() {
            return false;
        }
	}

	private class ButtonTest {
		private final String buttonTest;
		private final IGetWidget iG;

		ButtonTest(String buttonTest, IGetWidget iG) {
			this.buttonTest = buttonTest;
			this.iG = iG;
		}

		public String getButtonTest() {
			return buttonTest;
		}

		public IGetWidget getiG() {
			return iG;
		}

	}

	private final List<ButtonTest> bList = new ArrayList<ButtonTest>();
	private final DockLayoutPanel p = new DockLayoutPanel(Unit.EM);
	private Widget cWidget = null;

	/**
	 * The entry point method, called automatically by loading a module that
	 * declares an implementing class as an entry-point
	 */
	private void start(WebPanelHolder.TableType tType) {

		IGetWidget g = (IGetWidget) new TooltipTest();
		bList.add(new ButtonTest("Tooltip", g));
		g = (IGetWidget) new MailTest();
		bList.add(new ButtonTest("MailTest", g));
		VerticalPanel vB = new VerticalPanel();

		for (final ButtonTest bu : bList) {
			Button b = new Button(bu.getButtonTest());
			ClickHandler ha = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Widget w = bu.getiG().getW();
					if (cWidget != null) {
						p.remove(cWidget);
					}
					p.add(w);
					cWidget = w;
				}

			};
			b.addClickHandler(ha);
			vB.add(b);
		}
		p.addWest(vB, 10);

		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(p);
	}
}
