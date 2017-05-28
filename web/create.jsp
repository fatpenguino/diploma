<%@page import="entities.ProcessInstance"%>
<%@page import="entities.ProcessDefinition"%>
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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="container"> 
        <h1>Available Processes:</h1>
        <table class="table">
            <th>Process Id</th>
            <th>Process Name</th>
            <th>Process Details</th>
            <th>Action</th>
            
        <%
            User user=(User)session.getAttribute("user");
            InitialContext ctx = new InitialContext();
            RemoteAPI api= (RemoteAPI)ctx.lookup("rest");
                   List<ProcessDefinition> processdefs=api.getProcessDefinitions(user);
                   for (ProcessDefinition processdef :processdefs){
                   processdef.setId(processdef.getId().replace("guvnor-asset-management.",""));
                   processdef.setDeploymentId(processdef.getDeploymentId().replace("org.guvnor:guvnor-asset-mgmt-project:6.3.0.Final", "Template"));
        %>
               <tr>
                   <td><%=processdef.getId() %></td>
                   <td><%=processdef.getName() %> </td>
                   <td><%=processdef.getDeploymentId() %></td>
                  
                   <td><input type="button" class="btn" value="Start" data-toggle="modal" data-target="#start_modal<%=processdef.getId() %>"></td>

                      <div class="modal fade" id="start_modal<%=processdef.getId()%>" role="dialog">
                        <div class="modal-dialog">
                        <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Candidate data:</h4>
                                </div>
                                <div class="modal-body">
                                    <form action="ProcessServlet" method="post">
                                        Candidate name: <input type="text" name="param">
                                         <input type="hidden" name="action" value="start">
                                         <input type="hidden" name="processid" value="<%=processdef.getId() %>">
                                         <div class="modal-footer">
                                         <input type="submit" value="Start" class="btn btn-default"  id="submit_button" success_or_not="0">
                                         </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                      </div>
                    
               </tr>    
       
        <% }%>
        </table>
</div>