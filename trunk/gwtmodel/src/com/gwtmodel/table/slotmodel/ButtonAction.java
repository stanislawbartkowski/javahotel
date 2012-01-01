/*
 *  Copyright 2012 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.gwtmodel.table.slotmodel;

/**
 * Button action description
 * @author hotel
 */
public class ButtonAction {

    public enum Action {
        ClickButton, // click button 
        EnableButton, // enable button
        DisableButton, // disable button
        RedirectButton, // redirect button, make button behave like another button
        ForceButton // artificially causes push button
    }

    /** Action */
    private final Action action;
    /** Redirected to button. */
    private final ClickButtonType redirectB;

    public ButtonAction(Action action, ClickButtonType redirectB) {
        this.action = action;
        this.redirectB = redirectB;
    }

    public ButtonAction(Action action) {
        this(action, null);
    }

    /**
     * @return the action
     */
    public Action getAction() {
        return action;
    }

    /**
     * @return the redirectB
     */
    public ClickButtonType getRedirectB() {
        return redirectB;
    }

}
