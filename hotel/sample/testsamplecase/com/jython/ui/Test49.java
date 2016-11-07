/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jython.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

public class Test49 extends TestHelper {

    @Before
    public void setUp() {
        super.setUp();
        M.setJythonSerialized(true);
    }

    private class RunJ implements Runnable {

        @Override
        public void run() {
            System.out.println("run");
            DialogVariables v = new DialogVariables();
            System.out.println("run 1");
            runAction(v, "test94.xml", "runthreadx");
            System.out.println("after run");
        }

    }

    @Test
    public void test1() {
        DialogFormat d = findDialog("test94.xml");
        assertNotNull(d);
        Thread t1 = new Thread(new RunJ());
        Thread t2 = new Thread(new RunJ());
        t1.start();
        t2.start();
        do {

        } while (t1.isAlive() || t2.isAlive());
    }

    private final int NO = 10;

    private Thread threads[] = new Thread[NO];

    private class RunNumber implements Runnable {

        private final int no;

        RunNumber(int no) {
            this.no = no;
        }

        @Override
        public void run() {
            System.out.println("start:" + no);
            DialogVariables v = new DialogVariables();
            v.setValueL("no", no);
            runAction(v, "test94.xml", "runthread");
            System.out.println("stop:" + no);
        }

    }

    @Test
    public void test2() throws InterruptedException {
        DialogFormat d = findDialog("test94.xml");
        assertNotNull(d);
        for (int i = 0; i < NO; i++)
            threads[i] = new Thread(new RunNumber(i));
        for (int i = 0; i < NO; i++)
            threads[i].start();
        boolean finished = false;
        while (!finished) {
            Thread.currentThread().sleep(1000);
            finished = true;
            for (int i = 0; i < NO; i++)
                if (threads[i].isAlive()) {
                    finished = false;
                    break;
                }

        }
    }

}
