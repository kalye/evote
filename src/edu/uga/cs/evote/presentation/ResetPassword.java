package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.ElectionsOfficerCtrl;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.logic.impl.VoterCtrl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class ResetPassword extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7650008624164994001L;

	public void doPost( HttpServletRequest  request, HttpServletResponse response ){
		try{


			Class.forName("com.mysql.jdbc.Driver").newInstance();

			response.setContentType("text/html");

			String email = request.getParameterValues("email")[0];
			Connection  conn = null;

			// get a database connection
			try {
				conn = DbUtils.connect();
			} 
			catch (Exception seq) {
				System.err.println( "DeleteTest: Unable to obtain a database connection" );
			}

			if( conn == null ) {
				System.out.println( "DeleteTest: failed to connect to the database" );
				return;
			}

			LogicLayer logicLayer = new LogicLayerImpl(conn);
			String redirect = "";
			List<Voter> voters;
			User user = null;
			try{

				voters = logicLayer.findAllVoters();
				user = checkEmailExists(voters, email);
				if(user != null){
					updateUserPassword(user, conn);
					sendEmailWithNewPassword(request, response, user);
					return;
				}
				List<ElectionsOfficer> electionOfficers = logicLayer.findAllElectionsOfficers();
				user = checkEmailExists(electionOfficers, email);
				if(user != null){
					updateUserPassword(user, conn);
					sendEmailWithNewPassword(request, response, user);
					return;
				}

				redirect = new String("wrongEmail.html");
				response.sendRedirect(redirect);  
				conn.close();
				return;

			} catch(EVException e){
				redirect = new String("wrongEmail.html");
				response.sendRedirect(redirect);  
				conn.close();
				return;
			}

		} catch(Exception e){

			System.out.println(e.getMessage());
		}//try
	}

	private void updateUserPassword(User user, Connection conn) throws EVException {
		ObjectLayer objectLayer = null;
        PersistenceLayer persistence = null;
        objectLayer = new ObjectLayerImpl();
        // obtain a reference to Persistence module and connect it to the ObjectModel        
        persistence = new PersistenceLayerImpl( conn, objectLayer ); 
        // connect the ObjectModel module to the Persistence module
        objectLayer = new ObjectLayerImpl(persistence);
        persistence.setObjectLayer(objectLayer);
		String password = RandomStringUtils.randomAlphanumeric(6);
		user.setPassword(password);
		if(user instanceof Voter){
			new VoterCtrl( objectLayer).updateVoter((Voter)user);
		} else {
			new ElectionsOfficerCtrl( objectLayer).updateElectionOfficer((ElectionsOfficer)user);
		}

	}

	private void sendEmailWithNewPassword(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {

		String from = "UGAClassof2017Team4@gmail.com";
		String password = "team42017";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});
		response.setContentType("text/html");
		try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(user.getEmailAddress()));
			// Set Subject: header field
			message.setSubject("Password Change Notification!");
			String msgString ="Hi " + user.getFirstName() + ",\n\n\n"
					+ "Your new password change to: \n"
					+ "Password: " + user.getPassword() + "\n"
				    + "User Name: " + user.getUserName() + "\n\n\n\n"
				    + "Thanks, "
				    + "\n\n\n\n\n"
				    + "Password Admin\n";
			message.setText(msgString);
			// Send message
			Transport.send(message);
			String redirect = new String("successAnon.html");
			response.sendRedirect(redirect);  

		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	private User checkEmailExists(List<? extends User> users, String email) {
		if(users != null && !users.isEmpty()){
			for(User user: users){
				if(user.getEmailAddress().equals(email)){
					return user;
				}
			}
		}
		return null;
	}

}
