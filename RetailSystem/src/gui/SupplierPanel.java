package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;

import net.miginfocom.swing.MigLayout;

import org.codehaus.jackson.map.ObjectMapper;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import data.Json;
import data.Product;
import data.ProductToOrder;
import data.Supplier;


public class SupplierPanel extends JSplitPane{
	// declaring instance variables
	private Supplier supplier;
	private JLabel title;
	private DefaultListModel listModel ;
	private static JList suppliersList;
	private JLabel idLabel;
	private JLabel nameLabel;
	private JLabel addressLabel;
	private JTextField nameField;
	private JTextField addressField;
	private JPopupMenu rightClickMenu;


	public SupplierPanel() {

		// create list type
		createList();
		showSuppliers();

		// create a split pane with a scroll pane in it and a Jpanel

		// set the right component to the scrollpane
		setDividerLocation(750);
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

		// create title
		JLabel titleLabel = new JLabel("SUPPLIERS");
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		buttonPanel.add(titleLabel, "cell 0 0");

		// create labels and textfields for user input
		nameLabel = new JLabel(" Name");
		addressLabel = new JLabel(" Address");
		nameField = new JTextField(20);
		addressField = new JTextField(20);
		// add them to the leftpane, to the JPanel
		buttonPanel.add(nameLabel, "cell 0 4,alignx trailing");
		buttonPanel.add(nameField, "cell 1 4,flowx");
		buttonPanel.add(addressLabel, "cell 0 5,alignx trailing");
		buttonPanel.add(addressField, "cell 1 5,flowx");

		/* create buttons to display suppliers;
		 * edit supplier details;
		 * add a new supplier;
		 * restore a deleted supplier.
		 */
		JButton showS = new JButton("Show Suppliers");
		JButton showDeleted = new JButton("Show deleted suppliers");
		final JButton edited = new JButton("SUBMIT CHANGES");
		edited.setEnabled(false);
		final JButton add = new JButton("ADD NEW supplier");
		final JButton cancel = new JButton("CANCEL");

		// create the items for the right click popup menu.
		rightClickMenu = new JPopupMenu();
		final JMenuItem showProducts = new JMenuItem("Show Products ");
		final JMenuItem editDetails = new JMenuItem("Edit Supplier");
		final JMenuItem removeSupplier = new JMenuItem("Remove Supplier");
		final JMenuItem restore = new JMenuItem("Restore deleted supplier");
		restore.setEnabled(false);

		// add action listener to display suppliers button
		showS.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				editDetails.setEnabled(true);
				showProducts.setEnabled(true);
				removeSupplier.setEnabled(true);
				restore.setEnabled(false);
				showSuppliers();
				
			}
		});
		buttonPanel.add(showS, "cell 3 1, growx");

		// add action listener to add suppliers button
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createSupplier();
			}
		});
		buttonPanel.add(add, "flowx,cell 1 6");

		// add action listener to edit supplier details button
		edited.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				addEditedSupplier();
				add.setEnabled(true);
				edited.setEnabled(false);	
			}
		});
		buttonPanel.add(edited, "flowx,cell 1 7");
		
		// add action listener to cancel button
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				nameField.setText("");
				addressField.setText("");
			}
		});
		buttonPanel.add(cancel, "flowx,cell 1 8");

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

		// add action listener to edit details  menu item
		editDetails.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				edited.setEnabled(true);
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
							add.setEnabled(false);
						}
					}
				}
				//	 if no supplier is selected show message
				else{
					JOptionPane.showMessageDialog(null, "Please select a supplier from the list.");
				}	
			}
		});
		rightClickMenu.add(editDetails);

		// add action listener to remove menu item
		removeSupplier.addActionListener(new Remove());
		rightClickMenu.add(removeSupplier);

		// add action listener to restore deleted suppliers menu item		
		restore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// get index of selected supplier
				int index = suppliersList.getSelectedIndex();
				if (index != -1) {

					// get the supplier at the selected index
					String supplierFromList = (String) suppliersList.getSelectedValue();
					for(Supplier supplier:Shop.getSuppliers()){
						char[] charList = supplierFromList.toCharArray();
						String id = "";
						id = id + charList[4] + charList[5] + charList[6];
						int idS = Integer.parseInt(id);
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
		final JTextField searchField = new JTextField (20); 
		JButton searchSupplier = new JButton ("GO");
		buttonPanel.add(searchField, "flowx,cell 1 9 ");
		buttonPanel.add(searchSupplier, "flowx,cell 2 9");
		
		// create action listener to search a supplier by name and select it in the GUI list if found
		ActionListener listener = new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
		    	 boolean foundIt = true;
					if((searchField.getText().isEmpty()==false)){
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
		
		};
	
		
		// add action listener to search button and search text field
		searchField.addActionListener(listener); 
		searchSupplier.addActionListener(listener);

		// display deleted suppliers
		
		showDeleted.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				editDetails.setEnabled(true);
				showProducts.setEnabled(true);
				restore.setEnabled(true);
				removeSupplier.setEnabled(false);
				showDeletedSuppliers();
			}
		});
		buttonPanel.add(showDeleted, "cell 3 2, growx");
		
		// add photo to left panel
		JLabel supplierImage = new JLabel(new ImageIcon("resources/supplier photo.jpg"));
		buttonPanel.add(supplierImage, "cell 1 14, flowx");

}
	
	
	// Methods; classes

	// setup list 
	public void createList(){
		listModel = new DefaultListModel();
		suppliersList = new JList(listModel);
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
	public void createSupplier(){
		boolean isValid= true ;
		// check if there are details entered for the new supplier
		if((nameField.getText().isEmpty()==false)&&(addressField.getText().isEmpty()==false)){
			for(Supplier supplier:Shop.getSuppliers()){
				// check if another supplier with the same details exists
				if((nameField.getText().equalsIgnoreCase(supplier.getSupplierName()))&& (addressField.getText().equalsIgnoreCase(supplier.getSupplierAddress()))){
					isValid = false;
						if(supplier.isSupplierDeleted()==false){
							
							showSuppliers();
								for (int i = 0; i < suppliersList.getModel().getSize(); i++) {
									Object item = suppliersList.getModel().getElementAt(i);
									// if an element in the GUI list contains the searched name select it 
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
									JOptionPane.showMessageDialog(null, " The supplier entered exists. Plese restore the deleted supplier.");
								}
					
					nameField.setText("");
					addressField.setText("");
					break;
				}
			}
		
			// if a supplier with the same details doesn't exist add the new supplier to the list
			if(isValid){
				Supplier newSupplier = new Supplier(nameField.getText(), addressField.getText());
				Shop.getSuppliers().add(newSupplier);
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

	// inner class get suppliers details in the textfield to be edited
	class Edit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = suppliersList.getSelectedIndex();
			// select supplier
			if (index != -1) {
				for(Supplier supplier:Shop.getSuppliers()){
					// store in a string the id corresponding characters of selected supplier from the GUI list
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
					// replace old details with new details using index of selected supplier
					supplier.setSupplierName(nameField.getText());
					supplier.setSupplierAddress(addressField.getText());

					Object newElement = "Id: "+ ids +", name: " + nameField.getText()+
							", address: "+ addressField.getText();
					listModel.setElementAt(newElement, index);

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

	// display deleted suppliers
	public void showDeletedSuppliers(){
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

	// save updates to file
	public void saveDetails(){
		Json.clearList("resources/suppliers.json");
		for(Supplier supplier: Shop.getSuppliers()){
			Json.saveSupplierToFile(supplier);
		}
	}

}
