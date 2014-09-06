package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import data.Json;
import data.Product;
import data.Supplier;


public class SupplierPanel extends JSplitPane{
	
	/**
	 * declaring instance variables
	 */
	private static final long serialVersionUID = 1L;
	private Supplier supplier;
	private DefaultListModel<Object> listModel ;
	private static JList<Object> suppliersList;
	private static String supplierName;
	private static String supplierAddress;
	private JLabel nameLabel;
	private JLabel addressLabel;
	private JTextField nameField;
	private JTextField addressField;
	private JTextField searchField;
	private JPopupMenu rightClickMenu;
	private JMenuItem restore;
	private JMenuItem  removeSupplier;
	private JMenuItem showProducts;
	private JMenuItem editDetails;
	


	public SupplierPanel() {

		// create list type
		createList();
		showSuppliers();

		/**
		 ** create a split pane with a scroll pane in it and a Jpanel
		 **/

		// set the right component to the scrollpane
		setDividerLocation(700);
		JScrollPane listScroller = new JScrollPane(suppliersList);
		setRightComponent(listScroller);
		listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// create panel for left component
		JPanel buttonPanel = new JPanel();	
		buttonPanel.setLayout(new MigLayout());
		setLeftComponent(buttonPanel);
		

		//set a preferred size for the split pane.
		setPreferredSize(new Dimension(320, 350));

		// create labels and textfields for user input
		nameLabel = new JLabel(" Name");
		addressLabel = new JLabel(" Address");
		nameField = new JTextField(20);
		addressField = new JTextField(20);
		
		// add them to the leftpane, to the JPanel
		buttonPanel.add(nameLabel, "cell 0 3,alignx trailing");
		buttonPanel.add(nameField, "cell 1 3,flowx");
		buttonPanel.add(addressLabel, "cell 0 4,alignx trailing");
		buttonPanel.add(addressField, "cell 1 4,flowx");

		/**
		 ** create buttons 
		 **/ 
		
		 // display suppliers list  and deleted suppliers list; submit changes for edited supplier;cancel actions.
		JButton showS = new JButton("Show Suppliers");
		JButton showDeleted = new JButton("Show deleted suppliers");
		final JButton edited = new JButton("SUBMIT CHANGES");
		edited.setEnabled(false);
		final JButton add = new JButton("ADD NEW supplier");
		final JButton cancel = new JButton("CANCEL");

		/**
		 ** create the items for the right click popup menu.
		 **/
		
		// show products; edit supplier; remove supplier; restore deleted supplier
		rightClickMenu = new JPopupMenu();
		showProducts = new JMenuItem("Show Products ");
		editDetails = new JMenuItem("Edit Supplier");
		removeSupplier = new JMenuItem("Remove Supplier");
		restore = new JMenuItem("Restore deleted supplier");
		restore.setEnabled(false);

		/*
		 * add action listener to buttons
		 * 
		 *  1. display suppliers
		 */
		showS.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				editDetails.setEnabled(true);
				showProducts.setEnabled(true);
				removeSupplier.setEnabled(true);
				restore.setEnabled(false);
				showSuppliers();
				
			}
		});
		buttonPanel.add(showS, "cell 4 3, growx");

		// 2. add action listener to add suppliers button
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				addSupplier();
				removeSupplier.setEnabled(true);
				restore.setEnabled(false);
			}
		});
		buttonPanel.add(add, "flowx,cell 1 6");

		// 3. add action listener to edit supplier details button
		edited.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				addEditedSupplier();
				add.setEnabled(true);
				edited.setEnabled(false);	
			}
		});
		buttonPanel.add(edited, "flowx,cell 1 7");
		
		// 4. add action listener to cancel button
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				nameField.setText("");
				addressField.setText("");
				edited.setEnabled(false);
				add.setEnabled(true);
			}
		});
		buttonPanel.add(cancel, "flowx,cell 1 8");
		
		// 5. action listener to display deleted suppliers button
		showDeleted.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showDeletedSuppliers();
			}
		});
				buttonPanel.add(showDeleted, "cell 4 4, growx");

		// add listener to suppliers list to bring up popup menu
		MouseListener popupListener = new PopupListener();
		suppliersList.addMouseListener(popupListener);


		/* add action listener to the submenu items
		 * 
		 * 1. add action listener to show products  menu item
		 */

		showProducts.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				editDetails.setEnabled(false);
				showProducts.setEnabled(false);
				removeSupplier.setEnabled(false);
				showProducts();
				
			}
		});
		rightClickMenu.add(showProducts);

		// 2. add action listener to edit details  menu item
		editDetails.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				edited.setEnabled(true);
				edit();
				add.setEnabled(false);
			}
		});
		rightClickMenu.add(editDetails);

		// 3. add action listener to remove menu item
		removeSupplier.addActionListener(new Remove());
		rightClickMenu.add(removeSupplier);

		// 4. add action listener to restore deleted suppliers menu item		
		restore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				restore();
				saveDetails();
			}
		});
		rightClickMenu.add(restore);

		
		// create GUI panel search field and button for looking for a supplier by name

		JLabel search = new JLabel(" Search for a supplier by name");
		JLabel  sens = new JLabel("* case sensitive field ");
		sens.setFont(new Font("Tahoma", Font.ITALIC, 11));
		buttonPanel.add(search, "flowx,cell 0 9 ");
		buttonPanel.add(sens, "flowx,cell 1 10 ");
		searchField = new JTextField (20); 
		searchField.setName("input");
	
		JButton searchSupplier = new JButton ("GO");
		buttonPanel.add(searchField, "flowx,cell 1 9 ");
		buttonPanel.add(searchSupplier, "flowx,cell 2 9");
		
		// add action listener to search button and search text field
		searchField.addActionListener(new Search()); 
		searchSupplier.addActionListener(new Search());
				
	}
	
		
	
	/**
	 ********* Create methods and classes to implement features ***********
	**/
	

	// setup list 
	public void createList(){
		listModel = new DefaultListModel<Object>();
		suppliersList = new JList<Object>(listModel);
		suppliersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		suppliersList.setVisibleRowCount(0);
		suppliersList.setSelectedIndex(0);

	}

	// inner class to show right-click menu
	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			// show right click menu at mouse point
			if(e.isMetaDown()){
				suppliersList.setSelectedIndex(suppliersList.locationToIndex(e.getPoint()));
				rightClickPopup(e);
			}
		}

		public void mouseReleased(MouseEvent e) {
			rightClickPopup(e);
		}

		private void rightClickPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {

				rightClickMenu.show(e.getComponent(),
						e.getX(), e.getY());
			}
		}
	}
	// display suppliers

	public void showSuppliers(){
		listModel.clear();
		// show suppliers list on GUI
		for(Supplier supplier:Shop.getSuppliers()){
			if(supplier.isSupplierDeleted()== false){
				listModel.addElement("Id: "+supplier.getSupplierId()+", name: " + supplier.getSupplierName()+
						", address: "+ supplier.getSupplierAddress());
			}
		}
	}

	// create new supplier
	public void createSupplier(String name, String address){
		Supplier newSupplier = new Supplier(name,address);
		Shop.getSuppliers().add(newSupplier);
	}
	
	// add new supplier
	public void addSupplier(){
		boolean isValid= true ;
		// check if there are details entered for the new supplier
		if((nameField.getText().isEmpty()==false)&&(addressField.getText().isEmpty()==false)){
			for(Supplier supplier:Shop.getSuppliers()){
				// check if another supplier with the same details exists
				if((nameField.getText().equalsIgnoreCase(supplier.getSupplierName()))){
					isValid = false;
						if(supplier.isSupplierDeleted()==false){
							showSuppliers();
								// if an element in the GUI list contains the searched name select it 
								for (int i = 0; i < suppliersList.getModel().getSize(); i++) {
									Object item = suppliersList.getModel().getElementAt(i);
									if(item.toString().contains(nameField.getText())){
										suppliersList.setSelectedValue(item,true);
									}
								}
						
								JOptionPane.showMessageDialog(null, " The supplier entered exists");
						}
						else{
							showDeletedSuppliers();
								for (int i = 0; i < suppliersList.getModel().getSize(); i++) {
									Object item = suppliersList.getModel().getElementAt(i);
										// if an element in the GUI list contains the searched name select it 
										if(item.toString().contains(nameField.getText())){
												suppliersList.setSelectedValue(item,true);
										}
								}
							
								JOptionPane.showMessageDialog(null, " The supplier entered exists. Please restore the deleted supplier.");
						}
					
					nameField.setText("");
					addressField.setText("");
					break;
				}
			}
		
			// if a supplier with the same details doesn't exist add the new supplier to the list
			if(isValid){
			//	Supplier newSupplier = new Supplier();
				createSupplier(nameField.getText(), addressField.getText());
				showSuppliers();
				nameField.setText("");
				addressField.setText("");
			}
		}
		else{
			JOptionPane.showMessageDialog(null, " Enter details for new supplier");
		}
		saveDetails();
		
	}
	// inner class to delete supplier from GUI list and set it as deleted in the Arraylist
	class Remove implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// get index and id for selected supplier
			int index = suppliersList.getSelectedIndex();
			if (index != -1) {
				// get the id corresponding characters from the GUI list
				
				String supplierFromList = (String) suppliersList.getSelectedValue();
				char[] charList = supplierFromList.toCharArray();
				String id = "";
				id = id + charList[4] + charList[5] + charList[6];
				
				// create object to store the selected supplier
				Supplier deleted = null;
				for(Supplier supplier:Shop.getSuppliers()){
					if(supplier.getSupplierId() == Integer.parseInt(id)){
						deleted = supplier;
					}
				}
				// delete supplier from GUI list and set him as deleted in the arraylist
				if(deleted!= null){
					listModel.remove(index);
					deleted.setSupplierDeleted(true);
				}
				
				
			}
			else{
				JOptionPane.showMessageDialog(null, " You didn't select a supplier to remove!");
			}
			saveDetails();
		}

	}
	
	// delete supplier
	public void deleteSupplier(String supplierFromList, int ids, Supplier deleted){
		// get the id corresponding characters from the GUI list
		char[] charList = supplierFromList.toCharArray();
		String id = "";
		id = id + charList[4] + charList[5] + charList[6];
		ids = Integer.parseInt(id);
		// create object to store the selected supplier
		
		for(Supplier supplier:Shop.getSuppliers()){
			if(supplier.getSupplierId() == ids){
				deleted = supplier;
			}
		}
		// delete supplier from GUI list and set him as deleted in the arraylist
		if(deleted!= null){
			deleted.setSupplierDeleted(true);
		}
	}

	// select supplier to be edited and set textfields with the supplier's details
	public void edit(){
			int index = suppliersList.getSelectedIndex();
			// select supplier
			if (index != -1) {
				for(Supplier supplier:Shop.getSuppliers()){
					// get the characters corresponding to the id of selected supplier
					String supplierFromList = (String) suppliersList.getSelectedValue();
					char[] charList = supplierFromList.toCharArray();
					String id = "";
					id = id + charList[4] + charList[5] + charList[6];
					String ids = Integer.toString(supplier.getSupplierId());
					// show details of selected supplier in the textfield
					if(ids.equals(id)){
						nameField.setText(supplier.getSupplierName());
						addressField.setText(supplier.getSupplierAddress());
					}
				}
			}
			//	 if no supplier is selected show message
			else{
				JOptionPane.showMessageDialog(null, "Please select a supplier from the list.");
			}	
	}	

	// method for editing supplier details
	public void addEditedSupplier(){
		if((nameField.getText().isEmpty()==false)&&(addressField.getText().isEmpty()==false)){
			for(Supplier supplier:Shop.getSuppliers()){
				// store in a string the id corresponding characters of selected supplier from the GUI list
				int index = suppliersList.getSelectedIndex();
				String supplierFromList = (String) suppliersList.getSelectedValue();
				char[] charList = supplierFromList.toCharArray();
				String id = "";
				id = id + charList[4] + charList[5] + charList[6];
				String ids = Integer.toString(supplier.getSupplierId());

					// get the edited details from the GUI textfield
					if(id.equals(ids)){
						if(supplier.isSupplierDeleted()==false){
							// replace old details with new details using index of selected supplier
							supplier.setSupplierName(nameField.getText());
							supplier.setSupplierAddress(addressField.getText());

							Object newElement = "Id: "+ ids +", name: " + nameField.getText()+
								", address: "+ addressField.getText();
							listModel.setElementAt(newElement, index);
						}
						else if(supplier.isSupplierDeleted()==true){
							// replace old details with new details using index of selected supplier
							supplier.setSupplierName(nameField.getText());
							supplier.setSupplierAddress(addressField.getText());

							Object newElement = "Id: "+ ids +", name: " + nameField.getText()+
								", address: "+ addressField.getText()+"- SUPPLIER IS DELETED.";
							listModel.setElementAt(newElement, index);
						}

						nameField.setText("");
						addressField.setText("");
					}
			}
		}	
		else{
			JOptionPane.showMessageDialog(null, "Select a supplier to edit");
		}
		saveDetails();
	}
	
	// create action listener to search a supplier by name and select it in the GUI list if found
	class Search implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			boolean foundIt = true;
			
			// if something is typed in the search field
				if((searchField.getText().isEmpty()==false)){
					// check if the entered supplier name exists in the Array List
					for(Supplier supplier:Shop.getSuppliers()){
						if(searchField.getText().equalsIgnoreCase(supplier.getSupplierName())){
							// check the suppliers list for the name entered if supplier isn't deleted
							if(supplier.isSupplierDeleted()==false){
								showSuppliers();
									for (int i = 0; i < suppliersList.getModel().getSize(); i++) {
										Object item = suppliersList.getModel().getElementAt(i);
											// if an element in the GUI list contains the searched name select it 
											if(item.toString().contains(searchField.getText())){
												suppliersList.setSelectedValue(item,true);
												foundIt = true;
											}
									}
								searchField.setText("");
							}
							// check the deleted suppliers list if supplier is deleted
						else if(supplier.isSupplierDeleted()==true){
							showDeletedSuppliers();
								for (int i = 0; i < suppliersList.getModel().getSize(); i++) {
									Object item = suppliersList.getModel().getElementAt(i);
										// if an element in the GUI list contains the searched name select it
										if(item.toString().contains(searchField.getText())){
											suppliersList.setSelectedValue(item,true);
											foundIt = true;
										}
								}
								searchField.setText("");
						}
					break; 	
						}
						else{
							foundIt = false;
						}
					}
					// show message if supplier is not found
					if(foundIt == false){
						JOptionPane.showMessageDialog(null, "Didn't find supplier");
						searchField.setText("");
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "Please enter a name");
				}
		}
	}
	
	public void search(){
		// check if something is typed in the search field
		if((searchField.getText().isEmpty()==false)){
			// check if the entered supplier name exists in the Array List
			for(Supplier supplier:Shop.getSuppliers()){
				if(searchField.getText().equalsIgnoreCase(supplier.getSupplierName())){
					// check the suppliers list for the name entered if supplier isn't deleted
					if(supplier.isSupplierDeleted()==false){
						showSuppliers();
					}
				}
			}
		}
	}
	

	// display deleted suppliers
	public void showDeletedSuppliers(){
		editDetails.setEnabled(true);
		showProducts.setEnabled(true);
		restore.setEnabled(true);
		removeSupplier.setEnabled(false);
		listModel.clear();
		for(Supplier supplier:Shop.getSuppliers()){
			if(supplier.isSupplierDeleted()==true){
				listModel.addElement( "Id: "+supplier.getSupplierId( )+", name: " + supplier.getSupplierName()+
						", address: "+ supplier.getSupplierAddress()+"- SUPPLIER IS DELETED.");
			}
		}
	}
	// display products for each supplier
	public void showProducts(){
		int index = suppliersList.getSelectedIndex();
		String supplierFromList = (String) suppliersList.getSelectedValue();
		if (index != -1) {
			for(Supplier supplier:Shop.getSuppliers()){
				// store in a string the id corresponding characters of selected supplier from the GUI list
				char[] charList = supplierFromList.toCharArray();
				String id = "";
				id = id + charList[4] + charList[5] + charList[6];
				int idS = Integer.parseInt(id);
				// get the selected supplier's id to find him in the arraylist 
				if(idS==supplier.getSupplierId()){
					if(supplier.isSupplierDeleted()==false){
						listModel.clear();
						listModel.addElement("The products for Supplier id "+ supplier.getSupplierId()+" are: ");

						// call Shop class to display products for the selected supplier
						for(Product product:Shop.getProducts()){	
							if((product.getSupplier().getSupplierId() == idS)&& product.getName().isEmpty()==false){
								listModel.addElement("\n"+ product.getName());
								
							}
							else if((product.getSupplier().getSupplierId() == idS)&&product.getName().isEmpty()==false){
								listModel.addElement("There are no products from this supplier");
								
							}
							//break;
						}
					}
					else if(supplier.isSupplierDeleted()==true){
						restore.setEnabled(false);
						listModel.clear();
						listModel.addElement("The supplier was deleted.The products from Supplier id "+ supplier.getSupplierId()+" are: ");

						// call Shop class to display products for the selected supplier
						for(Product product:Shop.getProducts()){	
							if((product.getSupplier().getSupplierId() == idS)&& product.getName().isEmpty()==false){
								listModel.addElement("\n"+ product.getName());
								
							}
							else if ((product.getSupplier().getSupplierId() == idS)&& product.getName().isEmpty()==true){
								listModel.addElement("There are no products from this supplier");
								
							}
						}
					}
				}
			}
		}
		// if no supplier is selected show warning message
		else{
			JOptionPane.showMessageDialog(null, "Please select a supplier from the list  ");
		}	
	}	
	
	// method to restore to deleted supplier instead of creating  a new one
	public void restore(){
		// get index of selected supplier
		int index = suppliersList.getSelectedIndex();
		if (index != -1) {

			// get the id corresponding characters from selection
			String supplierFromList = (String) suppliersList.getSelectedValue();
			for(Supplier supplier:Shop.getSuppliers()){
				char[] charList = supplierFromList.toCharArray();
				String id = "";
				id = id + charList[4] + charList[5] + charList[6];
				int idS = Integer.parseInt(id);
				// get the supplier with the same id from the array list
				if(idS==supplier.getSupplierId()){
					if(supplier.isSupplierDeleted()==true){
						supplier.setSupplierDeleted(false);
						listModel.remove(index);
						JOptionPane.showMessageDialog(null, "The deleted supplier was restored to the suppliers list");
						break;
					}

				}
			}
		}
	}

	// save updates to file
	public void saveDetails(){
		Json.clearList("resources/suppliers.json");
		for(Supplier supplier: Shop.getSuppliers()){
			Json.saveSupplierToFile(supplier);
		}
	}

	// getter and setters
	public String getSupplierName() {
		return supplierName;
	}


	public void setSupplierAddress(String name) {
		SupplierPanel.supplierName = name;
	}
	
	public String getSupplierAddress() {
		return supplierAddress;
	}


	public void setSupplierName(String address) {
		SupplierPanel.supplierAddress = address;
	}


	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	

}
