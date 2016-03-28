<%@page contentType="text/html"
   import="java.util.*, javax.servlet.ServletContext, org.jboss.test.cluster.web.aop.Person,
   org.jboss.test.cluster.web.aop.Address, java.security.SecureRandom"
%>

<% 
   // Modify the POJO that was bound to the servlet context and
   // to the session as well.  Only access it via the servlet context
   // so we can check whether modifying it causes the session
   // to be replicated.
   ServletContext ctx = getServletConfig().getServletContext();
   Person ben = (Person)ctx.getAttribute("TEST_PERSON");
   ben.setAge(new SecureRandom().nextInt(100));
%>
