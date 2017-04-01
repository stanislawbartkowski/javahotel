/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.polymerui.client.util;

/**
 * Common class for synchronizing different events before going forward
 * 
 * @author stanislawbartkowski@gmail.com
 */
public abstract class SynchronizeList {

    private final int noSync;
    private int actSync;

    /**
     * Main task being fired after reaching max counter
     */
    protected abstract void doTask();

    /**
     * Constructor
     * 
     * @param noSync
     *            Number of signals before going forward
     */
    protected SynchronizeList(final int noSync) {
        this.noSync = noSync;
        reset();
    }

    /**
     * Reset counter
     */
    public final void reset() {
        actSync = 0;
    }

    /**
     * Check if counter hit already
     * 
     * @return true: yes, false : no
     */
    public boolean signalledAlready() {
        return (actSync >= noSync);
    }

    /**
     * Signal done, increase counter. After reaching max doTask
     */
    public void signalDone() {
        actSync++;
        runDoneIfPossible();
    }

    /**
     * Signal undone, decrease the counter
     */

    public void signalUndone() {
        actSync--;
    }

    public void runDoneIfPossible() {
        if (signalledAlready()) {
            doTask();
        }
    }
}
