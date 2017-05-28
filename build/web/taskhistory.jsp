<%@page import="beans.UserCRUD"%>
<%@page import="entities.TaskSummary"%>
<%@page import="beans.RemoteAPI"%>
<%@page import="org.kie.api.task.TaskService"%>
<jsp:include page="header.jsp" />
<%@page import="java.util.ArrayList"%>
<%@page import="entities.User"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page import="javax.ejb.EJB"%>
<%@page import="java.util.List"%>
<%@page import="beans.UserRemote"%>
<%@page import="javax.naming.InitialContext"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Task Archive</title>
    </head>
    <div class="container">
        <% 
                    
        User user=(User)session.getAttribute("user");
        %> 
    <body>
       <h1>Tasks:</h1>
        <table class="table">
                <tr>
                    <th>TaskId</th>
                    <th>Task Name</th>
                    <th>Task Variable</th>
                    <th>Process Name</th>
                    <th>Owner</th>
                </tr>
               
        <%
            List<TaskSummary> completed=(List<TaskSummary>)request.getAttribute("tasks");
            for (TaskSummary task :completed)
            {
             
        %>
               <tr>
                   <td><%=task.getTaskId()%> </td>
                   <td><%=task.getName()%></td>
                   <td><%=task.getDescription()%></td>
                   <td><%=task.getProcessInstance().getProcessName() %> </td>
                   <td><%=task.getProcessInstance().getIdentity() %></td>
                   
               </tr>    
        
        <%    }
            
        %>    
        </table>
    
    </body>
     <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/comm.js"></script>

</html>
