package com.ejbjsp.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sample.ejbcalc.IHelloCalc;

@WebServlet("/Servlet2")
public class Servlet2 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private IHelloCalc i;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        response.getOutputStream().println("<H1> I'm Servlet 2 </H1");
        response.getOutputStream().println("<H1> Bean greetings " + i.getHello() + "</H1>");
        response.getOutputStream().println("<H1> Ask Bean. What is 2 + 2 ? Bean's advice: " + i.add(2, 2) + " ! </H1>");
        response.getOutputStream().flush();
    }

}