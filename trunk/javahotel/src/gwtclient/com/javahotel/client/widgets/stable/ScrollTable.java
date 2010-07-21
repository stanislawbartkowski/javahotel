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
package com.javahotel.client.widgets.stable;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Utils;
import com.javahotel.client.CommonUtil;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.idialog.GetIEditFactory;
import com.javahotel.client.ifield.ILineField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class ScrollTable implements IScrollSeason {

    final private IResLocator pLoc;
    private final static Integer FPA = new Integer(0);
    private final static Integer LPA = new Integer(1);
    private final static Integer NEPA = new Integer(2);
    private final static Integer PREPA = new Integer(3);
    private final static Integer APA = new Integer(4);
    /** Number of actual segment */
    private int ap;
    private int pNo;
    private int lastR;
    private int aPanel;
    private final IDrawPartSeason ii;
    private final static String A_MAX_LEFT = "arrow-left-end-default.gif";
    private final static String A_MAX_RIGHT = "arrow-right-end-default.gif";
    private final static String A_LEFT = "arrow-left-default.gif";
    private final static String A_RIGHT = "arrow-right-default.gif";
    private final static String A_DOWN = "arrow-down-default.gif";
    /** Total number of lines. */
    private int lNo;
    /** Number of first line draw actually */
    private int startno;
    private final ILineField edP;

    ScrollTable(final IResLocator pLoc, final IDrawPartSeason i, final int pNo) {
        this.ii = i;
        this.pLoc = pLoc;
        edP = GetIEditFactory.getLabelEdit(pLoc, "Liczba");
        edP.setVal("" + pNo);
    }

    /**
     * Return number of columns displayed actually, panel size
     *
     * @return number of columns
     */
    private int getMno() {
        int no = edP.getIntVal();
        return no;
    }

    public void createVPanel(List<Date> dList, int actC) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class SListener implements ClickListener {

        private final int newp;

        SListener(final int l) {
            this.newp = l;
        }

        public void onClick(final Widget arg0) {
            ap = newp;
            countS();
        }
    }

    private Widget createS(final int id, final boolean drawP) {
        VerticalPanel hP = new VerticalPanel();
        String im = null;
        int sno, lno;
        sno = 0;
        int newP = 0;
        int mNo = getMno();
        HasHorizontalAlignment.HorizontalAlignmentConstant al = HasHorizontalAlignment.ALIGN_CENTER;
        switch (id) {
            case 0:
                im = Utils.getImageHTML(A_MAX_LEFT);
                al = HasHorizontalAlignment.ALIGN_LEFT;
                sno = 0;
                newP = 0;
                break;
            case 1:
                im = Utils.getImageHTML(A_MAX_RIGHT);
                al = HasHorizontalAlignment.ALIGN_RIGHT;
                sno = lNo - lastR;
                newP = pNo - 1;
                break;
            case 2:
                im = Utils.getImageHTML(A_RIGHT);
                sno = (ap + 1) * mNo;
                newP = ap + 1;
                break;
            case 3:
                im = Utils.getImageHTML(A_LEFT);
                sno = (ap - 1) * mNo;
                newP = ap - 1;
                break;
            case 4:
                im = Utils.getImageHTML(A_DOWN);
                sno = ap * mNo;
                newP = ap;
                break;
            default:
                assert false : "Cannot be here";
        }
        if (sno < 0) {
            sno = 0;
        }
        lno = sno + mNo - 1;
        if (lno >= lNo) {
            lno = lNo - 1;
        }

        HTML i = new HTML(im);
        hP.add(i);
        hP.setCellWidth(i, "100%");
        hP.setCellHorizontalAlignment(i, al);

        Label l = new Label();
        hP.add(l);

        l.setText(pLoc.getMessages().scrollNum(sno, lno));
        l.addClickListener(new SListener(newP));
        i.addClickListener(new SListener(newP));
        if (drawP) {
            startno = sno;
            ii.draw(sno, lno);
        }
        if (drawP) {
            l.addStyleName("actual-elem");
        } else {
            l.addStyleName("table-scroll-elem");
        }
        return hP;
    }

    private Vector<Integer> createV() {
        Vector<Integer> v = new Vector<Integer>();
        v.add(FPA);
        if (ap == 0) {
            if (pNo == 2) {
                aPanel = 0;
            } else {
                v.add(NEPA);
                aPanel = 0;
            }
        } else if (ap == 1) {
            if (pNo == 2) {
                aPanel = 1;
            }
            if (pNo == 3) {
                v.add(APA);
                aPanel = 1;
            }
            if (pNo > 3) {
                v.add(APA);
                v.add(NEPA);
                aPanel = 1;
            }
        } else if (ap == pNo - 1) {
            if (pNo == 2) {
                aPanel = 1;
            } else {
                v.add(PREPA);
                aPanel = 2;
            }
        } else if (ap == pNo - 2) {
            if (pNo == 2) {
                aPanel = 0;
            }
            if (pNo == 3) {
                v.add(APA);
                aPanel = 1;
            }
            if (pNo > 3) {
                v.add(PREPA);
                v.add(APA);
                aPanel = 2;
            }
        } else {
            v.add(PREPA);
            v.add(APA);
            v.add(NEPA);
            aPanel = 2;
        }
        v.add(LPA);
        return v;
    }

    private void setUPanel(final HorizontalPanel vvP) {
        HorizontalPanel uP = new HorizontalPanel();
        if (vvP != null) {
            uP.add(vvP);
            uP.setSpacing(1);
        }
        uP.add(edP.getMWidget().getWidget());
        ii.setGwtWidget(new DefaultMvcWidget(uP));
//        ii.setSWidget(uP);
        HasVerticalAlignment.VerticalAlignmentConstant al = HasVerticalAlignment.ALIGN_BOTTOM;
        uP.setCellVerticalAlignment(edP.getMWidget().getWidget(), al);
    }

    private void drawS() {
        Vector<Integer> pV = createV();
        HorizontalPanel vvP = new HorizontalPanel();
        vvP.addStyleName("table-scroll-panel");
        vvP.setSpacing(1);
        for (int i = 0; i < pV.size(); i++) {
            Integer id = pV.get(i);
            Widget w = createS(id.intValue(), i == aPanel);
            vvP.add(w);
        }
        setUPanel(vvP);
    }

    private void countS() {
        int mNo = getMno();
        pNo = this.lNo / mNo;
        lastR = this.lNo - (pNo * mNo);
        if (lastR > 0) {
            pNo++;
        } else {
            lastR = mNo;
        }
        if (pNo <= 1) {
            ii.draw(0, this.lNo - 1);
            setUPanel(null);
            return;
        }
        drawS();
    }

    /**
     * Creates scroll panel.
     *
     * @param no
     *            Total number of lines
     * @param actC
     *            if != -1 then column number to display
     */
    public void createVPanel(final int no, final int actC) {
        this.lNo = no;
        ap = 0;
        if ((actC != -1) && (actC < no)) {
            ap = actC / getMno();
        }
        countS();
    }

    public int getStartNo() {
        return startno;
    }
}
