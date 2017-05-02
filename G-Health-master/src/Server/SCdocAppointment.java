package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import models.*;
import enums.*;

/**
 * @author G5 lab group
 * The Class SCdocAppointment.
 */
public class SCdocAppointment {
	
	/**
	 * Gets the recorded appointments.
	 *
	 * @param ptID the pt id
	 * @return the envelope
	 */
	public static Envelope GetRecordedAppointments(String ptID)
	{
		Statement stmt;
		String querystr;
		ResultSet result;
		Envelope en = new Envelope();
		AppointmentSettings as;
		Doctor doctor;
		
		querystr="SELECT  apsID,apsPtID,apsDate,apsTime,apsCreateDate,apsCreateTime,apsStatus,apsDocID,uFirstName,uLastName,cID,cName,cLocation,dSpeciality,apsSummery "
				+ "FROM appointmentsettings,user,clinic,doctor "
				+ "WHERE apsPtID='"+ptID+"' AND apsStatus='ARRIVED' AND uID=apsDocID AND cID=ucID AND dID=uID"
						+ " ORDER BY apsDate DESC; ";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Create new appointment in DB: " + querystr);
			result = stmt.executeQuery(querystr);
			en.setStatus(Status.NOT_EXIST);
			while (result.next())
            {
				System.out.println(result.getString(15));// printing the summery
				
				
				Status st =  Status.valueOf(result.getString(7));
				as = new AppointmentSettings(result.getInt(1),result.getString(2),result.getString(3),result.getString(4),
						result.getString(5),result.getString(6),st,result.getString("apsDocID"));
				
				as.setApsSummery(result.getString(15));
				
				Clinic clinic = new Clinic(result.getInt("cID"),result.getString("cName"),result.getString("cLocation"));
				DoctorSpeciallity ds = DoctorSpeciallity.valueOf(result.getString("dSpeciality"));
				doctor = new Doctor(result.getString("apsDocID"),result.getString("uFirstName"),result.getString("uLastName"),clinic,ds);
				as.setDoctor(doctor);
				en.addobjList(as);
				System.out.println(as);
				en.setStatus(Status.EXIST);
            }   
			
			en.setType(task.GET_ARRIVED_APPOINTMENTS);
			mysqlConnection.conn.close();
		}
		catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
          en.setStatus(Status.FAILED_EXCEPTION);
          return en;
        }
		
		return en;

	}
		
	/**
	 * Gets the current appointment.
	 *
	 * @param ptID the pt id
	 * @param apsDocID the aps doc id
	 * @return the envelope
	 */
	public static Envelope GetCurrentAppointment(String ptID, String apsDocID)
	{
		Statement stmt;
		String querystr;
		ResultSet result;
		Envelope en = new Envelope();
	
		Doctor doctor;
		
		querystr="SELECT apsID"
				+ " FROM appointmentsettings"
				+ " WHERE apsPtID='"+ptID+"' AND apsStatus='SCHEDUELD' AND apsDocID='"+apsDocID+"'";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			//System.out.println("Create new appointment in DB: " + querystr);
			result = stmt.executeQuery(querystr);
			en.setStatus(Status.NOT_EXIST);
			
			while (result.next())
            {
				int appointementID = result.getInt(1);
				
				en.addobjList(appointementID);
				System.out.println("the appointement id received from DB was:" + appointementID);
				en.setStatus(Status.EXIST);
            }   
			
			en.setType(task.GET_CURRENT_APPOINTMENT_ID);
			mysqlConnection.conn.close();
		}
		catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
          en.setStatus(Status.FAILED_EXCEPTION);
          return en;
        }
		
		return en;

	}

	/**
	 * Gets the p doc mail.
	 *
	 * @param nt the nt
	 * @return the p doc mail
	 */
	public static Notification getPDocMail(Notification nt){
		Statement stmt;
		String querystr,querystr2;
		ResultSet result;
		
		querystr= ""
				+ "SELECT pd.PersonalDoctorEmail "
				+ "FROM ghealth.personaldoctor pd, ghealth.patient p "
				+ "WHERE p.ptID = '" + nt.patient.getpID() + "' AND p.ptDoctorID = pd.PersonalDoctorID";
		querystr2= ""
				+ "SELECT pd.PersonalDoctorName "
				+ "FROM ghealth.personaldoctor pd, ghealth.patient p "
				+ "WHERE p.ptID = '" + nt.patient.getpID() + "' AND p.ptDoctorID = pd.PersonalDoctorID";
		
		try {
			stmt = mysqlConnection.conn.createStatement();
			result = stmt.executeQuery(querystr);
			result.next();
			nt.mail = result.getString(1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt = mysqlConnection.conn.createStatement();
			result = stmt.executeQuery(querystr2);
			result.next();
			nt.docName = result.getString(1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nt;
	}
	
	
	/**
	 * Record appointment.
	 *
	 * @param apsID the aps id
	 * @param summery the summery
	 * @return the status
	 */
	public static Status RecordAppointment(String apsID, String summery)
	{
		Statement stmt;
		
		String querystr;
		
		int result;
		
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
		String createdHour = hourFormat.format(new Date());
		
		querystr="UPDATE appointmentsettings "
				+ "SET apsStatus='ARRIVED', apsSummery ='"+summery+"', apsStartTime ='"+createdHour+"' "
				+ "WHERE apsID='"+apsID+"'";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("UPDATE Patient Arrived to appointment in DB: " + querystr);
			result = stmt.executeUpdate(querystr);
		
			mysqlConnection.conn.close();
		}
		catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
          return Status.FAILED_EXCEPTION;
        }
		
		return Status.ARRIVED;

	}
	
}
