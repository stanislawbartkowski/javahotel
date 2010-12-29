/*
 *  Copyright 2010 stanislawbartkowski@gmail.com
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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Table;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.WebPanelHolder;
import com.gwtmodel.table.view.button.ImgButtonFactory;
import com.gwtmodel.table.view.util.PopupTip;

/**
 * Main entry point.
 * 
 * @author hotel
 */
public class testEntryPoint implements EntryPoint {

    /**
     * Creates a new instance of testEntryPoint
     */
    public testEntryPoint() {
    }

    @Override
    public void onModuleLoad() {
        ITableAbstractFactories tFactories = GwtGiniInjector.getI()
                .getITableAbstractFactories();
        tFactories.registerGetCustomValues(new CustomFactory());
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
    }

    private class LabTip extends PopupTip {

        LabTip() {
            Label l = new Label("Label");
            initWidget(l);
            setMessage("Help test - look how it looks like !");
        }
    }

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry-point
     */
    private void start(WebPanelHolder.TableType tType) {
        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(60);
        vp.add(new LabTip());
        final IGFocusWidget but = ImgButtonFactory.getButton(null,
                "Click on me and look who disables !", "gwt.png");
        final IGFocusWidget but1 = ImgButtonFactory.getButton(null,
                "Click on me and look who disables !", null);
        but.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                but1.setEnabled(!but1.isEnabled());
            }

        });

        but1.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                but.setEnabled(!but.isEnabled());
            }

        });

        vp.add(but.getGWidget());
        vp.add(but1.getGWidget());
        RootPanel.get().add(vp);
    }
}
