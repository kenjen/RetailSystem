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
	private JList suppliersList;
	private JLabel idLabel;
	private JLabel nameLabel;
	private JLabel addressLabel;
	private JTextField nameField;
	private JTextField addressField;
	private JPopupMenu rightClickMenu;


	public SupplierPanel() {
		
		// create list type
		createList();
		  
		// create a split pane with a scroll panes in it and a Jpanel
		setDividerLocation(650);
		JScrollPane listScroller = new JScrollPane(suppliersList);
		setRightComponent(listScroller);
		listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		// create panel for left component
		JPanel buttonPanel = new JPanel();	
		buttonPanel.setLayout(new MigLayout("", "[][][][grow][grow][][][][][][][][]",
				"[][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]"));
		setLeftComponent(buttonPanel);
		//set minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(320, 350);
        listScroller.setMinimumSize(minimumSize);
        buttonPanel.setMinimumSize(minimumSize);
 
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
		// add them to the leftpane
		buttonPanel.add(nameLabel, "cell 0 4,alignx trailing");
		buttonPanel.add(nameField, "cell 1 4,growx");
		buttonPanel.add(addressLabel, "cell 0 5,alignx trailing");
		buttonPanel.add(addressField, "cell 1 5,growx");
		
		/* create buttons to display suppliers;
		 * edit supplier details;
		 * add a new supplier;
		 * restore a deleted supplier.
		 */
		JButton showS = new JButton("Show Suppliers");
		final JButton edited = new JButton("SUBMIT CHANGES");
		edited.setEnabled(false);
		final JButton add = new JButton("ADD NEW supplier");
		final JButton restore = new JButton("Restore deleted supplier");
		
		// add action listener to display suppliers button
		showS.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showSuppliers();
			}
		});
		buttonPanel.add(showS, "cell 3 6, growx");
		
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
		
		// add action listener to restore deleted suppliers button		
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
									else{
										JOptionPane.showMessageDialog(null, "Select a supplier from the deleted suppliers list");
									}
								}
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a supplier from the deleted suppliers list");
				}
				saveDetails();
			}
		});
		buttonPanel.add(restore, "flowx,cell 1 8");
		
		// create the items for the right click popup menu.
		rightClickMenu = new JPopupMenu();
        JMenuItem showProducts = new JMenuItem("Show Products ");
        JMenuItem editDetails = new JMenuItem("Edit Supplier");
        JMenuItem removeSupplier = new JMenuItem("Remove Supplier");
        
        // add action listener to the submenu items
        showProducts.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showProducts();
			}
		});
        rightClickMenu.add(showProducts);
        
        editDetails.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		edited.setEnabled(true);
        		int index = suppliersList.getSelectedIndex();
			
        		// select supplier
        		if (index != -1) {
        			for(Supplier supplier:Shop.getSuppliers()){
        				// get index and id of selected supplier
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
               
        removeSupplier.addActionListener(new Remove());
        rightClickMenu.add(removeSupplier);
       
        //Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener();
        suppliersList.addMouseListener(popupListener);
		
		// create GUI panel search field and button for looking for a supplier by name
		
		JLabel search = new JLabel(" Search for a supplier by name");
		buttonPanel.add(search, "flowx,cell 2 1 ");
		final JTextField searchField = new JTextField (20); 
		JButton searchSupplier = new JButton ("GO");
		buttonPanel.add(searchField, "flowx,cell 3 1 ");
		buttonPanel.add(searchSupplier, "flowx,cell 4 1");
		// add action listener to button
		searchSupplier.addActionListener(new ActionListener(){
			// create method to look for a supplier by name
			public void actionPerformed(ActionEvent e){
				boolean foundIt = true;
				if((searchField.getText().isEmpty()==false)){
					for(Supplier supplier:Shop.getSuppliers()){
						// check if name entered matches existent supplier name
						if(searchField.getText().equalsIgnoreCase(supplier.getSupplierName())){
							if(supplier.isSupplierDeleted()==false){
							JOptionPane.showMessageDialog(null, "Found it! Supplier id is: "+ supplier.getSupplierId());
							foundIt = true;
							searchField.setText("");
							break;
							}
							else{
								JOptionPane.showMessageDialog(null, "Supplier was deleted. The supplier's id was: "+ supplier.getSupplierId());
								foundIt = true;
								searchField.setText("");
								break;
							}
						
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
				else{
					JOptionPane.showMessageDialog(null, "Please enter a name");
				}
			}
		
		});
			
		// display deleted suppliers
		JButton showDeleted = new JButton("Show deleted suppliers");
		showDeleted.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showDeletedSuppliers();
			}
		});
		buttonPanel.add(showDeleted, "cell 3 7, growx");
		
	}
	
	// Methods
	

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
	    		System.out.println("right click pressed");
	    		rightClickPopup(e);
	    	}else{
	    		System.out.println("left pressed");
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
		if((nameField.getText().isEmpty()==false)&&(addressField.getText().isEmpty()==false)){
			Supplier newSupplier = new Supplier(nameField.getText(), addressField.getText());
			Shop.getSuppliers().add(newSupplier);
			// add new supplier to gUI list
			showSuppliers();
			nameField.setText("");
			addressField.setText("");
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
				String supplierFromList = (String) suppliersList.getSelectedValue();
				char[] charList = supplierFromList.toCharArray();
				String id = "";
				id = id + charList[4] + charList[5] + charList[6];
				Supplier deleted = null;
				for(Supplier supplier:Shop.getSuppliers()){
						if(supplier.getSupplierId() == Integer.parseInt(id)){
							deleted = supplier;
						}
					}
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
						// get index and id of selected supplier
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
			// get index and id of selected supplier
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
		listModel.addElement("Deleted suppliers");
			for(Supplier supplier:Shop.getSuppliers()){
				if(supplier.isSupplierDeleted()==true){
					listModel.addElement( "Id: "+supplier.getSupplierId( )+", name: " + supplier.getSupplierName()+
							", address: "+ supplier.getSupplierAddress());
				}
			}
	}
	// display products for each supplier
		public void showProducts(){
			int index = suppliersList.getSelectedIndex();
			String supplierFromList = (String) suppliersList.getSelectedValue();
			if (index != -1) {
				
				// get the supplier at the selected index
				for(Supplier supplier:Shop.getSuppliers()){
						char[] charList = supplierFromList.toCharArray();
						String id = "";
						id = id + charList[4] + charList[5] + charList[6];
						int idS = Integer.parseInt(id);
							if(idS==supplier.getSupplierId()){
								if(supplier.isSupplierDeleted()==false){
									listModel.clear();
									listModel.addElement("The products for Supplier id "+ supplier.getSupplierId()+" are: ");
					
										// call Shop class to display products for the selected supplier
									for(Product product:Shop.getProducts()){	
										System.out.println("product.getSupplier().getSupplierId()= "+product.getSupplier().getSupplierId()+" idS = "+idS);
										if((product.getSupplier().getSupplierId() == idS)&& product.getName().isEmpty()==false){
											listModel.addElement("\n"+ product.getName());
										}
										else{
											listModel.addElement("There are no products from this supplier");
											break;
										}
									}
								}
								else if(supplier.isSupplierDeleted()==true){
						
									listModel.clear();
									listModel.addElement("The supplier was deleted.");
									listModel.addElement("The products from Supplier id "+ supplier.getSupplierId()+" are: ");
					
										// call Shop class to display products for the selected supplier
									for(Product product:Shop.getProducts()){	
										if((product.getSupplier().getSupplierId() == idS)&& product.getName().isEmpty()==false){
											listModel.addElement("\n"+ product.getName());
										}
										else{
											listModel.addElement("There are no products from this supplier");
											break;
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
			System.out.println("Finished saving suppliers");
		}
}
