/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javahotel.client.widgets.popup;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.dialog.IWidgetSize;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;

/**
 *
 * @author hotel
 */
public class PopUpWithMenuClose {

    private final PopUpAPanel aPanel;
    private final ClickPopUp pUp;

    public PopUpWithMenuClose(final IWidgetSize w, final IContrPanel coP,
            final IControlClick cli) {
        ICloseAction cL = new ICloseAction() {

            public void setVisible(boolean visible) {
                pUp.setVisible(visible);
            }
        };
        aPanel = new PopUpAPanel(cL, coP, cli);
        pUp = new ClickPopUp(w, aPanel);

    }

    public VerticalPanel getVP() {
        return aPanel.getVP();
    }
}
