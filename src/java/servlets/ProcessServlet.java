/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.RemoteAPI;
import entities.ProcessDefinition;
import entities.ProcessInstance;
import entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "ProcessServlet", urlPatterns = {"/ProcessServlet"})
public class ProcessServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NamingException {
        response.setContentType("text/html;charset=UTF-8");
        InitialContext ctx = new InitialContext();
        RemoteAPI api= (RemoteAPI)ctx.lookup("rest");
        User user=(User)request.getSession().getAttribute("user");
        String action = request.getParameter("action");
        
        if (action.equals("start")){
        String param=request.getParameter("param");
        String processid=request.getParameter("processid");
        ProcessDefinition pd= api.getProcessDefinition(processid);
        int result= api.startProcess(user, pd, param);
        //api.send(user.getEmail(), "You startet new process","Title of process is"+pd.getName());
        response.sendRedirect("processes.jsp?page=create&result="+result);
      
        }
        
        if (action.equals("abort")){
        String processid=request.getParameter("processid");
        ProcessInstance pi= api.getProcessInstance(processid);
        int result= api.abortProcess(user, pi);
        response.sendRedirect("processes.jsp?page=create&statusofabort="+result);
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
            Logger.getLogger(ProcessServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ProcessServlet.class.getName()).log(Level.SEVERE, null, ex);
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
