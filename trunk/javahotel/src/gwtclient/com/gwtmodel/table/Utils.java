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
import com.google.gwt.user.client.ui.HTML;

public class Utils {
    
    public static HTML createHTML(final String s) {
        HTML ha = new HTML("<a href='javascript:;'>" + s + "</a>");
        return ha;
    }

    public static String getResAdr(final String res) {
        String path;
        path = "com.javahotel.web/";
        path = GWT.getModuleBaseURL();
        return path + "/res/" + res;
    }

    public static String getImageAdr(final String image) {
        String path = getResAdr("img/" + image);
        return path;
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

}
