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
<!DOCTYPE html>
<html>
      
        <% 
        User u=(User)session.getAttribute("user");
        %>        
   

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= u.getName() %> <%= u.getSurname() %> </title>
    </head>
    <body>
    
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
    
<div class="occasions">
    <h2> Tasks </h2>

    <div class="col-md-3">
        <div class="tasks_for_hr">
            <h3>Tasks for HR:</h3>
            <a href="tasks.jsp?sort=potential" class="col-md-12">
                <div class="number">
                    <span><%=tasks.size()%></span>
                </div>
            </a>
        </div>
    </div>

    <div class="col-md-3">
        <div class="tasks_for_me">
            <h3>Tasks for me: <%=formeReady+formeinProgress %> </h3>
            <a href="tasks.jsp?sort=reserved" class="col-md-6 not_accepted">
                <div class="number">
                    <span><%=formeReady %></span>
                </div>
                <p>Ready</p>
            </a>
            <a href="tasks.jsp?sort=inprogress" class="col-md-6">
                <div class="number overdue">
                    <span><%=formeinProgress %></span>
                </div>
                <p>In process</p>
            </a>
        </div>
    </div>

    <div class="col-md-3">
        <div class="history">
            <h3>Task history:</h3>
            <a href="tasks.jsp?sort=completed" class="col-md-12">
                <div class="number">
                    <span><%=task4me.size()-formeinProgress-formeReady%></span>
                </div>
            </a>
        </div>
    </div>

    <div class="col-md-3">
        <div class="calendar">
            <h3>Calendar: 27</h3>
            Thursday
        </div>
    </div>

    <div class="col-md-3"></div>
    <div class="col-md-3"></div>
</div>

    </body>
</html>
