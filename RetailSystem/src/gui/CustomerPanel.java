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
import data.Json;
import data.Product;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

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
	private JButton customerDelete;
	private JButton customerEdit;
	private JLabel lblCustomer;
	private JLabel lblCustomerId;
	private static JComboBox comboBox;
	private JCheckBox chckbxEditdelete;
	private JLabel lblEnterSurnameTo;
	private JTextField searchString;
	private JButton btnSearch;
	private static JComboBox comboSelectCustomer;
	private Customer selectedCustomer = null;

	@SuppressWarnings("null")
	public CustomerPanel() {
		setLayout(new MigLayout("", "[62px][200px,grow][]",
				"[22px][][][][][][][][][]"));

		lblCustomer = new JLabel("CUSTOMER DETAILS");
		lblCustomer.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(lblCustomer, "cell 1 0");

		// ADD CUSTOMER
		btnNewButton = new JButton("Add Customer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createNewCustomer();
			}

		});

		lblCustomerId = new JLabel("Customer ID");
		add(lblCustomerId, "cell 0 2,alignx trailing");

		// COMBOBOX POPULATION
		ArrayList<Integer> num = new ArrayList<Integer>();
		
		for (Customer customer : Shop.getCustomers()) {
			if(customer.isDeleted()==false){
			num.add(customer.getCustomerID());
			}
		}
		Integer[] ids = new Integer[num.size()];
		ids = num.toArray(ids);
		comboBox = new JComboBox(ids);
		comboBox.setEnabled(false);
		comboBox.setPreferredSize(new Dimension(225, 20));
		add(comboBox, "flowx,cell 1 2,alignx left,growy");

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int chosen = (Integer) comboBox.getSelectedItem();
				System.out.println(chosen);

				for (Customer customer : Shop.getCustomers()) {
					if (customer.getCustomerID() == chosen
							&& customer.isDeleted() == false) {
						String name = customer.getCustomerFName();
						String surname = customer.getCustomerLName();
						String address = customer.getCustomerAddress();
						String mobile = customer.getCustomerMobile();
						String home = customer.getCustomerHome();

						fNameInput.setText(name);
						lNameInput.setText(surname);
						addressInput.setText(address);
						mobileInput.setText(mobile);
						homeInput.setText(home);
					}
				}
			}
		});

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

		// EDIT CUSTOMER
		customerEdit = new JButton("Edit Customer");
		add(customerEdit, "cell 1 8");
		customerEdit.setEnabled(false);
		customerEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editCustomer();
			}

		}

		);

		// DELETE CUSTOMER
		customerDelete = new JButton("Delete Customer");
		add(customerDelete, "cell 1 8");
		customerDelete.setEnabled(false);
		customerDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteCustomer();
				/*
				 * fNameInput.setText(""); lNameInput.setText("");
				 * addressInput.setText(""); mobileInput.setText("");
				 * homeInput.setText("");
				 */
				
				saveDetails();
				refreshCombo();
			}

			public void deleteCustomer() {
				Shop.deleteCustomer((Integer) comboBox.getSelectedItem());
				String fname = fNameInput.getText();
				String lname = lNameInput.getText();
				comboBox.removeItem(comboBox.getSelectedItem());
				JOptionPane.showMessageDialog(null, "You have deleted "
						+ fname + " " + lname);
				//saveDetails();
				//refreshCombo();
			}
	
		});

		// SEARCH BY SURNAME
		lblEnterSurnameTo = new JLabel("Enter Surname to find:");
		add(lblEnterSurnameTo, "flowx,cell 1 9");
		/*
		 * searchString = new JTextField(); add(searchString,
		 * "cell 1 9,alignx trailing"); searchString.setColumns(10);
		 */

		ArrayList<String> surnames = new ArrayList<String>();
		surnames.add("");

		for (Customer customer : Shop.getCustomers()) {
			if(customer.isDeleted()==false){
			String name = customer.getCustomerLName();
			surnames.add(name);
			}
		}

		comboSelectCustomer = new JComboBox(surnames.toArray());
		add(comboSelectCustomer, "cell 1 9,alignx trailing");

		comboSelectCustomer.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectCustomer);
		comboSelectCustomer.getEditor().getEditorComponent()
				.addKeyListener(new CBListener());
		comboSelectCustomer.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Customer customer = getCustomerbyID(e.getItem().toString());
					if (customer != null) {
						selectedCustomer = customer;
						comboBox.setSelectedItem(selectedCustomer
								.getCustomerID());
					}

				}

			}

		});

		/*
		 * btnSearch = new JButton("Search"); add(btnSearch, "cell 1 9");
		 * btnSearch.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent arg0) { ArrayList<Integer> idLocations =
		 * new ArrayList<Integer>(); String find = searchString.getText(); for
		 * (Customer customer : Shop.getCustomers()) { if
		 * (find.equals(customer.getCustomerLName())) {
		 * idLocations.add(customer.getCustomerID());
		 * comboBox.setSelectedItem(customer.getCustomerID());
		 * 
		 * // System.out.println(idLocations); } } if (idLocations.size() == 0)
		 * { JOptionPane .showMessageDialog(null,
		 * "There are no customers of this name in the system"); } else {
		 * JOptionPane .showMessageDialog(null,
		 * "Customer "+idLocations+" have the surname "+find); } } });
		 */

		// EDIT/DELETE CHECKBOX
		chckbxEditdelete = new JCheckBox("Edit/Delete");
		add(chckbxEditdelete, "cell 1 2");
		chckbxEditdelete.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (chckbxEditdelete.isSelected()) {
					comboBox.enable(true);
					btnNewButton.setEnabled(false);
					;
					customerDelete.setEnabled(true);
					customerEdit.setEnabled(true);
					;
				} else {
					comboBox.enable(false);
					btnNewButton.setEnabled(true);
					customerDelete.setEnabled(false);
					customerEdit.setEnabled(false);
					fNameInput.setText("");
					lNameInput.setText("");
					addressInput.setText("");
					mobileInput.setText("");
					homeInput.setText("");
					;
				}
			}

		});

	}

	public void saveDetails() {
		// TODO Auto-generated method stub
		Json.clearList("resources/customers.json");
		for (Customer customer : Shop.getCustomers()) {
			Json.saveCustomerToFile(customer);
		}
		System.out.println("Finished save");
	}

	public Customer getCustomerbyID(String string) {
		// TODO Auto-generated method stub
		for (Customer customer : Shop.getCustomers()) {
			if (customer.getCustomerLName().equalsIgnoreCase(string)) {
				return customer;
			}
		}
		return null;
	}

	public class CBListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String surname = comboSelectCustomer.getSelectedItem()
						.toString();
				if (getCustomerbyID(surname) != null) {
					selectedCustomer = getCustomerbyID(surname);
					comboBox.setSelectedItem(selectedCustomer.getCustomerID());
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

	public void createNewCustomer() {
		if (fNameInput.getText().length() > 0
				&& lNameInput.getText().length() > 0
				&& addressInput.getText().length() > 0
				&& (mobileInput.getText().length() > 0 || homeInput.getText()
						.length() > 0)) {
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
			JOptionPane.showMessageDialog(null, "You have added a customer!");
			comboBox.addItem(c1.getCustomerID());
			
			//refreshCombo();
			fNameInput.setText("");
			lNameInput.setText("");
			addressInput.setText("");
			mobileInput.setText("");
			homeInput.setText("");

		} else {
			JOptionPane.showMessageDialog(null, "Please input into all fields");
		}
		saveDetails();
		refreshCombo();
	}

	public void editCustomer() {
		if (fNameInput.getText().length() > 0
				&& lNameInput.getText().length() > 0
				&& addressInput.getText().length() > 0
				&& (mobileInput.getText().length() > 0 || homeInput.getText()
						.length() > 0)) {
		Shop.editCustomer((Integer) comboBox.getSelectedItem(),
				fNameInput.getText(), lNameInput.getText(),
				addressInput.getText(), mobileInput.getText(),
				homeInput.getText());

		fNameInput.setText("");
		lNameInput.setText("");
		addressInput.setText("");
		mobileInput.setText("");
		homeInput.setText("");
		JOptionPane.showMessageDialog(null, "You have editted customer "
				+ comboBox.getSelectedItem());
		}else {
			JOptionPane.showMessageDialog(null,
					"You have to fill in all fields ");
		}
		saveDetails();
		refreshCombo();
	}

	// refresh combo box
	public static void refreshCombo() {
		ArrayList<String> surnames = new ArrayList<String>();
		surnames.add("");

		for (Customer customer : Shop.getCustomers()) {
			if (!customer.isDeleted()) {
			String name = customer.getCustomerLName();
			surnames.add(name);
			}
			
		}
			comboSelectCustomer.removeAllItems();
			for (String current : surnames){
				comboSelectCustomer.addItem(current);
			}
		
		
	}
	


}
