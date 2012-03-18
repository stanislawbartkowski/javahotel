/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.daytimetable.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickHandler;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.daytimeline.MoveSkip;
import com.gwtmodel.table.daytimeline.PanelDesc;
import com.gwtmodel.table.htmlview.HtmlElemDesc;
import com.gwtmodel.table.htmlview.HtmlPanelFactory;
import com.gwtmodel.table.htmlview.HtmlTypeEnum;
import com.gwtmodel.table.htmlview.IHtmlPanelCallBack;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.button.ImgButtonFactory;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ScrollArrowWidget {

    private final static String C_BUTTON_LEFT_END = "BUTTON-LEFT-END";
    private final static String C_BUTTON_LEFT = "BUTTON-LEFT";
    private final static String C_BUTTON_RIGHT = "BUTTON-LRIGHT";
    private final static String C_BUTTON_RIGHT_END = "BUTTON-RIGHT-END";
    private final IGFocusWidget begP;
    private final IGFocusWidget leftP;
    private final IGFocusWidget rightP;
    private final IGFocusWidget endP;
    private final IFormLineView dDate;
    private final IsignalP iP;
    private final static String scrollBegId = "scrollpanel_Beg";
    private final static String scrollEndId = "scrollpanel_End";
    private final static String scrollLeftId = "scrollpanel_Left";
    private final static String scrollRightId = "scrollpanel_Right";
    private final static String scrollDateId = "scrollpanel_Date";

    interface IsignalP {

        void clicked(MoveSkip a);

        void clicked(Date d);
    }

    private class ClickEvent implements ClickHandler {

        private final MoveSkip clickType;

        ClickEvent(MoveSkip clickType) {
            this.clickType = clickType;
        }

        @Override
        public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
            iP.clicked(clickType);
        }
    }

    private class ChangeD implements IFormChangeListener {

        @Override
        public void onChange(IFormLineView i, boolean aferFocus) {
            Date d = (Date) i.getValObj();
            if (d == null) {
                return;
            }
            iP.clicked(d);
        }
    }

    ScrollArrowWidget(IsignalP i, boolean withDate, IHtmlPanelCallBack cBack,
            String[] titles) {
        begP = ImgButtonFactory.getButton(C_BUTTON_LEFT_END, titles[0],
                "arrow-left-end-default");
        leftP = ImgButtonFactory.getButton(C_BUTTON_LEFT, titles[1],
                "arrow-left-default");
        rightP = ImgButtonFactory.getButton(C_BUTTON_RIGHT, titles[2],
                "arrow-right-default");
        endP = ImgButtonFactory.getButton(C_BUTTON_RIGHT_END, titles[3],
                "arrow-right-end-default");

        EditWidgetFactory eFactory = GwtGiniInjector.getI()
                .getEditWidgetFactory();
        dDate = eFactory.construcDateBoxCalendar(Empty.getFieldType());
        dDate.addChangeListener(new ChangeD());
        List<HtmlElemDesc> li = new ArrayList<HtmlElemDesc>();
        li.add(new HtmlElemDesc(begP.getGWidget(), scrollBegId));
        li.add(new HtmlElemDesc(endP.getGWidget(), scrollEndId));
        li.add(new HtmlElemDesc(leftP.getGWidget(), scrollLeftId));
        li.add(new HtmlElemDesc(rightP.getGWidget(), scrollRightId));
        begP.addClickHandler(new ClickEvent(MoveSkip.BEG));
        leftP.addClickHandler(new ClickEvent(MoveSkip.LEFT));
        rightP.addClickHandler(new ClickEvent(MoveSkip.RIGHT));
        endP.addClickHandler(new ClickEvent(MoveSkip.END));
        HtmlPanelFactory hFactory = GwtGiniInjector.getI()
                .getHtmlPanelFactory();
        this.iP = i;
        if (withDate) {
            li.add(new HtmlElemDesc(dDate.getGWidget(), scrollDateId));
            hFactory.getHtmlPanel(HtmlTypeEnum.scrollWithDate, cBack, li);
        } else {
            hFactory.getHtmlPanel(HtmlTypeEnum.scrollWithoutDate, cBack, li);
        }
    }

    private void setB(IGFocusWidget b1, IGFocusWidget b2, boolean enable) {
        b1.setEnabled(enable);
        b2.setEnabled(enable);
    }

    void setState(PanelDesc pa) {
        setB(begP, leftP, pa.isScrollLeftActive());
        setB(endP, rightP, pa.isScrollRightActive());
    }
}
