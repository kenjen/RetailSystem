package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import tableModels.ProductTableModel;
import data.Customer;
import data.CustomerOrder;
import data.Json;
import data.Product;
import data.ProductToOrder;
import data.Supplier;

//this class deals with customer ordering only.
public class CustomerOrderPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JLabel lblActiveCustomerText = null;
	private JLabel lblActiveCustomer = null;
	private JLabel lblPreviousCustomerOrder = new JLabel("Previous Orders:");
	private JLabel lblError = new JLabel("");
	private JLabel lblFindInvoice = new JLabel("Find Invoice");
	private JButton btnOrder = new JButton("Submit Order");
	private JButton btnDisplayOrdersForSelectedCustomer = new JButton("Customer Orders");
	//private JButton btnUpdateOrderCompletion = new JButton("Update order");
	private JButton btnDisplayAllOrders = new JButton("All orders");
	private JButton btnAddToOrder = new JButton ("Add to order");
	private JButton btnViewCurrentOrder = new JButton ("Current Order");
	private JButton btnAllProducts = new JButton("All products");
	private static JComboBox<String> comboSelectCustomer;
	private static JComboBox<String> comboSearchForProducts;
	private Customer selectedCustomer;
	private Object[][] availableProductsArray ;
	private Object[][] arrayOrders;
	private static ArrayList<Object[]> arrayCurrentOrder = new ArrayList<Object[]>();
	private static ArrayList<String> arrayCustomerNames;
	private static ArrayList<String> arrayProductNames;
	private static ArrayList<Integer> invoiceIDData = new ArrayList<Integer>();
	private ProductTableModel tableModelForOrdersTable;
	private JTable tableAvailableProducts;
	private JTable tableOrders;
	private JScrollPane scrollPane;
	private JScrollPane scrollPaneForOrdersTable;
	private Timer timer;
	private String typeOfOrder = "All";
	private JTextField txtInvoiceIDSearch = new JTextField("",5);
	private boolean isActiveCustomerOrderTablePopulated;
	
	/**
	 * Adds all the GUI components on the panel
	 */
	public CustomerOrderPanel() {
		
		comboSelectCustomer = new JComboBox<String>();
		populateCustomerNames();
		comboSelectCustomer.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectCustomer); //allow for auto-completion
		comboSelectCustomer.getEditor().getEditorComponent().addKeyListener(new ComboBoxKeyListener() );
		comboSelectCustomer.addItemListener(new ComboBoxItemListener());
		
		lblActiveCustomerText = new JLabel("Active Customer: ");
		lblActiveCustomer = new JLabel("none");
		lblActiveCustomer.setForeground(Color.red);
		
		comboSearchForProducts = new JComboBox<String>();
		populateProductNamesComboBox();
		comboSearchForProducts.setEditable(true);
		AutoCompleteDecorator.decorate(comboSearchForProducts);
		comboSearchForProducts.getEditor().getEditorComponent().addKeyListener(new ComboBoxKeyListener());
		comboSearchForProducts.addItemListener(new ComboBoxItemListener());
		
		btnAllProducts.addActionListener(new ButtonActionListener());
		btnOrder.addActionListener(new ButtonOrderHandler());
		btnDisplayAllOrders.addActionListener(new ButtonDisplayOrdersForAllCustomersHandler());
		btnDisplayOrdersForSelectedCustomer.addActionListener(new ButtonDisplayOrdersForSelectedCustomersHandler());
		//btnUpdateOrderCompletion.addActionListener(new UpdateOrdersHandler());
		btnAddToOrder.addActionListener(new BtnAddToOrderActionListener());
		txtInvoiceIDSearch.addKeyListener(new TxtInvoiceIDSearchKeyListener());
		
		scrollPane = new JScrollPane();
		displayProductsTable("");
		scrollPaneForOrdersTable = new JScrollPane();
		displayOrderTable(false, 0);
		typeOfOrder = "All";
		
		lblError.setVisible(false);
		lblError.setForeground(Color.red);
		lblError.setFont(new Font("Serif",Font.BOLD,15));
		
		btnViewCurrentOrder.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				new CurrentCustomerOrderDialog(arrayCurrentOrder);
			}
			
		});
		
		//adding everything to the panel
		setLayout(new MigLayout());
		add(new JLabel("Customer:"), "split 7");
		add(comboSelectCustomer);
		add(lblActiveCustomerText, "gapx 20px");
		add(lblActiveCustomer);
		add(new JLabel("Product search:"), "gapx 50");
		add(comboSearchForProducts);
		add(btnAllProducts, "wrap, gapx 20");
		add(new JLabel("Product list:"), "wrap");
		add(scrollPane, "span 5, grow, push");
		JPanel panelx = new JPanel();
		panelx.setLayout(new MigLayout());
		panelx.add(btnAddToOrder,"growx, pushx, wrap");
		panelx.add(btnViewCurrentOrder,"growx, pushx, wrap");
		panelx.add(btnOrder,"growx, pushx");
		add(panelx, "aligny top, alignx left, wrap, growx");
		add(lblError,"cell 0 3, wrap, pushx, alignx center");
		add(lblPreviousCustomerOrder, "split 3");
		add(lblFindInvoice,"gapx 50");
		add(txtInvoiceIDSearch,"wrap");
		add(scrollPaneForOrdersTable, "span 5 3, grow, push");
		JPanel oneCellPanel = new JPanel();
		oneCellPanel.setLayout(new MigLayout());
		oneCellPanel.add(btnDisplayAllOrders, "wrap, growx, aligny top");
		oneCellPanel.add(btnDisplayOrdersForSelectedCustomer, "wrap, growx, aligny top");
		//oneCellPanel.add(btnUpdateOrderCompletion, "wrap, growx, aligny top");
		add(oneCellPanel, "aligny top");
		
	}//end constructor
	
	//repopulate any/all arrays/tables in case anything has been updated
	public void repopulateAll(){
		populateCustomerNames();
		populateProductNamesComboBox();
		displayOrderTable(false, 0);
		displayProductsTable("");
		//selectedCustomer = null;
	}
	
	//populate customer names for the combo box
	public static void populateCustomerNames(){
		arrayCustomerNames = new ArrayList<String>();
		arrayCustomerNames.add("");
		
		//concatenate customer names and add them to the combo box
		for (Customer customer: Shop.getCustomers()){
			if(customer.isDeleted() == false){
				String name = customer.getCustomerFName()+" "+customer.getCustomerLName();
				arrayCustomerNames.add(name);
			}
		}		
		comboSelectCustomer.removeAllItems();
		for(String x:arrayCustomerNames){
			comboSelectCustomer.addItem(x);
		}
		comboSelectCustomer.revalidate();
	}
	
	//populate product names for the combo box
	public void populateProductNamesComboBox(){
		arrayProductNames = new ArrayList<String>();
		for(Product product:Shop.getProducts()){
			if(product.isAvailable() && product.isDeleted()==false){
				boolean found = false;
				innerFor:for(String s:arrayProductNames){
					if(product.getName().equalsIgnoreCase(s)){
						found=true;
						break innerFor;
					}
				}
				if(found==false){
					arrayProductNames.add(product.getName());
				}
			}
		}
		comboSearchForProducts.removeAllItems();
		for(String x:arrayProductNames){
			comboSearchForProducts.addItem(x);
		}
		comboSearchForProducts.revalidate();
	}
	
	public static ArrayList<Object[]> getArrayCurrentOrder() {
			return arrayCurrentOrder;
		}

	public static void setArrayCurrentOrder(ArrayList<Object[]> list) {
		arrayCurrentOrder = list;
	}
	
