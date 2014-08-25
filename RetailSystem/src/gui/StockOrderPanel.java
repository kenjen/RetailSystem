package gui;

import gui.CustomerOrderPanel.OrderTableJPopupMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.InputMismatchException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import tableModels.TableModelWithLastColEditable;
import data.Customer;
import data.CustomerOrder;
import data.Json;
import data.Product;
import data.ProductToOrder;
import data.StockOrder;
import data.Supplier;

public class StockOrderPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JComboBox<String> comboSuppliers;
	private static JComboBox<String> comboSearchForProducts;
	private Supplier selectedSupplier;
	private JLabel lblProductList;
	private JTable tableProducts;
	private JTable tableOrders;
	private JScrollPane scrollPaneProducts;
	private JScrollPane scrollPaneOrders;
	private JButton btnDisplayAllProducts = new JButton("Display all products");
	private JButton btnAddToOrder = new JButton("Add to order");
	private JButton btnOrder = new JButton("Submit order");
	private static ArrayList<Object[]> arrayTemporaryOrder = new ArrayList<Object[]>();
	private static ArrayList<Integer> invoiceIDData = new ArrayList<Integer>();
	private ArrayList<Object[]> arrayOrder = new ArrayList<Object[]>();
	private static ArrayList<String> supplierNames;
	private static ArrayList<String> productNames = new ArrayList<String>();
	private Object[][] arrayTableProducts;
	private Object[][] arrayTableOrders;
	private JLabel lblError = new JLabel("");
	private Timer timer;
	private JButton btnDisplayCurrentOrder = new JButton("Current Order");
	//private JButton btnUpdateOrderCompletion = new JButton("Update Orders");
	private JLabel lblProductsSearch = new JLabel("Product search: ");
	private JTextField txtInvoiceIDSearch = new JTextField("",5);
	private JLabel lblFindInvoice = new JLabel("Find Invoice");
	private JButton btnDisplayAllInvoices = new JButton("Show all");
	private boolean stockOrderLoaded;

	public StockOrderPanel() {
		setLayout(new MigLayout());
		comboSuppliers = new JComboBox<String>();
		populateSupplierNames();
		
		JPanel jpanel = new JPanel();
		jpanel.setBackground(Color.BLUE);
		jpanel.setOpaque(true);
		setVisible(true);

		JLabel supplier = new JLabel("Search for Supplier:");
		add(btnDisplayAllProducts, "split 5");
		add(supplier,"gapx 50");
		add(comboSuppliers);

		comboSuppliers.setEditable(true);
		AutoCompleteDecorator.decorate(comboSuppliers);
		comboSuppliers.getEditor().getEditorComponent()
				.addKeyListener(new KeyListener() {

					@Override
					public void keyTyped(KeyEvent e) {
					}

					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							displayProductsTable("",comboSuppliers.getSelectedItem().toString());
							comboSearchForProducts.setSelectedItem(comboSearchForProducts.getItemAt(0));
						}

					}

					@Override
					public void keyReleased(KeyEvent e) {
					}

				});

		btnDisplayAllProducts.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				displayProductsTable("","");
				//resets the comboboxes to the first item which is empty string
				comboSuppliers.setSelectedItem(comboSuppliers.getItemAt(0));
				comboSearchForProducts.setSelectedItem(comboSearchForProducts.getItemAt(0));
			}

		});
		
		add(lblProductsSearch,"gapx 50");
		
		comboSearchForProducts = new JComboBox<String>();
		populateProductNames();
		add(comboSearchForProducts, "wrap");
		comboSearchForProducts.setEditable(true);
		AutoCompleteDecorator.decorate(comboSearchForProducts);
		comboSearchForProducts.getEditor().getEditorComponent().addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_ENTER){
					displayProductsTable("",comboSearchForProducts.getSelectedItem().toString());
					comboSuppliers.setSelectedItem(comboSuppliers.getItemAt(0));
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		});
		
		comboSearchForProducts.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					String product = e.getItem().toString();
					for(Product prod:Shop.getProducts()){
						if(prod.getName().equalsIgnoreCase(product) && prod.isDeleted() == false){
							displayProductsTable(product,"");
							comboSuppliers.setSelectedItem(comboSuppliers.getItemAt(0));
							break;
						}
					}
				}
			}
			
		});
		
		comboSuppliers.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					String supplier = e.getItem().toString();
					for(Supplier sup:Shop.getSuppliers()){
						if(sup.getSupplierName().equalsIgnoreCase(supplier) && sup.isSupplierDeleted() == false){
							displayProductsTable("",comboSuppliers.getSelectedItem().toString());
							comboSearchForProducts.setSelectedItem(comboSearchForProducts.getItemAt(0));
							break;
						}
					}
				}
			}
			
		});

		lblProductList = new JLabel("Supplier's Product list:");
		add(lblProductList, "wrap");
