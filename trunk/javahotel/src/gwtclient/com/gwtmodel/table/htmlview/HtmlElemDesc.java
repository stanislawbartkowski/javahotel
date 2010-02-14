/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gwtmodel.table.htmlview;

import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author perseus
 */
public class HtmlElemDesc {

    private final String iD;
    private final Widget w;

    public HtmlElemDesc(Widget w, String iD) {
        this.iD = iD;
        this.w =w ;
    }

    public String getiD() {
        return iD;
    }
    public Widget getW() {
        return w;
    }


}
