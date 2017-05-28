<%@page import="entities.Comment"%>
<%@page import="entities.Uploads"%>
<%@page import="beans.UserCRUD"%>
<%@page import="entities.TaskSummary"%>
<%@page import="beans.RemoteAPI"%>
<%@page import="org.kie.api.task.TaskService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entities.User"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page import="javax.ejb.EJB"%>
<%@page import="java.util.List"%>
<%@page import="beans.UserRemote"%>
<%@page import="javax.naming.InitialContext"%>
<jsp:include page="header.jsp" />
    <div class="container">
        <% 
                    
        User user=(User)session.getAttribute("user");
        List<User> users=(List<User>)request.getAttribute("userlist");
        String status = (String) request.getAttribute("status");
        String taskId = (String) request.getAttribute("taskId");
        
        %> 
        
    <div id="content" class="row" style="margin-top: 30px;">
    <div class="col-md-3">
    
        <ul class="list-group">
        <h3><%=status%>:</h3>
        <%
                   List<TaskSummary> tasks=(List<TaskSummary>)request.getAttribute("tasks");
                   for (TaskSummary task :tasks){
        %>
        
        <li class="list-group-item"><a href="#" onclick="getTask(<%=task.getTaskId() %>)"><%=task.getName()+": "+task.getDescription() %>  </a></li>
        <% } %>
        </ul>
      </div>
        <div class='col-md-9'>
            <div id="infoshow" style="margin-top:30px;"><h3>Please choose some task</h3> </div>
        
        <%
                   
                   for (TaskSummary task :tasks){
        %>
        <div id="task<%=task.getTaskId() %>"  style="display: none; width: 500px;">
                <h3>Task Details:</h3>
               <div class="taskdetailsdiv">
                <p class="taskdetails"> Task Name: </p>  <%=task.getName() %> <br>
                <p class="taskdetails"> Task Description: </p>  <%=task.getDescription() %> <br>
                <p class="taskdetails"> Process Name: </p>  <%=task.getProcessInstance().getProcessName() %> <br>
                <p class="taskdetails"> Process Owner: </p>  <%=task.getProcessInstance().getIdentity() %> <br>
               </div>
               <div class="taskdetailsdiv">
                <p class="taskdetails"> Attachments: </p> 
                <% for (Uploads upload: task.getProcessInstance().getUploads()){
                    
                %>
                <a href="FileUploadHandler?file=<%=upload.getNamePath()%>"><%=upload.getNamePath()%> </a> 
                <%
                } %>
               </div>
               <div class="actions">
                   <% if (task.getStatus().equals("Ready")){ %>
                <a href="task?act=claim&id=<%=task.getTaskId()%>&login=<%=task.getProcessInstance().getIdentity() %>" class="btn btn-default">Claim</a>
                <% } %>
                <% if (task.getStatus().equals("Reserved")){ %>
                <a href="task?act=start&id=<%=task.getTaskId() %>&login=<%=task.getProcessInstance().getIdentity() %>"class="btn btn-default">Start</a>
                <% } %>
                <% if (task.getStatus().equals("InProgress")){ %>
                <a href="task?act=complete&id=<%=task.getTaskId() %>&login=<%=task.getProcessInstance().getIdentity() %>" class="btn btn-default">Complete</a>   <% } %>
                    <%
                            if (user.isBoss() && task.getStatus().equals("Ready")){
                    %>           
                <input type="button" class="btn btn-default" value="Delegate" data-toggle="modal" data-target="#delegate_modal<%=task.getTaskId() %>"></td>
                <div class="modal fade" id="delegate_modal<%=task.getTaskId()%>" role="dialog">
                    <div class="modal-dialog">
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
                    <% } %>                     

               </div>
                    <div class="comments">     
                    <h4>Comments: </h4>
                    <ul class="list-group">
                    <%for (Comment c: task.getComments()){ %>
                         <%=c.getUser().getName()+" "+c.getUser().getSurname() %>:
                         <li class="list-group-item">
                            <%=c.getText()%>
                        </li>
                    
                    <% } %>                     
                    </ul>
                                    <form action="task" method="post">
                                          Add Comment: <br>
                                         <textarea class="form-control" rows="3" name="text"></textarea> 
                                         <input type="hidden" name="act" value="addcomment">
                                         <input type="hidden" name="userid" value="<%=user.getId() %>">
                                         <input type="hidden" name="taskid" value="<%=task.getTaskId() %>">
                                         <input type="hidden" name="login" value="<%=task.getProcessInstance().getIdentity() %>">
                                         <br>
                                         <input type="submit" value="Add" class="btn btn-default"  id="submit_button" success_or_not="0">
                                    </form>
                    </div>
            </div>
        <% } %>
        </div>
    </div>
    </div>                     
         
     <script>
function getTask(id) {
    document.getElementById("infoshow").style.display = "none";
   document.getElementById("task"+id).style.display = "inline-block";
}   
</script>  
<jsp:include page="footer.jsp" />
  