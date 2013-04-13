package com.jythonui.db2.scheduler;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

import static org.junit.Assert.*;

public class Test2 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = getD(D);
        DialogVariables v = new DialogVariables();
        v = new DialogVariables();
        iJ.runAction(v, D, "cleardefapar");
        iJ.runAction(v, D, "readdefaparam");
        String s = v.getValueS("par1");
        System.out.println(s);
        assertEquals(s, "");
        s = v.getValueS("par2");
        System.out.println(s);
        assertEquals(s, "");
    }

    @Test
    public void test2() {
        test1();
        DialogFormat d = getD(D);
        DialogVariables v = new DialogVariables();
        iJ.runAction(v, D, "writedefaparam");
        iJ.runAction(v, D, "readdefaparam");
        String s = v.getValueS("par1");
        System.out.println(s);
        assertEquals(s, "val1");
        s = v.getValueS("par2");
        System.out.println(s);
        assertEquals(s, "val2");
    }

}
