/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

package com.gwtmodel.table.daytimeline;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class PanelDesc {

    private final boolean scrollLeftActive;
    private final boolean scrollRightActive;

    PanelDesc(boolean l, boolean r) {
        this.scrollLeftActive = l;
        this.scrollRightActive = r;
    }

    /**
     * @return the scrollLeftActive
     */
    public boolean isScrollLeftActive() {
        return scrollLeftActive;
    }

    /**
     * @return the scrollRightActive
     */
    public boolean isScrollRightActive() {
        return scrollRightActive;
    }

}
