package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import tableModels.ProductTableModel;
import data.Customer;
import data.CustomerOrder;
import data.Product;
import data.ProductToOrder;
import data.Supplier;

//this class deals with customer ordering only.
public class CustomerOrderPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JComboBox comboSelectCustomer;
	private JButton btnSelectCustomer;
	private Customer selectedCustomer = null;;
	private JLabel lblProductList;
	private JButton btnOrder = null;
	private Object[][] availableProductsArray ;
	private JLabel lblActiveCustomerText = null;
	private JLabel lblActiveCustomer = null;
	private JTable tableAvailableProducts;
	private JTable tableOrders;
	private JLabel lblPreviousCustomerOrder;
	private Object[][] ordersArray;
	private Object[][] ordersArrayOfSelectedCustomer;
	private JScrollPane scrollPaneForOrdersTable;
	private ProductTableModel tableModelForOrdersTable;
	private JButton btnDisplayOrdersForSelectedCustomer = new JButton("Customer Orders");
	private JButton btnUpdateOrderCompletion = new JButton("Update order");
	private JButton btnDisplayAllOrders = new JButton("All orders");
	private boolean isActiveCustomerOrderTablePopulated;
	private JLabel lblError = new JLabel("");
	private Timer timer;
	
	/**
	 * Adds all the GUI components on the panel
	 */
	public CustomerOrderPanel() {
		setLayout(new MigLayout());
		
		JLabel lblCustomer = new JLabel("Customer:");
		add(lblCustomer, "split 5");
		
		ArrayList<String> customerNames = new ArrayList<String>();
		customerNames.add("");
		
		//concatenate customer names and add them to the combo box
		for ( Customer customer: Shop.getCustomers()){
			String name = customer.getCustomerFName()+" "+customer.getCustomerLName();
			customerNames.add(name);
		}		
		comboSelectCustomer = new JComboBox(customerNames.toArray());
		add(comboSelectCustomer);
		//allow for auto-completion
		comboSelectCustomer.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectCustomer);
		
		btnSelectCustomer = new JButton("Select");
		add(btnSelectCustomer);
		btnSelectCustomer.addActionListener(new ButtonHandlerForSelectingCustomer());
		
		lblActiveCustomerText = new JLabel("Active Customer: ");
		lblActiveCustomer = new JLabel("none");
		lblActiveCustomer.setForeground(Color.red);
		add(lblActiveCustomerText, "gapx 20px");
		add(lblActiveCustomer, "wrap");
		
		lblProductList = new JLabel("Product list:");
		add(lblProductList, "wrap");
		
		availableProductsArray = new Object[Shop.getProducts().size()][8];
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
				availableProductsArray[counter][6] = product.getQuantity();
				//this column will be editable
				availableProductsArray[counter][7] = 0;
				counter ++;
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
			        		 System.out.println("Parsed choice: "+parsedChoice);
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
		
		JScrollPane scrollPane = new JScrollPane(tableAvailableProducts);
		add(scrollPane, "span 5, grow, push");
		
		//add the order button
		btnOrder = new JButton("New Order");
		btnOrder.setToolTipText("All the products that have \"Amount to order\" greater than 0 will be placed on the order.");
		JPanel panelx = new JPanel();
		panelx.setLayout(new MigLayout());
		panelx.add(btnOrder,"growx, pushx");
		add(panelx, "aligny top, alignx left, wrap, growx");
		btnOrder.addActionListener(new ButtonOrderHandler());
		
		
		//add the gui elements to view previous orders of a customer
		lblPreviousCustomerOrder = new JLabel("Previous Orders:");
		add(lblError,"cell 0 3, wrap, pushx, alignx center");
		add(lblPreviousCustomerOrder, "wrap");
		lblError.setVisible(false);
		lblError.setForeground(Color.red);
		lblError.setFont(new Font("Serif",Font.BOLD,15));
		
		scrollPaneForOrdersTable = new JScrollPane();
		add(scrollPaneForOrdersTable, "span 5 3, grow, push");
		displayOrderTable(false);
		
		JPanel oneCellPanel = new JPanel();
		oneCellPanel.setLayout(new MigLayout());
		oneCellPanel.add(btnDisplayAllOrders, "wrap, growx, aligny top");
		btnDisplayAllOrders.addActionListener(new ButtonDisplayOrdersForAllCustomersHandler());
		oneCellPanel.add(btnDisplayOrdersForSelectedCustomer, "wrap, growx, aligny top");
		btnDisplayOrdersForSelectedCustomer.addActionListener(new ButtonDisplayOrdersForSelectedCustomersHandler());
		oneCellPanel.add(btnUpdateOrderCompletion, "wrap, growx, aligny top");
		btnUpdateOrderCompletion.addActionListener(new UpdateOrdersHandler());
		add(oneCellPanel, "aligny top");
		
	}//end constructor
	
	/**
	 *This handler is called when selecting customer. Selects the customer object and modifies appropriate label.
	 */
	public class ButtonHandlerForSelectingCustomer implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
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
		}
	}//end ButtonHandlerForSelectingCustomer
	
	/**
	 *This handler is called when user clicks on New Order button.
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
			int option = JOptionPane.showConfirmDialog(CustomerOrderPanel.this, 
					"All the products that have \"Amount to order\" greater than 0\n will be placed on the order." 
					, "", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION){
				//get Updated product list
				Object[][] updatedProductList = CustomerOrderPanel.this.getAvailableProductsArray();
				ArrayList<ProductToOrder> productsToOrder = new ArrayList<ProductToOrder>();
				for(Object[] x : updatedProductList){
					//add only products that have amount set to > 0
					if((Integer) x[7] > 0){
						
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
							if(supplier.getSupplierName() == (String) x[2]){
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
				}
				
				//create the actual order
				if(productsToOrder.size() == 0){
					displayErrorMessage("Nothing to order!", Color.red);
					return;
				}else{
					CustomerOrder order = new CustomerOrder(selectedCustomer, GUIBackBone.getLoggedStaffMember(), productsToOrder);
					Shop.getCustomerOrders().add(order);
					System.out.println("Order has been created\nOrder id:"+order.getId()+"\nOrder totalGross: "+order.getTotalGross()+"\nOrder totalNet: "+order.getTotalNet() + order.getCustomer().getCustomerFName());
					//update table model data to reflect changes
					for(ProductToOrder x:productsToOrder){
						decrementProductAvalableQuantity(x.getId(),x.getAmount());
					}
					AbstractTableModel model = (AbstractTableModel) tableAvailableProducts.getModel();
					model.fireTableDataChanged();
					
					//update previousCustomerOrderTable with the new order.
					displayOrderTable(false);
				}//end else
			}//end if
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
				displayOrderTable(true);
			}else{
				System.out.println("clearTable");
				String columnNames1[] = {"Id","Customer","Staff","Date","Total Net","Total Gross","Completed?"};
				Object[][] emptyObjectArray = new Object[1][7];
				emptyObjectArray[0][0] = "";
				emptyObjectArray[0][1] = "";
				emptyObjectArray[0][2] = "";
				emptyObjectArray[0][3] = "";
				emptyObjectArray[0][4] = "";
				emptyObjectArray[0][5] = "";
				emptyObjectArray[0][6] = "";
				
				tableModelForOrdersTable = new ProductTableModel(emptyObjectArray, columnNames1);
				tableOrders = new JTable(tableModelForOrdersTable);
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
			displayOrderTable(false);
		}//end actionPerformed
		
	}//end inner class ButtonDisplayOrdersForAllCustomersHandler
	
	/**
	 *This Button handler updates the table data and CustomerOrder object of all orders that have been assigned to "Completed"
	 */
	public class UpdateOrdersHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//find all orders that have "completed" value changed to true
			Object[][] orders = null;
			if (isActiveCustomerOrderTablePopulated == true){
				orders = CustomerOrderPanel.this.getOrdersArrayOfSelectedCustomer();
			}else{
				orders = CustomerOrderPanel.this.getOrdersArray();
			}
			for(Object[] x:orders){
				if((boolean) x[6] == true){
					changeOrderCompletion((int)x[0], true);
				}else{
					changeOrderCompletion((int)x[0], false);
				}
			}
			AbstractTableModel model = (AbstractTableModel) tableOrders.getModel();
			model.fireTableDataChanged();
			
			//update previousCustomerOrderTable with the new order.
			displayOrderTable(false);
			displayErrorMessage("Order(s) has(have) been updated successfully.", Color.green);
			
		}//end actionPerformed
		
		public void changeOrderCompletion(int orderId, boolean isComplete){
			for(CustomerOrder x:Shop.getCustomerOrders()){
				if(x.getId() == orderId){
					x.setComplete(isComplete);
				}
			}
		}
		
	}//end inner class UpdateOrdersHandler
	 
	/**
	 * Returns ArrayList of CustomerOrder for the currently selected customer. If none found a message is displayed and null returned.
	 * @return
	 */
	public ArrayList<CustomerOrder> getSelectedCustomerOrders(){
		ArrayList<CustomerOrder> customerOrd = new ArrayList<CustomerOrder>();
		boolean foundAtLeastOne = false;
		for(CustomerOrder order:Shop.getCustomerOrders()){
			if(order.getCustomer() == selectedCustomer){
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
		return ordersArray;
	}

	public void setOrdersArray(Object[][] ordersArray) {
		this.ordersArray = ordersArray;
	}

	public Object[][] getOrdersArrayOfSelectedCustomer() {
		return ordersArrayOfSelectedCustomer;
	}

	public void setOrdersArrayOfSelectedCustomer(Object[][] ordersArrayOfSelectedCustomer) {
		this.ordersArrayOfSelectedCustomer = ordersArrayOfSelectedCustomer;
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
		for(Product y:Shop.getProducts()){
			if(y.getId() == productId){
				y.setQuantity(y.getQuantity()-deductableAmount);
/*				if(y.getQuantity() == 0){
					y.setAvailable(false);
				}*/
				return true;
			}
		}
		return false;
	}//end decrementProductAvalableQuantity
	
	
	public void displayOrderTable(boolean forCustomerOnly){
		//display the order in the previousOrderTable
		String columnNames1[] = {"Id","Customer","Staff","Date","Total Net","Total Gross","Completed?"};
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		DecimalFormat df = new DecimalFormat("#.00");
		ArrayList<CustomerOrder> currentCustomerOrders = Shop.getCustomerOrders();
		ordersArray = new Object[currentCustomerOrders.size()][7];
		
		//if forCustomerOnly = false then display all orders in the CustomeOrder array, otherwise only for the selected customer
		if(forCustomerOnly == false){
			for(int i=0; i< currentCustomerOrders.size(); i++){
				ordersArray[i][0] = currentCustomerOrders.get(i).getId();
				ordersArray[i][1] = currentCustomerOrders.get(i).getCustomer().getCustomerFName()+" "+currentCustomerOrders.get(i).getCustomer().getCustomerLName();
				ordersArray[i][2] = currentCustomerOrders.get(i).getStaff().getName()+" "+currentCustomerOrders.get(i).getStaff().getSurname();
				ordersArray[i][3] = sdf.format(currentCustomerOrders.get(i).getCreationDate());
				ordersArray[i][4] = df.format(currentCustomerOrders.get(i).getTotalNet());
				ordersArray[i][5] = df.format(currentCustomerOrders.get(i).getTotalGross());
				ordersArray[i][6] = currentCustomerOrders.get(i).isComplete();
			}
			tableModelForOrdersTable = new ProductTableModel(ordersArray, columnNames1);
			tableOrders = new JTable(tableModelForOrdersTable);
			tableOrders.setAutoCreateRowSorter(true);
			
			//Handle double clicking on table to display details of the double-clicked row's order
			tableOrders.addMouseListener(new MouseAdapter() {
				   public void mouseClicked(MouseEvent e) {
				      if (e.getClickCount() == 2) {
				         JTable target = (JTable)e.getSource();
				         int row = target.getSelectedRow();
				         
				         int id = (int) ordersArray[row][0];
				         String customer = (String) ordersArray[row][1];
				         String staff = (String) ordersArray[row][2];
				         String date = (String) ordersArray[row][3];
				         String totalNet = (String) ordersArray[row][4];
				         String totalGross = (String) ordersArray[row][5];
				         String htmlString = "<html><span style='color:rgb(92, 150, 238); font-size:1.2em'>Order ID:</span> "+id+"<span style='color:rgb(92, 150, 238); font-size:1.2em'> For Customer:</span> "+
				         customer+"<span style='color:rgb(92, 150, 238); font-size:1.2em'> Made by staff member:</span> "+staff+"<span style='color:rgb(92, 150, 238); font-size:1.2em'> On date:</span> "+date+"<br>";
				         CustomerOrder doubleClickedOrder = null;
				         for(CustomerOrder order:Shop.getCustomerOrders()){
				        	 if(order.getId() == id){
				        		 doubleClickedOrder = order;
				        	 }
				         }
				         	htmlString += "<br><span style='color:rgb(92, 150, 238); font-size:1.2em'>Products: </span><br>";
				         	htmlString +="<table style='text-align:left;'><tr style='color:rgb(240, 157, 52)'><th>ID</th><th>Name</th><th>Supplier</th><th>Category</th><th>Price</th><th>Amount</th><th>Total</th></tr>";
				         for(ProductToOrder prod:doubleClickedOrder.getProducts()){
				        	 htmlString +=prod.toHtmlString();
				         }
				         htmlString +="</table><br><span style='color:rgb(92, 150, 238); font-size:1.2em'>Total Net:</span> "+totalNet+" <span style='color:rgb(92, 150, 238); font-size:1.2em'>Total Gross:</span> "+totalGross+"</html>";
				         new PopupDialog(htmlString);
				         }
				   }
				});
			scrollPaneForOrdersTable.getViewport().add(tableOrders);
		}else{
			int counter = 0;
			ArrayList<CustomerOrder> thisCustomerOrders = getSelectedCustomerOrders();
			ordersArrayOfSelectedCustomer = new Object[thisCustomerOrders.size()][7];
			for(int j=0; j < thisCustomerOrders.size(); j++){
				if(thisCustomerOrders.get(j).getCustomer().equals(selectedCustomer)){
					ordersArrayOfSelectedCustomer[counter][0] = thisCustomerOrders.get(j).getId();
					ordersArrayOfSelectedCustomer[counter][1] = thisCustomerOrders.get(j).getCustomer().getCustomerFName()+" "+thisCustomerOrders.get(j).getCustomer().getCustomerLName();
					ordersArrayOfSelectedCustomer[counter][2] = thisCustomerOrders.get(j).getStaff().getName()+" "+thisCustomerOrders.get(j).getStaff().getSurname();
					ordersArrayOfSelectedCustomer[counter][3] = sdf.format(thisCustomerOrders.get(j).getCreationDate());
					ordersArrayOfSelectedCustomer[counter][4] = df.format(thisCustomerOrders.get(j).getTotalNet());
					ordersArrayOfSelectedCustomer[counter][5] = df.format(thisCustomerOrders.get(j).getTotalGross());
					ordersArrayOfSelectedCustomer[counter][6] = thisCustomerOrders.get(j).isComplete();
					counter++;
				}
			}
			tableModelForOrdersTable = new ProductTableModel(ordersArrayOfSelectedCustomer, columnNames1);
			tableOrders = new JTable(tableModelForOrdersTable);
			tableOrders.setAutoCreateRowSorter(true);
			//Handle double clicking on table to display details of the double-clicked row's order
			tableOrders.addMouseListener(new MouseAdapter() {
				   public void mouseClicked(MouseEvent e) {
					      if (e.getClickCount() == 2) {
						         JTable target = (JTable)e.getSource();
						         int row = target.getSelectedRow();
						         
						         int id = (int) ordersArrayOfSelectedCustomer[row][0];
						         String customer = (String) ordersArrayOfSelectedCustomer[row][1];
						         String staff = (String) ordersArrayOfSelectedCustomer[row][2];
						         String date = (String) ordersArrayOfSelectedCustomer[row][3];
						         String totalNet = (String) ordersArrayOfSelectedCustomer[row][4];
						         String totalGross = (String) ordersArrayOfSelectedCustomer[row][5];
						         String htmlString = "<html><span style='color:rgb(92, 150, 238); font-size:1.2em'>Order ID:</span> "+id+"<span style='color:rgb(92, 150, 238); font-size:1.2em'> For Customer:</span> "+
						         customer+"<span style='color:rgb(92, 150, 238); font-size:1.2em'> Made by staff member:</span> "+staff+"<span style='color:rgb(92, 150, 238); font-size:1.2em'> On date:</span> "+date+"<br>";
						         CustomerOrder doubleClickedOrder = null;
						         for(CustomerOrder order:Shop.getCustomerOrders()){
						        	 if(order.getId() == id){
						        		 doubleClickedOrder = order;
						        	 }
						         }
						         	htmlString += "<br><span style='color:rgb(92, 150, 238); font-size:1.2em'>Products: </span><br>";
						         	htmlString +="<table style='text-align:left;'><tr style='color:rgb(240, 157, 52)'><th>ID</th><th>Name</th><th>Supplier</th><th>Category</th><th>Price</th><th>Amount</th><th>Total</th></tr>";
						         for(ProductToOrder prod:doubleClickedOrder.getProducts()){
						        	 htmlString +=prod.toHtmlString();
						         }
						         htmlString +="</table><br><span style='color:rgb(92, 150, 238); font-size:1.2em'>Total Net:</span> "+totalNet+" <span style='color:rgb(92, 150, 238); font-size:1.2em'>Total Gross:</span> "+totalGross+"</html>";
						         new PopupDialog(htmlString);
						         }
				   }
				});
			scrollPaneForOrdersTable.getViewport().add(tableOrders);
		}//end else
	}//end displayPreviousCustomerOrderTable
	
}//end class CustomerOrderPanel
