/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

import com.jythonui.shared.ElemDescription;

abstract public class PropDescription extends ElemDescription {

    public String getId() {
        return getAttr(IHotelConsts.ID);
    }

    public String getDescription() {
        return getAttr(IHotelConsts.DESCRIPTION);
    }

    public String getName() {
        return getAttr(IHotelConsts.NAME);
    }

    public void setName(String name) {
        setAttr(IHotelConsts.NAME, name);
    }

    public void setDescription(String de) {
        setAttr(IHotelConsts.DESCRIPTION, de);
    }
}
