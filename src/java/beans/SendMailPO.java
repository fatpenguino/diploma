/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import api.REST;
import entities.PotentialOwner;
import entities.TaskSummary;
import entities.User;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 *
 * @author fatpenguino
 */
public class SendMailPO implements Runnable 
{    
    private final String processId;
    private final String title;
    private final String message;
    private final List<User> users;
     public SendMailPO( String processId, String message, String title,List<User> users){
     this.processId=processId;
     this.title=title;
     this.message=message;
     this.users=users;
     }
     @Override
     public void run(){
        
         REST rest=new REST();
         UserCRUD user=new UserCRUD();
         String taskDescription="";
         List<TaskSummary> tasks=rest.getAllTasks();
         String taskId="-1";
         for (TaskSummary task :tasks){
         if (task.getProcessInstance().getId().equals(processId) && task.getStatus().equals("Ready"))
             taskId=task.getTaskId();
             taskDescription=task.getName()+"\n"+task.getDescription();
         }  
         PotentialOwner owner=new PotentialOwner();
         owner=rest.getPotentialOwner(taskId);
         for (User u : users ){
             if (u.getGroups().contains(owner.getId())){
            Properties props = new Properties();    
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
             //get Session   
            Session ses = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("iitu.bpm", "Diploma322");
                }
            });    
              //compose message    
              try {    
               MimeMessage mess = new MimeMessage(ses);    
               mess.addRecipient(Message.RecipientType.TO,new InternetAddress(u.getEmail()));    
               mess.setSubject(title);    
               mess.setText(taskDescription);    
               //send message  
               Transport.send(mess);    
               System.out.println("message sent successfully");    
              } catch (MessagingException e) {throw new RuntimeException(e);}    
             }
         }   
     }
     
     
}
