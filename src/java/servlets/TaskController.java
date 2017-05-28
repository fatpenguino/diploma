/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.RemoteAPI;
import beans.UserRemote;
import entities.TaskSummary;
import entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fatpenguino
 */
public class TaskController extends HttpServlet {
        @EJB
        UserRemote bean;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NamingException {
        InitialContext ctx = new InitialContext();
        RemoteAPI api= (RemoteAPI)ctx.lookup("rest");
        User user=(User)request.getSession().getAttribute("user");
        List<User> users=bean.getUsers();
        request.setAttribute("userlist", users);
        String sort=request.getParameter("sort");
        List<TaskSummary> taskresult=new ArrayList<>();
            
        switch (sort){
            case "ready" :
             List<TaskSummary> tasks=api.getPotentialTasks(user);   
             request.setAttribute("tasks", tasks);
             request.setAttribute("status", "Ready");
             break;
            case "reserved": 
            List<TaskSummary> mytasks=api.getTasks(user);
            taskresult=new ArrayList<>();
             for (TaskSummary task :mytasks)
            {
              if (task.getStatus().equals("Reserved"))
              {
            taskresult.add(task);
              }
            }
            request.setAttribute("tasks", taskresult);
            request.setAttribute("status", "Reserved");
            break;
            case "inprogress":
            List<TaskSummary> mytasksi=api.getTasks(user);
            taskresult=new ArrayList<>();
             for (TaskSummary task :mytasksi)
            {
              if (task.getStatus().equals("InProgress"))
              {
            taskresult.add(task);
              }
            }
            request.setAttribute("tasks", taskresult);
            request.setAttribute("status", "Inprogress");
            break;    
        }
        RequestDispatcher rd=request.getRequestDispatcher("tasks.jsp");  
            rd.forward(request, response);  
        
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            try {
                processRequest(request, response);
            } catch (NamingException ex) {
                Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            try {
                processRequest(request, response);
            } catch (NamingException ex) {
                Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
