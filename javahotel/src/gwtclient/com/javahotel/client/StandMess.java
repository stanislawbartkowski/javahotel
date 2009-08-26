/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public final class StandMess {

    private StandMess() {
    }
    public final static int OK = 0;
    public final static int CANCEL = 1;
    public final static int YES = 2;
    public final static int NO = 3;

    private native static String getMess(final String p) /*-{
    var s = $wnd.Ext.MessageBox.buttonText[p];
    return s;
    }-*/;

    public static String getMess(final int id) {
        String p = null;
        switch (id) {
            case OK:
                p = "ok";
                break;
            case CANCEL:
                p = "cancel";
                break;
            case YES:
                p = "yes";
                break;
            case NO:
                p = "no";
                break;
        }
        return getMess(p);
    }
}
