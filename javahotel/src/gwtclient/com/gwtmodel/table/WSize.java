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
package com.gwtmodel.table;

import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
public class WSize {

    private final int top;
    private final int left;
    private final int height;
    private final int width;

    public WSize(int top, int left, int height, int width) {
        this.top = top;
        this.left = left;
        this.height = height;
        this.width = width;
    }

    public WSize(Widget w) {
        this.top = w.getAbsoluteTop();
        this.left = w.getAbsoluteLeft();
        this.height = w.getOffsetHeight();
        this.width = w.getOffsetWidth();
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
