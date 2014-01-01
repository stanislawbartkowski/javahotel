/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.mailcommon;

import com.gwtmodel.table.common.CUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author perseus
 */
public class CListOfMailProperties implements Serializable {

    private List<Map<String, String>> mList;
    private String errMess;
    private final static String F_NAME = "mail.com.filename";
    private final static String B_NAME = "mail.com.displayname";
    private final static String M_FROM = "mail.from";
    private final static String BOX_TYPE = "mail.box.type";
    private final static String BOX_IN = "in";

    public void setmList(List<Map<String, String>> mList) {
        this.mList = mList;
    }

    public void setErrMess(String errMess) {
        this.errMess = errMess;
    }

    public Map<String, String> getM(String n) {
        for (Map<String, String> m : mList) {
            String na = getName(m);
            if (CUtil.EqNS(na, n)) {
                return m;
            }
        }
        return null;
    }

    public String getName(Map<String, String> ma) {
        String s = ma.get(B_NAME);
        if (s != null) {
            return s;
        }
        return ma.get(F_NAME);
    }

    public String getFrom(Map<String, String> ma) {
        return ma.get(M_FROM);
    }

    /**
     * @return the mList
     */
    public List<Map<String, String>> getmList(boolean outBox) {
        if (mList == null) {
            mList = new ArrayList<Map<String, String>>();
        }
        List<Map<String, String>> oList = new ArrayList<Map<String, String>>();
        for (Map<String, String> ma : mList) {
            String inS = ma.get(BOX_TYPE);
            boolean inBS = CUtil.EqNS(inS, BOX_IN);
            if (outBox != inBS) {
                oList.add(ma);
            }
        }
        return oList;
    }

    public void addMap(Map<String, String> ma) {
        if (mList == null) {
            mList = new ArrayList<Map<String, String>>();
        }
        mList.add(ma);
    }

    /**
     * @return the errMess
     */
    public String getErrMess() {
        return errMess;
    }
}
