/**
 * TODO This is the class description
 */


package Server;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import models.Envelope;
import models.Patient;



// TODO: Auto-generated Javadoc
/**
 * The Class mysqlConnection.
 *
 * @author G5 lab group
 * MySQL Connection Class
 */
public class mysqlConnection {
	
	/** The auto Connection. */
	public static Connection conn,autoConn; //TODO - will be changed (maby) to private	
	
	/** The temporary mysqlConnection. */
	public mysqlConnection temp;
	
	/** The Server view. */
	private ServerGui ServerView;
	
	/** The server log view. */
	private serverLogGui serverLogView;
	
	/** The user log. */
	private  ArrayList<String> userLog;
	
	/** The user name db. */
	private static String userNameDB = "root";
	
	/** The password db. */
	private static String passwordDB = "1234";
	
	/** The Defport. */
	private static String Defport = "5555";
	
	/** The port. */
	private static int port = 0;
	
	/** The Scheama. */
	private String Scheam = "jdbc:mysql://localhost/ghealth";
	
	/** The Server. */
	public Server sv;
	
	/** The Automation. */
	public Automation auto = null;
	
	/**
	 * Default constructor, sets conn DB connector.
	 */
	public mysqlConnection() 
	{
		System.out.println("mysqlConnection() constructor");
		
		try 
		{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}
        
        try 
        {
        	this.conn = DriverManager.getConnection(Scheam,userNameDB,passwordDB);
            //this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ghealth","root","a4m3i2r1");
            //Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
            System.out.println("SQL connection succeed");
           
    		
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
   	}
	
	/**
	 * constructor.
	 *
	 * @param SerGui is the start gui that open first when we open
	 * the server - need to field port user name and password of workbench
	 * @param servLog show when client connect or disconnect to server
	 */

	public mysqlConnection(ServerGui SerGui,serverLogGui servLog) 
	{
		
		ServerView = SerGui;
		serverLogView = servLog;
		temp = this;
		ServerView.setTextFieldPass(passwordDB);
		ServerView.setTextFieldUser(userNameDB);
		ServerView.setTextFieldPort(Defport);
		ServerView.setTextFieldscheam(Scheam);
		userLog = new ArrayList<String>();
		
		ServerView.addLoginActionListener(new LoginListener());
		serverLogView.addDisconnectedBottonActionListener(new DisconnectedListener());

   	}
	
	  /**
  	 * Inner class that handles when Button Logout Pressed, implements ActiontListener.
  	 *
  	 * @see DisconnectedEvent
  	 */
	class DisconnectedListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			 System.out.println("Connection closed, program terminated");
		System.exit(0);
			
		}
	
	}
	
	/**
	 *  Inner class that handles when Button Login Pressed, implements ActiontListener.
	 *
	 * @see LoginEvent
	 */
	class LoginListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			userNameDB = ServerView.getTextUserName();
			passwordDB = ServerView.getTextPassword();
			Defport = ServerView.getTextPort();
			port = Integer.parseInt(Defport);
			Scheam = ServerView.getTextScheam();
			
			System.out.println("GUI ACTION SERVER LOGIN TRY...");
			
			if(openConnectionDB()){
				
				 //sv = new Server(port);
				 
				   try 
				    {
					  //sv.start();
				      ServerView.dispose();
				      serverLogView.setVisible(true);
				      
				    } 
				    catch (Exception ex) 
				    {
				    	 ServerView.setWarningMessageVisibleTrue("ERROR - Could not listen for clients!");
				    }
				
			}
			
			
			
		}
	
	}
	
	
	/**
	 * openConnectionDB is method that check if the open Connection to DB.
	 *
	 * @return boolean
	 */
	  public boolean openConnectionDB(){
		  
			
			try 
			{
	          Class.forName("com.mysql.jdbc.Driver").newInstance();
	      } catch (Exception ex) {/* handle the error*/
	    	  System.out.println("Failed to open com.mysql.jdbc.Driver...");
	      }
	      
	      try 
	      {
	           this.conn = DriverManager.getConnection(Scheam,userNameDB,passwordDB);
	          //Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
	          System.out.println("SQL connection succeed");
	          /** Automated machine connector**/
	          this.autoConn = DriverManager.getConnection(Scheam,userNameDB,passwordDB);
	          System.out.println("SQL connection for Auto succeed");
	          
	            /** Automated machine Start**/
	        	auto = new Automation();
	    		System.out.println("Automated machine has launched... *\\0/*");
	    		auto.run();
	    		/**                 **/
	    		
	          return true;
	          
	   	} catch (SQLException ex) 
	   	    {/* handle any errors*/
	          ServerView.setWarningMessageVisibleTrue("SQLException: " + ex.getMessage());
	          ServerView.setWarningMessageVisibleTrue("SQLState: " + ex.getSQLState());
	          ServerView.setWarningMessageVisibleTrue("VendorError: " + ex.getErrorCode());
	          return false;
	          }
		  
	}
		
		/**
		 * set the new log of Employee that connect or disconnect to server.
		 *
		 * @param e1 the e1
		 * @param Task the task
		 */
	  public void SetLog(String e1, String Task){
			ZonedDateTime zonedDateTime = ZonedDateTime.now();
	        if(Task.equals("login")){
	        	userLog.add(e1);
	        	serverLogView.getTextArea().setForeground(Color.green);
	        	serverLogView.getTextArea().append(zonedDateTime.toLocalTime()+" : " +" User " + e1 + "- connected\n");	
	        }
	        if(Task.equals("loginTry")){
	        	userLog.add(e1);
	        	serverLogView.getTextArea().setForeground(Color.red);
	        	serverLogView.getTextArea().append(zonedDateTime.toLocalTime()+" : " +" User " + e1 + "- Tried to log in\n");	
	        }
		
	        
	        if(Task.equals("Register")){
	        	serverLogView.getTextArea().setForeground(Color.red);
	        	serverLogView.getTextArea().append(zonedDateTime.toLocalTime()+" : " +e1 + "\n");	
	        	userLog.remove(e1);
	        
	        }	
	        
	        
	        if(Task.equals("logout")){
	        	serverLogView.getTextArea().setForeground(Color.red);
	        	serverLogView.getTextArea().append(zonedDateTime.toLocalTime()+" : " +" User " + e1 + "- Disconnected\n");	
	        	userLog.remove(e1);
	        
	        }	
			
		}
	
	
	/**
	 * **********************************************Getters and setters**************************************.
	 *
	 * @return the server view
	 */
	public ServerGui getServerView() {
		return ServerView;
	}

	/**
	 * Sets the server view.
	 *
	 * @param serverView the new server view
	 */
	public void setServerView(ServerGui serverView) {
		ServerView = serverView;
	}

	/**
	 * Gets the server log view.
	 *
	 * @return the server log view
	 */
	public serverLogGui getServerLogView() {
		return serverLogView;
	}

	/**
	 * Sets the server log view.
	 *
	 * @param serverLogView the new server log view
	 */
	public void setServerLogView(serverLogGui serverLogView) {
		this.serverLogView = serverLogView;
	}
	
	 /**
 	 * Sets the password db.
 	 *
 	 * @param password1 the new password db
 	 */
 	public void setPasswordDB(String password1) {
			this.passwordDB = password1;
		}
	
	/**
	 * Sets the user name db.
	 *
	 * @param userName the new user name db
	 */
	public void setUserNameDB(String userName) {
		this.userNameDB = userName;
	}
	

}


