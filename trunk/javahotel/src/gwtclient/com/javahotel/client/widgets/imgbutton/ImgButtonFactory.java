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
package com.javahotel.client.widgets.imgbutton;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.gwtmodel.table.Utils;
import com.javahotel.client.CommonUtil;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */

// ToDO remove

class ImgButtonFactory {

    public static Button getButton(String bName, String img) {
        Button but;
        if (img != null) {
//            String h = CommonUtil.getImageHTML(img + ".gif");
            String h = Utils.getImageHTML(img + ".gif");
            but = new Button();
            but.setHTML(h);
        } else {
            but = new Button(bName);
        }
        return but;
    }

//    <button>
//    <table border="1" width="100%">
//    <tr>
//    <td width="19%"><img src="construct.gif" /></td>
//    <td width="81%">Under Construction</td>
//    </tr>
//    </table>
//    </button>
    //        String s = "<td><img src='" + getImageAdr(imageUrl) + "'></td>";
    static Button getButtonTextImage(String bName, String img) {
        String ht = "<table><tr>";
        String h = Utils.getImageHTML(img + ".gif");
        ht += h;
        Label la = new Label(bName);
        ht += "<td>" + la.getElement().getInnerHTML() + "</td>";
        ht += "</tr></table>";
        Button b = new Button();
        b.setHTML(ht);
        return b;
    }
}
