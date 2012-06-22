/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.stackpanelcontroller;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.view.util.PopupCreateMenu;
import java.util.List;

/**
 *
 * @author perseus
 */
//        String htmlImage = Utils.getImageHTML(IImageGallery.DOWNMENU);
//        return PopupCreateMenu.createImageMenu(htmlImage,
//                new ListOfControlDesc(li), new Click(), null);
//    }
//     String DOWNMENU = "DataViewerMax.gif";
class MenuPanelController extends AbstractStackPanelController {

    MenuPanelController(IDataType dType, String downMenuImage, List<ControlButtonDesc> li) {
        this.dType = dType;
        this.sView = PopupCreateMenu.createImageMenu(downMenuImage,
                new ListOfControlDesc(li), new CallBack(), null);
    }
}
