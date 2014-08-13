package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
	private JButton selectCustomer;
	private Customer selectedCustomer = null;;
	private JLabel productListLabel;
	private JButton btnOrder = null;
	private Object myProducts[][] ;
	private JLabel lblActiveCustomerText = null;
	private JLabel lblActiveCustomer = null;
	private JTable productsTable;
	private JTable previousOrdersTable;
	private JLabel lblPreviousCustomerOrder;
	private Object[][] custOrders;
	private JScrollPane previousOrderScrollPane;
	private ProductTableModel previousOrderTableModel;
	private JButton btnDisplayOrdersForSelectedCustomer = new JButton("Customer Orders");
	private JButton btnUpdateOrderCompletion = new JButton("Update order");
	private JButton btnDisplayAllOrders = new JButton("All orders");
	
	public CustomerOrderPanel() {
		setLayout(new MigLayout());
		ArrayList<String> customerNames = new ArrayList<String>();
		customerNames.add("");
		for ( Customer customer: Shop.getCustomers()){
			String name = customer.getCustomerFName()+" "+customer.getCustomerLName();
			customerNames.add(name);
		}
		
		JLabel lblCustomer = new JLabel("Customer:");
		add(lblCustomer, "split 5");
		
		comboSelectCustomer = new JComboBox(customerNames.toArray());
		add(comboSelectCustomer);
		comboSelectCustomer.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectCustomer);
		
		selectCustomer = new JButton("Select");
		add(selectCustomer);
		selectCustomer.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String concatenatedName = comboSelectCustomer.getSelectedItem().toString();
				if(getCustomerFromConcatenatedName(concatenatedName) != null){
					selectedCustomer = getCustomerFromConcatenatedName(concatenatedName);
					lblActiveCustomer.setText(selectedCustomer.getCustomerFName()+" "+selectedCustomer.getCustomerLName());
				}else{
					JOptionPane.showMessageDialog(CustomerOrderPanel.this, "No such customer in the list");
				}
			}
			
		});
		
		lblActiveCustomerText = new JLabel("Active Customer: ");
		lblActiveCustomer = new JLabel("");
		add(lblActiveCustomerText, "gapx 20px");
		add(lblActiveCustomer, "wrap");
		
		
		productListLabel = new JLabel("Product list:");
		add(productListLabel, "wrap");
		
		myProducts = new Object[Shop.getProducts().size()][8];
		int counter = 0;
		//make products array to feed into the table model
		for(Product product:Shop.getProducts()){
			if(product.isAvailable() && product.isDeleted()==false){
				myProducts[counter][0] = product.getId();
				myProducts[counter][1] = product.getName();
				myProducts[counter][2] = product.getSupplier().getSupplierName();
				myProducts[counter][3] = product.getCategory();
				myProducts[counter][4] = product.getPrice();
				myProducts[counter][5] = product.isDiscounted();
				myProducts[counter][6] = product.getQuantity();
				//this column will be editable
				myProducts[counter][7] = 0;
				counter ++;
			}
		}
		
		// make column names for table. Must be the same size as the Object[][] you will populate it with.
		String columnNames[] = {"Id","Name","Supplier","Category","Price","Discounted?","Quantity","Amount to Order"};
		//this is your table model. See below is a class that implements AbstractTableModel
		//the table model takes in the array of objects you want to populate and array of column names
		ProductTableModel productsTableModel = new ProductTableModel(myProducts, columnNames);
		//make the actual table and pass it the table model
		productsTable = new JTable(productsTableModel);
		//make the table sortable
		productsTable.setAutoCreateRowSorter(true);
		//add the table to scroll pane if the content is greater than the container
		JScrollPane scrollPane = new JScrollPane(productsTable);
		productsTable.setCellSelectionEnabled(true);
		
		productsTable.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = productsTable.getSelectedRow();
				productsTable.changeSelection(row, 7, false, false);
				productsTable.requestFocus();
				productsTable.editCellAt(row, 7);
			}
			
		});
		
		add(scrollPane, "span 3, grow, push");
		
		//add the order button
		btnOrder = new JButton("Order");
		btnOrder.setToolTipText("All the products that have \"Amount to order\" greater than 0 will be placed on the order.");
		add(btnOrder, "aligny top, alignx left, wrap");
		btnOrder.addActionListener(new ButtonOrderHandler());
		
		
		//add the gui elements to view previous orders of a customer
		lblPreviousCustomerOrder = new JLabel("Previous Orders:");
		add(lblPreviousCustomerOrder, "cell 0 3, split 4");
		add(btnDisplayAllOrders);
		btnDisplayAllOrders.addActionListener(new ButtonDisplayOrdersForAllCustomersHandler());
		add(btnDisplayOrdersForSelectedCustomer);
		btnDisplayOrdersForSelectedCustomer.addActionListener(new ButtonDisplayOrdersForSelectedCustomersHandler());
		add(btnUpdateOrderCompletion, "wrap");
		btnUpdateOrderCompletion.addActionListener(new UpdateOrdersHandler());
		
		previousOrderScrollPane = new JScrollPane();
		add(previousOrderScrollPane, "span 3, grow, push");
		displayPreviousCustomerOrderTable(false);
		
	}//end constructor
	
	
	public class ButtonOrderHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(selectedCustomer == null){
				JOptionPane.showMessageDialog(CustomerOrderPanel.this, "Please select a customer!");
				return;
			}
			int option = JOptionPane.showConfirmDialog(CustomerOrderPanel.this, 
					"All the products that have \"Amount to order\" greater than 0\n will be placed on the order." 
					, "", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION){
				//get Updated product list
				Object[][] updatedProductList = CustomerOrderPanel.this.getMyProducts();
				ArrayList<ProductToOrder> productsToOrder = new ArrayList<ProductToOrder>();
				for(Object[] x : updatedProductList){
					//add only products that have amount set to > 0
					if((Integer) x[7] > 0){
						
						//check whether the available quantity is less than the amount entered
						if((Integer) x[6] < (Integer) x[7]){
							JOptionPane.showMessageDialog(CustomerOrderPanel.this, "You cannot order higher amount than currently available");
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
					JOptionPane.showMessageDialog(CustomerOrderPanel.this, "Nothing to order!");
					return;
				}else{
					CustomerOrder order = new CustomerOrder(selectedCustomer, GUIBackBone.getLoggedStaffMember(), productsToOrder);
					Shop.getCustomerOrders().add(order);
					System.out.println("Order has been created\nOrder id:"+order.getId()+"\nOrder totalGross: "+order.getTotalGross()+"\nOrder totalNet: "+order.getTotalNet() + order.getCustomer().getCustomerFName());
					//update table model data to reflect changes
					for(ProductToOrder x:productsToOrder){
						decrementProductAvalableQuantity(x.getId(),x.getAmount());
					}
					AbstractTableModel model = (AbstractTableModel) productsTable.getModel();
					model.fireTableDataChanged();
					
					//update previousCustomerOrderTable with the new order.
					displayPreviousCustomerOrderTable(false);
				}//end else
			}//end if
		}//end actionPerformed()
	}//end inner class ButtonOrderHandler
	
	public class ButtonDisplayOrdersForSelectedCustomersHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(selectedCustomer != null && getSelectedCustomerOrders() != null){
				displayPreviousCustomerOrderTable(true);
			}
		}//end actionPerformed
		
	}//end inner class ButtonDisplayOrdersForSelectedCustomersHandler
	
	public class ButtonDisplayOrdersForAllCustomersHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			displayPreviousCustomerOrderTable(false);
		}//end actionPerformed
		
	}//end inner class ButtonDisplayOrdersForAllCustomersHandler
	
	public class UpdateOrdersHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//find all orders that have "completed" value changed to true
			for(Object[] x:CustomerOrderPanel.this.getCustOrders()){
				if((boolean) x[6] == true){
					changeOrderCompletion((int)x[0], true);
				}else{
					changeOrderCompletion((int)x[0], false);
				}
			}
			AbstractTableModel model = (AbstractTableModel) previousOrdersTable.getModel();
			model.fireTableDataChanged();
			
			//update previousCustomerOrderTable with the new order.
			displayPreviousCustomerOrderTable(false);
			
		}//end actionPerformed
		
		public void changeOrderCompletion(int orderId, boolean isComplete){
			for(CustomerOrder x:Shop.getCustomerOrders()){
				if(x.getId() == orderId){
					x.setComplete(isComplete);
				}
			}
		}
		
	}//end inner class UpdateOrdersHandler
	 
	
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
			return null;
		}
	}
	
	//customers are displayed as firstName + Surname
	public Customer getCustomerFromConcatenatedName(String name){
		for(Customer customer:Shop.getCustomers()){
			String thisFullName = customer.getCustomerFName()+" "+customer.getCustomerLName();
			if(thisFullName.equalsIgnoreCase(name)){
				return customer;
			}
		}
		return null;
	}//end getCustomerFromConcatenatedName()

	public Object[][] getMyProducts() {
		return myProducts;
	}

	public void setMyProducts(Object[][] myProducts) {
		this.myProducts = myProducts;
	}
	
	public Object[][] getCustOrders() {
		return custOrders;
	}

	public void setCustOrders(Object[][] custOrders) {
		this.custOrders = custOrders;
	}

	public boolean decrementProductAvalableQuantity(int productId, int deductableAmount){
		Object[][] productList = CustomerOrderPanel.this.getMyProducts();
		for(Object[] x:productList){
			if((int) x[0] == productId){
				x[6] = (int) x[6] - deductableAmount;
				x[7] = 0;
				return true;
			}
		}
		return false;
	}//end decrementProductAvalableQuantity
	
	public void displayPreviousCustomerOrderTable(boolean forCustomerOnly){
		//display the order in the previousOrderTable
		String columnNames1[] = {"Id","Customer","Staff","Date","Total Net","Total Gross","Completed?"};
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		DecimalFormat df = new DecimalFormat("#.00");
		ArrayList<CustomerOrder> currentCustomerOrders = Shop.getCustomerOrders();
		custOrders = new Object[currentCustomerOrders.size()][7];
		
		//if forCustomerOnly = false then display all orders in the CustomeOrder array, otherwise only for the selected customer
		if(forCustomerOnly == false){
			for(int i=0; i< currentCustomerOrders.size(); i++){
				custOrders[i][0] = currentCustomerOrders.get(i).getId();
				custOrders[i][1] = currentCustomerOrders.get(i).getCustomer().getCustomerFName()+" "+currentCustomerOrders.get(i).getCustomer().getCustomerLName();
				custOrders[i][2] = currentCustomerOrders.get(i).getStaff().getName()+" "+currentCustomerOrders.get(i).getStaff().getSurname();
				custOrders[i][3] = sdf.format(currentCustomerOrders.get(i).getCreationDate());
				custOrders[i][4] = df.format(currentCustomerOrders.get(i).getTotalNet());
				custOrders[i][5] = df.format(currentCustomerOrders.get(i).getTotalGross());
				custOrders[i][6] = currentCustomerOrders.get(i).isComplete();
			}
			previousOrderTableModel = new ProductTableModel(custOrders, columnNames1);
			previousOrdersTable = new JTable(previousOrderTableModel);
			previousOrdersTable.setAutoCreateRowSorter(true);
			
			//Handle double clicking on table to display details of the double-clicked row's order
			previousOrdersTable.addMouseListener(new MouseAdapter() {
				   public void mouseClicked(MouseEvent e) {
				      if (e.getClickCount() == 2) {
				         JTable target = (JTable)e.getSource();
				         int row = target.getSelectedRow();
				         
				         int id = (int) custOrders[row][0];
				         String customer = (String) custOrders[row][1];
				         String staff = (String) custOrders[row][2];
				         String date = (String) custOrders[row][3];
				         String totalNet = (String) custOrders[row][4];
				         String totalGross = (String) custOrders[row][5];
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
			previousOrderScrollPane.getViewport().add(previousOrdersTable);
		}else{
			int counter = 0;
			ArrayList<CustomerOrder> thisCustomerOrders = getSelectedCustomerOrders();
			Object[][] selectedCustomerOrders = new Object[thisCustomerOrders.size()][7];
			for(int j=0; j < thisCustomerOrders.size(); j++){
				if(thisCustomerOrders.get(j).getCustomer().equals(selectedCustomer)){
					selectedCustomerOrders[counter][0] = thisCustomerOrders.get(j).getId();
					selectedCustomerOrders[counter][1] = thisCustomerOrders.get(j).getCustomer().getCustomerFName()+" "+thisCustomerOrders.get(j).getCustomer().getCustomerLName();
					selectedCustomerOrders[counter][2] = thisCustomerOrders.get(j).getStaff().getName()+" "+thisCustomerOrders.get(j).getStaff().getSurname();
					selectedCustomerOrders[counter][3] = sdf.format(thisCustomerOrders.get(j).getCreationDate());
					selectedCustomerOrders[counter][4] = df.format(thisCustomerOrders.get(j).getTotalNet());
					selectedCustomerOrders[counter][5] = df.format(thisCustomerOrders.get(j).getTotalGross());
					selectedCustomerOrders[counter][6] = thisCustomerOrders.get(j).isComplete();
					counter++;
				}
			}
			previousOrderTableModel = new ProductTableModel(selectedCustomerOrders, columnNames1);
			previousOrdersTable = new JTable(previousOrderTableModel);
			previousOrdersTable.setAutoCreateRowSorter(true);
			//Handle double clicking on table to display details of the double-clicked row's order
			previousOrdersTable.addMouseListener(new MouseAdapter() {
				   public void mouseClicked(MouseEvent e) {
				      if (e.getClickCount() == 2) {
				         JTable target = (JTable)e.getSource();
				         int row = target.getSelectedRow();
				         System.out.println("the id of the selected row's order is "+custOrders[row][0]);
				        // JDialog.setDefaultLookAndFeelDecorated(true);
				         }
				   }
				});
			previousOrderScrollPane.getViewport().add(previousOrdersTable);
		}//end else
	}//end displayPreviousCustomerOrderTable
	
}//end class CustomerOrderPanel
