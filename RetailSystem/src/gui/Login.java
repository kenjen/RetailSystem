package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import data.Staff;

import java.awt.GridBagLayout;
import java.util.ArrayList;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JPanel panel;
	private ArrayList<Staff> listOfMemebers = new ArrayList<Staff>();
	private boolean admin = false;
	
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
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridBagLayout());

		JLabel lblUsername = new JLabel("Username");

		textField = new JTextField();
		textField.setColumns(20);

		JLabel lblPassword = new JLabel("Password");

		passwordField = new JPasswordField();
		passwordField.setColumns(20);
		
		//if enter is pressed treat it as if login would be pressed
		passwordField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_ENTER
						&& textField.getText().length() > 0
						&& passwordField.getPassword().length > 0) {
					if (findLoginDetailsFromList()) {
						//redirect to main app, set user type = admin
						new GUIBackBone(admin);
						Login.this.setVisible(false);
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "False");
					}
				}
			}
		});
		

		JButton btnLogin = new JButton("Login");

		panel = new JPanel();

		panel.setLayout(new MigLayout());
		panel.add(lblUsername);
		panel.add(textField, "wrap");
		panel.add(lblPassword);
		panel.add(passwordField, "wrap");
		panel.add(btnLogin, "span, center");

		contentPane.add(panel);

		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (textField.getText().length() > 0 && passwordField.getPassword().length > 0) {
					if (findLoginDetailsFromList()) {
						//redirect to main app, set user type = admin
						new GUIBackBone(admin);
						Login.this.setVisible(false);
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "False");
					}
				}
			}

		});
		
		setVisible(true);
	}
	
	public boolean findLoginDetailsFromList(){
		boolean found = false;
		for(Staff staff:listOfMemebers){
			if(staff.getUsername().equalsIgnoreCase(textField.getText().toString()) &&
					staff.getPassword().equals(new String(passwordField.getPassword())) && 
					staff.isDeleted() == false){
				found = true;
				if(staff.isAdmin()){
					admin=true;
				}
				break;
			}
		}
		return found;
	}

}
