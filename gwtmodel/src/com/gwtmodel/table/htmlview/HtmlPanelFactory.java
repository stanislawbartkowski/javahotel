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
package com.gwtmodel.table.htmlview;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtmodel.table.readres.IReadRes;
import com.gwtmodel.table.readres.ISetResText;
import com.gwtmodel.table.readres.ReadResFactory;
import java.util.List;

/**
 *
 * @author perseus
 */
public class HtmlPanelFactory {

    private final ReadResFactory rFactory;

    @Inject
    public HtmlPanelFactory(ReadResFactory rFactory) {
        this.rFactory = rFactory;

    }

    private String getResName(HtmlTypeEnum e) {
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

    private class HtmlCallBack implements ISetResText {

        private final IHtmlPanelCallBack cBack;
        private final List<HtmlElemDesc> hList;

        private HtmlCallBack(IHtmlPanelCallBack cBack, List<HtmlElemDesc> hList) {
            this.cBack = cBack;
            this.hList = hList;
        }

        @Override
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

    public void getHtmlPanel(String resName,
            IHtmlPanelCallBack c, List<HtmlElemDesc> hList) {
        IReadRes iRes = rFactory.getReadRes();
        iRes.readRes(new HtmlCallBack(c, hList), resName);

    }

    public void getHtmlPanel(HtmlTypeEnum e,
            IHtmlPanelCallBack c, List<HtmlElemDesc> hList) {
        String resName = getResName(e);
        getHtmlPanel(resName, c, hList);
    }
}
