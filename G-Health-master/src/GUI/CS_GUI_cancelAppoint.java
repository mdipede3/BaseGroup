package GUI;

import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.LoginControl;
import models.Patient;

import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLayeredPane;
import javax.swing.JButton;
import java.awt.Font;

/**
 * @author G5 lab group
 * The Class  costumer service of cancel Appointment.
 */
public class CS_GUI_cancelAppoint extends LoggingOut{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1645191120165568000L;
	
	/** The content pane. */
	private JPanel contentPane;
	
	/** The patient details. */
	private JPanel patientDetails;
	
	/** The combo box. */
	private JComboBox comboBox;
	
	/** The btn cancel appointment. */
	private JButton btnCancelAppointment;
	
	/** The btn cancel appoint. */
	private JButton btnCancelAppoint;
	
	/**
	 * Instantiates a new c s_ gu i_cancel appoint.
	 */
	public CS_GUI_cancelAppoint() {
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
		
		comboBox = new JComboBox();
		comboBox.setBounds(58, 147, 443, 24);
		contentPane.add(comboBox);
		
		JLabel lblPleaseSelectAppointment = new JLabel("Please select appointment to cancel");
		lblPleaseSelectAppointment.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPleaseSelectAppointment.setBounds(87, 112, 359, 24);
		contentPane.add(lblPleaseSelectAppointment);
		
		btnCancelAppointment = new JButton("Cancel Selected Appointment");
		btnCancelAppointment.setFont(new Font("Tahoma", Font.PLAIN, 23));
		btnCancelAppointment.setBounds(87, 224, 391, 65);
		contentPane.add(btnCancelAppointment);
		JLabel lblLogo;
		if(LoginControl.getUser_full_name() == null)
			lblLogo = new JLabel("Welcome CS!");
		else lblLogo = new JLabel("Hi "+LoginControl.getUser_full_name()+"!");
		
		btnCancelAppoint = new JButton("CLOSE WINDOW");
		btnCancelAppoint.setBounds(202, 470, 172, 68);
		contentPane.add(btnCancelAppoint);
		btnCancelAppoint.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
			}
		});

		setLocationRelativeTo(null);
		
		setVisible(true);
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
	 * Gets the btn cancel appointment.
	 *
	 * @return the btn cancel appointment
	 */
	public JButton getbtnCancelAppointment()
	{
		return btnCancelAppointment;
	}

	/**
	 * Gets the combo box.
	 *
	 * @return the combo box
	 */
	public JComboBox getcomboBox()
	{
		return comboBox;
	}
}
