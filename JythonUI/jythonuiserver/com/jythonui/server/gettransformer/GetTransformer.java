/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.gettransformer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import com.google.common.io.Files;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.IConsts;
import com.jythonui.server.IGetResourceFile;
import com.jythonui.server.IGetResourceMap;
import com.jythonui.server.IGetTransformer;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.Util;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class GetTransformer extends UtilHelper implements IGetTransformer {

    private final IGetResourceMap iGet;
    private final IJythonUIServerProperties p;
    private final IGetResourceFile iGetResFile;

    @Inject
    public GetTransformer(IJythonUIServerProperties p, IGetResourceMap iGet,
            IGetResourceFile iGetResFile) {
        this.iGet = iGet;
        this.p = p;
        this.iGetResFile = iGetResFile;
    }

    private String getName(String baseName, String loca, String exte) {
        if (CUtil.EmptyS(exte))
            exte = IConsts.XSLTEXT;
        if (CUtil.EmptyS(loca))
            return baseName + "." + exte;
        return baseName + "_" + loca + "." + exte;

    }

    private String getLocaleName(String baseName, String exte, boolean locale) {
        if (locale) {
            String loc = Util.getLocale();
            if (CUtil.EmptyS(loc))
                return null;
            return getName(baseName, loc, exte);
        }
        return getName(baseName, null, exte);
    }

    private class Resolver implements URIResolver {

        private final String baseDir;

        Resolver(String baseDir) {
            this.baseDir = baseDir;
        }

        @Override
        public Source resolve(String href, String base)
                throws TransformerException {
            URL i = iGetResFile.getFirstURL(baseDir, href);
            try {
                return new StreamSource(i.openStream());
            } catch (IOException e) {
                throw new TransformerException(e);
            }
        }

    }

    @Override
    public Transformer getTranformer(String xsltName) {
        File f = new File(xsltName);
        String dirName = f.getParent();
        String exte = Files.getFileExtension(f.getName());
        String baseName = Files.getNameWithoutExtension(f.getName());
        Map<String, String> res = iGet.getResourceMap(p.getResource(), true,
                dirName, baseName);
        Holder.setLastBundle(res);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        String xName = getLocaleName(baseName, exte, true);
        URL u = null;
        if (xName != null)
            u = iGetResFile.getFirstURLIfExists(dirName, xName);
        if (u == null) {
            xName = getLocaleName(baseName, exte, false);
            u = iGetResFile.getFirstURL(dirName, xName);
        }

        try {
            tFactory.setURIResolver(new Resolver(dirName));
            Transformer trans = tFactory.newTransformer(new StreamSource(u.openStream()));
            trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            return trans;
        } catch (TransformerConfigurationException | IOException e) {
            errorMess(L(), IErrorCode.ERRORCODE120,
                    ILogMess.ERRORWHILEOPENINGXSTLFILE, e, xsltName, dirName,
                    xName);
            return null;
        }
    }
}
