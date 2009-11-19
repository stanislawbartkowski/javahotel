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
package com.javahotel.client.mvc.util;

import java.util.List;

import com.javahotel.client.rdata.RData;
import com.javahotel.common.toobject.AbstractTo;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class DefDrawCol implements RData.IVectorList {

    private final IDrawCol i;

    public DefDrawCol(final IDrawCol i) {
        this.i = i;
    }

    public void doVList(final List<? extends AbstractTo> val) {
        i.draw(val);
    }
}
