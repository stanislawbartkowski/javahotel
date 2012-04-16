<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="pl" />
<fmt:setBundle basename="localize.default"/>
<%@ page import="com.jsp.util.localize.GetLocalizedMessage" %>
 
<%
//  Locale loc = new Locale("pl");
  String hello = GetLocalizedMessage.getMessage(pageContext, "Hello");
  String helloJohn = GetLocalizedMessage.getMessage(pageContext, "HelloName","John");
%>
 
<!DOCTYPE html>
<html>
 <head>
  <title>Hello</title>
 </head>
<body>
    <fmt:message key="Hello">
    </fmt:message>
  
  <%=hello%>
  <%=helloJohn%>
 
</body>
</html>