//--------------------//
//---Products table---//
//--------------------//
		scrollPaneProducts = new JScrollPane();
		add(scrollPaneProducts, "span 3, grow, push");
		displayProductsTable("","");

		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new MigLayout());
		tempPanel.add(btnAddToOrder, "wrap, growx, pushx");
		tempPanel.add(btnDisplayCurrentOrder,"growx, wrap");
		tempPanel.add(btnOrder, "growx, pushx");
		add(tempPanel, "wrap, alignx left, aligny top, growx");

		btnAddToOrder.addActionListener(new ButtonTemporaryOrderHandler());
		btnOrder.addActionListener(new ButtonOrderHandler());
		JLabel lblOrders = new JLabel("Orders:");
		add(lblError, "pushx ,alignx center, wrap");
		add(lblOrders, "split 3");
		lblError.setVisible(false);
		lblError.setFont(new Font("Serif",Font.BOLD,15));
		
		add(lblFindInvoice,"gapx 50");
		add(txtInvoiceIDSearch,"wrap");
//-----------------//
//---ORDER TABLE---//
//-----------------//
		scrollPaneOrders = new JScrollPane();
		add(scrollPaneOrders, "push,grow,span 3");
		displayOrderTable(0);
		
		JPanel panelx = new JPanel();
		panelx.setLayout(new MigLayout());
		//panelx.add(btnUpdateOrderCompletion,"growx, wrap");
		panelx.add(btnDisplayAllInvoices,"growx");
		add(panelx,"alignx left, aligny top");
		
		btnDisplayCurrentOrder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new CurrentStockOrderDialog(arrayTemporaryOrder);
			}
		});
		
		//load invoice id's in the array 
				invoiceIDData = new ArrayList<Integer>();
				for(StockOrder order:Shop.getStockOrders()){
					invoiceIDData.add(order.getId());
				}
				
				txtInvoiceIDSearch.addKeyListener(new KeyAdapter(){
					@Override
					public void keyPressed(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							
							if(txtInvoiceIDSearch.getText() == "" || txtInvoiceIDSearch.getText() == null){
								displayOrderTable(0);
							}else{
								try{
									int x = Integer.parseInt(txtInvoiceIDSearch.getText());
									if(invoiceIDData.contains(x)){
										displayOrderTable(x);
									}else{
										displayErrorMessage("No such invoice", Color.red);	
									}
								}catch(InputMismatchException ime){
									displayErrorMessage("Invalid Input", Color.red);
								}catch(NumberFormatException nfe){
									displayErrorMessage("Invalid Input", Color.red);
								}
							}
						}
					}
				});

				btnDisplayAllInvoices.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						displayOrderTable(0);
					}
					
				});
				
	}// end Constructor
	
	public void repopulateAll(){
		populateSupplierNames();
		populateProductNames();
		displayProductsTable("", "");
		displayOrderTable(0);
	}
	
	//populate supplier names for the combo box
	public static void populateSupplierNames(){
		supplierNames = new ArrayList<String>();
		supplierNames.add("");
		// populates the supplier name array
		for (Supplier supplier : Shop.getSuppliers()) {
			if(supplier.isSupplierDeleted() == false){
				String name = supplier.getSupplierName();
				supplierNames.add(name);
			}
		}
		//repopulate comboSuppliers
		comboSuppliers.removeAllItems();
		for(String name:supplierNames){
			comboSuppliers.addItem(name);
		}
		comboSuppliers.revalidate();
	}
	
	public static void populateProductNames(){
		productNames = new ArrayList<String>();
		productNames.add("");
		// populates the supplier name array
		for (Product product : Shop.getProducts()) {
			if(product.isDeleted() == false){
				String name = product.getName();
				productNames.add(name);
			}
		}
		//repopulate comboSuppliers
		comboSearchForProducts.removeAllItems();
		for(String name:productNames){
			comboSearchForProducts.addItem(name);
		}
		comboSearchForProducts.revalidate();
	}
	
	public void saveDetails(){
		 Json.clearList("resources/stockOrders.json");
		for(StockOrder stockOrder: Shop.getStockOrders()){
				Json.saveStockOrdersToFile(stockOrder);
			}
			System.out.println("Finished saving stockOrders");
		}
	
	
	//
	public void changeStockOrderToComplete(int id){
		for(StockOrder order:Shop.getStockOrders()){
			if(order.getId() == id){
				order.setCompleted(true);
				order.setExpectedDeliveryDate(new Date());
				//update products from this stock order
	        	 for(ProductToOrder prodToOrder:order.getProductsToOrder()){
	        		 for (Product product:Shop.getProducts()){
		        		 if(prodToOrder.getId() == product.getId()){
		        			 product.setQuantity(product.getQuantity()+prodToOrder.getAmount());
		        			 product.setAvailable(true);
		        			 product.setFlaggedForOrder(false);
		        			 break;
		        		 }
	        		 }
	        	 }
			}
		}
		
	}//end changeStockOrderToComplete
	
	//returns an array of product names
	public String[] getProductNamesForComboBox(){
		ArrayList<String> names = new ArrayList<String>();
		for(Product product:Shop.getProducts()){
			boolean found = false;
			innerFor:for(String s:names){
				if(product.getName().equalsIgnoreCase(s)){
					found=true;
					break innerFor;
				}
			}
			if(found==false){
				names.add(product.getName());
			}
		}
		String[] arrayToSort = new String[names.size()];
		arrayToSort = (String[]) names.toArray(new String[names.size()]);
		Arrays.sort(arrayToSort);
		return arrayToSort;
	}

	/**
	 * Shows the lblError text for 4 seconds.
	 * @param error Text for the error message
	 * @param color Color of the message
	 */
	public void displayErrorMessage(String error, Color color) {
		if (lblError.isVisible() == false) {
			lblError.setForeground(color);
			lblError.setText(error);
			lblError.setVisible(true);
			timer = new Timer(4000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					lblError.setVisible(false);
					timer.stop();
				}

			});
			timer.start();
		}
	}
