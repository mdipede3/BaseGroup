/**
 * TODO This is the class description
 */


package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import enums.Status;
import models.*;
import enums.*;

/**
 * @author G5 lab group
 * The Class SCuser.
 */
public class SCuser {

	/**
	 * Gets the exist user.
	 *
	 * @param uID the u id
	 * @return the envelope
	 */
	public static Envelope GetExistUser(String uID)
	{
		int rowCount=0;
		ResultSet result = null;
		Statement stmt;
		String querystr;
		User us = null;
		Envelope en = new Envelope();
		Clinic cl = new Clinic();
		/* Return patient row if exist */
		querystr="SELECT *"
				+ "FROM user u,clinic c "
				+ "WHERE u.ucID = c.cID AND u.uID = '"+uID+"';";
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println(querystr+"\n(Check if user: '"+uID+"' is exist in DB:)");
			result = stmt.executeQuery(querystr);
			
			result.last();
			rowCount = result.getRow();
			System.out.println("rowcount="+rowCount);
			result.first();
			
			if(rowCount == 1)
			{
				/* Get & Create the exist user from DB */
				us = new User();
				us.setuID(result.getString("uID"));
				us.setuPassword(result.getString("uPassword"));
				us.setuFirstName(result.getString("uFirstName"));
				us.setuLastName(result.getString("uLastName"));
				us.setuEmail(result.getString("uEmail"));
				
				cl.setcID(result.getInt("ucID"));
				cl.setcLocation(result.getString("cLocation"));
				cl.setcName(result.getString("cName"));
				us.setuClinic(cl);
				
				String temp124=result.getString("role");
				us.setuRole(Roles.valueOf(temp124));
				
				en.addobjList(us);
				
				en.setStatus(Status.EXIST);
				
				System.out.println("ResultSet - uID - "+result.getString("uID") );
				mysqlConnection.conn.close();
			}else{
				
				en.setStatus(Status.NOT_EXIST);
				us = new User(null,null,null,null,null,null,null);
				en.addobjList(us);
				
				System.out.println("ResultSet - uID - NO SUCH USER ");
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
	
	

	
	
	
}
