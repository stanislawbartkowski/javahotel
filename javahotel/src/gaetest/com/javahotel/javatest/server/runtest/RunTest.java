package com.javahotel.javatest.server.runtest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.javahotel.javatest.gaetest.LocalDataStoreTestEnvironment;
import com.javahotel.test.RunSuite;
//import com.javahotel.test.TestHelper;
import com.javahotel.test.TestSuite1;
import com.javahotel.test.TestSuite10;
import com.javahotel.test.TestSuite11;
import com.javahotel.test.TestSuite12;
import com.javahotel.test.TestSuite13;
import com.javahotel.test.TestSuite14;
import com.javahotel.test.TestSuite15;
import com.javahotel.test.TestSuite16;
import com.javahotel.test.TestSuite17;
import com.javahotel.test.TestSuite18;
import com.javahotel.test.TestSuite19;
import com.javahotel.test.TestSuite2;
import com.javahotel.test.TestSuite20;
import com.javahotel.test.TestSuite21;
import com.javahotel.test.TestSuite22;
import com.javahotel.test.TestSuite23;
import com.javahotel.test.TestSuite24;
import com.javahotel.test.TestSuite25;
import com.javahotel.test.TestSuite26;
import com.javahotel.test.TestSuite27;
import com.javahotel.test.TestSuite28;
import com.javahotel.test.TestSuite29;
import com.javahotel.test.TestSuite3;
import com.javahotel.test.TestSuite30;
import com.javahotel.test.TestSuite31;
import com.javahotel.test.TestSuite32;
import com.javahotel.test.TestSuite4;
import com.javahotel.test.TestSuite5;
import com.javahotel.test.TestSuite6;
import com.javahotel.test.TestSuite7;
import com.javahotel.test.TestSuite8;
import com.javahotel.test.TestSuite9;

public class RunTest {

    public void runTests() {

//        TestHelper.setiSetup(new LocalDataStoreTestEnvironment());

        Class<?> cla1 = TestSuite1.class;
        Class<?> cla2 = TestSuite2.class;
        Class<?> cla3 = TestSuite3.class;
        Class<?> cla4 = TestSuite4.class;
        Class<?> cla5 = TestSuite5.class;
        Class<?> cla6 = TestSuite6.class;
        Class<?> cla7 = TestSuite7.class;
        Class<?> cla8 = TestSuite8.class;
        Class<?> cla9 = TestSuite9.class;
        Class<?> cla10 = TestSuite10.class;
        Class<?> cla11 = TestSuite11.class;
        Class<?> cla12 = TestSuite12.class;
        Class<?> cla13 = TestSuite13.class;
        Class<?> cla14 = TestSuite14.class;
        Class<?> cla15 = TestSuite15.class; // thread
        Class<?> cla16 = TestSuite16.class; // thread
        Class<?> cla17 = TestSuite17.class;
        Class<?> cla18 = TestSuite18.class;
        Class<?> cla19 = TestSuite19.class;
        Class<?> cla20 = TestSuite20.class; // thread
        Class<?> cla21 = TestSuite21.class;
        Class<?> cla22 = TestSuite22.class;
        Class<?> cla23 = TestSuite23.class;
        Class<?> cla24 = TestSuite24.class;
        Class<?> cla25 = TestSuite25.class;
        Class<?> cla26 = TestSuite26.class;
        Class<?> cla27 = TestSuite27.class;
        Class<?> cla28 = TestSuite28.class;
        Class<?> cla29 = TestSuite29.class;
        Class<?> cla30 = TestSuite30.class;
        Class<?> cla31 = TestSuite31.class;
        Class<?> cla32 = TestSuite32.class;
        Class<?> runSuite = RunSuite.class;
        // Result res = JUnitCore.runClasses(new Class[] { cla1, cla2, cla3 ,
        // cla4, cla5,cla6, cla7 } );
        // Result res = JUnitCore.runClasses(new Class[] { cla8,
        // cla9,cla10,cla11,cla12,cla13,cla14 } );
        // Result res = JUnitCore.runClasses(new Class[] { cla17,
        // cla18,cla19,cla21,cla22,cla23,cla24 } );
        // Result res = JUnitCore.runClasses(new Class[] {
        // cla25,cla26,cla27,cla28,cla29,cla30, cla31 } );
        // Result res = JUnitCore.runClasses(new Class[] { cla29 });
        Result res = JUnitCore.runClasses(new Class[] { runSuite } );
        System.out.println("Number of tests :" + res.getRunCount());
        if (res.wasSuccessful()) {
            System.out.println("Success");
        }
        for (int i = 0; i < res.getFailureCount(); i++) {
            System.out.println(res.getFailures().get(i).getTrace());
        }
    }

}
