/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwtmodel.table.common;

/**
 *
 * @author perseus
 */
public class CUtil {

    private CUtil() {
    }

    public static boolean EmptyS(String s) {
        if (s == null) {
            return true;
        }
        return s.equals("");
    }

    public static boolean OkNumber(String s) {
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static String toAfterS(String s, int afterdot) {
        String a[] = s.split("\\.");
        String beforeS = a[0];
        String afterS = "";
        String ss;
        if (a.length > 1) {
            afterS = a[1];
        }
        while (afterS.length() < afterdot) {
            afterS += "0";
        }
        afterS = afterS.substring(0, afterdot);
        if (afterdot > 0) {
            ss = beforeS + "." + afterS;
        } else {
            ss = beforeS;
        }
        return ss;
    }

    public static boolean EqNS(String p1, String p2) {
        // important: if both null that not equal
        if ((p1 == null) || (p2 == null)) { return false; }
        return p1.equalsIgnoreCase(p2);
    }

    public static int getNumb(String s) {
        Integer i = new Integer(s);
        return i.intValue();
    }


}
