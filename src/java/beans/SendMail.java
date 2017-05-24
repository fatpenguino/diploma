/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.PotentialOwner;
import entities.TaskSummary;
import entities.User;
import java.util.List;
import java.util.Properties;
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
public class SendMail implements Runnable 
{    
    private final String to;
    private final String title;
    private final String message;
    
     public SendMail( String to, String message, String title){
     this.to=to;
     this.title=title;
     this.message=message;
     }
     @Override
     public void run(){
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
               mess.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
               mess.setSubject(title);    
               mess.setText(message);    
               //send message  
               Transport.send(mess);    
               System.out.println("message sent successfully");    
              } catch (MessagingException e) {throw new RuntimeException(e);}    

     }
     
    
}
