<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.*" %>
<fmt:setLocale value="pl" />
<fmt:setBundle basename="localize.default"/>
<%
  String hello = LocaleSupport.getLocalizedMessage(pageContext, "Hello");
  Object params[] = { "John" };
  String helloJohn = LocaleSupport.getLocalizedMessage(pageContext, "HelloName",params);  
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