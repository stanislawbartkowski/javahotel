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
package com.javahotel.client.start;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.javahotel.client.IResLocator;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class CallBackProgress {

    final private IResLocator pLoc;
    private int coL = 0;

    CallBackProgress(final IResLocator p) {
        this.pLoc = p;
    }

    private class TimerT extends Timer {

        public void run() {
            Label l = pLoc.getPanel().getReplyL();
            l.setVisible(true);
            l.setStyleName("wait-reply");
        }
    }
    private Timer tim = null;

    void IncDecL(final boolean inc) {
        Label lR = pLoc.getPanel().getReplyL();
        if (inc) {
            coL++;
        } else {
            coL--;
        }
        if (coL == 0) {
            lR.setVisible(false);
            tim.cancel();
            tim = null;
        } else {
            String ss = pLoc.getMessages().countNum(coL);
            lR.setText(ss);
            if (tim == null) {
                tim = new TimerT();
                tim.schedule(100);
            }
        }
    }
}
