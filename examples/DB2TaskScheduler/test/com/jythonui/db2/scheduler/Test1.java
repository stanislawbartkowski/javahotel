package com.jythonui.db2.scheduler;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class Test1 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = null;
        d = getD(D);
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, D, "readlist");
        ListOfRows li = v.getList("list");
        assertNotNull(li);
    }

    @Test
    public void test2() {
        DialogFormat d = getD(D);
        DialogVariables v = new DialogVariables();
        v.setValueS("datasource", "aix");
        v.setValueS("host", "192.168.1.1");
        v.setValueS("database", "SAMPLE");
        v.setValueL("port", 50000L);
        v.setValueB("list_lineset", false);

        runAction(v, D, "writedatasource");
        v = new DialogVariables();
        runAction(v, D, "readlist");
        ListOfRows li = v.getList("list");
        assertNotNull(li);
        ListFormat list = d.findList("list");
        RowIndex rI = new RowIndex(list.getColumns());
        boolean found = false;
        for (RowContent r : li.getRowList()) {
            String s = rI.get(r, "datasource").getValueS();
            System.out.println(s);
            if (s.equals("aix")) {
                Long l = rI.get(r, "port").getValueL();
                assertEquals(50000L, l.longValue());
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void test3() {
        test2();
        DialogFormat d = getD(D);
        DialogVariables v = new DialogVariables();
        v.setValueS("datasource", "linux");
        v.setValueS("host", "192.168.1.2");
        v.setValueS("database", "SAMPLE");
        v.setValueL("port", 50004L);

        runAction(v, D, "writedatasource");
        v = new DialogVariables();
        runAction(v, D, "readlist");
        ListOfRows li = v.getList("list");
        assertNotNull(li);
        ListFormat list = d.findList("list");
        RowIndex rI = new RowIndex(list.getColumns());
        int no = 0;
        for (RowContent r : li.getRowList()) {
            String s = rI.get(r, "datasource").getValueS();
            System.out.println(s);
            if (s.equals("aix")) {
                Long l = rI.get(r, "port").getValueL();
                assertEquals(50000L, l.longValue());
                no++;
            }
            if (s.equals("linux")) {
                Long l = rI.get(r, "port").getValueL();
                assertEquals(50004L, l.longValue());
                no++;
            }
        }
        assertEquals(2, no);

        v = new DialogVariables();
        v.setValueS("datasource", "aix");
        runAction(v, D, "deletedatasource");
        v = new DialogVariables();
        runAction(v, D, "readlist");
        li = v.getList("list");
        assertNotNull(li);
        no = 0;
        for (RowContent r : li.getRowList()) {
            String s = rI.get(r, "datasource").getValueS();
            System.out.println(s);
            if (s.equals("aix")) {
                fail("aix not removed");
            }
            if (s.equals("linux")) {
                Long l = rI.get(r, "port").getValueL();
                assertEquals(50004L, l.longValue());
                no++;
            }
        }
        assertEquals(1, no);
    }

}
