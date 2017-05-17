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
        <title>User List</title>
    </head>
    <div class="container">
        <% 
        User user=(User)session.getAttribute("user");
        %> 
    <body>
       <%        InitialContext ctx = new InitialContext();
     
        %>
        <h1>Ready:</h1>
        <table class="table">
                <tr>
                    <th>TaskId</th>
                    <th>Task Name</th>
                    <th>Task Variable</th>
                    <th>Process Name</th>
                    <th>Owner</th>
                    <th>Action</th>
                </tr>
                
        <%
                   RemoteAPI api= (RemoteAPI)ctx.lookup("rest");
                   List<TaskSummary> tasks=api.getPotentialTasks(user);
                   for (TaskSummary task :tasks){
        %>
                <tr>
                   <td><%=task.getTaskId()%> </td>
                   <td><%=task.getName()%></td>
                   <td><%=task.getDescription()%></td>
                   <td><%=task.getProcessInstance().getProcessName()%></td>
                   <td><%=task.getProcessInstance().getIdentity()%></td>
                   <td> <a href="task?act=claim&id=<%=task.getTaskId()%>&login=<%=task.getProcessInstance().getIdentity() %>">Claim</a></td>
               </tr>    
       
        <% }%>
        </table>
        
        <h1>Reserved:</h1>
        <table class="table">
            
                <tr>
                    <th>TaskId</th>
                    <th>Task Name</th>
                    <th>Task Variable</th>
                    <th>Process Name</th>
                    <th>Owner</th>
                    <th>Action</th>
                </tr>
        <%
            List<TaskSummary> mytasks=api.getTasks(user);
            for (TaskSummary task :mytasks)
            {
              if (task.getStatus().equals("Reserved"))
              {
        %>
               <tr>
                   <td><%=task.getTaskId()%> </td>
                   <td><%=task.getName()%></td>
                   <td><%=task.getDescription()%></td>
                   <td><%=task.getProcessInstance().getProcessName() %> </td>
                   <td><%=task.getProcessInstance().getIdentity() %></td>
                   <td> <a href="task?act=start&id=<%=task.getTaskId() %>&login=<%=task.getProcessInstance().getIdentity() %> ">Start</a></td>
                   
               </tr>    
        
        <%    }
            }
        %>    
        </table>
        
        
        <h1>In Progress:</h1>
        <table class="table">
            
                <tr>
                    <th>TaskId</th>
                    <th>Task Name</th>
                    <th>Task Variable</th>
                    <th>Process Name</th>
                    <th>Owner</th>
                    <th>Action</th>
                </tr>
        <%
            List<TaskSummary> inprogress=api.getTasks(user);
            for (TaskSummary task :inprogress)
            {
              if (task.getStatus().equals("InProgress"))
              {
        %>
               <tr>
                   <td><%=task.getTaskId()%> </td>
                   <td><%=task.getName()%></td>
                   <td><%=task.getDescription()%></td>
                   <td><%=task.getProcessInstance().getProcessName() %> </td>
                   <td><%=task.getProcessInstance().getIdentity() %></td>
                   <td> <a href="task?act=complete&id=<%=task.getTaskId() %>&login=<%=task.getProcessInstance().getIdentity() %> ">Complete</a></td>
                   
               </tr>    
        
        <%    }
            }
        %>    
        </table>
    </body>
    </body>
</html>