//-------------------------//
//---Display Order Table---//
//-------------------------//
	public void displayOrderTable(int orderID) {
		String columnNamesForOrders[] = { "Id", "Staff member",
				"Date of order", "Total", "Completed" };
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		ArrayList<StockOrder> stockOrders = Shop.getStockOrders();

		//no id has been specified for an order
		if(orderID ==0){
			arrayTableOrders = new Object[stockOrders.size()][5];
			
			for (int x=0; x<stockOrders.size(); x++) {
				arrayTableOrders[x][0] = stockOrders.get(x).getId();
				arrayTableOrders[x][1] = stockOrders.get(x).getStaff().getName()
						+ " " + stockOrders.get(x).getStaff().getSurname();
				arrayTableOrders[x][2] = sdf.format(stockOrders.get(x).getDate());
				arrayTableOrders[x][3] = stockOrders.get(x).getTotal();
				arrayTableOrders[x][4] = stockOrders.get(x).isCompleted();
			}
		}else{
			//and id is specified, make a model only for this one id
			arrayTableOrders = new Object[1][5];
			
			for (StockOrder so:stockOrders) {
				if(so.getId() == orderID){
					arrayTableOrders[0][0] = so.getId();
					arrayTableOrders[0][1] = so.getStaff().getName()
							+ " " + so.getStaff().getSurname();
					arrayTableOrders[0][2] = sdf.format(so.getDate());
					arrayTableOrders[0][3] = so.getTotal();
					arrayTableOrders[0][4] = so.isCompleted();
				}
			}
		}

		TableModelWithLastColEditable ordersTableModel = new TableModelWithLastColEditable(
				arrayTableOrders, columnNamesForOrders);
		tableOrders = new JTable(ordersTableModel);
		tableOrders.setAutoCreateRowSorter(true);
		scrollPaneOrders.getViewport().add(tableOrders);
		scrollPaneOrders.repaint();
		tableOrders.getColumnModel().getSelectionModel()
				.addListSelectionListener(new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent e) {
						int row = tableOrders.getSelectedRow();
						tableOrders.requestFocus();
						// tableProducts.editCellAt(row, 7);
						tableOrders.changeSelection(row, 4, false, false);
					}

				});
		tableOrders.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
			      if (e.getClickCount() == 2) {
			         JTable target = (JTable)e.getSource();
			         int row = target.getSelectedRow();
			         int cell = 0;
			         
			         displayOrderDialog(row);
			      }
			}
			//selects the row at right click
			public void mousePressed(MouseEvent e){
				if(e.isPopupTrigger()){
					JTable source = (JTable)e.getSource();
	                int row = source.rowAtPoint( e.getPoint() );
	                int column = source.columnAtPoint( e.getPoint() );

	                if (! source.isRowSelected(row)){
	                    source.changeSelection(row, column, false, false);
	                }
	                OrderTableJPopupMenu m = new OrderTableJPopupMenu();
	                m.show(e.getComponent(), e.getX(), e.getY());
				}
			}
			
		});
		
	}//end displayOrderTable
	
