/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    public void getHtmlPanel(HtmlTypeEnum e,
            IHtmlPanelCallBack c, List<HtmlElemDesc> hList) {
        String resName = getResName(e);
        IReadRes iRes = rFactory.getReadRes();
        if (e == HtmlTypeEnum.MainStatus) {
            iRes.readResMain(new HtmlCallBack(c, hList), resName);
        } else {
            iRes.readRes(new HtmlCallBack(c, hList), resName);
        }

    }

}
