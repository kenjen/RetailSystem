package gui;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import data.Staff;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUserName;
	private JPasswordField passwordField;
	private JPanel panel;
	private Timer timer;
	private JLabel lblError;
	private ArrayList<Staff> listOfMemebers = new ArrayList<Staff>();
	private boolean admin = false;
	private Staff loggedStaffMember;
	
	public Login(ArrayList<Staff> members){
		listOfMemebers = members;
	}
	
		
	/**
	 * Create the frame.
	 */
	public void drawFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("Login");
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridBagLayout());

		JLabel lblUsername = new JLabel("Username");

		txtUserName = new JTextField();
		txtUserName.setColumns(20);

		JLabel lblPassword = new JLabel("Password");

		passwordField = new JPasswordField();
		passwordField.setColumns(20);
		
		//if enter is pressed treat it as if login would be pressed
		passwordField.addFocusListener(new FocusListener() {	
			@Override
			public void focusLost(FocusEvent arg0) {
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				//passwordField.setText("");
				passwordField.selectAll();
			}
		});
		txtUserName.addFocusListener(new FocusListener() {	
			@Override
			public void focusLost(FocusEvent arg0) {
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				//passwordField.setText("");
				txtUserName.selectAll();
			}
		});
		passwordField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER
						&& txtUserName.getText().length() > 0
						&& passwordField.getPassword().length > 0) {
					if (findLoginDetailsFromList()) {
						//redirect to main app, set user type = admin
						new GUIBackBone(admin, loggedStaffMember);
						Login.this.setVisible(false);
						dispose();
					} else {
						displayErrorMessage("");
					}
				}
			}
		});
		
		txtUserName.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER
						&& txtUserName.getText().length() > 0
						&& passwordField.getPassword().length > 0) {
					if (findLoginDetailsFromList()) {
						//redirect to main app, set user type = admin
						new GUIBackBone(admin, loggedStaffMember);
						Login.this.setVisible(false);
						dispose();
					} else {
						displayErrorMessage("");
					}
				}
			}
		});
		

		JButton btnLogin = new JButton("Login");
		lblError = new JLabel("Incorrect details!");
		lblError.setForeground(Color.RED);
		lblError.setVisible(false);
		
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/Lock.png"));

		panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.add(new JLabel(icon),"pushx, alignx center, wrap, span");
		panel.add(lblUsername);
		panel.add(txtUserName, "wrap");
		panel.add(lblPassword);
		panel.add(passwordField, "wrap");
		panel.add(btnLogin, "span, center");
		panel.add(lblError, "span, center");

		contentPane.add(panel);

		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (txtUserName.getText().length() > 0 && passwordField.getPassword().length > 0) {
					if (findLoginDetailsFromList()) {
						//redirect to main app, set user type = admin
						new GUIBackBone(admin, loggedStaffMember);
						Login.this.setVisible(false);
						dispose();
					} else {
						displayErrorMessage("");
					}
				}
			}

		});
		
		//Populate login details for quick login, Remove this when project is complete
		txtUserName.setText("admin");
		passwordField.setText("admin");
		
		setVisible(true);
	}
	
	public boolean findLoginDetailsFromList(){
		boolean found = false;
		for(Staff staff:listOfMemebers){
			if(staff.getUsername().equalsIgnoreCase(txtUserName.getText().toString()) &&
					staff.getPassword().equals(new String(passwordField.getPassword())) && 
					staff.isDeleted() == false){
				found = true;
				if(staff.isAdmin()){
					admin=true;
				}
				Login.this.loggedStaffMember = staff;
				break;
			}
		}
		return found;
	}
	
	public void displayErrorMessage(String error){
		if(error != ""){
			lblError.setText(error);
		}
		setVisible(lblError);
		timer = new Timer(2000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(lblError);
				timer.stop();
			}
			
		});
		timer.start();
	}
	
	public void setVisible(Object o){
		JLabel error = (JLabel) o;
		if(error.isVisible()==true){
			error.setVisible(false);
		}else{
			error.setVisible(true);
		}
	}

}