//-------------------------
//---Display Products Table 
//-------------------------
	public void displayProductsTable(String productName, String supplierName){
		if(productName != "" && supplierName == ""){
		//get products that contain that name
			ArrayList<Product> productsArrayList = new ArrayList<Product>();
			for(Product product:Shop.getProducts()){
				if(product.getName().equalsIgnoreCase(productName) && product.getSupplier().isSupplierDeleted() == false){
					productsArrayList.add(product);
				}
			}
			arrayTableProducts = new Object[productsArrayList.size()][9];
			int counter = 0;
			for (Product products : productsArrayList) {
				arrayTableProducts[counter][0] = products.getId();
				arrayTableProducts[counter][1] = products.getName();
				arrayTableProducts[counter][2] = products.getCategory();
				arrayTableProducts[counter][3] = products.getPrice();
				arrayTableProducts[counter][4] = products.getSupplier().getSupplierName();
				arrayTableProducts[counter][5] = products.getQuantity();
				arrayTableProducts[counter][6] = products.isAvailable();
				arrayTableProducts[counter][7] = products.isFlaggedForOrder();
				arrayTableProducts[counter][8] = 0;
				counter++;
			}
		}else if(supplierName == ""){
			//remove products that have deleted supplier
			ArrayList<Product> productsArrayList = new ArrayList<Product>();
			for(Product p : Shop.getProducts()){
				//System.out.println("supllier "+p.getSupplier().getSupplierName()+" is deleted: "+p.getSupplier().isSupplierDeleted());
				if(p.getSupplier().isSupplierDeleted() == false){
					productsArrayList.add(p);
				}
			}
			/*for(Supplier s:Shop.getSuppliers()){
				System.out.println(s.getSupplierName() + " "+s.isSupplierDeleted());
			}*/
			//display table for all products
			arrayTableProducts = new Object[productsArrayList.size()][9];
			int counter = 0;
			for (Product products : productsArrayList) {
				arrayTableProducts[counter][0] = products.getId();
				arrayTableProducts[counter][1] = products.getName();
				arrayTableProducts[counter][2] = products.getCategory();
				arrayTableProducts[counter][3] = products.getPrice();
				arrayTableProducts[counter][4] = products.getSupplier().getSupplierName();
				arrayTableProducts[counter][5] = products.getQuantity();
				arrayTableProducts[counter][6] = products.isAvailable();
				arrayTableProducts[counter][7] = products.isFlaggedForOrder();
				arrayTableProducts[counter][8] = 0;
				counter++;
			}
		}else{
			// display products only for the selected supplier
			ArrayList<Product> productsArrayList = new ArrayList<Product>();
			for (Product products : Shop.getProducts()) {
				if (products.getSupplier().getSupplierName().equalsIgnoreCase(supplierName) && products.getSupplier().isSupplierDeleted() == false) {
					productsArrayList.add(products);
				}
			}
			arrayTableProducts = new Object[productsArrayList.size()][9];
			int counter = 0;

			for (Product products : productsArrayList) {
					arrayTableProducts[counter][0] = products.getId();
					arrayTableProducts[counter][1] = products.getName();
					arrayTableProducts[counter][2] = products.getCategory();
					arrayTableProducts[counter][3] = products.getPrice();
					arrayTableProducts[counter][4] = products.getSupplier().getSupplierName();
					arrayTableProducts[counter][5] = products.getQuantity();
					arrayTableProducts[counter][6] = products.isAvailable();
					arrayTableProducts[counter][7] = products.isFlaggedForOrder();
					arrayTableProducts[counter][8] = 0;
					counter++;
			}
		}

		String columnNames[] = { "Id", "Name", "Category", "Price", "Supplier",
				"Quantity", "In Shop?", "Required", "Amount to Order" };
		TableModelWithLastColEditable productsTableModel = new TableModelWithLastColEditable(arrayTableProducts, columnNames);
		tableProducts = new JTable(productsTableModel);
		tableProducts.setAutoCreateRowSorter(true);
		tableProducts.setAutoCreateRowSorter(true);
		tableProducts.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent e) {
						int row = tableProducts.getSelectedRow();
						tableProducts.requestFocus();
						tableProducts.changeSelection(row, 8, false, false);
					}

				});	
		
		tableProducts.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
			      if (e.getClickCount() == 2) {
			         JTable target = (JTable)e.getSource();
			         int row = target.getSelectedRow();
			         int cell = 8;
			         String choice = JOptionPane.showInputDialog(StockOrderPanel.this, "Enter the amount");
			         try{
			        	 if(choice != null){
			        		 int parsedChoice = Integer.parseInt(choice);
			        		 target.setValueAt(parsedChoice, row, cell);
			        	 }
			         }catch (InputMismatchException ex){
			        	 ex.printStackTrace();
			         }catch (NumberFormatException nfe){
			        	 displayErrorMessage("Invalid input", Color.RED);
			         }
			      }
			}
			
		});
		
		scrollPaneProducts.getViewport().add(tableProducts);
	}//end displayProductsTable
	
	//used in Order table when user right clicks and picks the option to show order or when user double clicks on a table row
	//display the selected order in a JDialog
	public void displayOrderDialog(int row){
		StockOrder thisOrder = null;
        for(StockOrder order : Shop.getStockOrders()){
       	 if(order.getId()==(int) tableOrders.getValueAt(row, 0)){
       		 thisOrder = order;	
       		 break;
       	 }
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DecimalFormat df = new DecimalFormat("#.00");
        String htmlText = "<html><head><style>th {color:#305EE3;font-variant:small-caps;}</style></head>";
        
       //displays orders with deliveries in the past
       if(thisOrder.getExpectedDeliveryDate().before(new Date())){
       	 htmlText += "<h2>Order details:</h2><table border='1'><tr><th>ID</th><th>Date of creation</th><th>Delivery date</th><th>Staff member</th><th>Total</th></tr>";
	         htmlText += "<tr><td>"+thisOrder.getId()+"</td></td>"+sdf.format(thisOrder.getDate())+"</td><td>"+"Delivered on "+ sdf.format(thisOrder.getExpectedDeliveryDate())+"</td><td>"+GUIBackBone.getLoggedStaffMember().getName()+" "+GUIBackBone.getLoggedStaffMember().getSurname()+"</td><td>"+df.format(thisOrder.getTotal())+"</td></tr>";
	         htmlText += "</table><br><h2>Products:</h2><table border='1'><tr><th>ID</th><th>Name</th><th>Supplier</th><th>Category</th><th>Price</th><th>Amount</th><th>Total</th></tr>";	        	 
	         
	         saveDetails();

       }
       
       //when completed(delivered) is checked do this
       else if(thisOrder.isCompleted()  ){
   	    Date now = new Date();
   	    if(thisOrder.getExpectedDeliveryDate().after(now)){
   	    	String savedDate = sdf.format(now);
   	    	htmlText += "<h2>Order details:</h2><table border='1'><tr><th>ID</th><th>Date of creation</th><th>Delivery date</th><th>Staff member</th><th>Total</th></tr>";
	    		htmlText += "<tr><td>"+thisOrder.getId()+"</td></td>"+sdf.format(thisOrder.getDate())+"</td><td>"+"Delivered on " +savedDate+"</td><td>"+GUIBackBone.getLoggedStaffMember().getName()+" "+GUIBackBone.getLoggedStaffMember().getSurname()+"</td><td>"+df.format(thisOrder.getTotal())+"</td></tr>";
	    		htmlText += "</table><br><h2>Products:</h2><table border='1'><tr><th>ID</th><th>Name</th><th>Supplier</th><th>Category</th><th>Price</th><th>Amount</th><th>Total</th></tr>";
	    		saveDetails();
   	    }
       }
       
        //displays expected delivery time if order is not yet delivered
        else{ 			        	 
       	 htmlText += "<h2>Order details:</h2><table border='1'><tr><th>ID</th><th>Date of creation</th><th>Expected delivery date</th><th>Staff member</th><th>Total</th></tr>";
	         htmlText += "<tr><td>"+thisOrder.getId()+"</td></td>"+sdf.format(thisOrder.getDate())+"</td><td>"+sdf.format(thisOrder.getExpectedDeliveryDate())+"</td><td>"+GUIBackBone.getLoggedStaffMember().getName()+" "+GUIBackBone.getLoggedStaffMember().getSurname()+"</td><td>"+df.format(thisOrder.getTotal())+"</td></tr>";
	         htmlText += "</table><br><h2>Products:</h2><table border='1'><tr><th>ID</th><th>Name</th><th>Supplier</th><th>Category</th><th>Price</th><th>Amount</th><th>Total</th></tr>";
	         saveDetails();
        }
        for(ProductToOrder product:thisOrder.getProductsToOrder()){
       	 htmlText += product.toHtmlString();
        }
        htmlText += "</table></html>";
        new PopupDialog(htmlText);
     }
	
	//loops through all StockOrders and those that have isCompleted set to True from previously false will be set to True and saved
	//Any Order that is set to true then updates the product quantity.
	public void updateOrders(){
		boolean found = false;
		int counter = 0;
		for(Object[] object:arrayTableOrders){
			if((boolean)object[4] == true && Shop.getStockOrders().get(counter).isCompleted() == false){
				changeStockOrderToComplete((int)object[0]);
				found = true;
			}
			counter ++;
		}
		AbstractTableModel model = (AbstractTableModel) tableOrders.getModel();
		model.fireTableDataChanged();
		
		if(found){
			displayErrorMessage("Order has been updated successfully", Color.blue);
			
			//since we have updated the quantity of some products, reload the product table
			displayProductsTable("","");
			
			//save orders to a persistent format
			if(Json.clearList("resources/stockOrders.json") && Json.clearList("resources/products.json")){
				for(StockOrder so:Shop.getStockOrders()){
					Json.saveStockOrdersToFile(so);
				}
				
				for(Product p:Shop.getProducts()){
					Json.saveProductToFile(p);
				}
			}else{
				displayErrorMessage("Could not persist changes!", Color.red);
			}
		}else{
			displayErrorMessage("Nothing to update", Color.red);
		}
	}//end updateOrders
	
//-------------------------------------//
//---Handler for Add To Array Button---//
//-------------------------------------//
	public class ButtonTemporaryOrderHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			AbstractTableModel tableModel = (AbstractTableModel) tableProducts
					.getModel();
			tableModel.fireTableDataChanged();

			boolean found = false;
			for (int i = 0; i < arrayTableProducts.length; i++) {
				if ((int) arrayTableProducts[i][8] > 0) {
					Object[] thisObject = new Object[9];
					thisObject[0] = arrayTableProducts[i][0];
					thisObject[1] = arrayTableProducts[i][1];
					thisObject[2] = arrayTableProducts[i][2];
					thisObject[3] = arrayTableProducts[i][3];
					thisObject[4] = arrayTableProducts[i][4];
					thisObject[5] = arrayTableProducts[i][5];
					thisObject[6] = arrayTableProducts[i][6];
					thisObject[7] = arrayTableProducts[i][7];
					thisObject[8] = arrayTableProducts[i][8];
					arrayTemporaryOrder.add(thisObject);
					found = true;
				}
			}
			if (found == false) {
				displayErrorMessage("Nothing to order!", Color.red);
			}else{
				for (int i = 0; i < arrayTableProducts.length; i++) {
					if ((int) arrayTableProducts[i][8] > 0) {
						arrayTableProducts[i][8] = 0;
					}
				}
				tableModel.fireTableDataChanged();
				displayOrderTable(0);
				displayErrorMessage("Added", Color.blue);
				saveDetails();
			}
		}

	}// end inner class handler

