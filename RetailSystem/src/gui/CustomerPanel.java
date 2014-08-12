package gui;

import java.awt.Dimension;

import javax.swing.ComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import data.Customer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class CustomerPanel extends JPanel {

	private JLabel customerFName;
	private JTextField fNameInput;
	private JLabel customerLName;
	private JTextField lNameInput;
	private JLabel customerMobile;
	private JTextField mobileInput;
	private JLabel customerHome;
	private JTextField homeInput;
	private JLabel customerAddress;
	private JTextArea addressInput;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JLabel lblCustomer;
	private JLabel lblCustomerId;
	private JComboBox comboBox;
	private JCheckBox chckbxEditdelete;

	public CustomerPanel() {
		setLayout(new MigLayout("", "[62px][200px,grow]",
				"[22px][][][][][][][][]"));

		lblCustomer = new JLabel("CUSTOMER DETAILS");
		lblCustomer.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(lblCustomer, "cell 1 0");

		btnNewButton = new JButton("Add Customer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String fname = fNameInput.getText();
				String lname = lNameInput.getText();
				String address = addressInput.getText();
				String mobile = mobileInput.getText();
				String home = homeInput.getText();

				Customer c1 = new Customer(fname, lname, address, mobile, home);
				Shop.getCustomers().add(c1);
				for (Customer customer : Shop.getCustomers()) {
					System.out.println(customer.getCustomerID()
							+ customer.getCustomerLName() + " "
							+ customer.getCustomerAddress());
				}
				JOptionPane.showMessageDialog(null,
						"You have added a customer!");

				fNameInput.setText("");
				lNameInput.setText("");
				addressInput.setText("");
				mobileInput.setText("");
				homeInput.setText("");

			}
		});

		lblCustomerId = new JLabel("Customer ID");
		add(lblCustomerId, "cell 0 2,alignx trailing");
		comboBox = new JComboBox();
		comboBox.setEnabled(false);
		comboBox.setPreferredSize(new Dimension(225, 20));
		add(comboBox, "flowx,cell 1 2,alignx left, growy");

		customerFName = new JLabel("First Name");
		add(customerFName, "cell 0 3,alignx left,aligny center");
		fNameInput = new JTextField();
		fNameInput.setPreferredSize(new Dimension(225, 20));
		add(fNameInput, "cell 1 3,alignx left,growy");

		customerLName = new JLabel("Surname");
		add(customerLName, "cell 0 4,alignx trailing");
		lNameInput = new JTextField();
		lNameInput.setPreferredSize(new Dimension(225, 20));
		add(lNameInput, "cell 1 4,alignx left,growy");

		customerAddress = new JLabel("Address");
		add(customerAddress, "cell 0 5");
		addressInput = new JTextArea();
		addressInput.setColumns(20);
		addressInput.setRows(4);
		add(addressInput, "cell 1 5,alignx left");

		customerMobile = new JLabel("Mobile No.");
		add(customerMobile, "cell 0 6,alignx trailing");
		mobileInput = new JTextField();
		mobileInput.setPreferredSize(new Dimension(225, 20));
		add(mobileInput, "cell 1 6,alignx left,growy");

		customerHome = new JLabel("Home No.");
		add(customerHome, "cell 0 7,alignx trailing");
		homeInput = new JTextField();
		homeInput.setPreferredSize(new Dimension(225, 20));
		add(homeInput, "cell 1 7,alignx left,growy");
		add(btnNewButton, "flowx,cell 1 8");

		btnNewButton_2 = new JButton("Edit Customer");
		add(btnNewButton_2, "cell 1 8");

		btnNewButton_1 = new JButton("Delete Customer");
		add(btnNewButton_1, "cell 1 8");

		chckbxEditdelete = new JCheckBox("Edit/Delete");
		add(chckbxEditdelete, "cell 1 2");
		chckbxEditdelete.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(chckbxEditdelete.isSelected()){
					comboBox.enable(true);
				}else{
					comboBox.enable(false);
				}
			}

		});
	}

}
