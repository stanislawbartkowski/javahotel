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
package com.gwtmodel.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DateFormatUtil;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import java.math.BigDecimal;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;

public class Utils {

    public static HTML createHTML(final String s) {
        HTML ha = new HTML("<a href='javascript:;'>" + s + "</a>");
        return ha;
    }

    private static String addPath(String path, String file) {
        int len = path.length();
        if ((len != 0) && path.charAt(len - 1) == '/') {
            return path + file;
        }
        return path + '/' + file;
    }

    public static String getResAdr(final String res) {
        String path;
        IGetCustomValues c = GwtGiniInjector.getI()
                .getTableFactoriesContainer().getGetCustomValues();
        String resF = c.getCustomValue(IGetCustomValues.RESOURCEFOLDER);
        path = GWT.getModuleBaseURL();
        if (resF == null) {
            return addPath(path, res);
        }
        return addPath(addPath(path, resF), res);
    }

    public static String getImageAdr(final String image) {
        IGetCustomValues c = GwtGiniInjector.getI()
                .getTableFactoriesContainer().getGetCustomValues();
        String folder = c.getCustomValue(IGetCustomValues.IMAGEFOLDER);
        String img;
        if (folder == null) {
            img = image;
        } else {
            img = addPath(folder, image);
        }
        String path = getResAdr(img);
        return path;
    }

    public static String getEmptytyLabel() {
        return ".";
    }

    public static String getImageHTML(final String imageUrl, int w, int h) {
        String s = "<td><img src='" + getImageAdr(imageUrl) + "'";
        if (w != 0) {
            s += " width='" + w + "px'";
        }
        if (h != 0) {
            s += " height='" + w + "px'";
        }
        s += "></td>";
        return s;
    }

    public static String getImageHTML(final String imageUrl) {
        return getImageHTML(imageUrl, 0, 0);
    }

    public static <T> boolean eqE(T t1, T t2) {
        if (t1 == null) {
            return (t2 == null);
        }
        if (t2 == null) {
            return false;
        }
        return t1 == t2;
    }

    public static final int BADNUMBER = -1;

    public static int getNum(final String s) {
        if ((s == null) || s.equals("")) {
            return BADNUMBER;
        }
        int i;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return BADNUMBER;
        }
        return i;
    }

    public static BigDecimal toBig(final String s) {
        if (s == null) {
            return null;
        }
        if (s.equals("")) {
            return null;
        }
        return new BigDecimal(s);
    }

    public static BigDecimal toBig(final Double d) {
        if (d == null) {
            return null;
        }
        String s = Double.toString(d);
        return new BigDecimal(s);
    }

    public static Double toDouble(final BigDecimal b) {
        if (b == null) {
            return null;
        }
        String s = DecimalToS(b);
        return new Double(s);
    }

    public static String DecimalToS(final BigDecimal c) {
        int l = c.intValue();
        String sl = Integer.toString(l);
        BigDecimal re = new BigDecimal(sl);
        int rest = c.subtract(re).intValue();
        String sr = DateFormatUtil.toNS(rest, 2);
        String st = sl + "." + sr;
        return st;
    }

    public static BigDecimal percent0(final BigDecimal c,
            final BigDecimal percent) {
        BigDecimal res = c.multiply(percent);
        BigDecimal l100 = new BigDecimal("100");
        BigDecimal res1 = res.divide(l100, 2, 0);
        return res1;
    }

    public static int CalculateNOfRows(WSize w) {
        int up = w.getTop();
        int he = Window.getClientHeight();

        int size = (he - up - 10) / 30;
        return size;
    }

    public static boolean TrueL(String s) {
        IGetCustomValues c = GwtGiniInjector.getI()
                .getTableFactoriesContainer().getGetCustomValues();
        String yesv = c.getCustomValue(IGetCustomValues.YESVALUE);
        return CUtil.EqNS(s, yesv);
    }

    public static String LToS(boolean l) {
        IGetCustomValues c = GwtGiniInjector.getI()
                .getTableFactoriesContainer().getGetCustomValues();
        if (l) {
            return c.getCustomValue(IGetCustomValues.YESVALUE);
        }
        return c.getCustomValue(IGetCustomValues.NOVALUE);
    }

    public static String getURLParam(String key) {
        return Window.Location.getParameter(key);
    }

    public static void setCaption(Widget w, String title, String attr) {
        Element e = DOM.createCaption();
        e.setInnerText(title);
        if (attr != null) {
            e.setAttribute("class", attr);
        }
        NodeList<Node> li = w.getElement().getChildNodes();
        li.getItem(0);
        w.getElement().insertBefore(e, li.getItem(0));
    }

    public static void errAlert(String s) {
        Window.alert(s);
    }

    public static void errAlert(String s1, String s2) {
        errAlert(s1 + " " + s2);
    }

    public static void errAlert(String err, Exception e) {
        errAlert(err, e.getMessage());
    }
}
