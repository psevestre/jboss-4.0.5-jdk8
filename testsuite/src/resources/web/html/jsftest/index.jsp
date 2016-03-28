<html>
   <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
   <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
   <%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>
   
   <f:view>
      <head>          
         <title><h:outputText value="Hello from JSF"/></title>
      </head>
      <body>
         <h:outputText value="Hello from JSF"/>
         <br><br>
         
<!-- Generate the HTML for the custom JSF component, but don't try to render the chart -->
<!-- This tests that TLD's can be read from WEB-INF/lib/*.jar                          -->
         <d:chart width="10" height="10" type="PieChart">
             <d:chartItem itemColor="blue" itemLabel="foo" itemValue="10" />
         </d:chart>  

      </body>
   </f:view>
</html>