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

package com.gwtmodel.table.view.webpanel;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
class CallBackProgress {

    final private IWebPanel pLoc;
    private int coL = 0;
    private final IWebPanelResources pResources;

    CallBackProgress(final IWebPanel p,IWebPanelResources pResources) {
        this.pLoc = p;
        this.pResources = pResources;
    }

    private class TimerT extends Timer {

        public void run() {
            Label l = pLoc.getReplyL();
            l.setStyleName("wait-reply");
        }
    }
    private Timer tim = null;

    void IncDecL(final boolean inc) {
        Label lR = pLoc.getReplyL();
        if (inc) {
            coL++;
        } else {
            coL--;
        }
        if (coL == 0) {
            pLoc.clearReply();
            tim.cancel();
            tim = null;
        } else {
            String ss = pResources.getRes(IWebPanelResources.COUNTER) + coL;
            lR.setText(ss);
            if (tim == null) {
                tim = new TimerT();
                tim.schedule(100);
            }
        }
    }
}
