package gui;

import gui.CustomerPanel.CBListener;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;

import net.miginfocom.swing.MigLayout;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BoxLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import data.Customer;
import data.JsonExample;
import data.Product;
import data.Staff;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
public class StaffPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField surNameField;
	private JTextField salaryField;
	private JTextField userNameField;
	private JTextField passwordField;

	private Staff activeMember;

	ArrayList<String> staffMembers = new ArrayList<String>();
	JComboBox staffComboBox = new JComboBox(staffMembers.toArray());
	
	//this.comboBox = new JComboBox(new Object[] { "Ester", "Jordi",
	//        "Jordina", "Jorge", "Sergi" });
	//AutoCompleteDecorator.decorate(this.comboBox);

	// private Shop shop;

	public StaffPanel() {
		setLayout(new MigLayout("", "[][][][grow][grow][][][][][][][][]", "[][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]"));

		for (Staff s : Shop.getStaffMembers()) {
			String name = s.getName() + " " + s.getSurname();
			staffMembers.add(name);
			if (s.isDeleted() == false) {
				staffComboBox.addItem(s.getUsername());
			}
		}
		
		saveDetails();

		JLabel lblName = new JLabel("ADD NEW STAFF HERE: ");
		add(lblName, "cell 0 0");

		JLabel lblNewLabel = new JLabel("Name");
		add(lblNewLabel, "cell 0 2,alignx trailing");

		// Get values from each field and add to staffMembers
		nameField = new JTextField();
		add(nameField, "cell 3 2,growx");
		nameField.setColumns(10);

		JLabel lblSurname = new JLabel("Surname");
		add(lblSurname, "cell 0 3,alignx trailing");

		surNameField = new JTextField();
		add(surNameField, "cell 3 3,growx");
		surNameField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Salary");
		add(lblNewLabel_1, "cell 0 4,alignx trailing");

		salaryField = new JTextField();
		add(salaryField, "cell 3 4,growx");
		salaryField.setColumns(10);

		JLabel lblUsername = new JLabel("UserName");
		add(lblUsername, "cell 0 5,alignx trailing");

		userNameField = new JTextField();
		add(userNameField, "cell 3 5,growx");
		userNameField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		add(lblPassword, "cell 0 6,alignx trailing");

		passwordField = new JPasswordField();
		add(passwordField, "cell 3 6,growx");
		passwordField.setColumns(10);

		// Get the Values from each textField and save them to their respective
		// slots in the StaffMenbers Array
		// Add a new Staff Member with these values
		// Reset the form
		JButton btnAddStaff = new JButton("Add Staff Member");
		btnAddStaff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (nameField.getText().length() > 0
						&& surNameField.getText().length() > 0
						&& salaryField.getText().length() > 0
						&& userNameField.getText().length() > 0
						&& passwordField.getText().length() > 0) {

					// variables for parsing
					double salaryD = Double.parseDouble(salaryField.getText());

					// add the values to the array
					Staff s = new Staff(nameField.getText(), surNameField
							.getText(), salaryD, userNameField.getText(),
							passwordField.getText());
					
					// Access the Shop class and the staffMembers Array
					// shop = new Shop();
					ArrayList<Staff> staffMembers = Shop.getStaffMembers();

					// Remove items from Combo Box and re-add the entire list,
					// which
					// contains the new Staff Member
					staffComboBox.removeAllItems();
					staffMembers.add(s);

					// Iterate through the Array staffMembers
					for (Staff staff : staffMembers) {
						System.out.println("Staff" + staff.getName());

						// Add new Staff To ComboBox "staffComboBox"
						if (staff.isDeleted() == false) {
							staffComboBox.addItem(staff.getUsername());
							// staffComboBox.addItem(s.getName());
						}
					}

					// Reset each TextField
					nameField.setText("");
					surNameField.setText("");
					salaryField.setText("");
					userNameField.setText("");
					passwordField.setText("");

					System.out.println("New Staff Member Added");
				} else {
					System.out
							.println("Invalid. Please Make Sure you fill in each TextField");
				}
				saveDetails();
			}
		});
		add(btnAddStaff, "flowx,cell 3 8");
		
				JLabel lblMembers = new JLabel("Members");
				add(lblMembers, "flowx,cell 0 15");
				
				//ComboBox Design with AutoComplete
				
				ArrayList<String> userNames = new ArrayList<String>();
				userNames.add("");
				
				for (Staff staff: Shop.getStaffMembers()){
					String name = staff.getUsername();
					userNames.add(name);
				}
				
				add(staffComboBox, "wrap");
				add(staffComboBox, "cell 1 15,alignx trailing");
				
				staffComboBox.setEditable(true);
				AutoCompleteDecorator.decorate(staffComboBox);
				
				staffComboBox.getEditor().getEditorComponent().addKeyListener(new CBListener());
				staffComboBox.addItemListener(new ItemListener(){

								@Override
								public void itemStateChanged(ItemEvent e) {
									// TODO Auto-generated method stub
									if(e.getStateChange() == ItemEvent.SELECTED){
										Staff selectedStaff = getStaffbyID(e.getItem().toString());
										if(selectedStaff != null){
											//Staff selectedStaff = staff;
											//staffComboBox.setSelectedItem(selectedStaff.getUsername());
											
											//Object selectedStaff1 = staffComboBox.getSelectedItem();
											
											System.out.println("Combo Select: " + selectedStaff);
							
											//for (Staff s : Shop.getStaffMembers()) {
							
												
												//add each staff members details to respective textfields upon selection
												//if (s.getUsername().equals(selectedStaff)) {
													System.out.println(selectedStaff.getUsername());
													System.out.println("SelectedStaff: " +selectedStaff);
													// Shop.deleteStaff((String)
													// staffComboBox.getSelectedItem());
													// staffComboBox.removeItem(staffComboBox.getSelectedItem());
							
													nameField.setText(selectedStaff.getName());
													surNameField.setText(selectedStaff.getSurname());
													salaryField.setText(String.valueOf(selectedStaff.getSalary()));
													
													userNameField.setText(selectedStaff.getUsername());
													passwordField.setText(selectedStaff.getPassword());
													//System.out.println("Here");
												//}
											//}
											
										}
										
									}
									
								}								
							});
	
				

								// selectedStaff = null;					

		JButton btnRemove = new JButton("Remove");
		add(btnRemove, "cell 5 28");

		// Remove Selected Item from ComboBox
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Shop.deleteStaff((String) staffComboBox.getSelectedItem());
				staffComboBox.removeItem(staffComboBox.getSelectedItem());
				saveDetails();
			}
		});
		

		// Edit Details
		JButton btnEditdetails = new JButton("EditDetails");
		add(btnEditdetails, "cell 6 28");

		btnEditdetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// variables for parsing
				double salaryD = Double.parseDouble(salaryField.getText());

				System.out.println("Cannot Change userName");
				Shop.EditDetails(nameField.getText(), surNameField.getText(),
						salaryD, userNameField.getText(),
						passwordField.getText());
				
				saveDetails();

			}
		});

	}
	
	public Staff getStaffbyID(String string) {
		// TODO Auto-generated method stub
		for(Staff staff :Shop.getStaffMembers()){
			if(staff.getUsername().equalsIgnoreCase(string)){
				return staff;
			}
		}
		return null;
	}
	
	public void saveDetails(){
		
		JsonExample.clearList("resources/staff.json");
		for(Staff staff : Shop.getStaffMembers()){
			if(staff.isDeleted() == false){
				JsonExample.saveStaffToFile(staff);
			}
		}
		System.out.println("Finished save");
	}

	// Method to retrieve the name from The Array staffMembers in the Shop class
	public Staff getStaffName(String name) {
		for (Staff staff : Shop.getStaffMembers()) {
			String sName = staff.getName();
			if (sName.equalsIgnoreCase(name)) {
				return staff;
			}
		}
		return null;
	}
	public class CBListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
			String username = staffComboBox.getSelectedItem().toString();
			if(getStaffbyID(username) != null){
				Staff selectedStaff = getStaffbyID(username);
				staffComboBox.setSelectedItem(selectedStaff.getUsername());
			}else{
				System.out.println("ERROR");
			}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}