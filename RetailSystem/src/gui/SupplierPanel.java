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
	private static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	private static ArrayList<Product> products = new ArrayList<Product>();
	private ArrayList<Supplier> deletedSuppliers = new ArrayList<Supplier>();
	
	private JLabel title;
	private DefaultListModel listModel ;
	private JList suppliersList;
	private JLabel idLabel;
	private JLabel nameLabel;
	private JLabel addressLabel;
//	private JTextField idField;
	private JTextField nameField;
	private JTextField addressField;
	//private JTextField editIdField;
//	private JTextField editNameField;
//	private JTextField editAddressField;
	//private static final String removeSupplier = "REMOVE Supplier";
//	private JButton remove;
//	private static final String editSupplier = "EDIT SUPPLIER DETAILS";
//	private JButton edit;
	private JPopupMenu rightClickMenu = new JPopupMenu();


	public SupplierPanel() {
		// call method to populate the suppliers list
		populateSuppliers();
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
        Dimension minimumSize = new Dimension(350, 350);
        listScroller.setMinimumSize(minimumSize);
        buttonPanel.setMinimumSize(minimumSize);
 
        //set a preferred size for the split pane.
        setPreferredSize(new Dimension(350, 300));
		
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
		
		// create buttons 
		JButton showS = new JButton("SHOW Suppliers");
		final JButton edited = new JButton("SUBMIT CHANGES");
		edited.setEnabled(false);
		final JButton add = new JButton("ADD NEW supplier");
		
		// add action listener to buttons
		showS.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showSuppliers();
			}
		});
		buttonPanel.add(showS, "cell 3 8, growx");
		
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createSupplier();
				saveDetails();
		}
		});
		buttonPanel.add(add, "flowx,cell 1 6");
				
		edited.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				addEditedSupplier();
				add.setEnabled(true);
				edited.setEnabled(false);	
			}
		});
		buttonPanel.add(edited, "flowx,cell 2 6");
	
		 //Create the items for the right click popup menu.
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
        		Supplier tempSupplier = null;
        		int index = suppliersList.getSelectedIndex();
			
        		// select supplier
        		if (index != -1) {
        			for(Supplier supplier:suppliers){
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
		searchSupplier.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boolean foundIt = true;
				if((searchField.getText().isEmpty()==false)){
					// create method to look for a supplier by name
					for(Supplier supplier:suppliers){
					
						if(searchField.getText().equalsIgnoreCase(supplier.getSupplierName())){
							JOptionPane.showMessageDialog(null, "Found it! Supplier id is: "+ supplier.getSupplierId());
							foundIt = true;
							searchField.setText("");
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
		buttonPanel.add(showDeleted, "cell 3 11, growx");
		
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
	
	// inner class 
	class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	    	rightClickPopup(e);
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
		for(Supplier supplier:suppliers){
			if(supplier.isSupplierDeleted()== false){
			listModel.addElement("Id: "+supplier.getSupplierId()+", name: " + supplier.getSupplierName()+
			", address: "+ supplier.getSupplierAddress());
			}
		}
		saveDetails();
	}
	

	public void populateSuppliers(){
		for(Supplier supplier: Shop.getSuppliers()){
			suppliers.add(supplier);
		}
	}

	// create new supplier
	public void createSupplier(){
		if((nameField.getText().isEmpty()==false)&&(addressField.getText().isEmpty()==false)){
			
			Supplier newSupplier = new Supplier(nameField.getText(), addressField.getText());
			suppliers.add(newSupplier);
			Json.saveSupplierToFile(newSupplier);
			listModel.addElement("Id: "+newSupplier.getSupplierId()+", name: " + newSupplier.getSupplierName()+
					", address: "+ newSupplier.getSupplierAddress());
			nameField.setText("");
			addressField.setText("");
		}
		else{
			JOptionPane.showMessageDialog(null, " Enter details for new supplier");
		}
		
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
				for(Supplier supplier:suppliers){
					//deleted = suppliers.get(index);
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
				
				Supplier tempSupplier = null;
				int index = suppliersList.getSelectedIndex();
			
				// select supplier
			if (index != -1) {
					
					for(Supplier supplier:suppliers){
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
		Supplier tempSupplier = null;
		if((nameField.getText().isEmpty()==false)&&(addressField.getText().isEmpty()==false)){
		for(Supplier supplier:suppliers){
			// get index and id of selected supplier
			int index = suppliersList.getSelectedIndex();
			String supplierFromList = (String) suppliersList.getSelectedValue();
			char[] charList = supplierFromList.toCharArray();
			String id = "";
			id = id + charList[4] + charList[5] + charList[6];
			System.out.println(id);
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
			for(Supplier supplier:suppliers){
			if(supplier.isSupplierDeleted()==true){
			listModel.addElement( "Deleted supplier: "+"  Id: "+supplier.getSupplierId( )+", name: " + supplier.getSupplierName()+
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
				for(Supplier supplier:suppliers){
					char[] charList = supplierFromList.toCharArray();
					String id = "";
					id = id + charList[4] + charList[5] + charList[6];
					Supplier tempSupplier = suppliers.get(index);
					int idS = tempSupplier.getSupplierId( );
					String ids = Integer.toString(tempSupplier.getSupplierId());
					
						if(ids.equalsIgnoreCase(id)){
							listModel.clear();
							listModel.addElement("The products for Supplier id "+ tempSupplier.getSupplierId()+" are: ");
					
						// call Shop class to display products for the selected supplier
						for(Product product:Shop.getProducts()){	
							if(product.getSupplier().getSupplierId() == idS){
								listModel.addElement("\n"+ product.getName());
								
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
