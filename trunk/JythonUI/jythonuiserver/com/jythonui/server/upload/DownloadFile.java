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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.shared.ICommonConsts;

/**
 * 
 * @author perseus
 */
@SuppressWarnings("serial")
public class DownloadFile extends HttpServlet {

    private static final Logger log = Logger.getLogger(DownloadFile.class
            .getName());

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        String s = request.getParameter(ICommonConsts.BLOBDOWNLOADPARAM);
        MimetypesFileTypeMap mim = new MimetypesFileTypeMap();
        String param[] = s.split(":");
        String filename = param[2];
        String realM = param[0];
        String key = param[1];
        // String e = mim.getContentType(new File(filename));
        String e = mim.getContentType(filename);
        response.setContentType(e);
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + filename + '\"');
        IBlobHandler iBlob = SHolder.getBlobHandler();
        IGetLogMess iLog = SHolder.getM();
        byte[] content = iBlob.findBlob(realM, key);
        if (content == null) {
            String mess = iLog.getMess(IErrorCode.ERRORCODE78,
                    ILogMess.DOWNLOADCANNOTFINDBLOB, filename, realM, key);
            log.severe(mess);
            return;
        }
        ByteArrayInputStream input = new ByteArrayInputStream(content);
        int size = 0;
        try {
            byte[] buffer = new byte[8192];
            int len;
            BufferedOutputStream output = new BufferedOutputStream(
                    response.getOutputStream());
            while ((len = input.read(buffer)) != -1) {
                size += len;
                output.write(buffer, 0, len);
            }
            output.close();
            input.close();
        } catch (IOException e1) {
            String mess = iLog.getMess(IErrorCode.ERRORCODE79,
                    ILogMess.ERRORDOWNLOADCANNOTFINDBLOB, filename, realM, key);
            log.log(Level.SEVERE, mess, e);
        }
        response.setHeader("Content-Length", "" + size);
    }
}