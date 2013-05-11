package com.ejbjsp.servlet;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sample.ejbcalc.IHelloCalc;
import com.sample.ejblocator.EJBSampleLocator;

@WebServlet("/Servlet1")
public class Servlet1 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private IHelloCalc i = null;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        if (i == null)
            try {
                i = EJBSampleLocator.construct();
            } catch (NamingException e1) {
                throw new ServletException(e1);
            }
        response.getOutputStream().println("<H1> Servlet1 response </H1");
        response.getOutputStream().println("<H1> Bean greetings " + i.getHello() + "</H1>");
        response.getOutputStream().println("<H1> Ask Bean. What is 2 + 2 ? Bean's advice: " + i.add(2, 2) + " ! </H1>");
        response.getOutputStream().flush();
    }

}
