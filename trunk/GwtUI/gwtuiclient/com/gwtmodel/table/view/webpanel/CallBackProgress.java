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

package com.gwtmodel.table.view.webpanel;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
class CallBackProgress {

    final private IWebPanel pLoc;
    private int coL = 0;

    CallBackProgress(final IWebPanel p) {
        this.pLoc = p;
    }

    void IncDecL(final boolean inc) {
        if (inc) {
            coL++;
        } else {
            coL--;
        }
        pLoc.setReplay(coL);
    }
}
