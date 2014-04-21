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
package com.jythonui.server.getbundle;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.jython.ui.shared.MUtil;
import com.jython.ui.shared.ReadUTF8Properties;
import com.jython.ui.shared.UtilHelper;
import com.jython.ui.shared.resource.IReadResource;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class ReadBundle extends UtilHelper {

    private interface ILocaleBundle {
        String getDefa();

        String getLoc();
    }

    private static ILocaleBundle getLocale(final String loc, final String dir,
            final String bundle) {

        return new ILocaleBundle() {

            @Override
            public String getDefa() {
                return dir + "/" + bundle + ".properties";
            }

            @Override
            public String getLoc() {
                if (loc == null)
                    return null;
                return dir + "/" + bundle + "_" + loc + ".properties";
            }
        };
    }

    public static Map<String, String> getBundle(IReadResource i,
            final String loc, final String dir, final String bundle) {
        ILocaleBundle l = getLocale(loc, dir, bundle);
        Properties defa = null;
        URL defaU = i.getRes(l.getDefa());
        if (defaU == null)
            return new HashMap<String, String>();
        try {
            defa = ReadUTF8Properties.readProperties(defaU.openStream());
        } catch (IOException e) {
            if (SHolder.getM() != null)
                errorLog(
                        SHolder.getM().getMess(IErrorCode.ERRORCODE46,
                                ILogMess.ERRORWHILELOADINGBUNDLERESOURCE,
                                defaU.toString()), e);
            else
                errorLog("Error while reading " + defaU.toString(), e);
        }
        Properties prop = null;

        if (l.getLoc() != null && i.getRes(l.getLoc()) != null) {
            URL iloca = i.getRes(l.getLoc());
            try {
                if (iloca != null)
                    prop = ReadUTF8Properties
                            .readProperties(iloca.openStream());
            } catch (IOException e1) {
                // expected, do nothing
                logDebug(iloca.toString() + " not found");
            }
        }
        Map<String, String> b = new HashMap<String, String>();
        MUtil.toElem(b, defa);
        if (prop != null)
            MUtil.toElem(b, prop);
        return b;
    }

}
