<%@page contentType="text/html"
   import="java.util.*, javax.servlet.ServletContext, org.jboss.test.cluster.web.aop.Person,
   org.jboss.test.cluster.web.aop.Address"
%>

<% 
   // Note: The name are hard-coded in the test case as well!!!
   session.removeAttribute("TEST_PERSON");
%>
