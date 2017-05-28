<%-- 
    Document   : profile
    Created on : 08.02.2016, 21:50:21
    Author     : Feisal
--%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DateFormat"%>
<%@page import="entities.TaskSummary"%>
<%@page import="beans.RemoteAPI"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page import="java.util.List"%>
<%@page import="beans.UserRemote"%>
<%@page import="entities.User"%>
<%@page import="javax.naming.InitialContext"%> 

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />
        <% 
        User u=(User)session.getAttribute("user");
        %>        
   

        <% 
            InitialContext ctx = new InitialContext();
            RemoteAPI api= (RemoteAPI)ctx.lookup("rest");
            List<TaskSummary> tasks=api.getPotentialTasks(u);
            List<TaskSummary> task4me=api.getTasks(u);
            int formeReady=0;
            int formeinProgress=0;
            for (TaskSummary t : task4me){
                if (t.getStatus().equals("Reserved") ){
                formeReady++;
                }
                else if (t.getStatus().equals("InProgress")){
                formeinProgress++;
                }
            }
                          
%>
    
<div class="occasions container">

    <h2 style="margin-left: 15px;"> Tasks </h2>
    
    <div class="col-md-3 mtb">
        <div class="tasks_for_hr">
            <h3>Tasks for <%=u.getGroups()%>:</h3>
            <a href="tasks?sort=ready" class="col-md-12">
                <div class="number">
                    <span><%=tasks.size()%></span>
                </div>
            </a>
        </div>
    </div>

    <div class="col-md-3 mtb">
        <div class="tasks_for_me">
            <h3>Tasks for me: <%=formeReady+formeinProgress %> </h3>
            <a href="tasks?sort=reserved" class="col-md-6 not_accepted">
                <div class="number">
                    <span><%=formeReady %></span>
                </div>
                <p>Ready</p>
            </a>
            <a href="tasks?sort=inprogress" class="col-md-6">
                <div class="number overdue">
                    <span><%=formeinProgress %></span>
                </div>
                <p>In process</p>
            </a>
        </div>
    </div>

    <div class="col-md-3 mtb">
        <div class="history">
            <h3>Task history:</h3>
            <a href="taskhistory" class="col-md-12">
                <div class="number">
                    <span><%=task4me.size()-formeinProgress-formeReady%></span>
                </div>
            </a>
        </div>
    </div>

    <div class="col-md-3">
        <div class="calendar">
            <h3>Calendar:</h3>
            <h4 style="text-align: center;">Thursday, 25 March</h4> 
        </div>
    </div>

</div>

<jsp:include page="footer.jsp" />