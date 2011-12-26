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

import com.gwtmodel.table.common.IConstUtil;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author perseus
 */
@SuppressWarnings("serial")
public class DownloadFile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        String filename = request.getParameter(IConstUtil.FILENAMEID);
        String tempfilename = request.getParameter(IConstUtil.TEMPORARYFILENAME);
        MimetypesFileTypeMap mim = new MimetypesFileTypeMap();
        String e = mim.getContentType(new File(filename));
        response.setContentType(e);
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + filename + '\"');
        File f = new File(tempfilename);
        int size = 0;
        try {
            InputStream input = new FileInputStream(f);
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
            e1.printStackTrace();
        }
        response.setHeader("Content-Length", "" + size);
    }
}