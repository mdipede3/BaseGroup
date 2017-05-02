package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import client.Controller;
import client.LoginControl;
import enums.task;
import models.User;

/**
 * @author G5 lab group
 * The Class LoggingOut.
 */
public class LoggingOut extends JFrame{
	
	/**
	 * A father class which all GUI classes inherit from.
	 * Controls the window X button and logging out function
	 */
	private static final long serialVersionUID = 270872694035967708L;

	/**
	 * Setting X button to do nothing and adds the listener.
	 */
	public LoggingOut(){
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(exitListener);
	}
	
	/** The exit listener. */
	WindowListener exitListener = new WindowAdapter() {
		
	    @Override
	    public void windowClosing(WindowEvent e) {
	        int confirm = JOptionPane.showOptionDialog(
	             null, "Are You Sure you want to close and log out?", 
	             "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
	             JOptionPane.QUESTION_MESSAGE, null, null, null);
	        if (confirm == 0) 
	        {
	        	Controller.Control(new User(LoginControl.getUserId()),task.LOG_OUT);
				dispose();
				LoginControl userctrl = new LoginControl(new LoginGUI());
	           System.exit(0);
	        }
	    }
	};
	
	/**
	 * Logging out listener.
	 *
	 * 
	 */
	class LogOutListener  implements ActionListener 
	{
	
		/**
		 * @param e the event
		 *
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			
			Controller.Control(new User(LoginControl.getUserId()),task.LOG_OUT);
			dispose();
			LoginControl userctrl = new LoginControl(new LoginGUI());
	
		}
		
	}//LogOutListener
	
//logout
}
