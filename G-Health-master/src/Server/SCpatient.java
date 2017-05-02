package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import enums.Status;
import models.*;

/**
 * @author G5 lab group
 * The Class SCpatient.
 */
public class SCpatient {



	/**
	 * Gets the exist patient.
	 *
	 * @param ptID the pt id
	 * @return the envelope
	 */
	public static Envelope GetExistPatient(String ptID)
	{
		int rowCount=0;
		ResultSet result = null;
		Statement stmt;
		String querystr;
		Patient pt = new Patient();
		Envelope en = new Envelope();
		
		/* Return patient row if exist */
		querystr="SELECT * FROM patient "
				+ "WHERE ptID = '"+ptID+"';";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println(querystr+"\n(Check if patient: '"+ptID+"' is exist in DB:)");
			result = stmt.executeQuery(querystr);
			result.last();
			rowCount = result.getRow();
			System.out.println("rowcount="+rowCount );
			result.first();
			
			if(rowCount == 0)
			{
				en.setStatus(Status.NOT_EXIST);
				//en.addobjList(pt);
				System.out.println("Patient Not Exist in DB");
				mysqlConnection.conn.close();
			}
			else if(result.getString(8).equals("NOT_REG"))
			{
				en.setStatus(Status.NOT_REG);
				System.out.println("Patient Exist in DB BUT IS CANCELLED!");
				/* Get & Create the patient from DB */
				
				pt.setpID(result.getString("ptID"));
				pt.setpFirstName(result.getString("ptFirstName"));
				pt.setpLastName(result.getString("ptLastName"));
				pt.setPtEmail(result.getString("ptEmail"));
				pt.setPtPhone(result.getString("ptPhone"));
				pt.setPtPrivateClinic(result.getString("ptPrivateClinic"));
				String ptdid = result.getString("ptDoctorID");
				pt.setptpersonalDoctorID(ptdid);
				
				
				en.addobjList(pt);
				//en.setObj(pt);
				System.out.println("ResultSet - ptID - "+result.getString("ptID") );
				mysqlConnection.conn.close();
			}
			else
			{
				en.setStatus(Status.EXIST);
				System.out.println("Patient Exist in DB!");
				/* Get & Create the patient from DB */
				
				pt.setpID(result.getString("ptID"));
				pt.setpFirstName(result.getString("ptFirstName"));
				pt.setpLastName(result.getString("ptLastName"));
				pt.setPtEmail(result.getString("ptEmail"));
				pt.setPtPhone(result.getString("ptPhone"));
				pt.setPtPrivateClinic(result.getString("ptPrivateClinic"));
				String ptdid = result.getString("ptDoctorID");
				pt.setptpersonalDoctorID(ptdid);
				
				
				en.addobjList(pt);
				//en.setObj(pt);
				System.out.println("ResultSet - ptID - "+result.getString("ptID") );
				mysqlConnection.conn.close();
			}
		}
		catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
          return null;
        }
		
		return en;
	}
	
	/**
	 * Creates the patient.
	 *
	 * @param pt the pt
	 * @return the status
	 */
	public static Status CreatePatient(Patient pt)
	{
		Statement stmt;
		String querystr;
		querystr="INSERT INTO patient " 
		+ " VALUES ('"+pt.getpID()+"','"+pt.getpFirstName()+"','"+pt.getpLastName()+"', '"+pt.getPtEmail()+"', '"+pt.getPtPhone()+"', '"+pt.getPtPrivateClinic()+"', '"+pt.getPd()+"', 'IS_REG',null)";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Create new patient in DB: " + querystr);
			stmt.executeUpdate(querystr);
			mysqlConnection.conn.close();
		}
		catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
          return Status.FAILED_EXCEPTION;
        }
		
		return Status.CREATED;

	}
	
	
	/**
	 * Gets the clinic list.
	 *
	 * @return the envelope
	 */
	public static Envelope GetClinicList()
	{
		Envelope en = new Envelope();
		Statement stmt;
		String querystr;
		Patient pt = null;
		ResultSet result = null;
		String [] contactListNames = null;
		
		querystr="SELECT * "
				+ "FROM privateclniic";
		//System.out.println(querystr);
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Get Clinic List " + querystr);
			result = stmt.executeQuery(querystr);
			while (result.next())
            {
				en.addobjList(new PrivateClinic(result.getString(1),result.getString(2)));
				System.out.println(result.getString(1)+" "+result.getString(2));
            }   
			mysqlConnection.conn.close();
		}
		catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
         
        }
		return en;
		

	}
	
	/**
	 * methood chages the registration status of the patient and
	 * canceles his SCHEDUALED Appointments.
	 *
	 * @param pt the pt
	 * @return the status
	 */
	
	public static Status UncreatePatient(Patient pt)
	{
		Statement stmt;
		String querystr;
		int result;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String leftDate = formatter.format(new Date());

		
		querystr="UPDATE patient "
				+ "SET ptIsRegistered='NOT_REG',ptLeaveDate='"+leftDate+"'"
				+ "WHERE ptID='"+pt.getpID()+"'";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Cancel appointment in DB: " + querystr);
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
		
		return Status.NOT_REG;

	}
	
	/**
	 * Recover patient.
	 *
	 * @param pt the pt
	 * @return the status
	 */
	public static Status RecoverPatient(Patient pt)
	{
		Statement stmt;
		String querystr;
		int result;
		
		
		querystr="UPDATE patient "
				+ "SET ptIsRegistered='IS_REG' "
				+ "WHERE ptID='"+pt.getpID()+"'";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Cancel appointment in DB: " + querystr);
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
		
		return Status.IS_REG;

	}
	
	/**
	 * Cancel all appointments.
	 *
	 * @param pt the pt
	 * @return the status
	 */
	public static Status CANCEL_ALL_APPOINTMENTS(Patient pt)
	{
		Statement stmt;
		String querystr,querystr_b;
		int result;
			
		querystr="UPDATE appointmentsettings "
				+ "SET apsStatus='CANCELED'"
				+ "WHERE apsPtID='"+pt.getpID()+"' AND apsStatus='SCHEDUELD'";
		
		querystr_b="UPDATE labsettings "
				+ "SET labStatus='CANCELED'"
				+ "WHERE labPtID='"+pt.getpID()+"' AND labStatus='SCHEDUELD'";
		
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Cancel appointment in DB: " + querystr);
			result = stmt.executeUpdate(querystr);
			result = stmt.executeUpdate(querystr_b);
			mysqlConnection.conn.close();
		}
		catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
          return Status.FAILED_EXCEPTION;
        }
		
		return Status.CANCEL_ALL;

	}
	
}
