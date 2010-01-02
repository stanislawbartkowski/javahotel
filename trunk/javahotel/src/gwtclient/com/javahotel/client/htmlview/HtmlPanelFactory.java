/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.htmlview;

import java.util.List;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.readres.ISetResText;
import com.javahotel.client.IResLocator;

public class HtmlPanelFactory {

    private HtmlPanelFactory() {

    }

    private static String getResName(HtmlTypeEnum e) {
        switch (e) {
        case MainStatus:
            return "header.html";
        case scrollWithDate:
            return "scrollWithDate.html";
        case scrollWithoutDate:
            return "scrollWithoutDate.html";
        }
        return null;
    }

    private static class HtmlCallBack implements ISetResText {

        private final IHtmlPanelCallBack cBack;
        private final List<HtmlElemDesc> hList;

        private HtmlCallBack(IHtmlPanelCallBack cBack, List<HtmlElemDesc> hList) {
            this.cBack = cBack;
            this.hList = hList;
        }

        public void setResText(String s) {
            HTMLPanel ha = new HTMLPanel(s);
            if (hList != null) {
                for (HtmlElemDesc e : hList) {
                    ha.addAndReplaceElement(e.getW(), e.getiD());
                }
            }
            cBack.setHtmlPanel(ha);
        }

    }

    public static void getHtmlPanel(IResLocator sI, HtmlTypeEnum e,
            IHtmlPanelCallBack c, List<HtmlElemDesc> hList) {
        String resName = getResName(e);
        if (e == HtmlTypeEnum.MainStatus) {
            sI.readRes().readResMain(new HtmlCallBack(c, hList), resName);
        } else {
            sI.readRes().readRes(new HtmlCallBack(c, hList), resName);
        }

    }

}
