/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.rhtml;

import java.util.HashMap;
import java.util.Map;

import com.javahotel.client.CommonUtil;
import com.javahotel.client.IResLocator;
import com.javahotel.client.ReadRequestHtml;
import com.javahotel.client.ReadRequestHtml.ISetRequestText;

class ReadRes implements IReadRes {

    private Map<String, String> ma;

    ReadRes(IResLocator sI) {
        ma = new HashMap<String, String>();
    }

    private class SetRequest implements ISetRequestText {

        private final ISetResText sRes;
        private final String resName;

        SetRequest(ISetResText i, String s) {
            sRes = i;
            resName = s;
        }

        public void setText(String s) {
            ma.put(resName, s);
            sRes.setResText(s);
        }

    }

    private void preadRes(ISetResText sRes, String resName, boolean main) {

        String h = ma.get(resName);
        if (h != null) {
            sRes.setResText(h);
            return;
        }
        String u = CommonUtil.getResAdr(resName, main);
        ReadRequestHtml.doGet(u, new SetRequest(sRes, resName));
    }

    public void readRes(ISetResText sRes, String resName) {
        preadRes(sRes, resName, false);

    }

    public void readResMain(ISetResText sRes, String resName) {
        preadRes(sRes, resName, true);
    }
}
