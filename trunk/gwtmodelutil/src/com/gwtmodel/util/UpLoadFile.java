/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.util;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.IConstUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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

        PrintWriter out = response.getWriter();
        try {
            FileItemIterator iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();

                String name = item.getFieldName();
                if (!CUtil.EqNS(IConstUtil.FILENAMEID, name)) {
                    continue;
                }
                InputStream stream = item.openStream();
                File temp = File.createTempFile("upload-", ".attach");
                temp.deleteOnExit();
                OutputStream bout = new FileOutputStream(temp);
                byte[] buffer = new byte[8192];
                int len;
                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
                    bout.write(buffer, 0, len);
                }
                bout.close();
                out.print(name + ":" + temp.getAbsolutePath());
            } // while

        } catch (Exception e) {
            e.printStackTrace();
            out.print(IConstUtil.UPLOADFILEERROR);
        }
        out.close();

    }
}
