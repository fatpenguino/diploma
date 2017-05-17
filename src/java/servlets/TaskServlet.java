/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.RemoteAPI;
import beans.SendMail;
import beans.UserRemote;
import entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fatpenguino
 */
@WebServlet(name = "TaskServlet", urlPatterns = {"/task"})
public class TaskServlet extends HttpServlet {
@EJB
UserRemote bean;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NamingException {
        response.setContentType("text/html;charset=UTF-8");
        InitialContext ctx = new InitialContext();
        RemoteAPI api= (RemoteAPI)ctx.lookup("rest");
        User user=(User)request.getSession().getAttribute("user");
        String action = request.getParameter("act");
        String email="";
        switch (action) {
            case "claim":
                api.claimTask(user, request.getParameter("id"));
                email = bean.getUserByLogin(request.getParameter("login")).getEmail();
                Runnable claim= new SendMail(email,"Your task: "+ request.getParameter("id")+" was claimed by "+user.getName()+" "+user.getSurname(),"Task status changed");
                new Thread(claim).start(); 
                response.sendRedirect("tasks.jsp?&result="+0);
                break;
            case "start":
                api.startTask(user, request.getParameter("id"));
                email = bean.getUserByLogin(request.getParameter("login")).getEmail();
                Runnable start= new SendMail(email, "Your task: "+ request.getParameter("id")+" was started by "+user.getName()+" "+user.getSurname(),"Task status changed");
                new Thread(start).start(); 
                response.sendRedirect("tasks.jsp?sresult=0");
                break;
            case "complete":
                api.completeTask(user, request.getParameter("id"));
                email = bean.getUserByLogin(request.getParameter("login")).getEmail();
                Runnable complete= new SendMail(email, "Your task: "+ request.getParameter("id")+" was completed by "+user.getName()+" "+user.getSurname(),"Task status changed");
                new Thread(complete).start(); 
                response.sendRedirect("tasks.jsp?&result="+0);
                break;
            default:
                break;
        }
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
            Logger.getLogger(TaskServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TaskServlet.class.getName()).log(Level.SEVERE, null, ex);
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
