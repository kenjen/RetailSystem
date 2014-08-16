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

import data.Product;
import data.ProductToOrder;
import data.Supplier;


public class SupplierPanel extends JPanel{
	private Supplier supplier;
	private static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	private static ArrayList<Product> products = new ArrayList<Product>();
	
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
		super(new GridLayout(4,0));
		setBorder(BorderFactory.createLineBorder(Color.black));
		populateSuppliers();


		JPanel listPanel = new JPanel();
		listPanel.setBackground(Color.WHITE);
		listPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		listModel = new DefaultListModel();
		suppliersList = new JList(listModel);

		suppliersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		suppliersList.setVisibleRowCount(-1);
		suppliersList.setSelectedIndex(0);

		title= new JLabel("SUPPLIERS");	
		listPanel.add(title,BorderLayout.PAGE_START);
		idLabel = new JLabel(" ID Number");
		nameLabel = new JLabel(" Name");
		addressLabel = new JLabel(" Address");


		listPanel.add(idLabel);
		listPanel.add(nameLabel);
		listPanel.add(addressLabel);
		for(Supplier supplier:suppliers){
			listModel.addElement("Id: "+supplier.getSupplierId()+", name: " + supplier.getSupplierName()+
					", address: "+ supplier.getSupplierAddress());
		}
		JScrollPane listScroller = new JScrollPane(suppliersList);
		listPanel.add(listScroller);
		listPanel.add(suppliersList);
		add(listPanel, BorderLayout.PAGE_START);


		JPanel addPanel= new JPanel();
		addPanel.setBackground(Color.lightGray);
		addPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		JLabel newSupplier = new JLabel("CREATE NEW SUPPLIER");
		addPanel.add(newSupplier);
		JButton add = new JButton("SUBMIT");
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createSupplier();
			}
		});
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
		add(addPanel, BorderLayout.CENTER);

		JPanel editPanel= new JPanel();
		editPanel.setBackground(Color.lightGray);
		editPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		JLabel editS = new JLabel("EDIT SUPPLIER DETAILS");
		editPanel.add(editS);
		JButton edit = new JButton(editSupplier);
		edit.setActionCommand(editSupplier);
		edit.addActionListener(new Edit());
		editPanel.add(edit);
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

		JPanel deletePanel = new JPanel();
		deletePanel.setBackground(Color.lightGray);
		deletePanel.setBorder(BorderFactory.createLineBorder(Color.red));

		remove = new JButton(removeSupplier);
		remove.setActionCommand(removeSupplier);
		remove.addActionListener(new Remove());
		deletePanel.add(remove);		
		
		JButton showDeleted = new JButton("Show deleted suppliers");
		showDeleted.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showDeletedSuppliers();
			}
		});
		deletePanel.add(showDeleted);
		
		JLabel search = new JLabel(" SEARCH FOR A SUPPLIER BY NAME");
		final JTextField searchField = new JTextField (20); 
		JButton searchSupplier = new JButton ("SEARCH");
		searchSupplier.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(Supplier supplier:suppliers){
					if(searchField.getText().equalsIgnoreCase(supplier.getSupplierName())){
						JOptionPane.showMessageDialog(null, "Found it! Supplier id is: "+ supplier.getSupplierId());
					break;
					}
				}
			}
		});
		deletePanel.add(search);
		deletePanel.add(searchField);
		deletePanel.add(searchSupplier);
		add(deletePanel, BorderLayout.PAGE_END);




	}
	public void populateSuppliers(){
		Supplier supplier1 = new Supplier(123,"Doyle's", "St.Stephens,Dublin");
		Supplier supplier2 = new Supplier(234,"Profi", "Baldara, Ashbourne");
		Supplier supplier3 = new Supplier(345,"Jane LTD", "Kileen, Cork");
		Supplier supplier4 = new Supplier(456,"G&M", "Hunter's Lane, Navan");

		suppliers.add(supplier1);
		suppliers.add(supplier2);
		suppliers.add(supplier3);
		suppliers.add(supplier4);


	}


	public void createSupplier( ){
		String ids = idField.getText();
		int id = Integer.parseInt(ids);	
		Supplier newSupplier = new Supplier(id, nameField.getText(), addressField.getText());
		boolean isValid = true;

		for(Supplier supplier:suppliers){	
			if(supplier.getSupplierId() != id){
				idField.setText("");
				nameField.setText("");
				addressField.setText("");
			}else if(supplier.getSupplierId() == id){
				isValid=false;
			}

		}
		if(isValid){
			suppliers.add(newSupplier);
			listModel.addElement("Id: "+newSupplier.getSupplierId()+", name: " + newSupplier.getSupplierName()+
					", address: "+ newSupplier.getSupplierAddress());
		}
		else{
			JOptionPane.showMessageDialog(null, "The ID entered exists. Please enter another ID");
		}
	}

	class Remove implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = suppliersList.getSelectedIndex();
			if (index != -1) {
				listModel.remove(index);
			}
			for(Supplier supplier:suppliers){
				Supplier s= suppliers.get(index);
				s.setSupplierDeleted(true);
			}

		}
	}
	class Edit implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			Supplier tempSupplier = null;
			int index = suppliersList.getSelectedIndex();
			if (index != -1) {
				for(Supplier supplier:suppliers){
					tempSupplier = suppliers.get(index);
					int id = tempSupplier.getSupplierId( );
					String idS = Integer.toString(id);

					if(supplier.getSupplierId()==id){
						editIdField.setText(idS);
						editNameField.setText(supplier.getSupplierName());
						editAddressField.setText(supplier.getSupplierAddress());
					}
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Please select from the list a supplier to edit ");
			}	
		}
	}

	public void addEditedSupplier(){
		Supplier tempSupplier = null;
		for(Supplier supplier:suppliers){	
			int index = suppliersList.getSelectedIndex();
			tempSupplier = suppliers.get(index);
			int id = tempSupplier.getSupplierId( );
			
			if(supplier.getSupplierId()==id){
				supplier.setSupplierId(id);
				supplier.setSupplierName(editNameField.getText());
				supplier.setSupplierAddress(editAddressField.getText());
	
				Object newElement="Id: "+ editIdField.getText() +", name: " + editNameField.getText()+
					", address: "+ editAddressField.getText();
				listModel.setElementAt(newElement, index);

				editIdField.setText("");
				editNameField.setText("");
				editAddressField.setText("");
			
			}
		}		
	}
	
	public void showDeletedSuppliers(){
		for(Supplier supplier:suppliers){
			if(supplier.isSupplierDeleted()==true){
				int id=supplier.getSupplierId();
				String idS = Integer.toString(id);
				listModel.addElement("Deleted supplier: "+"  Id: "+ idS +", name: " + supplier.getSupplierName()+
					", address: "+ supplier.getSupplierAddress());
			}
		}
	}
	
	
}
