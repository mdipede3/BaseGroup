package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import enums.Status;
import enums.task;
import models.Envelope;
import models.Patient;
import models.User;
import models.Clinic;
import GUI.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.Executors;


/**
 * 
 * The Class LoginControl.
 * @author G5 lab group
 */
public class LoginControl {
	
	/** The login g. */
	private LoginGUI loginG;
	
	/** The User login. */
	private User UserLogin;
	
	/** The user_full_name. */
	private static String user_full_name;
	
	/** The u id. */
	private static String uId = null;
	
	/** The clinic. */
	private static Clinic clinic;
	//private User user;
	//private User U;
	
	
	/**
	 * constractor.
	 *
	 * @param lC the l c
	 */
	public LoginControl (LoginGUI lC )
	{
		loginG = lC;
		UserLogin = new User();
		loginG.addLoginActionListener(new LoginListener());
		//loginG.addCancelActionListener(new CancelListener());	
	}

    /**
     * Gets the user_full_name.
     *
     * @return the user_full_name
     */
    public static String getUser_full_name() {
		return user_full_name;
	}

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public static String getUserId() {
    	System.out.println("after get user id");
		return uId;
	}
    
    /**
     * Gets the user clinic.
     *
     * @return the user clinic
     */
    public static Clinic getUserClinic() {
    	System.out.println("after get clinic id");
		return clinic;
	}

	/**
	 * Sets the user_full_name.
	 *
	 * @param user_full_name the new user_full_name
	 */
	public static void setUser_full_name(String user_full_name) {
		LoginControl.user_full_name = user_full_name;
	}

	/**
	 * The listener interface for receiving cancel events.
	 * closing the windows
	 */
	class CancelListener implements ActionListener 
    {
    	
	    @Override
    	public void actionPerformed(ActionEvent e)
    	{
    	loginG.dispose();	//Closes the login window
    	}	
    }//action



    /**
     * The listener interface for receiving login events.
     * trying to login with the username and password strings
     * 
     */
    class LoginListener implements ActionListener
    {
         
         /* (non-Javadoc)
          * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
          */
         public void actionPerformed(ActionEvent ev)
         {      		
        	 String pass = loginG.getPasswordField();	//Gets the password the user entered
        	 String user = loginG.getUserField();		//Gets the user name the user entered
        	 
        	 System.out.println("Tried to login with user and pw " + user +" "+ pass);
        	
        	 if(pass.equals("")|| user.equals("")) 		//If fields are empty , show error message
        	 {
        		 JOptionPane.showMessageDialog(null,"Empty Fields!","Error", JOptionPane.INFORMATION_MESSAGE);    
        		 return;								//return to the login window
        	 }//if
        	 else
        	 {
        		 try
        		 {										 //set the user name and password and send to server
        		   UserLogin.setuPassword(pass);
        		   UserLogin.setuID(user);
        		   
        		   Envelope en = Controller.Control(UserLogin,task.GET_USER);
        		   User us = (User)en.getSingleObject();
        		   
        		   uId = us.getuID();
        		   clinic = us.getuClinic();
        		   System.out.println(en.getStatus().toString());
        		   
        		   //if(us.getuID() != null && !us.getuID().equals("0"))
        		   if(en.getStatus()!=Status.IN_SESSION)
        		   {
        			   setUser_full_name(us.getuFirstName()+" "+us.getuLastName());
	        		   if(UserLogin.getuPassword().equals(us.getuPassword()))
	        		   {
	        			   System.out.println("Password Match!");
	        		   	   loginG.dispose();	//Closes the login window
	        		   	   
	        		   	   switch(us.getuRole())
	        		   	   {
		        		   	   case CUSTOMER_SERVICE:
		        		   		   System.out.println("This user is CUSTOMER_SERVICE");
		        		   		   CS_GUI_findPatient cs = new CS_GUI_findPatient();
		        		   		   PatientControl pt = new PatientControl(cs);
		        		   		//TODO: open the next window (menu).
		        		   		   break;
		        		   	   case LAB_WORKER:
			        		   		System.out.println("This user is LAB_WORKER");
			        		   		LabController lb = new LabController(new LabGUI(),us.getuID());
		        		   		//TODO: open the next window (menu).
		        		   		   break;
		        		   	   case CLINIC_MANAGER:
			        		   		System.out.println("This user is CLINIC_MANAGER");
			        		   		ClinicManagerController CM_ctrl = new ClinicManagerController(new CM_GUI(),us.getuID());
		        		   		//TODO: open the next window (menu).
		        		   		   break;
		        		   	   case DOCTOR:
			        		   		System.out.println("This user is DOCTOR");	        		   		   
			        		   		DoctorGUI doc_gui = new DoctorGUI();	        		   		   
			        		   		DoctorController docCon = new DoctorController(doc_gui,us.getuID());
			        		   		//TODO: open the next window (menu).
		        		   	   		break;
		        		   	   case GENERAL_MANAGER:
			        		   		System.out.println("This user is GENERAL_MANAGER");
			        		   		GeneralManagerController GeneralCtrl = new GeneralManagerController(new GM_GUI(),us.getuID());
		        		   		//TODO: open the next window (menu).
		        		   		   break;
		        		   		   
		        		   		default:
		        		   		//TODO: open the next window (menu).
		        		   			break;
	        		   	   }
	        		   	   
	        		   }
	        		   else
	        		   { 
	        			   System.out.println("Password incorrect, Please try again.");
	        			   JOptionPane.showMessageDialog(null,"Pass not match!!!!","Error", JOptionPane.INFORMATION_MESSAGE);
	        		   }
        		   }
        		   else if (en.getStatus()==Status.IN_SESSION)
        			   JOptionPane.showMessageDialog(null,"User is in another session!","Error", JOptionPane.INFORMATION_MESSAGE);
        		   else
        			   JOptionPane.showMessageDialog(null,"No such User!!!!","Error", JOptionPane.INFORMATION_MESSAGE); 
        			   	
        		   System.out.println("pass: " + us.getuPassword() + " " + us.getuRole());
        		   
        		   
        		 }
        		 catch(Exception e)
        		 {	
        			 System.out.println("Exception from LoginControl:\n"+e);     	 
        		}
    	      }//else
          }
    }//action
}
