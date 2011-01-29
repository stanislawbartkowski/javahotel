/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.dialog;

import com.google.gwt.event.dom.client.HandlesAllMouseEvents;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SetMouseDownHandler {

    private enum TypeM {
        MOSEDOWN, MOUSESHIFTDOWN, MOUSECONTROLDOWN;
    }

    private SetMouseDownHandler() {

    }

    public interface IMouseCommand {
        void execute(Widget w);
    }

    private static class MouseEventHandler extends HandlesAllMouseEvents {

        private final IMouseCommand iC;
        private final TypeM t;
        private final Label l;
        private final IMouseCommand iC2;

        private MouseEventHandler(IMouseCommand iC, Label l, TypeM t) {
            this.iC = iC;
            this.t = t;
            this.l = l;
            this.iC2 = null;
        }

        private MouseEventHandler(IMouseCommand iC, IMouseCommand iC2, Label l,
                TypeM t) {
            this.iC = iC;
            this.t = t;
            this.l = l;
            this.iC2 = iC2;
        }

        public void onMouseDown(MouseDownEvent event) {
            if (t == TypeM.MOSEDOWN) {
                iC.execute(l);
            }
            if (t == TypeM.MOUSESHIFTDOWN) {
                if (event.isShiftKeyDown()) {
                    iC2.execute(l);
                } else {
                    iC.execute(l);
                }
            }
            if (t == TypeM.MOUSECONTROLDOWN) {
                if (event.isControlKeyDown()) {
                    iC2.execute(l);
                } else {
                    iC.execute(l);
                }
            }
        }

        public void onMouseUp(MouseUpEvent event) {
        }

        public void onMouseMove(MouseMoveEvent event) {
        }

        public void onMouseOut(MouseOutEvent event) {
        }

        public void onMouseOver(MouseOverEvent event) {
        }

        public void onMouseWheel(MouseWheelEvent event) {
        }
    }

    public static void setMouseDownClick(Label l, IMouseCommand com) {
        l.addMouseDownHandler(new MouseEventHandler(com, l, TypeM.MOSEDOWN));
    }

    public static void setMouseDownClick(Label l, IMouseCommand com,
            IMouseCommand comshift) {
        l.addMouseDownHandler(new MouseEventHandler(com, comshift, l,
                TypeM.MOUSECONTROLDOWN));
    }

}
