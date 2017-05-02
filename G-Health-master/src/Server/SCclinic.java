/**
 * TODO This is the class description
 */


package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import enums.Status;
import models.*;


/**
 * @author G5 lab group
 * The Class SCclinic.
 */
public class SCclinic {



	/**
	 * Gets the our clinic list.
	 *
	 * @return the envelope
	 */
	public static Envelope GetOurClinicList()
	{
		Envelope en = new Envelope();
		Statement stmt;
		String querystr;
		ResultSet result = null;
		
		querystr="SELECT * "
				+ "FROM clinic";
		System.out.println(querystr);
		
		try 
		{
			stmt = mysqlConnection.conn.createStatement();
			System.out.println("Get Clinic List " + querystr);
			result = stmt.executeQuery(querystr);
			while (result.next())
            {
				en.addobjList(new Clinic(result.getInt(1),result.getString(2),result.getString(3)));
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
	
	
}
