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
package com.gwtmodel.table.rdef;

import java.math.BigDecimal;

import com.gwtmodel.table.IGWidget;

public interface IFormLineView extends IGWidget {
    
    int NOCHOOSECHECK = 0;
    int CHOOSECHECKTRUE = 1;
    int CHOOSECHECKFALSE = 2;

    String getVal();

    void setVal(String s);
    
    void addChangeListener(IFormChangeListener cListener);
    
    void setReadOnly(boolean readOnly);
    
    void setInvalidMess(String errmess);
    
    void setStyleName(String styleMess, boolean set);
    
    void setOnTouch(ITouchListener lTouch);
    
    BigDecimal getDecimal();
    
    void setDecimal(BigDecimal b);

}
