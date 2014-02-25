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
package com.jythonui.server.upload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.shared.ICommonConsts;

/**
 * 
 * @author perseus
 */
@SuppressWarnings("serial")
public class UpLoadFile extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletFileUpload upload = new ServletFileUpload();

        ISequenceRealmGen iSeq = SHolder.getSequenceRealmGen();
        IBlobHandler iBlob = SHolder.getBlobHandler();

        PrintWriter out = response.getWriter();
        boolean first = true;
        try {
            FileItemIterator iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                // only uploaded files
                if (item.isFormField())
                    continue;

                String name = item.getFieldName();
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
                Long bkey = iSeq.genNext(ICommonConsts.BLOBUPLOAD_REALM,
                        ICommonConsts.BLOBUPLOAD_KEY);
                iBlob.addBlob(ICommonConsts.BLOBUPLOAD_REALM, bkey.toString(),
                        bout.toByteArray());
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
            e.printStackTrace();
            out.print(ICommonConsts.UPLOADFILEERROR);
        }
        out.close();

    }
}
