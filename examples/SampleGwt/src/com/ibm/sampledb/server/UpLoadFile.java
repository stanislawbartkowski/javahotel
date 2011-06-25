package com.ibm.sampledb.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UpLoadFile extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        ServletFileUpload upload = new ServletFileUpload();

        try{
            FileItemIterator iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();

                String name = item.getFieldName();
                InputStream stream = item.openStream();
                System.out.println(name);
                


                // Process the input stream
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int len;
                byte[] buffer = new byte[8192];
                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
//                	for (int i=0; i<10 && i<buffer.length; i++) {
//                		System.out.print((char)buffer[i] + " ");
//                	}
                    out.write(buffer, 0, len);
                }
                System.out.println("No of bytes received: " + out.size());
                
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