//---------------------------//
//---DISPLAY PRODUCT TABLE---//
//---------------------------//
	public void displayProductsTable(String productName){
			if(productName == ""){
				int availableCounter = 0;
				for(Product prod : Shop.getProducts()){
					if(prod.isAvailable() && prod.isDeleted() == false)
						availableCounter ++;
				}
				availableProductsArray = new Object[availableCounter][8];
				int counter = 0;
				//make products array to feed into the table model
				DecimalFormat df = new DecimalFormat("#.00");
				for(Product product:Shop.getProducts()){
					if(product.isAvailable() && product.isDeleted()==false){
						availableProductsArray[counter][0] = product.getId();
						availableProductsArray[counter][1] = product.getName();
						availableProductsArray[counter][2] = product.getSupplier().getSupplierName();
						availableProductsArray[counter][3] = product.getCategory();
						availableProductsArray[counter][4] = Double.parseDouble(df.format(product.getMarkupPrice()));
						availableProductsArray[counter][5] = product.isDiscounted();
						
						//update the price of the array if there are orders made for this products that are yet to be sumbitted
						//if the user adds to order but does not submit it, leaving the pane and coming back will redraw the table with current products
						//therefore this will make sure that if you have products in the order that is not submitted, will reflect the quantities
						availableProductsArray[counter][6] = product.getQuantity();
						//this column will be editable
						availableProductsArray[counter][7] = 0;
						counter ++;
					}
				}
			//Display only for selected product
			}else{
				int size = 0;
				for(Product prod:Shop.getProducts()){
					if(prod.getName().equalsIgnoreCase(productName) && prod.isAvailable()==true && prod.isDeleted() == false){
						size ++;
						System.out.println("Found "+ size +" "+prod.getName());
					}
				}
				availableProductsArray = new Object[size][8];
				int counter = 0;
				//make products array to feed into the table model
				DecimalFormat df = new DecimalFormat("#.00");
				for(Product product:Shop.getProducts()){
					if(product.isAvailable()==true && product.isDeleted()==false && product.getName().equalsIgnoreCase(productName)){
						System.out.println("populating Product table, found this product: "+product.getName());
						availableProductsArray[counter][0] = product.getId();
						availableProductsArray[counter][1] = product.getName();
						availableProductsArray[counter][2] = product.getSupplier().getSupplierName();
						availableProductsArray[counter][3] = product.getCategory();
						availableProductsArray[counter][4] = Double.parseDouble(df.format(product.getMarkupPrice()));
						availableProductsArray[counter][5] = product.isDiscounted();
						availableProductsArray[counter][6] = product.getQuantity();
						availableProductsArray[counter][7] = 0; //amount
						counter ++;
					}
				}
			}
		String columnNames[] = {"Id","Name","Supplier","Category","Price","Discounted?","Quantity","Amount to Order"};
		ProductTableModel productsTableModel = new ProductTableModel(availableProductsArray, columnNames);
		tableAvailableProducts = new JTable(productsTableModel);
		tableAvailableProducts.setAutoCreateRowSorter(true);
		tableAvailableProducts.setRowSelectionAllowed(true);
		tableAvailableProducts.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
			      if (e.getClickCount() == 2) {
			         JTable target = (JTable)e.getSource();
			         int row = target.getSelectedRow();
			         int cell = 7;
			         String choice = JOptionPane.showInputDialog(CustomerOrderPanel.this, "Enter the amount");
			         try{
			        	 if(choice != null){
			        		 int parsedChoice = Integer.parseInt(choice);
			        		 target.setValueAt(parsedChoice, row, cell);
			        	 }
			         }catch (InputMismatchException ex){
			        	 ex.printStackTrace();
			         }catch (NumberFormatException nfe){
			        	 System.out.println("Invalid Input");
			        	 //do nothing, swallow the exception
			        	 //not ideal but will do for now
			         }
			      }
			}
			
		});
		tableAvailableProducts.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = tableAvailableProducts.getSelectedRow();
				//tableAvailableProducts.editCellAt(row, 7);
				tableAvailableProducts.changeSelection(row, 7, false, false);
			}
		});
		//tableAvailableProducts.setComponentPopupMenu(new OrderTableJPopupMenu());
		scrollPane.getViewport().add(tableAvailableProducts);
		scrollPane.repaint();
		
	}//end displayProductsTable