//-------------------------------------//
//---Handler for Submit Order Button---//
//-------------------------------------//
	public class ButtonOrderHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//check whether there is an actual order to submit
			if (arrayTemporaryOrder.size() > 0) {
				int answer = JOptionPane.showConfirmDialog(StockOrderPanel.this, "Are you sure your order is complete?\nThis will submit the current order", "Decision time", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION){
					//create individual objects for all products selected and add them to a roductsToOrder list.
					//Use this list to create a new StockOrder object and add it to the list of existing stockOrders
					ArrayList<ProductToOrder> productsToOrder = new ArrayList<ProductToOrder>();
					for (int i = 0; i < arrayTemporaryOrder.size(); i++) {
						Object object[] = arrayTemporaryOrder.get(i);
						int id = (int) object[0];
						String name = (String) object[1];
						Supplier supplier = null;
	
						for (Supplier sup : Shop.getSuppliers()) {
							if (sup.getSupplierName().equalsIgnoreCase(object[4].toString())) {
								supplier = sup;
								break;
							}
						}
						String category = (String) object[2];
						double price = (double) object[3];
						int amount = (int) object[8];
	
						productsToOrder.add(new ProductToOrder(id, name, supplier,
								category, price, false, amount));
					}
					StockOrder sO = new StockOrder(productsToOrder, GUIBackBone.getLoggedStaffMember());
					Shop.getStockOrders().add(sO);
	
					displayOrderTable(0);
					
					// clear the temporaryArrayOforders
					arrayTemporaryOrder = new ArrayList<Object[]>();
					displayErrorMessage("Order has been submitted", Color.blue);
					
					//load invoice id's in the array 
					invoiceIDData = new ArrayList<Integer>();
					for(StockOrder or:Shop.getStockOrders()){
						invoiceIDData.add(or.getId());
					}
					
					//save orders to a persistent format
					if(Json.clearList("resources/stockOrders.json")){
						for(StockOrder so:Shop.getStockOrders()){
							Json.saveStockOrdersToFile(so);
						}
					}else{
						displayErrorMessage("Could not persist changes!", Color.red);
					}
				}
			} else {
				displayErrorMessage("No order to submit", Color.red);
			}
		}// end actionPerformed

	}// end inner class
	
	public class OrderTableJPopupMenu extends JPopupMenu{
		JMenuItem menuItemUpdate = new JMenuItem("Set To Complete");
		JMenuItem menuItemShow = new JMenuItem("Show Order");
		public OrderTableJPopupMenu(){
			add(menuItemShow);
			add(menuItemUpdate);
			menuItemUpdate.addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					int row = tableOrders.getSelectedRow();
					TableModelWithLastColEditable model = (TableModelWithLastColEditable) tableOrders.getModel();
					model.setValueAt(true, row, 4);
					model.fireTableDataChanged();
					updateOrders();
				}
			});
			menuItemShow.addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					int row = tableOrders.getSelectedRow();
			         displayOrderDialog(row);
				}
			});
		}
	}

	public static ArrayList<Object[]> getArrayTemporaryOrder() {
		return arrayTemporaryOrder;
	}

	public static void setArrayTemporaryOrder(ArrayList<Object[]> x) {
		arrayTemporaryOrder = x;
	}

	public void setStockOrderLoaded(boolean stockOrderLoaded) {
		this.stockOrderLoaded = stockOrderLoaded;
		
	}
}// end class
