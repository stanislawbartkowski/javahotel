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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.jythonui.server.BUtil;
import com.jythonui.server.MUtil;
import com.jythonui.server.ReadUTF8Properties;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.resource.IReadResource;

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
                return BUtil.addNameToPath(dir, bundle) + ".properties";
            }

            @Override
            public String getLoc() {
                if (loc == null)
                    return null;
                return BUtil.addNameToPath(dir, bundle + "_" + loc)
                        + ".properties";
            }
        };
    }

    public static Map<String, String> getBundle(IReadResource i,
            final String loc, final String dir, final String bundle) {
        ILocaleBundle l = getLocale(loc, dir, bundle);
        Properties defa = null;
        URL defaU = i.getRes(l.getDefa());
        if (defaU == null)
            return null;
        // return new HashMap<String, String>();
        try {
            defa = ReadUTF8Properties.readProperties(defaU.openStream());
        } catch (FileNotFoundException e) {
            return null;
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
