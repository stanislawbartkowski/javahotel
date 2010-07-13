/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.widgets.stable.seasonscroll;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.gwtmodel.table.htmlview.HtmlElemDesc;
import com.gwtmodel.table.htmlview.HtmlPanelFactory;
import com.gwtmodel.table.htmlview.HtmlTypeEnum;
import com.gwtmodel.table.htmlview.IHtmlPanelCallBack;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.button.ImgButtonFactory;
import com.javahotel.client.IResLocator;
import com.javahotel.client.idialog.GetIEditFactory;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.common.scrollseason.model.MoveSkip;
import com.javahotel.common.scrollseason.model.PanelDesc;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ScrollArrowWidget {

    private final static String C_BUTTON_LEFT_END = "BUTTON-LEFT-END";
    private final static String C_BUTTON_LEFT = "BUTTON-LEFT";
    private final static String C_BUTTON_RIGHT = "BUTTON-LRIGHT";
    private final static String C_BUTTON_RIGHT_END = "BUTTON-RIGHT-END";

    // private final HorizontalPanel hp = new HorizontalPanel();
    private final Button begP = ImgButtonFactory.getButton(C_BUTTON_LEFT_END,
            null, "arrow-left-end-default");
    private final Button leftP = ImgButtonFactory.getButton(C_BUTTON_LEFT,
            null, "arrow-left-default");
    private final Button rightP = ImgButtonFactory.getButton(C_BUTTON_RIGHT,
            null, "arrow-right-default");
    private final Button endP = ImgButtonFactory.getButton(C_BUTTON_RIGHT_END,
            null, "arrow-right-end-default");
    private final ILineField dDate;
    private final IsignalP iP;
    private final IResLocator rI;

    private final static String scrollBegId = "scrollpanel_Beg";
    private final static String scrollEndId = "scrollpanel_End";
    private final static String scrollLeftId = "scrollpanel_Left";
    private final static String scrollRightId = "scrollpanel_Right";
    private final static String scrollDateId = "scrollpanel_Date";

    interface IsignalP {

        void clicked(MoveSkip a);

        void clicked(Date d);
    }

    private class ClickEvent implements MouseDownHandler {

        private final MoveSkip clickType;

        ClickEvent(MoveSkip clickType) {
            this.clickType = clickType;
        }

        public void onMouseDown(MouseDownEvent event) {
            iP.clicked(clickType);
        }
    }

    private class ChangeD implements IChangeListener {

        public void onChange(ILineField i) {
            Date d = i.getDate();
            if (d == null) {
                return;
            }
            iP.clicked(d);
        }

    }

    ScrollArrowWidget(IResLocator rI, IsignalP i, boolean withDate,
            IHtmlPanelCallBack cBack) {
        this.rI = rI;
        dDate = GetIEditFactory.getTextCalendard(rI);
        dDate.setChangeListener(new ChangeD());
        List<HtmlElemDesc> li = new ArrayList<HtmlElemDesc>();
        li.add(new HtmlElemDesc(begP, scrollBegId));
        li.add(new HtmlElemDesc(endP, scrollEndId));
        li.add(new HtmlElemDesc(leftP, scrollLeftId));
        li.add(new HtmlElemDesc(rightP, scrollRightId));
        begP.addMouseDownHandler(new ClickEvent(MoveSkip.BEG));
        leftP.addMouseDownHandler(new ClickEvent(MoveSkip.LEFT));
        rightP.addMouseDownHandler(new ClickEvent(MoveSkip.RIGHT));
        endP.addMouseDownHandler(new ClickEvent(MoveSkip.END));
        HtmlPanelFactory hFactory = GwtGiniInjector.getI()
                .getHtmlPanelFactory();
        this.iP = i;
        if (withDate) {
            li.add(new HtmlElemDesc(dDate.getMWidget().getWidget(),
                    scrollDateId));
            hFactory.getHtmlPanel(HtmlTypeEnum.scrollWithDate, cBack, li);
        } else {
            hFactory.getHtmlPanel(HtmlTypeEnum.scrollWithoutDate, cBack, li);
        }
    }

    private void setB(Button b1, Button b2, boolean enable) {
        b1.setEnabled(enable);
        b2.setEnabled(enable);
    }

    void setState(PanelDesc pa) {
        setB(begP, leftP, pa.isScrollLeftActive());
        setB(endP, rightP, pa.isScrollRightActive());
    }

}
