/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.RemoteAPI;
import beans.SendMail;
import beans.UserRemote;
import entities.Comment;
import entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
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
                response.sendRedirect("tasks?&result="+0);
                break;
              case "delegate":
                User under = bean.getUserByLogin(request.getParameter("to"));
                api.claimTask(under, request.getParameter("id"));;
                email = bean.getUserByLogin(request.getParameter("login")).getEmail();
                Runnable delegate= new SendMail(email,"Your task: "+ request.getParameter("id")+" was delegated to "+under.getName()+" "+under.getSurname()+", by "+user.getName()+" "+user.getSurname(),"Task status changed");
                new Thread(delegate).start(); 
                response.sendRedirect("tasks?&result="+0);
                break;    
            case "start":
                api.startTask(user, request.getParameter("id"));
                email = bean.getUserByLogin(request.getParameter("login")).getEmail();
                Runnable start= new SendMail(email, "Your task: "+ request.getParameter("id")+" was started by "+user.getName()+" "+user.getSurname(),"Task status changed");
                new Thread(start).start(); 
                response.sendRedirect("tasks?sresult=0");
                break;
            case "complete":
                api.completeTask(user, request.getParameter("id"));
                email = bean.getUserByLogin(request.getParameter("login")).getEmail();
                Runnable complete= new SendMail(email, "Your task: "+ request.getParameter("id")+" was completed by "+user.getName()+" "+user.getSurname(),"Task status changed");
                new Thread(complete).start(); 
                response.sendRedirect("tasks?&result="+0);
                break;
            case "addcomment":
                email = bean.getUserByLogin(request.getParameter("login")).getEmail();
                Date date = new Date();
                Comment c= new Comment();
                c.setCreatedAt(date);
                c.setTaskId(Long.parseLong(request.getParameter("taskid")));
                c.setText(request.getParameter("text"));
                c.setUser(user);
                bean.addComment(c);
                Runnable addcomment= new SendMail(email, "You have new comment",user.getName()+" "+user.getSurname()+" added new comment:" + request.getParameter("text"));
                new Thread(addcomment).start(); 
                response.sendRedirect("tasks?&result="+0);
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
