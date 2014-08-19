package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.JsonExample;
import data.Product;
import data.ProductToOrder;
import data.Supplier;


public class SupplierPanel extends JPanel{
	// declaring instance variables
	private Supplier supplier;
	private static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	private static ArrayList<Product> products = new ArrayList<Product>();
	private ArrayList<Supplier> deletedSuppliers = new ArrayList<Supplier>();
	
	private JLabel title;
	private DefaultListModel listModel ;
	private JList suppliersList;
	private JLabel idLabel;
	private JLabel nameLabel;
	private JLabel addressLabel;
	private JTextField idField;
	private JTextField nameField;
	private JTextField addressField;
	private JTextField editIdField;
	private JTextField editNameField;
	private JTextField editAddressField;
	private static final String removeSupplier = "REMOVE Supplier";
	private JButton remove;
	private static final String editSupplier = "EDIT Supplier";
	private JButton edit;


	public SupplierPanel() {
		// set panel layout
		super(new GridLayout(4,0));
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		// call method to populate the suppliers list
		populateSuppliers();

		// create panel for the suppliers list
		JPanel listPanel = new JPanel();
		listPanel.setBackground(Color.WHITE);
		listPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		// create list type
		listModel = new DefaultListModel();
		suppliersList = new JList(listModel);
		suppliersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		suppliersList.setVisibleRowCount(-1);
		suppliersList.setSelectedIndex(0);
		JButton showS = new JButton("Show Suppliers");
		// add action listener to button
		showS.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showSuppliers();
			}
		});
		listPanel.add(showS);
		// create labels
		title= new JLabel("SUPPLIERS");	
		listPanel.add(title,BorderLayout.PAGE_START);
		idLabel = new JLabel(" ID Number");
		nameLabel = new JLabel(" Name");
		addressLabel = new JLabel(" Address");
		//add labels to list
		listPanel.add(idLabel);
		listPanel.add(nameLabel);
		listPanel.add(addressLabel);
	
		
		// add scroll to list
		JScrollPane listScroller = new JScrollPane(suppliersList);
		listPanel.add(listScroller);
		listPanel.add(suppliersList);
		add(listPanel, BorderLayout.PAGE_START);
		
		
		// create GUI panel for adding new supplier
		JPanel addPanel= new JPanel();
		//addPanel.setBackground(Color.lightGray);
		addPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		JLabel newSupplier = new JLabel("CREATE NEW SUPPLIER");
		addPanel.add(newSupplier);
		JButton add = new JButton("SUBMIT");
		// add action listener to button
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createSupplier();
			}
		});
		// create labels and textfields for user input
		idLabel = new JLabel(" ID Number");
		nameLabel = new JLabel(" Name");
		addressLabel = new JLabel(" Address");
		idField = new JTextField(10);
		nameField = new JTextField(10);
		addressField = new JTextField(30);
		addPanel.add(idLabel);
		addPanel.add(idField);
		addPanel.add(nameLabel);
		addPanel.add(nameField);
		addPanel.add(addressLabel);
		addPanel.add(addressField);
		addPanel.add(add);
		// add button to delete supplier
		remove = new JButton(removeSupplier);
		remove.setActionCommand(removeSupplier);
		remove.addActionListener(new Remove());
		addPanel.add(remove);	
		add(addPanel, BorderLayout.CENTER);
		
		
		// create GUI panel to edit supplier details
		JPanel editPanel= new JPanel();
		//editPanel.setBackground(Color.lightGray);
		editPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		JLabel editS = new JLabel("EDIT SUPPLIER DETAILS");
		editPanel.add(editS);
		JButton edit = new JButton(editSupplier);
		edit.setActionCommand(editSupplier);
		edit.addActionListener(new Edit());
		editPanel.add(edit);
		// create edit supplier details panel labels and textfields for user input
		idLabel = new JLabel(" ID Number");
		nameLabel = new JLabel(" Name");
		addressLabel = new JLabel(" Address");
		editIdField = new JTextField(10);
		editNameField = new JTextField(10);
		editAddressField = new JTextField(30);
		editPanel.add(idLabel);
		editPanel.add(editIdField);
		editPanel.add(nameLabel);
		editPanel.add(editNameField);
		editPanel.add(addressLabel);
		editPanel.add(editAddressField);
		JButton edited = new JButton("SUBMIT");
		edited.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				addEditedSupplier();
			}
		});
		editPanel.add(edited);
		add(editPanel, BorderLayout.WEST);
		

		/*
		 * create GUI panel for delete and show deleted buttons;
		 * search field and button for looking for a supplier by name
		 */
		JPanel deletePanel = new JPanel();
	//	deletePanel.setBackground(Color.lightGray);
		deletePanel.setBorder(BorderFactory.createLineBorder(Color.red));
					
		// add look for a supplier by name feature
		JLabel search = new JLabel(" SEARCH FOR A SUPPLIER BY NAME");
		final JTextField searchField = new JTextField (20); 
		JButton searchSupplier = new JButton ("SEARCH");
		searchSupplier.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boolean foundIt = true;
				// create method to look for a supplier by name
				for(Supplier supplier:suppliers){
					if(searchField.getText().equalsIgnoreCase(supplier.getSupplierName())){
						JOptionPane.showMessageDialog(null, "Found it! Supplier id is: "+ supplier.getSupplierId());
						foundIt = true;
						break;
					}
					else{
						foundIt = false;
					}
					}
				// show message if supplier is not found
				if(foundIt == false){
					JOptionPane.showMessageDialog(null, "Didn't find supplier");
				}
			}
		});
		deletePanel.add(search);
		deletePanel.add(searchField);
		deletePanel.add(searchSupplier);
		add(deletePanel, BorderLayout.PAGE_END);

	
		// display products for a selected supplier
		JPanel productPanel= new JPanel();
		//productPanel.setBackground(Color.lightGray);
		productPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		JLabel productLabel = new JLabel("DISPLAY ");
		productPanel.add(productLabel);
		JButton showProducts = new JButton("Show products list");
		showProducts.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showProducts();
			}
				});
		productPanel.add(showProducts);
		add(productPanel);
		
		// display deleted suppliers
		JButton showDeleted = new JButton("Show deleted suppliers");
		showDeleted.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showDeletedSuppliers();
			}
		});
		productPanel.add(showDeleted);
	}
	
	// Methods
	
	// display suppliers
	
	public void showSuppliers(){
		listModel.clear();
		// show suppliers list on GUI
		for(Supplier supplier:suppliers){
			
			listModel.addElement("Id: "+supplier.getSupplierId()+", name: " + supplier.getSupplierName()+
				", address: "+ supplier.getSupplierAddress());
			}
		
	}
	
	// call method from Shop class to populate suppliers list
	public void populateSuppliers(){
		for(Supplier supplier: Shop.getSuppliers()){
			suppliers.add(supplier);
		}
	}

	// create new supplier
	public void createSupplier( ){
		String ids = idField.getText();
		int id = Integer.parseInt(ids);	
		Supplier newSupplier = new Supplier(id, nameField.getText(), addressField.getText());
		boolean isValid = true;
		
		// if the id entered exists don't add to the list; 
		for(Supplier supplier:suppliers){	
			if(supplier.getSupplierId() != id){
				idField.setText("");
				nameField.setText("");
				addressField.setText("");
			}else if(supplier.getSupplierId() == id){
				isValid=false;
			}

		}
		// if the id entered doesn't exist don't add it to the list; 
		if(isValid){
			suppliers.add(newSupplier);
			listModel.addElement("Id: "+newSupplier.getSupplierId()+", name: " + newSupplier.getSupplierName()+
					", address: "+ newSupplier.getSupplierAddress());
		}
		// give warning message
		else{
			JOptionPane.showMessageDialog(null, "The ID entered exists. Please enter another ID");
		}
	}

	// inner class to delete supplier from GUI list and set it as deleted in the Arraylist
	class Remove implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = suppliersList.getSelectedIndex();
			if (index != -1) {
				Supplier deleted = null;
				for(Supplier supplier:suppliers){
					deleted = suppliers.get(index);
								
					}
				if(deleted!= null){
					deletedSuppliers.add(deleted);
					suppliers.remove(index);
					listModel.remove(index);
				}
			}
			else{
				JOptionPane.showMessageDialog(null, " You didn't select a supplier to remove!");
					}
			}
	}
	
	// inner class get suppliers details in the textfield to be edited
	class Edit implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			Supplier tempSupplier = null;
			int index = suppliersList.getSelectedIndex();
			// select supplier
			if (index != -1) {
				for(Supplier supplier:suppliers){
					// get index of selected supplier
					tempSupplier = suppliers.get(index);
					int id = tempSupplier.getSupplierId( );
					String idS = Integer.toString(id);
					// show details of selected supplier in the textfield
					if(supplier.getSupplierId()==id){
						editIdField.setText(idS);
						editNameField.setText(supplier.getSupplierName());
						editAddressField.setText(supplier.getSupplierAddress());
						// set the id field to non-editable
						editIdField.setEditable(false);
					}
				}
			}
			// if no supplier is selected show message
			else{
				JOptionPane.showMessageDialog(null, "Please select a supplier from the list.");
			}	
		}
	}

	// method for editing supplier details
	public void addEditedSupplier(){
		Supplier tempSupplier = null;
		for(Supplier supplier:suppliers){
			// get index of selected supplier
			int index = suppliersList.getSelectedIndex();
			tempSupplier = suppliers.get(index);
			int id = tempSupplier.getSupplierId( );
			// get the edited details from the GUI textfield
			if(supplier.getSupplierId() == id){
				// replace old details with new details using index of selected supplier
				supplier.setSupplierId(id);
				supplier.setSupplierName(editNameField.getText());
				supplier.setSupplierAddress(editAddressField.getText());
			
				Object newElement = "Id: "+ editIdField.getText() +", name: " + editNameField.getText()+
					", address: "+ editAddressField.getText();
				listModel.setElementAt(newElement, index);

				editIdField.setText("");
				editNameField.setText("");
				editAddressField.setText("");
			
			}
		}		
	}
	
	// display deleted suppliers
	public void showDeletedSuppliers(){
		listModel.clear();
		for(Supplier deletedSupplier : deletedSuppliers){
			listModel.addElement( "Deleted supplier: "+"  Id: "+deletedSupplier.getSupplierId( )+", name: " + deletedSupplier.getSupplierName()+
					", address: "+ deletedSupplier.getSupplierAddress());
		}
	}
	// display products for each supplier
		public void showProducts(){
			int index = suppliersList.getSelectedIndex();
			if (index != -1) {
				// get the supplier at the selected index
				for(Supplier supplier:suppliers){
					Supplier tempSupplier = suppliers.get(index);
					int id = tempSupplier.getSupplierId( );
					listModel.clear();
					listModel.addElement("The products for Supplier id "+ tempSupplier.getSupplierId()+" are: ");
						// call Shop class to display products for the selected supplier
						for(Product product:Shop.getProducts()){	
							if(product.getSupplier().getSupplierId() == id){
								listModel.addElement("\n"+ product.getName());
							}
					}
				}
			}
			// if no supplier is selected show warning message
			else{
				JOptionPane.showMessageDialog(null, "Please select a supplier from the list  ");
				}	
		}	
	
	 public void saveDetails(){
		 JsonExample.clearList("resources/suppliers.json");
			for(Supplier supplier: Shop.getSuppliers()){
				JsonExample.saveSupplierToFile(supplier);
			}
			System.out.println("Finished saving suppliers");
		}
}
