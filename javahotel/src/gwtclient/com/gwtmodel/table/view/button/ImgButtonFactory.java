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
import com.gwtmodel.table.Utils;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ImgButtonFactory {

    private ImgButtonFactory() {
    }

    public static Button getButton(String bId,String bName, String img) {
        Button but;
        if (img != null) {
            String h = Utils.getImageHTML(img + ".gif");
            but = new Button();
            but.setHTML(h);
        } else {
            but = new Button(bName);
        }
        but.getElement().setId(bId);
        return but;
    }

    public static Button getButtonTextImage(String bId, String bName, String img) {
        String ht = "<table><tr>";
        String h = Utils.getImageHTML(img + ".gif");
        ht += h;
        Label la = new Label(bName);
        ht += "<td>" + la.getElement().getInnerHTML() + "</td>";
        ht += "</tr></table>";
        Button b = new Button();
        b.setHTML(ht);
        b.getElement().setId(bId);
        return b;
    }
}
