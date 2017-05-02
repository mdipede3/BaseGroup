package Server;

import java.io.PrintStream;

/**
 * @author G5 lab group
 * main class that start the server, SQL connection, server log and print hook.
 */
public class StartServer {

	/** The serv log. */
	public static serverLogGui servLog = new serverLogGui();
    
    /** The ps. */
    public static PrintStream ps = null;
    
    /** The sv. */
    public static Server sv = null;
    
    /** The port. */
    public static int port = 5555;
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	    servLog.dispose();
	    ServerGui serv = new ServerGui();
	    mysqlConnection servCon = new mysqlConnection(serv,servLog);
	    sv = new Server(port);
	    sv.start();
	    ps = activateSYSOHook();
	    System.setOut(ps);
		
	}
	
	/**
	 * Taking control of the System.out.println output and redirect it to our server log screen
	 *
	 * @return the prints the stream
	 */
	public static PrintStream activateSYSOHook(){
		PrintStream myStream = new PrintStream(System.out) {
		    @Override
		    public void println(String x) {
		        //super.println(System.currentTimeMillis() + ": " + x);
		    	super.println(x);
		    	servLog.setLog(x);
		    }
		};
		//System.setOut(myStream);
		return myStream;
	}

}
