/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwthotel.shared;

import com.jythonui.shared.RMap;

abstract public class PropDescription extends RMap {

    private static final long serialVersionUID = 1L;


    private boolean gensymbol = false;
    

    public String getAutomPattern() {
        return getAttr(IHotelConsts.PATTPROP);
    }

    public boolean isGensymbol() {
        return gensymbol;
    }

    public void setGensymbol(boolean gensymbol) {
        this.gensymbol = gensymbol;
    }


}
