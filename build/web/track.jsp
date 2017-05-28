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
<div class="container">        <h1>In progress:</h1>
        <table class="table">
        <%  
            User user=(User)session.getAttribute("user");
            InitialContext ctx = new InitialContext();
            RemoteAPI api= (RemoteAPI)ctx.lookup("rest");
            List<ProcessInstance> processInstances=api.getProcessInstances(user);
        for (ProcessInstance processInstance :processInstances)
        {
            if (processInstance.getStatus()==1 && processInstance.getIdentity().equals(user.getLogin()))
            { 
        %>
               <tr>
                   <td><%=processInstance.getId() %></td>
                   <td><%=processInstance.getIdentity() %></td>
                   <td><%=processInstance.getDescription() %></td>
                   <td><%=processInstance.getStart() %></td>
                  
                   <td><form action="ProcessServlet" method="post">
                         <input type="hidden" name="action" value="abort">
                         <input type="hidden" name="processid" value="<%=processInstance.getId() %>">
                         <input type="submit" class="btn" value="Abort">
                       </form>
                   </td>
                   <td><button type="button" class="btn" data-toggle="modal" data-target="#route<%=processInstance.getId()%>">Show Route</button>
                     <!-- Modal -->
                            <div id="route<%=processInstance.getId()%>" class="modal fade" role="dialog">
                                <!-- Modal content-->
                                <button type="button" class="btn btn-default" data-dismiss="modal" style="float:right">X</button>
                                <div style="display: inline-block;">
                                <%=api.getSvgResponse(processInstance)%>
                                </div> 
                                
                           </div>
                   
                   </td> 
                   <td>
                                    <form action="FileUploadHandler" method="post" enctype="multipart/form-data">
                                       
                                        <input type="hidden" name="processId" value="<%=processInstance.getId()%>">
                                       <input type="file" name="file" />
                                        <input type="submit" value="Upload" />
                                    </form>
                    </td>  
               </tr>    
               
        <%}
    }%>
        </table>
</div>