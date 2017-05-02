package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.LoginControl;
import models.Patient;

import javax.swing.JTextField;

import java.awt.Window.Type;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Label;


/**
 * @author G5 lab group
 * The Class Doctor record the patient's meeting GUI.
 */
public class Doctor_rec_GUI extends LoggingOut {


	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5798215983453009657L;
	
	/** The content pane. */
	private JPanel contentPane;
	
	/** The text rec pane. */
	private JTextPane textRecPane;
	
	/** The btn cancel. */
	private JButton btnCancel;
	
	/** The save rec. */
	private JButton saveRec;
	
	/** The lblwarning message. */
	//private JButton btnCrtPt;
	private JLabel lblwarningMessage = null;
	
	/** The patient details. */
	private JPanel patientDetails;
	
	/** The Search patient. */
	JButton SearchPatient;
	
	/** The Log out. */
	JButton LogOut;
	
	/**
	 * Create the frame.
	 */
	public Doctor_rec_GUI() {
		setResizable(false);
		setTitle("G-Health");
		setIconImage(Toolkit.getDefaultToolkit().getImage(DoctorGUI.class.getResource("/images/logo2.PNG")));
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("New label");
		label.setBounds(0, 0, 0, 0);
		contentPane.add(label);
		JLabel lblLogo;
		if(LoginControl.getUser_full_name() == null)
			lblLogo = new JLabel("Welcome DOCTOR!");
		else lblLogo = new JLabel("Hi "+LoginControl.getUser_full_name()+"!");
		lblLogo.setIcon(new ImageIcon(DoctorGUI.class.getResource("/images/logo2.png")));
		lblLogo.setBounds(0, 0, 794, 79);
		contentPane.add(lblLogo);
		
		saveRec = new JButton("SAVE RECORD");
		saveRec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		saveRec.setBounds(93, 467, 140, 23);
		contentPane.add(saveRec);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCancel.setBounds(386, 467, 140, 23);
		contentPane.add(btnCancel);
		btnCancel.addActionListener(new CancelListener());
		
		textRecPane = new JTextPane();
		textRecPane.setText("Please fill appointment record here...");
		textRecPane.setBounds(202, 143, 273, 228);
		contentPane.add(textRecPane);
		
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	
	}
	
	/**
	 * Sets the warning message visible true.
	 */
	public void setWarningMessageVisibleTrue() {
		lblwarningMessage.setVisible(true);	
	}
	
	/**
	 * Sets the warning message visible true.
	 *
	 * @param st the new warning message visible true
	 */
	public void setWarningMessageVisibleTrue(String st) {
		lblwarningMessage.setText(st);
		lblwarningMessage.setForeground(Color.RED);
		lblwarningMessage.setBounds(10, 165, 245, 30);
		lblwarningMessage.setVisible(true);	
		
	}
	
	
	
	/**
	 * Undisplay warning message.
	 */
	public void undisplayWarningMessage() {
		lblwarningMessage.setVisible(false);
		
	}
	
	
	/**
	 * Record patient action listener.
	 *
	 * @param e the event
	 */
	public void RecordPatientActionListener(ActionListener e)
	{
		saveRec.addActionListener(e);
	}
	
	/**
	 * Adds the cancel action listener.
	 *
	 * @param e the event
	 */
	public void addCancelActionListener(ActionListener e)
	{
		btnCancel.addActionListener(e);
	}
	
/**
 * Sets the patient.
 *
 * @param pt the patient
 */
public void SetPatient(Patient pt) {
		
		
		JLabel lblPatientDetails = new JLabel("Patient Details:");
		lblPatientDetails.setBounds(541, 135, 107, 20);
		contentPane.add(lblPatientDetails);
		patientDetails = new JPanel();
		patientDetails.setBounds(497, 170, 273, 232);
		contentPane.add(patientDetails);
		patientDetails.setLayout(null);
		
		
		Label label_1 = new Label("Patient ID");
		label_1.setBounds(5, 5, 62, 22);
		patientDetails.add(label_1);
		
		Label label_2 = new Label("First Name");
		label_2.setBounds(5, 35, 62, 22);
		patientDetails.add(label_2);
		
		Label label_3 = new Label("Last Name");
		label_3.setBounds(5, 65, 62, 22);
		patientDetails.add(label_3);
		
		Label label_4 = new Label("Email");
		label_4.setBounds(5, 95, 62, 22);
		patientDetails.add(label_4);
		
		Label label_5 = new Label("Phone");
		label_5.setBounds(5, 125, 62, 22);
		patientDetails.add(label_5);
		
		Label label_6 = new Label("Private Clinic");
		label_6.setBounds(5, 155, 82, 22);
		patientDetails.add(label_6);
		
		Label label_7 = new Label("Doctor ID");
		label_7.setBounds(5, 185, 62, 22);
		patientDetails.add(label_7);
		
		JLabel PationID = new JLabel(pt.getpID());
		PationID.setBounds(95,5,300, 22);
		patientDetails.add(PationID);
		

		JLabel fName = new JLabel(pt.getpFirstName());
		fName.setBounds(95, 35, 300, 22);
		patientDetails.add(fName);	
		

		JLabel lName = new JLabel(pt.getpLastName());
		lName.setBounds(95, 65, 300, 22);
		patientDetails.add(lName);

		JLabel eMail = new JLabel(pt.getPtEmail());
		eMail.setBounds(95, 95, 300, 22);
		patientDetails.add(eMail);
		
		JLabel phone = new JLabel(pt.getPtPhone());
		phone.setBounds(95, 125, 300, 22);
		patientDetails.add(phone);
		
		JLabel pClinic = new JLabel(pt.getPtPrivateClinic());
		pClinic.setBounds(95, 155, 90, 22);
		patientDetails.add(pClinic);
		

		JLabel doctorID = new JLabel(pt.getPd());
		doctorID.setBounds(95, 185, 90, 22);
		patientDetails.add(doctorID);
		
		
		
		patientDetails.setVisible(true);
		
	}

	

	/**
	 * Gets the record field.
	 *
	 * @return the record field
	 */
	public String getRecordField() 
	{
		return textRecPane.getText();
	}


	/**
	* Cancell listener of the button.
	*/
	public class CancelListener implements ActionListener 
    {
    	
	    /** 
	     * closes the current frame of the class
	     */
	    @Override
    	public void actionPerformed(ActionEvent e)
    	{
    		dispose();
    	}	
    }//CancelListener
}
