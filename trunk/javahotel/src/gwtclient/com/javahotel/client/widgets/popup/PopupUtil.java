/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.widgets.popup;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Utils;
import com.javahotel.client.dialog.IWidgetSize;
import com.javahotel.client.dialog.WidgetSizeFactory;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class PopupUtil {

    @SuppressWarnings("deprecation")
    public static void addClose(final Panel hP, final ICloseAction i,
            final MenuBar menu, final MenuBar bmenu) {
        HorizontalPanel hC = new HorizontalPanel();
        hC.setWidth("100%");
        HorizontalPanel hG = new HorizontalPanel();
        HTML w = new HTML(Utils.getImageHTML("DataViewerClose.gif"));
        if (menu != null) {
            bmenu.addItem(Utils.getImageHTML("DataViewerMax.gif"), true, menu);
            hG.add(bmenu);
        }
        hG.add(w);
        hC.add(hG);
        w.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                i.setVisible(false);
            }
        });
        hC.setCellHorizontalAlignment(hC.getWidget(0),
                HasHorizontalAlignment.ALIGN_RIGHT);

        hP.add(hC);
    }

    public static void setPos(final PopupPanel pUp, final IWidgetSize w) {
        int t = w.getTop() + w.getHeight();
        int l = w.getLeft();
        pUp.setPopupPosition(l, t);
    }

    public static void setMiddlePos(final PopupPanel pUp, final IWidgetSize w) {
        int t = w.getTop() + (w.getHeight() / 2);
        int l = w.getLeft() + (w.getWidth() / 2);
        pUp.setPopupPosition(l, t);
    }

    public static void setPos(final PopupPanel pUp, final Widget w) {
        setPos(pUp, WidgetSizeFactory.getW(w));
    }

    public static void setMiddlePos(final PopupPanel pUp, final Widget w) {
        setMiddlePos(pUp, WidgetSizeFactory.getW(w));
    }
}
