package Server;
import models.*;
import enums.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;


/**
 * @author G5 lab group
 * Main server class.
 * includes mainly the server socket connections and the tasks switch case
 */
public class Server extends Thread
{

    /** The server socket. */
    private ServerSocket serverSocket = null;
    
    /** The conn. */
    public Connection conn;
    
    /** The status. */
    public Status status;
    
    /** The pt. */
    public Patient pt = null;
    
    /** The us. */
    public User us = null;
    
    /** The as. */
    public AppointmentSettings as = null;
    
    /** The filename. */
    public String filename;
    
    /** The session list. */
    public static List<String> sessionList = new ArrayList<String>();
    
    /** The ls. */
    public LabSettings ls = null;
    
    /** The clinic. */
    public Clinic clinic = null;
	
	/** The nt. */
	private Notification nt;
    
    
    
    
    /**
     * Starting server socket with given port.
     *
     * @param port the port
     */
    public Server(int port) 
    {
    	try 
    	{
			serverSocket = new ServerSocket(port);
			System.out.println("Starting Server class");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    
    /**
     * Waiting for connections.
     */
    public void run()
    {
    	while(true)
	    	try {
	    		System.out.println("Server: Waiting for connection...");
				Socket clientSocket = serverSocket.accept();
				System.out.println("Server: Connected");
				communicate(clientSocket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
    }

    
    /**
     * Taking care of network transportations and tasks switch case.
     *
     * @param cs the cs
     */
    public void communicate(Socket cs) {
    	
        //System.out.println("Server-> Start - Before Socket Coonnection.");
        try {
        	Envelope env;
        	List<Object> objList;
        	ObjectInputStream inputStream;
        	ObjectOutputStream outputStream;
        	
            
            /* getting object */
        	inputStream = new ObjectInputStream(cs.getInputStream());
            
           // System.out.println("Object is = " + inStream);
            
            /* parsing and switching are needed here */
            env  = (Envelope)inputStream.readObject();
            
            System.out.println("Object received (address) = ");
            //System.out.println("Object received (address) = " + env.address);
            
            
            
            
            /* ----- getting data from DB ------ */
            mysqlConnection msql = new mysqlConnection();
            
            System.out.println(env.getType());
     
            
            
            switch(env.getType()){
         
            /*---      User Tasks:    ---*/
            case GET_USER:
            	us = (User)env.getSingleObject();
            	
            	if(searchUserSession(us.getuID())==true){
            		
            		env.setStatus(Status.IN_SESSION);
            		
            		System.out.println("USER IN SESSION");
            		break;
            	}
            	else{
            		env=SCuser.GetExistUser(us.getuID());
            		if(env.getStatus() == Status.EXIST && ((User)(env.getSingleObject())).getuPassword().equals(us.getuPassword()))
            			sessionList.add(us.getuID());
            		else{System.out.println("******************************");}
	            	System.out.println("case GET_USER");
	            	break;
            	}
            	
            	
            /*---     Patient Tasks:   ---*/
            case ADD_PATIENT:
            	System.out.println("case ADD_PATIENT");
            	pt = (Patient)env.getSingleObject();
            	status=SCpatient.CreatePatient(pt);
            	env.setStatus(status);
            	break;

            case GET_PATIENT:
            	pt = (Patient)env.getSingleObject();
            	System.out.println("case GET_PATIENT");
            	env=SCpatient.GetExistPatient(pt.getpID());
            	break;
            	
            case GET_PRIVATE_CLINIC_LIST:
            	env = SCpatient.GetClinicList();
            	break;
            	
            
            case GET_CLINIC_LIST:
            	env = SCclinic.GetOurClinicList();
            	break;
            
            	
            /*---   Appointment Tasks: ---*/  
            case CANCEL_PATIENT_REGISTRATION:
            	
            	System.out.println("case CANCEL_PATIENT_REGISTRATION");
            	pt = (Patient)env.getSingleObject();
            	status=SCpatient.UncreatePatient(pt);           	
            	env.setStatus(status);
            	break;
            	
            case CANCEL_ALL_PATIENT_APPOINTMENTS:
            	
            	System.out.println("case CANCEL_ALL_PATIENT_APPOINTMENTS:");
            	pt = (Patient)env.getSingleObject();
            	status=SCpatient.CANCEL_ALL_APPOINTMENTS(pt);           	
            	env.setStatus(status);
            	break;
            	
            case RECOVER_PATIENT_REGISTRATION:
            	
            	System.out.println("case RECOVER_PATIENT_REGISTRATION:");
            	pt = (Patient)env.getSingleObject();
            	status=SCpatient.RecoverPatient(pt);           	
            	env.setStatus(status);
            	break;
            	
            	
            	
            	
            case CREATE_NEW_APPOINTMENT:
            	System.out.println("CREATE_NEW_APPOINTMENT");
            	as = (AppointmentSettings) env.getSingleObject();
            	status=SCappointment.CreateAppointment(as);
            	env.setStatus(status);
            	break;
            	
            case GET_DOCTORS_IN_CLINIC_BY_TYPE:
            	
            	objList = env.getobjList();
            	System.out.println(objList.get(0).toString() + objList.get(1).toString());
            	env = SCappointment.GetClinicDoctorList(objList.get(0).toString(),objList.get(1).toString());
            	System.out.println("GET_DOCTORS_IN_CLINIC_BY_TYPE");
            	break;
            	
            case GET_AVAILIBLE_DOCTOR_HOURS:
            	objList = env.getobjList();
            	System.out.println(objList.get(0).toString() + objList.get(1).toString());
            	System.out.println("GET_AVAILIBLE_DOCTOR_HOURS");
            	env = SCappointment.GetAvailibleDoctorHours(objList.get(0).toString(),objList.get(1).toString());
            	break;
            	
            	
            case GET_OPEN_APPOINTMENTS:
            	System.out.println("GET_OPEN_APPOINTMENTS");
            	pt = (Patient)env.getSingleObject();
            	env = SCappointment.GetSCHEDUELDAppointments(pt.getpID());
            	break;
            	
            	
            case CANCEL_APPOINTMENT_FROM_DB:
            	System.out.println("CANCEL_APPOINTMENT_FROM_DB");
            	as = (AppointmentSettings) env.getSingleObject();
            	env.setStatus(SCappointment.CancelAppointment(as.getApsID()));
            	break;
            	
            	
            	/*--- Doctor flow Tasks  ----*/
            	
            case CREATE_LAB_REF:
            	ls = (LabSettings)env.getSingleObject();
            	status=SClab.CreaetLabRef(ls);
            	env.setStatus(status);

            	break;
            case GET_CURRENT_APPOINTMENT_ID:
            	System.out.println("GET_CURRENT_APPOINTMENT_ID");
            	String[] patiend_doc =(String[])env.getSingleObject();
            	env = SCdocAppointment.GetCurrentAppointment(patiend_doc[0], patiend_doc[1]);
            	
            	break;
            	
            case SET_APPOINTMENT_RECORD:
            	System.out.println("SET_APPOINTMENT_RECORD");
            	String[] appID_appRec =(String[])env.getSingleObject();
            	SCdocAppointment.RecordAppointment(appID_appRec[0], appID_appRec[1]);
            	
            	break;
            	
            case GET_ARRIVED_APPOINTMENTS:
            	System.out.println("GET_ARRIVED_APPOINTMENTS");
            	pt = (Patient)env.getSingleObject();
            	env = SCdocAppointment.GetRecordedAppointments(pt.getpID());
            	break;
            /*---     Lab-Ref Tasks:   ---*/
            case SEND_FILE_TO_CLIENT:
            /* Sending file to client */
            	/* TODO: SQL query returns filename as string */
            	ls = (LabSettings)env.getSingleObject();
            	sendFile(ls.getFilePath(),cs);
            	break;
            
            	
            case UPLOAD_FILE_TO_LAB_RECORD:
            /* Geting file from client */
            	ls = (LabSettings)env.getSingleObject();
            	filename="src//Server//files//"+Integer.toString(ls.getLabID())+"."+ls.getFileExt();
            	saveFile(filename,cs);
            	SClab.UpdateLabFilePath(filename,ls.getLabID());
            	break;
				
            case GET_ARRIVED_LABS:
            	pt = (Patient)env.getSingleObject();
            	env = SClab.Get_ARRIVED_labs(pt.getpID());
            	break;
			case GET_SCHEDUELD_LAB:
            	pt = (Patient)env.getSingleObject();
            	env = SClab.Get_SCHEDUELD_labs(pt.getpID());
            	break;
            	
            case UPDATE_LAB_RECORD:
            	ls = (LabSettings)env.getSingleObject();
            	SClab.UpdateLabRecord(ls.getLabID(),ls.getLabWorkerSummery(),ls.getLabWorkerID());
            	break;
            	
            	
            case GET_CLINIC_WEEKLY_REPORT:
            	System.out.println("GET_CLINIC_WEEKLY_REPORT");
            	clinic = (Clinic)env.getSingleObject();

    			SCweeklyReports rep = SCweeklyReports.getInstance();
            	env = rep.getClinicWeeklyReport(clinic.getcID()); 
            	break;
            	
            case GET_CLINIC_MONTHLY_REPORT:
            	System.out.println("GET_CLINIC_MONTHLY_REPORT");
            	clinic = (Clinic)env.getSingleObject();

    			SCmonthlyReports report = SCmonthlyReports.getInstance();
            	env = report.getClinicMonthlyReport(clinic.getcID()); 
            	break;
            	
            case GET_CLINIC_CLUSTER_MONTHLY_REPORT:
            	System.out.println("GET_CLINIC_CLUSTER_MONTHLY_REPORT");
            	
    			SCmonthlyClusterReports reports = SCmonthlyClusterReports.getInstance();
    			env = reports.getClinicMonthlyClusterReport(env.getobjList());
            	break;
            	
            case SEND_PERSONAL_DOC_MAIL:
                /* Sending the patient's personal doctor mail with app details. */
            	nt = (Notification)env.getSingleObject();
            	nt = SCdocAppointment.getPDocMail(nt); //scda = null;
            	try {
					Email.generateAndSendEmail(nt);
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	System.out.println("\n\n ===> The System has just sent an Email successfully. Check your email..");
                	break;
                	
                	
            case LOG_OUT:
                /* client is logging out */
            	us = (User)env.getSingleObject();
            	
            	removeSession(us.getuID());
                	break;
            
            	
            default:
				break;
            
            }
            
                        
            
            System.out.println("Before sending object back");
            
            
            /* if the task is not to send FILE to client */
            if(env.getType() != task.UPLOAD_FILE_TO_LAB_RECORD && env.getType() != task.SEND_FILE_TO_CLIENT)
            {
	            /* Sending data back to client */
            	System.out.println("before new output stream");
	            outputStream = new ObjectOutputStream(cs.getOutputStream());
	            System.out.println("before write env to out stream");
	            outputStream.writeObject(env);
	            
            }
           //serverSocket.close();
           //cs.close();
           System.out.println("Server-> Finish - Socket waiting for new connection.");

        } catch (SocketException se) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cn) {
            cn.printStackTrace();
       }
        
        
        
    }
    
    
    /**
     * Removing user session from the active sessions list.
     *
     * @param getuID the getu id
     */
    public void removeSession(String getuID) {
    	System.out.println("before remove");
		sessionList.remove(getuID);
	}



	/**
	 * Searching user session in active sessions list.
	 *
	 * @param uid the uid
	 * @return true, if successful
	 */
	public boolean searchUserSession(String uid){
    	for(String str: sessionList) {
    	    if(str.trim().equals(uid))
    	       return true;
    	}
    	return false;
    }
    
    /**
     * Sending file.
     *
     * @param filename the filename
     * @param s the s
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void sendFile(String filename,Socket s) throws IOException {
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		
		String extension;
		extension=filename;
	    int index=extension.indexOf(".");
	    //get the extension of the file
	    extension=extension.substring(index+1, extension.length());
		
		FileInputStream fis = new FileInputStream(filename);
		byte[] buffer = new byte[4096];
		
		while (fis.read(buffer) > 0) {
			dos.write(buffer);
		}
		
		fis.close();
		dos.close();	
	}
    
    
    /**
     * Saving file in server storage.
     *
     * @param filename the filename
     * @param clientSock the client sock
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void saveFile(String filename,Socket clientSock) throws IOException {
		DataInputStream dis = new DataInputStream(clientSock.getInputStream());
		FileOutputStream fos = new FileOutputStream(filename);
		byte[] buffer = new byte[16*1024]; // 16 kb
		
		int filesize = 2097152; // 2mb files - Send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			System.out.println("read " + totalRead + " bytes.");
			fos.write(buffer, 0, read);
		}
		
		fos.close();
		dis.close();
	}
    
   
}