//---------------------------//
//---DISPLAY ORDERS TABLE---//
//---------------------------//
	public void displayOrderTable(boolean forCustomerOnly, int stockOrderID){
		//display the order in the previousOrderTable
		String columnNames1[] = {"Id","Customer","Staff","Date","Total Net","Total Gross","Completed?"};
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		DecimalFormat df = new DecimalFormat("#.00");
		ArrayList<CustomerOrder> currentCustomerOrders = Shop.getCustomerOrders();
		arrayOrders = new Object[currentCustomerOrders.size()][7];
		
		//if forCustomerOnly = false then display all orders in the CustomeOrder array, otherwise only for the selected customer
		if(stockOrderID !=0){
			arrayOrders = new Object[1][7];
			for(CustomerOrder custOrder:currentCustomerOrders){
				if(stockOrderID == custOrder.getId()){
					arrayOrders[0][0] = custOrder.getId();
					arrayOrders[0][1] = custOrder.getCustomer().getCustomerFName()+" "+custOrder.getCustomer().getCustomerLName();
					arrayOrders[0][2] = custOrder.getStaff().getName()+" "+custOrder.getStaff().getSurname();
					arrayOrders[0][3] = sdf.format(custOrder.getCreationDate());
					arrayOrders[0][4] = df.format(custOrder.getTotalNet());
					arrayOrders[0][5] = df.format(custOrder.getTotalGross());
					arrayOrders[0][6] = custOrder.isComplete();
					break;
				}
			}
			tableModelForOrdersTable = new ProductTableModel(arrayOrders, columnNames1);
			tableOrders = new JTable(tableModelForOrdersTable);
			tableOrders.setAutoCreateRowSorter(true);
			
			//Handle double clicking on table to display details of the double-clicked row's order
			tableOrders.addMouseListener(new DoubleClickMouseHandler());
			scrollPaneForOrdersTable.getViewport().add(tableOrders);
		}else if(forCustomerOnly == false){
			for(int i=0; i< currentCustomerOrders.size(); i++){
				arrayOrders[i][0] = currentCustomerOrders.get(i).getId();
				arrayOrders[i][1] = currentCustomerOrders.get(i).getCustomer().getCustomerFName()+" "+currentCustomerOrders.get(i).getCustomer().getCustomerLName();
				arrayOrders[i][2] = currentCustomerOrders.get(i).getStaff().getName()+" "+currentCustomerOrders.get(i).getStaff().getSurname();
				arrayOrders[i][3] = sdf.format(currentCustomerOrders.get(i).getCreationDate());
				arrayOrders[i][4] = df.format(currentCustomerOrders.get(i).getTotalNet());
				arrayOrders[i][5] = df.format(currentCustomerOrders.get(i).getTotalGross());
				arrayOrders[i][6] = currentCustomerOrders.get(i).isComplete();
			}
			tableModelForOrdersTable = new ProductTableModel(arrayOrders, columnNames1);
			tableOrders = new JTable(tableModelForOrdersTable);
			tableOrders.setAutoCreateRowSorter(true);
			
			//Handle double clicking on table to display details of the double-clicked row's order
			tableOrders.addMouseListener(new DoubleClickMouseHandler());
			scrollPaneForOrdersTable.getViewport().add(tableOrders);
		}else{
			int counter = 0;
			ArrayList<CustomerOrder> thisCustomerOrders = getSelectedCustomerOrders();
			arrayOrders = new Object[thisCustomerOrders.size()][7];
			for(int j=0; j < thisCustomerOrders.size(); j++){
					arrayOrders[j][0] = thisCustomerOrders.get(j).getId();
					arrayOrders[j][1] = thisCustomerOrders.get(j).getCustomer().getCustomerFName()+" "+thisCustomerOrders.get(j).getCustomer().getCustomerLName();
					arrayOrders[j][2] = thisCustomerOrders.get(j).getStaff().getName()+" "+thisCustomerOrders.get(j).getStaff().getSurname();
					arrayOrders[j][3] = sdf.format(thisCustomerOrders.get(j).getCreationDate());
					arrayOrders[j][4] = df.format(thisCustomerOrders.get(j).getTotalNet());
					arrayOrders[j][5] = df.format(thisCustomerOrders.get(j).getTotalGross());
					arrayOrders[j][6] = thisCustomerOrders.get(j).isComplete();
					counter++;
			}
			tableModelForOrdersTable = new ProductTableModel(arrayOrders, columnNames1);
			tableOrders = new JTable(tableModelForOrdersTable);
			tableOrders.setAutoCreateRowSorter(true);
			//Handle double clicking on table to display details of the double-clicked row's order
			tableOrders.addMouseListener(new DoubleClickMouseHandler());
			scrollPaneForOrdersTable.getViewport().add(tableOrders);
			
		}//end else
	}//end displayPreviousCustomerOrderTable
	 
	/**
	 * Returns ArrayList of CustomerOrder for the currently selected customer. If none found a message is displayed and null returned.
	 * @return
	 */
	public ArrayList<CustomerOrder> getSelectedCustomerOrders(){
		ArrayList<CustomerOrder> customerOrd = new ArrayList<CustomerOrder>();
		boolean foundAtLeastOne = false;
		for(CustomerOrder order:Shop.getCustomerOrders()){
			if(order.getCustomer().getCustomerID() == selectedCustomer.getCustomerID()){
				customerOrd.add(order);
				foundAtLeastOne = true;
			}
		}
		if(foundAtLeastOne){
			return customerOrd;
		}else{
			displayErrorMessage("No Customer orders were found",Color.red);
			return null;
		}
	}
	
	/**
	 * Returns the Customer object of the passed in concatenation.
	 * @param name
	 * @return
	 */
	public Customer getCustomerFromConcatenatedName(String name){
		for(Customer customer:Shop.getCustomers()){
			String thisFullName = customer.getCustomerFName()+" "+customer.getCustomerLName();
			if(thisFullName.equalsIgnoreCase(name)){
				return customer;
			}
		}
		return null;
	}//end getCustomerFromConcatenatedName()

	public Object[][] getAvailableProductsArray() {
		return availableProductsArray;
	}

	public void setAvailableProductsArray(Object[][] availableProductsArray) {
		this.availableProductsArray = availableProductsArray;
	}
	
	public Object[][] getOrdersArray() {
		return arrayOrders;
	}

	public void setOrdersArray(Object[][] arrayOrders) {
		this.arrayOrders = arrayOrders;
	}
	
	/**
	 * Shows the lblError text for 4 seconds. 
	 * @param error Text for the error message
	 * @param color Color of the message
	 */
	public void displayErrorMessage(String error, Color color){
		if(lblError.isVisible() == false){
			lblError.setForeground(color);
			lblError.setText(error);
			lblError.setVisible(true);
			timer = new Timer(4000, new ActionListener(){
	
				@Override
				public void actionPerformed(ActionEvent e) {
					lblError.setVisible(false);
					timer.stop();
				}
				
			});
			timer.start();
		}
	}

	/**
	 * Decrements the amount from product quantity. Products as well as the Object[][] array (availableProductsArray) are modified
	 * @param productId
	 * @param deductableAmount
	 * @return
	 */
	public boolean decrementProductAvalableQuantity(int productId, int deductableAmount){
		Object[][] productList = CustomerOrderPanel.this.getAvailableProductsArray();
		for(Object[] x:productList){
			if((int) x[0] == productId){
				x[6] = (int) x[6] - deductableAmount;
				x[7] = 0;
				break;
			}
		}
		return false;
	}//end decrementProductAvalableQuantity
	
	public boolean decrementProductQuantityFromProducts(int productId, int deductableAmount){
		for(Product y:Shop.getProducts()){
			if(y.getId() == productId){
				y.setQuantity(y.getQuantity()-deductableAmount);
				if(y.getQuantity() == 0){
					y.setAvailable(false);
					y.setFlaggedForOrder(true);
				}
				return true;
			}
		}
		return false;
	}
	

		public void updateOrders() {
			//find all orders that have "completed" value changed to true
			Object[][] orders = null;
			if(CustomerOrderPanel.this.getOrdersArray() != null){
				orders = CustomerOrderPanel.this.getOrdersArray();
				int counter = 0;
				boolean found = false;
				boolean foundInvalidOperation = false;
				for(Object[] x:orders){
					if((boolean) x[6] == true && Shop.getCustomerOrders().get(counter).isComplete() == false){
						changeOrderCompletion((int)x[0], true);
						found = true;
					}else if((boolean) x[6] == false && Shop.getCustomerOrders().get(counter).isComplete() == true){
						foundInvalidOperation = true;
					}
					counter ++;
				}
				if(found){
					AbstractTableModel model = (AbstractTableModel) tableOrders.getModel();
					model.fireTableDataChanged();
					
					//save orders to a persistent format
					if(Json.clearList("resources/customerOrders.json")){
						for(CustomerOrder co:Shop.getCustomerOrders()){
							Json.saveCustomerOrdersToFile(co);
						}
					}else{
						displayErrorMessage("Could not persist changes!", Color.red);
					}
					
					//update previousCustomerOrderTable with the new order.
					if(typeOfOrder.equals("All")){
						displayOrderTable(false,0);
						typeOfOrder = "All";
					}else{
						displayOrderTable(true,0);
						typeOfOrder = "Customer";
					}
					displayErrorMessage("Order(s) has(have) been updated successfully.", Color.BLUE);
				}else if(foundInvalidOperation){
					if(typeOfOrder.equals("All")){
						displayOrderTable(false,0);
						typeOfOrder = "All";
					}else{
						displayOrderTable(true,0);
						typeOfOrder = "Customer";
					}
					displayErrorMessage("You cannot change the status of already completed orders!", Color.red);
				}else{
					displayErrorMessage("Nothing to update", Color.red);
				}
			}else{
				displayErrorMessage("Nothing to update", Color.red);
			}
			
		}//end actionPerformed
		public void changeOrderCompletion(int orderId, boolean isComplete){
			for(CustomerOrder x:Shop.getCustomerOrders()){
				if(x.getId() == orderId){
					x.setComplete(isComplete);
				}
			}
		}
	
	
	public class OrderTableJPopupMenu extends JPopupMenu{
		JMenuItem menuItemUpdate = new JMenuItem("Set To Complete");
		JMenuItem menuItemShow = new JMenuItem("Show Order");
		public OrderTableJPopupMenu(){
			add(menuItemShow);
			add(menuItemUpdate);
			menuItemUpdate.addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					int row = tableOrders.getSelectedRow();
					ProductTableModel model = (ProductTableModel) tableOrders.getModel();
					model.setValueAt(true, row, 6);
					model.fireTableDataChanged();
					updateOrders();
				}
			});
			menuItemShow.addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					int row = tableOrders.getSelectedRow();
			         
			         int id = (int) arrayOrders[row][0];
			         String customer = (String) arrayOrders[row][1];
			         String staff = (String) arrayOrders[row][2];
			         String date = (String) arrayOrders[row][3];
			         String totalNet = (String) arrayOrders[row][4];
			         String totalGross = (String) arrayOrders[row][5];
			         
			         String htmlText = "<html><head><style>th {color:#305EE3;font-variant:small-caps;}</style></head>";
			         htmlText += "<h2>Order details:</h2><table border='1'><tr><th>ID</th><th>Customer</th><th>Staff</th><th>Creation Date</th><th>Total Net</ht<th>Total Gross</th></tr>";
			         htmlText += "<tr><td>"+id+"</td></td>"+customer+"</td><td>"+ staff+"</td><td>"+date+"</td><td>"+totalNet+"</td><td>"+totalGross+"</td></tr>";
		        
			         CustomerOrder orderClicked = null;
			         for(CustomerOrder order:Shop.getCustomerOrders()){
			        	 if(order.getId() == id){
			        		 orderClicked = order;
			        	 }
			         }
				         htmlText += "</table><br><h2>Products:</h2><table border='1'><tr><th>ID</th><th>Name</th><th>Supplier</th><th>Category</th><th>Price</th><th>Amount</th><th>Total</th></tr>";	        	 
			         for(ProductToOrder prod:orderClicked.getProducts()){
			        	 htmlText += prod.toHtmlString();
			         }
			         htmlText += "</table></html>";
			         new PopupDialog(htmlText);
				}
			});
		}
	}
	
