package com.testjsp.project;
import java.io.IOException;

import org.apache.taglibs.standard.tag.common.fmt.BundleSupport;

import javax.servlet.http.*;


@SuppressWarnings("serial")
public class Test_jspServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Class cl = BundleSupport.class;
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
