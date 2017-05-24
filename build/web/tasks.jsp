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
        <title>User List</title>
    </head>
    <div class="container">
        <% 
                    
        User user=(User)session.getAttribute("user");
        List<User> users=(List<User>)request.getAttribute("userlist");
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
                     <%
                     if (user.isBoss()){
                     %>
                    <th>Delegate</th>
                    <% }%>
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
        <%
                if (user.isBoss()){
        %>           
                  <td> <input type="button" class="btn" value="Delegate" data-toggle="modal" data-target="#delegate_modal<%=task.getTaskId() %>"></td>
                            <div class="modal fade" id="delegate_modal<%=task.getTaskId()%>" role="dialog">
                        <div class="modal-dialog">
                        <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Delegate task</h4>
                                </div>
                                <div class="modal-body">
                                    <form action="task" method="post">
                                            Delegate to: <select  name="to">
                                                <% for (User u: users){ if (u.getGroups().equals(user.getGroups()) && !u.getLogin().equals(user.getLogin())){%>
                                                        <option value="<%=u.getLogin()%>"> <%=u.getName()+" "+ u.getSurname() %> </option>>
                                                        <% }} %>    
                                            </select>
                                         <input type="hidden" name="act" value="delegate">
                                         <input type="hidden" name="login" value="<%=task.getProcessInstance().getIdentity() %>">
                                         <input type="hidden" name="id" value="<%=task.getTaskId() %>">
                                         <div class="modal-footer">
                                         <input type="submit" value="Delegate" class="btn btn-default"  id="submit_button" success_or_not="0">
                                         </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                      </div>

                </tr>    
       
        <% } }%>
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
                    <td> <input type="button" class="btn btn-default" value="Add Comment" data-toggle="modal" data-target="#addcomment_modal<%=task.getTaskId() %>"></td>
                            <div class="modal fade" id="addcomment_modal<%=task.getTaskId()%>" role="dialog">
                        <div class="modal-dialog">
                        <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Add comment</h4>
                                </div>
                                <div class="modal-body">
                                    <form action="task" method="post">
                                          Text:
                                          <textarea  name="text">   </textarea>
                                         <input type="hidden" name="act" value="addcomment">
                                         <input type="hidden" name="userid" value="<%=user.getId() %>">
                                         <input type="hidden" name="taskid" value="<%=task.getTaskId() %>">
                                         <input type="hidden" name="login" value="<%=task.getProcessInstance().getIdentity() %>">
                                         <div class="modal-footer">
                                         <input type="submit" value="Add" class="btn btn-default"  id="submit_button" success_or_not="0">
                                         </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                      </div>

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
     <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/comm.js"></script>

</html>
