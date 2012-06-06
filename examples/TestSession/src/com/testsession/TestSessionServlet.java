package com.testsession;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.testsession.bean.PersonBean;

@SuppressWarnings("serial")
public class TestSessionServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		req.getSession().setAttribute("attrib", "X");
        ApplicationContext ctx = 
                WebApplicationContextUtils.
                      getWebApplicationContext(req.getSession().getServletContext());
        PersonBean person = (PersonBean) ctx.getBean("person");
        Long id = person.getId();
        id++;
        person.setId(id);
		resp.getWriter().println("id=" + id);          
	}
}
