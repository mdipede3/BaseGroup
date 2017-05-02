
package Server;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import enums.Status;
import enums.task;
import models.Envelope;


/**
 * @author G5 lab group
 * The Class SCmonthlyReports.
 */
public class SCmonthlyReports {

	
	
	/** The instance. */
	private static SCmonthlyReports instance = null;
	
	/** The Report to env. */
	private List<Object> ReportToEnv;
	
	
	
	/**
	 * Instantiates a new s cmonthly reports.
	 */
	private SCmonthlyReports(){	
		// Exists only to defeat instantiation.
		System.out.println("in MonthlyReports constructor");
	}
	
	
	 /**
 	 * Gets the single instance of SCmonthlyReports.
 	 *
 	 * @return single instance of SCmonthlyReports
 	 */
 	public static SCmonthlyReports getInstance() {
	      if(instance == null) {
	         instance = new SCmonthlyReports();
	      }
	      return instance;
	   }
	
	 

	
	
	/**
	 * Creates the report.
	 *
	 * @param clinicID the clinic id
	 */
	private void createReport(int clinicID){
		
		
		this.ReportToEnv =  new ArrayList<Object>();
		
		ResultSet result = null;
		Statement stmt;
		

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		
		
		
		String to_date_str = formatter.format(today.getTime());
		today.add(Calendar.DATE, -29);
		
		String from_date_str = formatter.format(today.getTime());
		
		

		System.out.println(from_date_str);
		System.out.println(to_date_str);

		String query1 = ""
				+ "Create or replace view TrypA as "
				+ "(SELECT * "
				+ "  from appointmentsettings A "
				+ "  where A.apsstatus = 'ARRIVED');";


				String query2 = ""
				+ "Create or replace view TrypB as "
				+ "(SELECT idweeks AS weekNumar, "
				+ "        Count(DISTINCT A.apsptid) AS NumOfPatients, "
				+ "		AVG(DATEDIFF(A.apsDate,A.apsCreateDate)) as AvgProcessTime, "
				+ "		AVG(timediff(A.apsStartTime, A.apsTime)/60) as AvgWaitingTime "
				+ "FROM   TrypA A RIGHT OUTER JOIN Weeks ON WEEK(A.apsdate) = weeks.idweeks "
				+ "WHERE  weeks.idweeks>= week('" + from_date_str
				+ "') AND "
				+ "       weeks.idweeks<=week('" + to_date_str
				+ "') AND "
				+ "	   A.apsstatus IS NULL OR (A.apsstatus = 'ARRIVED' AND "
				+ "       A.apsdate >= '" + from_date_str
				+ "' AND "
				+ "       A.apsdate <= '" + to_date_str
				+ "') and "
				+ "       A.apsDocID in (SELECT doc.uID FROM user doc where doc.ucID=" + clinicID
				+ ") "
				+ "       GROUP BY idweeks "
				+ "       order by idweeks);";


				String query3 = ""
				+ "Create or replace view TryLEAVEa as "
				+ "(SELECT * "
				+ "	FROM patient P "
				+ "    where P.ptIsRegistered = 'NOT_REG' and P.ptLeaveDate IS NOT NULL);";


				String query4 = ""
				+ "Create or replace view TryLEAVEb as "
				+ "(SELECT idweeks AS weekNuml, "
				+ "   Count(Pa.ptLeaveDate) AS LeaveClients "
				+ "   FROM   TryLEAVEa Pa  RIGHT OUTER JOIN Weeks ON WEEK(Pa.ptLeaveDate) = weeks.idweeks "
				+ "   WHERE  weeks.idweeks>= week('" + from_date_str
				+ "') AND "
				+ "       weeks.idweeks<=week('" + to_date_str
				+ "') and Pa.ptLeaveDate is NULL "
				+ "   OR (Pa.ptIsRegistered = 'NOT_REG' AND "
				+ "      Pa.ptLeaveDate >= '" + from_date_str
				+ "' AND "
				+ "      Pa.ptLeaveDate <= '" + to_date_str
				+ "') "
				+ "      group by idweeks "
				+ "      order by idweeks "
				+ ");";


				String query5 = ""
				+ "Create or replace view TryNOSHOWa as "
				+ "(SELECT * "
				+ "from appointmentsettings A "
				+ "where A.apsStatus ='NOSHOW' "
				+ ");";


				String query6 = ""
				+ "Create or replace view TryNOSHOWb as "
				+ "(SELECT idweeks AS weekNumns, "
				+ "       Count(DISTINCT A.apsptid) AS NumOfNoshows "
				+ "FROM   TryNOSHOWa A RIGHT OUTER JOIN Weeks ON WEEK(A.apsdate) = weeks.idweeks "
				+ "WHERE  weeks.idweeks>= week('" + from_date_str
				+ "') AND "
				+ "       weeks.idweeks<=week('" + to_date_str
				+ "') AND "
				+ "	   A.apsstatus IS NULL OR "
				+ "       (A.apsstatus = 'NOSHOW' AND "
				+ "       A.apsdate >= '" + from_date_str
				+ "' AND "
				+ "       A.apsdate <= '" + to_date_str
				+ "' and "
				+ "       A.apsDocID in (SELECT doc.uID FROM user doc where doc.ucID=" + clinicID
				+ ")) "
				+ "GROUP BY idweeks);";


				String query7 = ""
				+ "Create or replace view NMonthlyView as "
				+ "(SELECT * "
				+ "FROM   TrypB AR,TryNOSHOWb NS, TryLEAVEb L "
				+ "WHERE  NS.weekNumns = L.weekNuml AND L.weekNuml = AR.weekNumar AND NS.weekNumns = AR.weekNumar "
				+ " "
				+ ");";


				/*String query8 = ""
				+ "SELECT * FROM ghealth.NMonthlyView;";
				*/
				String query8 = ""
						+ "SELECT weekNumar as weekNum,NumOfPatients,AvgProcessTime,AvgWaitingTime,NumOfNoshows,LeaveClients "
						+ "	   FROM ghealth.nmonthlyview "
						+ "       UNION "
						+ "       SELECT "
						+ "       'Min' as Op, "
						+ "       Min(NumOfPatients) as NumOfPatients, "
						+ "       MIN(AvgProcessTime) as AvgProcessTime, "
						+ "       MIN(AvgWaitingTime) as AvgWaitingTime , "
						+ "       MIN(NumOfNoshows) as NumOfNoshows, "
						+ "       MIN(LeaveClients) as LeaveClients "
						+ "	   FROM nmonthlyview "
						+ "       UNION "
						+ "       SELECT "
						+ "       'Max' as Op, "
						+ "       Max(NumOfPatients) , "
						+ "       Max(AvgProcessTime), "
						+ "       Max(AvgWaitingTime), "
						+ "	   MAX(NumOfNoshows) as NumOfNoshows, "
						+ "       MAX(LeaveClients) as LeaveClients "
						+ "       FROM nmonthlyview "
						+ "       UNION "
						+ "       SELECT "
						+ "       'Avg' as Op, "
						+ "       AVG(NumOfPatients)  , "
						+ "       AVG(AvgProcessTime), "
						+ "       AVG(AvgWaitingTime), "
						+ "	   AVG(NumOfNoshows) as NumOfNoshows, "
						+ "       AVG(LeaveClients) as LeaveClients "
						+ "       FROM nmonthlyview "
						+ "       UNION "
						+ "       SELECT "
						+ "       'SD' as Op, "
						+ "       STDDEV(NumOfPatients) , "
						+ "       STDDEV(AvgProcessTime), "
						+ "       STDDEV(AvgWaitingTime), "
						+ "	   STDDEV(NumOfNoshows) as NumOfNoshows, "
						+ "       STDDEV(LeaveClients) as LeaveClients "
						+ "       FROM nmonthlyview";
		try {
			mysqlConnection ms = new mysqlConnection();
			stmt = ms.conn.createStatement();
			
			stmt.executeUpdate(query1);
			stmt.executeUpdate(query2);
			stmt.executeUpdate(query3);
			stmt.executeUpdate(query4);
			stmt.executeUpdate(query5);
			stmt.executeUpdate(query6);
			stmt.executeUpdate(query7);
			result = stmt.executeQuery(query8);

			String WN,MN;
			/*-- for each day of the past week -- */
			while(result.next())
			{
				Calendar tempDay = Calendar.getInstance();
				WN = result.getString(1);
				if(!WN.equals("Min") && !WN.equals("Max") && !WN.equals("Avg") && !WN.equals("SD"))
				{	
					tempDay.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(WN));
					int week =(Integer.parseInt(result.getString(1))%4);
					int month = ((1+Integer.parseInt(result.getString(1)))/4);
					MN = getMonthName(month);
					WN = String.valueOf(week);
				}
				else MN = " ";
				
				
				
				
				this.ReportToEnv.add(new String[]{MN,result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6) });	
				
			}
			
			
		//	allReports.add(monthlyData);
	
			
			ms.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}//end of createReport function 
	
	
	
	/**
	 * Gets the clinic monthly report.
	 *
	 * @param cID the c id
	 * @return the clinic monthly report
	 */
	public Envelope getClinicMonthlyReport(int cID){
	    
		Envelope en = new Envelope();    
	
	           
        this.createReport(cID);
        
		try {
			en.setobjList(this.ReportToEnv);
			en.setStatus(Status.EXIST);		
			en.setType(task.GET_CLINIC_MONTHLY_REPORT);
		
			
		} catch (ArrayIndexOutOfBoundsException e) {
			
			e.printStackTrace();
		    en.setStatus(Status.FAILED_EXCEPTION);
		}
		
		return en;
             
	}//end of getClinicMonthlyReport
	
	
	
	/**
	 * Gets the week num.
	 *
	 * @param weekOfYear the week of year
	 * @return the week num
	 */
	private int getWeekNum(int weekOfYear){
		int weekNum =0;
		
		switch(weekOfYear%4){
			
		case 0:
			weekNum = 1;
		case 1:
			weekNum = 2;
		case 2:
			weekNum = 3;
		case 3:
			weekNum = 4; 
		
		}
		return weekNum;
		
	}
	
	/**
	 * Gets the month name.
	 *
	 * @param month_num the month_num
	 * @return the month name
	 */
	private String getMonthName(int month_num){
		
		switch(month_num){
		
		case 1:
			return "January";
		case 2:
			return "February";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6: 
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9: 
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
		default: 
			return "no_month";
		
		}
		
		
	}
	
}



