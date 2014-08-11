package gui;

import java.util.ArrayList;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextPane;




//import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import data.Customer;

//this class deals with customer ordering only.
public class CustomerOrderPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public CustomerOrderPanel() {
		setLayout(new MigLayout());
		ArrayList<String> customerNames = new ArrayList<String>();
		for ( Customer customer: Shop.getCustomers()){
			String name = customer.getCustomerFName()+" "+customer.getCustomerLName();
			customerNames.add(name);
		}
		
		JLabel lblCustomer = new JLabel("Customer:");
		add(lblCustomer);
		
		JComboBox comboBox = new JComboBox(customerNames.toArray());
		add(comboBox);
		comboBox.setEditable(true);
		//AutoCompleteDecorator.decorate(comboBox);
		
		
		
	}

}