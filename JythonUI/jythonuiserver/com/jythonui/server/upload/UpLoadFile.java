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
package com.jythonui.server.upload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.newblob.IAddNewBlob;
import com.jythonui.shared.ICommonConsts;

/**
 * 
 * @author perseus
 */
@SuppressWarnings("serial")
public class UpLoadFile extends HttpServlet {

    private static final Logger log = Logger.getLogger(UpLoadFile.class
            .getName());

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletFileUpload upload = new ServletFileUpload();

        IAddNewBlob addB = SHolder.getAddBlob();

        PrintWriter out = response.getWriter();
        boolean first = true;
        try {
            FileItemIterator iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                // only uploaded files
                if (item.isFormField())
                    continue;

                String fName = item.getName();
                // nothing uploaded
                if (CUtil.EmptyS(fName))
                    continue;
                InputStream stream = item.openStream();
                // may be set initial size not default
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int len;
                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
                    bout.write(buffer, 0, len);
                }
                bout.close();
                // store blob content
                String bkey = addB.addNewBlob(ICommonConsts.BLOBUPLOAD_REALM,
                        ICommonConsts.BLOBUPLOAD_KEY, bout.toByteArray());
                if (!first)
                    out.print(',');
                first = false;
                out.print(ICommonConsts.BLOBUPLOAD_REALM);
                out.print(':');
                out.print(bkey);
                out.print(':');
                out.print(fName);
            } // while

        } catch (Exception e) {
            out.print(ICommonConsts.UPLOADFILEERROR);
            IGetLogMess iLog = SHolder.getM();
            String mess = iLog.getMess(IErrorCode.ERRORCODE77,
                    ILogMess.ERRORWHILEUPLOADING);
            log.log(Level.SEVERE, mess, e);
        }
        out.close();

    }
}
