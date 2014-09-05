package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import data.Json;
import data.Staff;

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
	private JComboBox staffComboBox;
	private ArrayList<String> staffMembers;
	
	public int id;
	//loop through the ids in json file till you reach the last value. id = this value

	public StaffPanel() {
		setLayout(new MigLayout("", "[][][][grow][][grow][][][][][][][][]", "[][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]"));
		staffMembers = new ArrayList<String>();
		staffComboBox = new JComboBox(staffMembers.toArray());
		staffComboBox.addItem("");
		for (Staff s : Shop.getStaffMembers()) {
			String name = s.getName() + " " + s.getSurname();
			staffMembers.add(name);
			if (s.isDeleted() == false) {
				staffComboBox.addItem(s.getUsername());
			}
		}
		
		//disable staffComboBox 
		staffComboBox.setEnabled(false);

		saveDetails();

		JLabel lblName = new JLabel("ADD NEW STAFF HERE: ");
		add(lblName, "cell 0 0");

		JLabel lblNewLabel = new JLabel("Name");
		add(lblNewLabel, "cell 0 2,alignx trailing");

		// Get values from each field and add to staffMembers
		nameField = new JTextField();
		add(nameField, "cell 3 2,growx");
		nameField.setColumns(10);
		
		final JCheckBox chckbxEditDelete = new JCheckBox("Edit/Delete");
		add(chckbxEditDelete, "cell 4 2");

		JLabel lblSurname = new JLabel("Surname");
		add(lblSurname, "cell 0 3,alignx trailing");

		surNameField = new JTextField();
		add(surNameField, "cell 3 3,growx");
		surNameField.setColumns(10);
		
		final JCheckBox chckbxAdmin = new JCheckBox("Admin Access");
		add(chckbxAdmin, "cell 4 3");

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
		
		JLabel lblTextGoesHere = new JLabel("");
		add(lblTextGoesHere, "cell 5 5");

		JLabel lblPassword = new JLabel("Password");
		add(lblPassword, "cell 0 6,alignx trailing");

		passwordField = new JPasswordField();
		add(passwordField, "cell 3 6,growx");
		passwordField.setColumns(10);
		

		// Get the Values from each textField and save them to their respective slots in the StaffMenbers Array
		// Add a new Staff Member with these values Reset the form
		final JButton btnAddStaff = new JButton("Add Staff Member");
		btnAddStaff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// Access the Shop class and the staffMembers Array
				ArrayList<Staff> staffMembers = Shop.getStaffMembers();
				
				//Read the staff members from the json file
				//staffMembers = Json.readStaffFromFile();
				
				

				for (Staff s1 : Shop.getStaffMembers()) {
					if (s1.getUsername().equals(userNameField.getText().trim()) && s1.isDeleted() == false) {
						JOptionPane.showMessageDialog(null,
								"Error: UserName Already Exists!!!");
						return;
					}
				}
				
				if (nameField.getText().trim().length() > 0
						&& surNameField.getText().trim().length() > 0
						&& salaryField.getText().trim().length() > 0
						&& userNameField.getText().trim().length() > 0
						&& passwordField.getText().trim().length() > 0) {

					// if text entered to Salary field is not type double, make error message
					try {
						// variables for parsing
						double salaryD = Double.parseDouble(salaryField
								.getText().trim());
						// if the code gets to here, it was recognizable as a double

						// add the values to the array
						Staff s = new Staff(nameField.getText().trim(), surNameField
								.getText().trim(), salaryD, userNameField.getText().trim(),
								passwordField.getText().trim());
						if(chckbxAdmin.isSelected()){
							s.setAdmin(true);	
					}
					
						
				
						


						// Loop through Staff members match username entered to all
						// Usernames in the list. dont allow to add new staff if their username matches any in the list

						// Remove items from Combo Box and re-add the entire list, which contains the new Staff Member
						staffComboBox.removeAllItems();
						staffMembers.add(s);
						staffComboBox.addItem("");

						// Iterate through the Array staffMembers
						for (Staff staff : staffMembers) {

							// Add new Staff To ComboBox "staffComboBox"
							if (staff.isDeleted() == false) {
								staffComboBox.addItem(staff.getUsername());
								// staffComboBox.addItem(s.getName());
								
								//Add null to ComboBox
								//staffComboBox.addItem("");
							}
						}

						resetTextFields();
						saveDetails();
					} catch (NumberFormatException e) {
						JOptionPane
								.showMessageDialog(null,
										"Salary Should Be Of Type Double eg: 10.23 !!!");
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"You must fill in all details");
				}
				
			}
		});

		add(btnAddStaff, "flowx,cell 3 8");

		JLabel lblMembers = new JLabel("Members");
		add(lblMembers, "flowx,cell 0 15");

		// ComboBox Design with AutoComplete

		ArrayList<String> userNames = new ArrayList<String>();
		userNames.add("");

		for (Staff staff : Shop.getStaffMembers()) {
			String name = staff.getUsername();
			userNames.add(name);
		}

		add(staffComboBox, "wrap");
		add(staffComboBox, "cell 1 15,alignx trailing");

		staffComboBox.setEditable(true);
		AutoCompleteDecorator.decorate(staffComboBox);

		staffComboBox.getEditor().getEditorComponent()
				.addKeyListener(new CBListener());
		staffComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Staff selectedStaff = getStaffbyID(e.getItem().toString());
					if (selectedStaff != null) {

						// add each staff members details to respective
						// textfields upon selection
						nameField.setText(selectedStaff.getName().trim());
						surNameField.setText(selectedStaff.getSurname().trim());
						salaryField.setText(String.valueOf(selectedStaff.getSalary()));

						userNameField.setText(selectedStaff.getUsername().trim());
						passwordField.setText(selectedStaff.getPassword());
						chckbxAdmin.setSelected(selectedStaff.isAdmin());
					}

				}

			}
		});
		
		// add photo to left panel
		JLabel supplierImage = new JLabel(new ImageIcon("resources/Working_Together.jpg"));
		add(supplierImage, "flowx,cell 0 23");

		final JButton btnRemove = new JButton("Remove");
		add(btnRemove, "cell 6 30");
		btnRemove.setEnabled(false);

		// Remove Selected Item from ComboBox
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Shop.deleteStaff((String) staffComboBox.getSelectedItem());
				staffComboBox.removeItem(staffComboBox.getSelectedItem());
				saveDetails();
				resetTextFields();
				staffComboBox.setSelectedItem(null);
			}
		});

		// Edit Details
		final JButton btnEditdetails = new JButton("EditDetails");
		add(btnEditdetails, "cell 7 30");
		btnEditdetails.setEnabled(false);

		btnEditdetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				/*for (Staff s1 : Shop.getStaffMembers()) {
					if (s1.getUsername().equals(userNameField.getText().trim())) {
						JOptionPane.showMessageDialog(null,"Error: UserName Already Exists!!!");
						return;
					}
				}*/
				try {
					// variables for parsing
					double salaryD = Double.parseDouble(salaryField.getText().trim());

					// find the selected user by username and get the id.
					// The id never changes
					int id = 0;
					for (Staff staff : Shop.getStaffMembers()) {
						if (staff.getUsername().equals(
								staffComboBox.getSelectedItem())) {
							id = staff.getId();
						}
					}
					
					Shop.EditDetails(nameField.getText().trim(),
							surNameField.getText().trim(), salaryD,
							userNameField.getText().trim(), passwordField.getText().trim(),
							id, chckbxAdmin.isSelected());
					
					saveDetails();
					DefaultComboBoxModel model = (DefaultComboBoxModel) staffComboBox
							.getModel();
					model.removeAllElements();
					for (Staff staff : Shop.getStaffMembers()) {
						model.addElement(staff.getUsername());
					}
					staffComboBox.repaint();
				} catch (NumberFormatException er) {
					JOptionPane.showMessageDialog(null,"All TextFields Must Be Filled And Salary Should Be Of Type Double eg: 10.23 !!!");
				}
			}
		});
		
		
		chckbxEditDelete.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (chckbxEditDelete.isSelected()) {
					btnAddStaff.setEnabled(false);;
					userNameField.setEditable(false);
					btnEditdetails.setEnabled(true);
					btnRemove.setEnabled(true);
					staffComboBox.setEnabled(true);
				}
				else{
					btnAddStaff.setEnabled(true);
					userNameField.setEditable(true);
					btnEditdetails.setEnabled(false);
					btnRemove.setEnabled(false);
					staffComboBox.setEnabled(false);
					resetTextFields();
					//Reset the selected item to null
					staffComboBox.setSelectedItem(null);
				}
			}
		});

	}

	public Staff getStaffbyID(String string) {
		// TODO Auto-generated method stub
		for (Staff staff : Shop.getStaffMembers()) {
			if (staff.getUsername().equalsIgnoreCase(string)) {
				return staff;
			}
		}
		return null;
	}

	
	public void saveDetails(){
		
		Json.clearList("resources/staff.json");
		for(Staff staff : Shop.getStaffMembers()){
			//if(staff.isDeleted() == false){
				Json.saveStaffToFile(staff);
			//}
		}
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

	public class CBListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String username = staffComboBox.getSelectedItem().toString();
				if (getStaffbyID(username) != null) {
					Staff selectedStaff = getStaffbyID(username);
					staffComboBox.setSelectedItem(selectedStaff.getUsername());
				} else {
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

	public void resetTextFields() {
		// Reset each TextField
		nameField.setText("");
		surNameField.setText("");
		salaryField.setText("");
		userNameField.setText("");
		passwordField.setText("");
	}
}
