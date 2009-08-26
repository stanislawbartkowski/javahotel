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
package com.javahotel.client.widgets.disclosure;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.DisclosurePanelImages;
import com.google.gwt.user.client.ui.Image;
import com.javahotel.client.CommonUtil;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class DisclosureImages implements DisclosurePanelImages {

    private class IOpen extends AbstractImagePrototype {

        private final String hI;
        private Image a = null;

        IOpen(String ima) {
//            hI = DefektUtil.getImageAdr(ima);
            hI = CommonUtil.getImageAdr(ima);
            a = new Image(hI);
        }

        public void applyTo(final Image arg0) {
//                a = arg0;
        }

        public Image createImage() {
            return a;
        }

        public String getHTML() {
            return hI;
        }
    }

    public AbstractImagePrototype disclosurePanelOpen() {
        return new IOpen("DataViewerMin.gif");
    }

    public AbstractImagePrototype disclosurePanelClosed() {
        return new IOpen("DataViewerMax.gif");
    }
}