//----------------------------------------------------------LISTENERS-----------------------------------------------------------//
	
	public class ComboBoxItemListener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED){
				if(e.getSource() == comboSelectCustomer){
					Customer customer = getCustomerFromConcatenatedName(e.getItem().toString());
					if(customer != null){
						selectedCustomer = customer;
						lblActiveCustomer.setText(selectedCustomer.getCustomerFName()+" "+selectedCustomer.getCustomerLName());
						lblActiveCustomer.setForeground(Color.blue);
					}else{
						lblActiveCustomer.setText("none");
						lblActiveCustomer.setForeground(Color.red);
						//displayErrorMessage("No such customer in the list", Color.red);
					}
				}else if(e.getSource() == comboSearchForProducts){
					String product = e.getItem().toString();
					if(product != ""){
						for(Product prod:Shop.getProducts()){
							if(prod.getName().equalsIgnoreCase(product) && prod.isDeleted() == false){
								displayProductsTable(product);
								break;
							}
						}
					}else{
						displayProductsTable("");
					}
				}
			}
		}
	}//end ComboBoxItemListener class
	
	public class ComboBoxKeyListener implements KeyListener{
		@Override
		public void keyPressed(KeyEvent event) {
			if(event.getKeyCode() == KeyEvent.VK_ENTER){
				if(event.getSource() == CustomerOrderPanel.comboSelectCustomer){
					String concatenatedName = comboSelectCustomer.getSelectedItem().toString();
					if(getCustomerFromConcatenatedName(concatenatedName) != null){
						selectedCustomer = getCustomerFromConcatenatedName(concatenatedName);
						lblActiveCustomer.setText(selectedCustomer.getCustomerFName()+" "+selectedCustomer.getCustomerLName());
						lblActiveCustomer.setForeground(Color.blue);
					}else{
						lblActiveCustomer.setText("none");
						lblActiveCustomer.setForeground(Color.red);
						displayErrorMessage("No such customer in the list", Color.red);
					}
				}else if(event.getSource() == comboSearchForProducts){
					String product = comboSearchForProducts.getSelectedItem().toString();
					if(product != ""){
						displayProductsTable(product);
					}else{
						displayProductsTable("");
					}
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
	
	public class TxtInvoiceIDSearchKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				try{
					if(txtInvoiceIDSearch.getText() != null){
						int x = Integer.parseInt(txtInvoiceIDSearch.getText());
						boolean found = false;
						for(CustomerOrder o:Shop.getCustomerOrders()){
							if(x == o.getId()){
								displayOrderTable(false,x);
								found = true;
								break;
							}
						}
						if(found == false){
							displayErrorMessage("No such invoice", Color.red);	
						}
					}
				}catch(InputMismatchException ime){
					displayErrorMessage("Invalid Input", Color.red);
				}catch(NumberFormatException nfe){
					displayErrorMessage("Invalid Input", Color.red);
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
		
	}
	
	public class ButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnAllProducts){
				displayProductsTable("");
			}
		}
	}
	
	public class BtnAddToOrderActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean found = false;
			for(int i =0; i< availableProductsArray.length; i++){
				if((int)availableProductsArray[i][7] > 0 && (int)availableProductsArray[i][6] >= (int)availableProductsArray[i][7]){
					
					int id = (int) availableProductsArray[i][0];
					String name = (String) availableProductsArray[i][1];
					String supplier = null;
					for(Supplier sup:Shop.getSuppliers()){
						if(sup.getSupplierName().equalsIgnoreCase((String) availableProductsArray[i][2])){
							supplier = sup.getSupplierName();
						}
					}
					String category = (String) availableProductsArray[i][3];
					double markupPrice = (double) availableProductsArray[i][4];
					boolean discounted = (boolean)availableProductsArray[i][5];
					int quantity = (int) availableProductsArray[i][6];
					int amount = (int) availableProductsArray[i][7];
					Object[] p = {id, name, supplier, category, markupPrice, discounted, quantity, amount};
					//check if there already is a product with same id
					boolean foundIdInCurrentOrder = false;
					for(Object[] o : arrayCurrentOrder){
						if(id == (int)o[0]){
							foundIdInCurrentOrder = true;
							//check whether the total amount for this product exceeds the product's quantity
							if((int)o[7]+amount > quantity){
								JOptionPane.showMessageDialog(CustomerOrderPanel.this, "The total amount for "+(String)o[1]+" would exceed the available quantity: "+quantity+"\nCheck the Current Order");
								return;
							}
							o[7] = (int)o[7]+amount; 
							break;
						}
					}
					if(foundIdInCurrentOrder== false){
						arrayCurrentOrder.add(p);
					}
					
					found = true;
					decrementProductAvalableQuantity((int)availableProductsArray[i][0], (int)availableProductsArray[i][7]);
					displayErrorMessage("Product(s) added to order", Color.blue);
					AbstractTableModel model = (AbstractTableModel) tableAvailableProducts.getModel();
					model.fireTableDataChanged();
				}else if((int)availableProductsArray[i][7] > 0 && (int)availableProductsArray[i][6] < (int)availableProductsArray[i][7]){
					displayErrorMessage("You cannot order more than available!", Color.red);
				}
			}
			if(found == false){
				displayErrorMessage("Nothing to add", Color.red);
			}else{
				AbstractTableModel model = (AbstractTableModel) tableAvailableProducts.getModel();
				model.fireTableDataChanged();
			}
		}
	}
	
	/**
	 *This handler is called when user clicks on Submit Order button.
	 *If selected customer is not null, then user is prompted to submit the order. If user confirms, the table changes are 
	 *compared with availableProductArray to find if all product amounts can be purchased. Only then is the order created
	 *and the table redrawn to reflect changes in product quantity. Product quantity is also adjusted
	 */
	public class ButtonOrderHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(selectedCustomer == null){
				displayErrorMessage("Please select a customer!", Color.red);
				return;
			}
			if(arrayCurrentOrder.size() < 1){
				displayErrorMessage("Nothing to order", Color.red);
				return;
			}
			CurrentCustomerOrderDialog dialog = new CurrentCustomerOrderDialog();
			dialog.confirmOrder(arrayCurrentOrder);
			if(dialog.getValue() == 1){
				ArrayList<ProductToOrder> productsToOrder = new ArrayList<ProductToOrder>();
				for(Object[] x : arrayCurrentOrder){
						//check whether the available quantity is less than the amount entered
						if((Integer) x[6] < (Integer) x[7]){
							displayErrorMessage("You cannot order higher amount than currently available", Color.red);
							return;
						}
						int id = (Integer) x[0];
						String name = (String) x[1];
						Supplier tempSupplier = null;
						boolean supplierFound = false;
						for(Supplier supplier:Shop.getSuppliers()){
							if(supplier.getSupplierName().equalsIgnoreCase(x[2].toString())){
								tempSupplier = supplier;
								supplierFound = true;
								break;
							}
						}
						//if supplier is not found, break and display message.
						if(supplierFound == false){
							JOptionPane.showMessageDialog(CustomerOrderPanel.this, "No supplier has been found for at least on of the products. Make sure you do not edit suppliers and make the order in the same time.");
							return;
						}
						String category = (String) x[3];
						double price = (Double) x[4];
						boolean discounted = (Boolean) x[5];
						int amount = (Integer) x[7];
						productsToOrder.add(new ProductToOrder(id, name, tempSupplier, category, price, discounted, amount));	
				}
				
				//create the actual order
				//check if the user did not remove manually all products from table
				if(productsToOrder.size() == 0){
					displayErrorMessage("Nothing to order!", Color.red);
					return;
				}else{
					CustomerOrder order = new CustomerOrder(selectedCustomer, GUIBackBone.getLoggedStaffMember(), productsToOrder);
					order.setId(Shop.getCustomerOrders().get(Shop.getCustomerOrders().size()-1).getId()+1);
					//check if a discount applies
					if(order.getTotalGross() > 5000){
						order.setTotalGross(order.getTotalGross()*0.9);
						order.setTotalGross(order.getTotalNet()*0.9);
					}else if(order.getTotalGross() > 2000){
						order.setTotalGross(order.getTotalGross()*0.95);
						order.setTotalGross(order.getTotalNet()*0.95);
					}
					
					Shop.getCustomerOrders().add(order);
					//update table model data to reflect changes
					for(ProductToOrder x:productsToOrder){
						//decrementProductAvalableQuantity(x.getId(),x.getAmount());
						decrementProductQuantityFromProducts(x.getId(),x.getAmount());
					}
					AbstractTableModel model = (AbstractTableModel) tableAvailableProducts.getModel();
					model.fireTableDataChanged();
					
					//update previousCustomerOrderTable with the new order.
					displayOrderTable(false, 0);
					typeOfOrder = "All";
					
					//update products table just in case a product has been sold out
					displayProductsTable("");
					
					//reset the array of current orders
					arrayCurrentOrder = new ArrayList<Object[]>();
										
					//save orders to a persistent format
					if(Json.clearList("resources/customerOrders.json") && Json.clearList("resources/products.json")){
						for(CustomerOrder co:Shop.getCustomerOrders()){
							Json.saveCustomerOrdersToFile(co);
						}
						
						//save products to a persistent format
						for(Product p:Shop.getProducts()){
							Json.saveProductToFile(p);
						}
					}else{
						displayErrorMessage("Could not persist changes!", Color.red);
					}
					
					//repopulate product search combo box in case some of the products are no longer available
					populateProductNamesComboBox();
					
				}//end else
			}
		}//end actionPerformed()
	}//end inner class ButtonOrderHandler
	
	/**
	 *This button handler verifies if there are orders for selected customer. If so, a method is called to redraw the table with 
	 *a new table model data. Otherwise a blank table model is created to imitate empty table
	 */
	public class ButtonDisplayOrdersForSelectedCustomersHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(selectedCustomer != null && getSelectedCustomerOrders() != null){
				isActiveCustomerOrderTablePopulated = true;
				displayOrderTable(true,0);
				typeOfOrder = "Customer";
			}else{
				String columnNames1[] = {"Id","Customer","Staff","Date","Total Net","Total Gross","Completed?"};
				
				tableModelForOrdersTable = new ProductTableModel(arrayOrders, columnNames1);
				tableOrders = new JTable(tableModelForOrdersTable);
				tableOrders.addMouseListener(new DoubleClickMouseHandler());
				scrollPaneForOrdersTable.getViewport().add(tableOrders);
				displayErrorMessage("No previous orders found for selected customer", Color.red);
			}
		}//end actionPerformed
		
	}//end inner class ButtonDisplayOrdersForSelectedCustomersHandler
	
	/**
	 *This Button handler calls the method to display all orders in the table.
	 */
	public class ButtonDisplayOrdersForAllCustomersHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			isActiveCustomerOrderTablePopulated = false;
			displayOrderTable(false,0);
			typeOfOrder = "All";
		}//end actionPerformed
		
	}//end inner class ButtonDisplayOrdersForAllCustomersHandler
	
	public class DoubleClickMouseHandler implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
	      if (e.getClickCount() == 2) {
		         JTable target = (JTable)e.getSource();
		         int row = target.getSelectedRow();
		         
		         int id = (int) arrayOrders[row][0];
		         String customer = (String) arrayOrders[row][1];
		         String staff = (String) arrayOrders[row][2];
		         String date = (String) arrayOrders[row][3];
		         String totalNet = (String) arrayOrders[row][4];
		         String totalGross = (String) arrayOrders[row][5];
		         
		         String htmlText = "<html><head><style>th {color:#305EE3;font-variant:small-caps;}</style></head>";
		         htmlText += "<h2>Order details:</h2><table border='1'><tr><th>ID</th><th>Customer</th><th>Staff</th><th>Creation Date</th><th>Total Net</ht<th>Total Gross</th></tr>";
		         htmlText += "<tr><td>"+id+"</td></td>"+customer+"</td><td>"+ staff+"</td><td>"+date+"</td><td>"+totalNet+"</td><td>"+totalGross+"</td></tr>";
	        
		         CustomerOrder orderClicked = null;
		         for(CustomerOrder order:Shop.getCustomerOrders()){
		        	 if(order.getId() == id){
		        		 orderClicked = order;
		        	 }
		         }
			         htmlText += "</table><br><h2>Products:</h2><table border='1'><tr><th>ID</th><th>Name</th><th>Supplier</th><th>Category</th><th>Price</th><th>Amount</th><th>Total</th></tr>";	        	 
		         for(ProductToOrder prod:orderClicked.getProducts()){
		        	 htmlText += prod.toHtmlString();
		         }
		         htmlText += "</table></html>";
		         new PopupDialog(htmlText);
	      }
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
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

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
		
	}
	
}//end class CustomerOrderPanel
