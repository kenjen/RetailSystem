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
	private JButton customerDelete;
	private JButton customerEdit;
	private JLabel lblCustomer;
	private JLabel lblCustomerId;
	private JComboBox comboBox;
	private JCheckBox chckbxEditdelete;
	private JLabel lblEnterSurnameTo;
	private JTextField searchString;
	private JButton btnSearch;

	@SuppressWarnings("null")
	public CustomerPanel() {
		setLayout(new MigLayout("", "[62px][200px,grow][]",
				"[22px][][][][][][][][][]"));

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
				comboBox.addItem(c1.getCustomerID());
				fNameInput.setText("");
				lNameInput.setText("");
				addressInput.setText("");
				mobileInput.setText("");
				homeInput.setText("");

			}
		});

		lblCustomerId = new JLabel("Customer ID");
		add(lblCustomerId, "cell 0 2,alignx trailing");
		ArrayList<Integer> num = new ArrayList<Integer>();
		Integer[] ids = new Integer[Shop.getCustomers().size()];
		for (Customer customer : Shop.getCustomers()) {

			num.add(customer.getCustomerID());
		}
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

		customerEdit = new JButton("Edit Customer");
		add(customerEdit, "cell 1 8");
		customerEdit.setEnabled(false);
		customerEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Shop.editCustomer((Integer) comboBox.getSelectedItem(),
						fNameInput.getText(), lNameInput.getText(),
						addressInput.getText(), mobileInput.getText(),
						homeInput.getText());

				fNameInput.setText("");
				lNameInput.setText("");
				addressInput.setText("");
				mobileInput.setText("");
				homeInput.setText("");
				JOptionPane.showMessageDialog(
						null,
						"You have editted customer "
								+ comboBox.getSelectedItem());
			}

		}

		);

		customerDelete = new JButton("Delete Customer");
		add(customerDelete, "cell 1 8");
		customerDelete.setEnabled(false);
		customerDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Shop.deleteCustomer((Integer) comboBox.getSelectedItem());
				comboBox.removeItem(comboBox.getSelectedItem());
				/*
				 * fNameInput.setText(""); lNameInput.setText("");
				 * addressInput.setText(""); mobileInput.setText("");
				 * homeInput.setText("");
				 */

			}
		});

		lblEnterSurnameTo = new JLabel("Enter Surname to find:");
		add(lblEnterSurnameTo, "flowx,cell 1 9");
		searchString = new JTextField();
		add(searchString, "cell 1 9,alignx trailing");
		searchString.setColumns(10);
		btnSearch = new JButton("Search");
		add(btnSearch, "cell 1 9");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Integer> idLocations = new ArrayList<Integer>();
				String find = searchString.getText();
				for (Customer customer : Shop.getCustomers()) {
					if (find.equals(customer.getCustomerLName())) {
						idLocations.add(customer.getCustomerID());
						// System.out.println(idLocations);
					}
				}
				if (idLocations.size() == 0) {
					JOptionPane
							.showMessageDialog(null,
									"There are no customers of this name in the system");
				} else {
					JOptionPane
					.showMessageDialog(null,
							"Customer "+idLocations+" have the surname "+find);
				}
			}
		});

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
					;
				}
			}

		});
	}

}
