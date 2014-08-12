package gui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
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

import data.Staff;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class StaffPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField surNameField;
	private JTextField salaryField;
	private JTextField userNameField;
	private JTextField passwordField;

	public StaffPanel() {
		setLayout(new MigLayout("", "[][grow][grow][][][][][][][][]", "[][][][][][][][][][][][][][][][][][][][][][][][][][][]"));
		
		ArrayList<String> staffMembers = new ArrayList<String>();
		for(Staff s : Shop.getStaffMembers()){
			String name = s.getName() +" "+ s.getSurname();
			staffMembers.add(name);
		}
		
		JLabel lblName = new JLabel("ADD NEW STAFF HERE: ");
		add(lblName, "cell 0 0");
		
		JLabel lblNewLabel = new JLabel("Name");
		add(lblNewLabel, "cell 0 2,alignx trailing");
		
		nameField = new JTextField();
		add(nameField, "cell 1 2,growx");
		nameField.setColumns(10);
		
		JLabel lblSurname = new JLabel("Surname");
		add(lblSurname, "cell 0 3,alignx trailing");
		
		surNameField = new JTextField();
		add(surNameField, "cell 1 3,growx");
		surNameField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Salary");
		add(lblNewLabel_1, "cell 0 4,alignx trailing");
		
		salaryField = new JTextField();
		add(salaryField, "cell 1 4,growx");
		salaryField.setColumns(10);
		
		JLabel lblUsername = new JLabel("UserName");
		add(lblUsername, "cell 0 5,alignx trailing");
		
		userNameField = new JTextField();
		add(userNameField, "cell 1 5,growx");
		userNameField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		add(lblPassword, "cell 0 6,alignx trailing");
		
		passwordField = new JTextField();
		add(passwordField, "cell 1 6,growx");
		passwordField.setColumns(10);
		
		JButton btnAddStaff = new JButton("Add Staff Member");
		btnAddStaff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		add(btnAddStaff, "flowx,cell 1 8");
		
		JLabel lblMembers = new JLabel("Members");
		add(lblMembers, "cell 0 14");
		
		JComboBox staffComboBox = new JComboBox(staffMembers.toArray());
		add(staffComboBox, "wrap");
		add(staffComboBox, "cell 1 14,growx");
		
		JButton btnRemove = new JButton("Remove");
		add(btnRemove, "cell 3 25");
		
		JButton btnEditdetails = new JButton("EditDetails");
		add(btnEditdetails, "cell 4 25");
		
	}
	

}