package test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

import org.junit.Test;

import gui.Login;
import gui.Shop;
import data.Staff;

public class LoginTest {
	private ArrayList<Staff> listOfMembers;
	private JTextField txtUserName;
	private JPasswordField passwordField;
	private boolean admin = false;
	private Staff loggedStaffMember;
	private Timer timer;
	private JLabel lblError;
	
	public LoginTest(){
		Shop shop = new Shop();
		shop.populateStaffMembers();
		listOfMembers = Shop.getStaffMembers();
		txtUserName = new JTextField(Shop.getStaffMembers().get(0).getUsername());
		passwordField = new JPasswordField(Shop.getStaffMembers().get(0).getPassword());	
		lblError = new JLabel();
	}
	
	public boolean findLoginDetailsFromList(){
		boolean found = false;
		for(Staff staff:listOfMembers){
			if(staff.getUsername().equalsIgnoreCase(txtUserName.getText().toString()) &&
					staff.getPassword().equals(new String(passwordField.getPassword())) && 
					staff.isDeleted() == false){
				found = true;
				if(staff.isAdmin()){
					admin=true;
				}
				loggedStaffMember = staff;
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
	
	@Test
	public void checkIfEnteredUserExist_True(){
		assertTrue(findLoginDetailsFromList());
	}
	
	@Test
	public void displayErrorMessageByPassingAString_LabelErrorTextChanges(){
		displayErrorMessage("testing");
		assertEquals("testing", lblError.getText());
	}
}
