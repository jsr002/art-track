package com.articleTracking.email.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
	 
    public static void sendEmail(String toAddressList,String sub,String msg){  
          //Get properties object    
          Properties props = new Properties(); 
          props.put("mail.smtp.host", "smtp.gmail.com");    
          props.put("mail.smtp.socketFactory.port", "465");    
          props.put("mail.smtp.socketFactory.class",    
                    "javax.net.ssl.SSLSocketFactory");    
          props.put("mail.smtp.auth", "true");    
          props.put("mail.smtp.port", "465"); 
          
          String[] toList = toAddressList.split(",");
          
          
         /* props.put("mail.smtp.host", "mail.srojonline.org");    
          props.put("mail.smtp.socketFactory.port", "25");    
          props.put("mail.smtp.socketFactory.class",    
                    "javax.net.ssl.SSLSocketFactory");    
          props.put("mail.smtp.auth", "true");    
          props.put("mail.smtp.port", "25");  */
          //get Session   ------ "submission@srojonline.org","Omics@123"
          
          Session session = Session.getDefaultInstance(props,    
           new javax.mail.Authenticator() {    
           protected PasswordAuthentication getPasswordAuthentication() {    
           return new PasswordAuthentication("myarticletracking@gmail.com","Tracking@1");  
           }    
          });    
          //compose message    
          try {    
           MimeMessage message = new MimeMessage(session);  
           for(String to : toList){
        	   message.addRecipient(Message.RecipientType.TO,new InternetAddress(to)); 
           }
             
           message.setSubject(sub);    
           message.setText(msg);    
           //send message  
           Transport.send(message);    
           System.out.println("message sent successfully");    
          } catch (MessagingException e){
        	  System.out.println("Exception Occurred"+e);
        	  throw new RuntimeException(e);
        	  
        }    
             
    }  

    


}
