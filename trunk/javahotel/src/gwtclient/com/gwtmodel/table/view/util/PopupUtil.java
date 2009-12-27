package com.gwtmodel.table.view.util;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;

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
                i.onClose();
            }
        });
        hC.setCellHorizontalAlignment(hC.getWidget(0),
                HasHorizontalAlignment.ALIGN_RIGHT);

        hP.add(hC);
    }

    public static void setPos(final PopupPanel pUp, final WSize w) {
        int t = w.getTop() + w.getHeight();
        int l = w.getLeft();
        pUp.setPopupPosition(l, t);
    }

    public static void setMiddlePos(final PopupPanel pUp, WSize w) {
        int t = w.getTop() + (w.getHeight() / 2);
        int l = w.getLeft() + (w.getWidth() / 2);
        pUp.setPopupPosition(l, t);
    }
}
