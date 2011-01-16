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
package com.gwtmodel.table.view.button;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.gwtmodel.table.GFocusWidgetFactory;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.Utils;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ImgButtonFactory {

    private ImgButtonFactory() {
    }

    public static IGFocusWidget getButton(String bId, String bName, String img) {
        Button but;
        IGFocusWidget w;
        if (img != null) {
            String imageFile;
            if (img.indexOf('.') == -1) {
                imageFile = img + ".gif";
            } else {
                imageFile = img;
            }
            String h = Utils.getImageHTML(imageFile);
            but = new Button();
            but.setHTML(h);
            // w = new BImage(but, bName);
            w = GFocusWidgetFactory.construct(but, bName);
        } else {
            but = new Button(bName);
            w = GFocusWidgetFactory.construct(but);
        }
        if (bId != null) {
//            but.getElement().setId(bId);
            Utils.setId(but, bId);
        }
        return w;
    }

    public static IGFocusWidget getButtonTextImage(String bId, String bName,
            String img) {
        String ht = "<table><tr>";
        String h = Utils.getImageHTML(img + ".gif");
        ht += h;
        Label la = new Label(bName);
        ht += "<td>" + la.getElement().getInnerHTML() + "</td>";
        ht += "</tr></table>";
        Button b = new Button();
        b.setHTML(ht);
        b.getElement().setId(bId);
        return GFocusWidgetFactory.construct(b);
    }
}
