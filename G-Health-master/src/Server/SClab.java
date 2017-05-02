/**
 * TODO This is the class description
 */


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
 * The Class SClab.
 */
public class SClab {

	
	
	/**
	 * Get_ scheduel d_labs.
	 *
	 * @param ptID the pt id
	 * @return the envelope
	 */
	public static Envelope Get_SCHEDUELD_labs(String ptID)
	{
		Statement stmt;
		String querystr;
		ResultSet result;
		Envelope en = new Envelope();
		LabSettings ls;
		User doctor;
		Clinic cl;
		
		querystr="SELECT * "
				+ "FROM labsettings,user,clinic  "
				+ "WHERE labDocID=uID AND labPtID='"+ptID+"' AND labStatus='SCHEDUELD' AND cID = ucID"
				+ " ORDER BY labCreateDate DESC ";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Get SCHEDUELD labs from DB: " + querystr);
			result = stmt.executeQuery(querystr);
			en.setStatus(Status.NOT_EXIST);
			while (result.next())
            {
				Status st =  Status.valueOf(result.getString("labStatus"));
				
				ls = new LabSettings(result.getInt("labID"),result.getString("labPtID"), result.getString("labCreateDate"), result.getString("labCreateTime"), st,
						result.getString("labDocID"), result.getString("labDocReq"));
				
				
				doctor = new User();
				doctor.setuID(result.getString("labDocID"));
				doctor.setuFirstName(result.getString("uFirstName"));
				doctor.setuLastName(result.getString("uLastName"));
				
				cl = new Clinic();
				cl.setcID(result.getInt("cID"));
				cl.setcLocation(result.getString("cLocation"));
				cl.setcName("cName");
				doctor.setuClinic(cl);
				ls.setLabWorker(doctor);
				
				en.addobjList(ls);
				System.out.println(ls.toStringOpenLabs());
				en.setStatus(Status.EXIST);
            }   
			
			en.setType(task.GET_SCHEDUELD_LAB);
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
	 * Get_ arrive d_labs.
	 *
	 * @param ptID the pt id
	 * @return the envelope
	 */
	public static Envelope Get_ARRIVED_labs(String ptID)
	{
		Statement stmt;
		String querystr;
		ResultSet result;
		Envelope en = new Envelope();
		LabSettings ls;
		User doctor;
		Clinic cl;
		
		querystr="SELECT * "
				+ "FROM labsettings,user,clinic  "
				+ "WHERE labDocID=uID AND labPtID='"+ptID+"' AND labStatus='ARRIVED' AND cID = ucID"
				+ " ORDER BY labCreateDate DESC";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Create new appointment in DB: " + querystr);
			result = stmt.executeQuery(querystr);
			en.setStatus(Status.NOT_EXIST);
			while (result.next())
            {
				Status st =  Status.valueOf(result.getString("labStatus"));
				
				ls = new LabSettings(result.getInt("labID"),result.getString("labPtID"), result.getString("labCreateDate"), result.getString("labCreateTime"), st,
						result.getString("labDocID"), result.getString("labDocReq"));
				
				
				doctor = new User();
				doctor.setuID(result.getString("labDocID"));
				doctor.setuFirstName(result.getString("uFirstName"));
				doctor.setuLastName(result.getString("uLastName"));
				
				cl = new Clinic();
				cl.setcID(result.getInt("cID"));
				cl.setcLocation(result.getString("cLocation"));
				cl.setcName("cName");
				doctor.setuClinic(cl);
				ls.setLabWorker(doctor);
				ls.setLabWorkerSummery(result.getString("labWorkerSummery"));
				
				String filePath = result.getString("labPhotoPath");
				ls.setFilePath(filePath);
				if(!filePath.equals("NO FILE"))
				{
					String extension = filePath;
				    int index=extension.indexOf(".");
				    //get the extension of the file
				    extension=extension.substring(index+1, extension.length());
				    ls.setFileExt(extension);
				}
				
				en.addobjList(ls);
				System.out.println(ls.toStringOpenLabs());
				en.setStatus(Status.EXIST);
            }   
			
			en.setType(task.GET_SCHEDUELD_LAB);
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
	 * Update lab record.
	 *
	 * @param labID the lab id
	 * @param record the record
	 * @param labworkerID the labworker id
	 */
	public static void UpdateLabRecord(int labID,String record,String labworkerID)
	{
		Statement stmt;
		String querystr;
		int result;
		
		querystr="UPDATE labsettings "
				+ "SET labStatus='ARRIVED',labWorkerSummery='"+record+"',labworkerID='"+labworkerID+"'"
				+ "WHERE labID='"+labID+"'";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Update lab in DB: " + querystr);
			result = stmt.executeUpdate(querystr);
		
			mysqlConnection.conn.close();
		}
		catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
          //return Status.FAILED_EXCEPTION;
        }
		

	}




	/**
	 * Update lab file path.
	 *
	 * @param filename the filename
	 * @param labID the lab id
	 */
	public static void UpdateLabFilePath(String filename,int labID) {
		Statement stmt;
		String querystr;
		int result;
		
		querystr="UPDATE labsettings "
				+ "SET labPhotoPath='"+filename+"' "
				+ "WHERE labID='"+labID+"'";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Update lab in DB: " + querystr);
			result = stmt.executeUpdate(querystr);
		
			mysqlConnection.conn.close();
		}
		catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
          //return Status.FAILED_EXCEPTION;
        }
		
	}
	
	
	/**
	 * Gets the lab file path.
	 *
	 * @param labID the lab id
	 * @return the string
	 */
	public static String GetLabFilePath(int labID) {
		Statement stmt;
		String querystr;
		ResultSet result;
		String filePath = "";
		querystr="SELECT * labsettings "
				+ "WHERE labID='"+labID+"'";
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Get File Path lab in DB: " + querystr);
			result = stmt.executeQuery(querystr);
			result.next();
			filePath=result.getString("labPhotoPath");
			
			
			mysqlConnection.conn.close();
		}
		catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
          //return Status.FAILED_EXCEPTION;
        }
		
		return filePath;
	}

	/**
	 * Creaet lab ref.
	 *
	 * @param lb the lb
	 * @return the status
	 */
	public static Status CreaetLabRef(LabSettings lb) {
		Statement stmt;
		String querystr;
		int result;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
		String createdDate = formatter.format(new Date());
		String createdHour = hourFormat.format(new Date());
		
		
		querystr="INSERT INTO labsettings " + " (labPtID,labCreateDate,labCreateTime,labStatus,labDocID,labDocReq) "
				+ "VALUES ('"+lb.getLabPtID()+"','"+createdDate+"','"+createdHour+"','SCHEDUELD','"+lb.getLabDocID()+"'"
				+",'"+lb.getLabDoctorReq()+"')";

		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Insert lab in DB: " + querystr);
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
		return Status.CREATED;
	}
}
