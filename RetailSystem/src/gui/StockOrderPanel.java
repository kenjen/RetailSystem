package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import data.Product;
import data.ProductToOrder;
import data.StockOrder;
import data.Supplier;

public class StockOrderPanel extends JPanel {

	private JComboBox comboSuppliers;
	// private JButton btnSeclectSupplier;
	private Supplier selectedSupplier;
	// private JLabel lblActiveSupplierText = null;
	// private JLabel lblActiveSupplier = null;
	private JLabel lblProductList;
	private JTable tableProducts;
	private JTable tableOrders;
	private JScrollPane scrollPaneProducts;
	private JScrollPane scrollPaneOrders;
	private JButton btnDisplayAllProducts = new JButton("Display all products");
	private JButton btnAddToOrder = new JButton("Add to order");
	private JButton btnOrder = new JButton("Submit order");
	private static ArrayList<Object[]> arrayTemporaryOrder = new ArrayList<Object[]>();
	private ArrayList<Object[]> arrayOrder = new ArrayList<Object[]>();
	private Object[][] arrayTableProducts;
	private Object[][] arrayTableOrders;
	private JLabel lblError = new JLabel("");
	private Timer timer;
	private JComboBox comboSearchForProducts;
	private JButton btnDisplayCurrentOrder = new JButton("Show Current Order");
	private JButton btnUpdateOrderCompletion = new JButton("Update Orders");
	private JLabel lblProductsSearch = new JLabel("Product search: ");

	public StockOrderPanel() {
		setLayout(new MigLayout());
		ArrayList<String> supplierNames = new ArrayList<String>();
		// populates the suppliers combo box
		for (Supplier supplier : Shop.getSuppliers()) {
			String name = supplier.getSupplierName();
			supplierNames.add(name);
		}

		JPanel jpanel = new JPanel();
		jpanel.setBackground(Color.BLUE);
		jpanel.setOpaque(true);

		JLabel supplier = new JLabel("Supplier:");
		add(supplier, "split 5");
		// jpanel.add(supplier);
		setVisible(true);

		comboSuppliers = new JComboBox(supplierNames.toArray());
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
							populateProductsTable(comboSuppliers.getItemAt(comboSuppliers.getSelectedIndex()).toString());
						}

					}

					@Override
					public void keyReleased(KeyEvent e) {
					}

				});

		add(btnDisplayAllProducts);
		btnDisplayAllProducts.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				populateProductsTable("");
			}

		});
		
		add(lblProductsSearch,"gapx 50");
		String[] comboArray = getProductNamesForComboBox();
		comboSearchForProducts = new JComboBox(comboArray);
		add(comboSearchForProducts, "wrap");
		comboSearchForProducts.setEditable(true);
		AutoCompleteDecorator.decorate(comboSearchForProducts);
		comboSearchForProducts.getEditor().getEditorComponent().addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_ENTER){
					displayProductsTable(comboSearchForProducts.getSelectedItem().toString());
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		lblProductList = new JLabel("Supplier's Product list:");
		add(lblProductList, "wrap");
//--------------------//
//---Products table---//
//--------------------//
		scrollPaneProducts = new JScrollPane();
		add(scrollPaneProducts, "span 3, grow, push");
		displayProductsTable("");

		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new MigLayout());
		tempPanel.add(btnAddToOrder, "wrap, growx, pushx");
		tempPanel.add(btnOrder, "growx, pushx");
		add(tempPanel, "wrap, alignx left, aligny top, growx");

		btnAddToOrder.addActionListener(new ButtonTemporaryOrderHandler());
		btnOrder.addActionListener(new ButtonOrderHandler());
		JLabel lblOrders = new JLabel("Orders:");
		add(lblError, "pushx ,alignx center, wrap");
		add(lblOrders,"wrap");
		lblError.setVisible(false);
		lblError.setFont(new Font("Serif",Font.BOLD,15));
//-----------------//
//---ORDER TABLE---//
//-----------------//
		scrollPaneOrders = new JScrollPane();
		add(scrollPaneOrders, "push,grow,span 3");
		displayOrderTable();
		
		JPanel panelx = new JPanel();
		panelx.setLayout(new MigLayout());
		panelx.add(btnDisplayCurrentOrder,"growx, wrap");
		panelx.add(btnUpdateOrderCompletion,"growx");
		add(panelx,"alignx left, aligny top");
		
		//When this button is pressed, update the order for all orders that have true assigned to 'completed'
		//once set to true, should not be modifiable, at least not from the button event
		btnUpdateOrderCompletion.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
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
					displayProductsTable("");
				}else{
					displayErrorMessage("Nothing to update", Color.red);
				}
			}
			
		});
		
		btnDisplayCurrentOrder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new CurrentStockOrderDialog(arrayTemporaryOrder);
			}
		});

	}// end Constructor
	
	//
	public void changeStockOrderToComplete(int id){
		for(StockOrder order:Shop.getStockOrders()){
			if(order.getId() == id){
				order.setCompleted(true);
				//update products from this stock order
	        	 for(ProductToOrder prodToOrder:order.getProductsToOrder()){
	        		 for (Product product:Shop.getProducts()){
		        		 if(prodToOrder.getId() == product.getId()){
		        			 product.setQuantity(product.getQuantity()+prodToOrder.getAmount());
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
		int counter = 0;
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

//-----------------------------//
//---POPULATE PRODUCTS TABLE---//
//-----------------------------//
	public void populateProductsTable(String forSupplier) {
		boolean supplierFound = false;
		for (Supplier supplier : Shop.getSuppliers()) {
			if (supplier.getSupplierName().equalsIgnoreCase(forSupplier)) {
				supplierFound = true;
				break;
			}
		}

		String columnNames[] = { "Id", "Name", "Category", "Price", "Supplier",
				"Quantity", "In Shop?", "Required", "Amount to Order" };

		// display all products from all suppliers
		if (supplierFound != true) {

			arrayTableProducts = new Object[Shop.getProducts().size()][9];
			int counter = 0;

			for (Product products : Shop.getProducts()) {

				arrayTableProducts[counter][0] = products.getId();
				arrayTableProducts[counter][1] = products.getName();
				arrayTableProducts[counter][2] = products.getCategory();
				arrayTableProducts[counter][3] = products.getPrice();
				arrayTableProducts[counter][4] = products.getSupplier()
						.getSupplierName();
				arrayTableProducts[counter][5] = products.getQuantity();
				arrayTableProducts[counter][6] = products.isAvailable();
				arrayTableProducts[counter][7] = products.isFlaggedForOrder();

				arrayTableProducts[counter][8] = 0;
				counter++;
			}

			ProductTableModel productsTableModel = new ProductTableModel(
					arrayTableProducts, columnNames);
			tableProducts = new JTable(productsTableModel);
			tableProducts.setAutoCreateRowSorter(true);
			tableProducts.getColumnModel().getSelectionModel()
					.addListSelectionListener(new ListSelectionListener() {

						public void valueChanged(ListSelectionEvent e) {
							int row = tableProducts.getSelectedRow();
							tableProducts.requestFocus();
							// tableProducts.editCellAt(row, 7);
							tableProducts.changeSelection(row, 8, false, false);
						}

					});
			scrollPaneProducts.getViewport().add(tableProducts);
			StockOrderPanel.this.repaint();
		} else {
			// display products only for the selected supplier
			int counterForThisSuppliersProducts = 0;
			for (Product products : Shop.getProducts()) {
				if (products.getSupplier().getSupplierName()
						.equalsIgnoreCase(forSupplier)) {
					counterForThisSuppliersProducts++;
				}
			}
			arrayTableProducts = new Object[counterForThisSuppliersProducts][9];
			int counter = 0;

			for (Product products : Shop.getProducts()) {

				if (products.getSupplier().getSupplierName()
						.equalsIgnoreCase(forSupplier)) {
					arrayTableProducts[counter][0] = products.getId();
					arrayTableProducts[counter][1] = products.getName();
					arrayTableProducts[counter][2] = products.getCategory();
					arrayTableProducts[counter][3] = products.getPrice();
					arrayTableProducts[counter][4] = products.getSupplier()
							.getSupplierName();
					arrayTableProducts[counter][5] = products.getQuantity();
					arrayTableProducts[counter][6] = products.isAvailable();
					arrayTableProducts[counter][7] = products
							.isFlaggedForOrder();

					arrayTableProducts[counter][8] = 0;
					counter++;
				}
			}

			ProductTableModel productsTableModel = new ProductTableModel(
					arrayTableProducts, columnNames);
			tableProducts = new JTable(productsTableModel);
			tableProducts.setAutoCreateRowSorter(true);
			tableProducts.getColumnModel().getSelectionModel()
					.addListSelectionListener(new ListSelectionListener() {

						public void valueChanged(ListSelectionEvent e) {
							int row = tableProducts.getSelectedRow();
							tableProducts.requestFocus();
							// tableProducts.editCellAt(row, 7);
							tableProducts.changeSelection(row, 8, false, false);
						}

					});
			scrollPaneProducts.getViewport().add(tableProducts);
			StockOrderPanel.this.repaint();
		}
	}// end PopulateProducts()

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
	public void displayOrderTable() {
		String columnNamesForOrders[] = { "Id", "Staff member",
				"Date of order", "Total", "Completed" };

		ArrayList<StockOrder> stockOrders = Shop.getStockOrders();
		arrayTableOrders = new Object[stockOrders.size()][5];
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		int x = 0;
		for (StockOrder stockOrder : stockOrders) {
			arrayTableOrders[x][0] = stockOrders.get(x).getId();
			arrayTableOrders[x][1] = stockOrders.get(x).getStaff().getName()
					+ " " + stockOrders.get(x).getStaff().getSurname();
			arrayTableOrders[x][2] = sdf.format(stockOrders.get(x).getDate());
			arrayTableOrders[x][3] = stockOrders.get(x).getTotal();
			arrayTableOrders[x][4] = stockOrders.get(x).isCompleted();
			x++;
		}

		ProductTableModel ordersTableModel = new ProductTableModel(
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
			         
			         StockOrder thisOrder = null;
			         for(StockOrder order : Shop.getStockOrders()){
			        	 if(order.getId()==(int) target.getValueAt(row, cell)){
			        		 thisOrder = order;	
			        		 break;
			        	 }
			         }
			         
			         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			         DecimalFormat df = new DecimalFormat("#.00");
			         String htmlText = "<html><head><style>th {color:#305EE3;font-variant:small-caps;}</style></head>";
			         htmlText += "<h2>Order details:</h2><table border='1'><tr><th>ID</th><th>Date of creation</th><th>Expected delivery date</th><th>Staff member</th><th>Total</th></tr>";
			         htmlText += "<tr><td>"+thisOrder.getId()+"</td></td>"+sdf.format(thisOrder.getDate())+"</td><td>"+sdf.format(thisOrder.getExpectedDeliveryDate())+"</td><td>"+GUIBackBone.getLoggedStaffMember().getName()+" "+GUIBackBone.getLoggedStaffMember().getSurname()+"</td><td>"+df.format(thisOrder.getTotal())+"</td></tr>";
			         htmlText += "</table><br><h2>Products:</h2><table border='1'><tr><th>ID</th><th>Name</th><th>Supplier</th><th>Category</th><th>Price</th><th>Amount</th><th>Total</th></tr>";
			         for(ProductToOrder product:thisOrder.getProductsToOrder()){
			        	 htmlText += product.toHtmlString();
			         }
			         htmlText += "</table></html>";
			         new PopupDialog(htmlText);
			      }
			}
			
		});
		
	}//end displayOrderTable
	
//-------------------------
//---Display Products Table 
//-------------------------
	public void displayProductsTable(String productName){
		if(productName != ""){
		//get products that contain that name
			ArrayList<Product> productsArrayList = new ArrayList<Product>();
			for(Product product:Shop.getProducts()){
				if(product.getName().equalsIgnoreCase(productName)){
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
		}else{
			arrayTableProducts = new Object[Shop.getProducts().size()][9];
			int counter = 0;
			for (Product products : Shop.getProducts()) {
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
		ProductTableModel productsTableModel = new ProductTableModel(arrayTableProducts, columnNames);
		tableProducts = new JTable(productsTableModel);
		tableProducts.setAutoCreateRowSorter(true);
		tableProducts.setAutoCreateRowSorter(true);
		tableProducts.getColumnModel().getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

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
				displayOrderTable();
				displayErrorMessage("Added", Color.blue);
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
	
					displayOrderTable();
					// clear the temporaryArrayOforders
					arrayTemporaryOrder = new ArrayList<Object[]>();
					displayErrorMessage("Order has been submitted", Color.blue);
				}
			} else {
				displayErrorMessage("No order to submit", Color.red);
			}
		}// end actionPerformed

	}// end inner class

	public static ArrayList<Object[]> getArrayTemporaryOrder() {
		return arrayTemporaryOrder;
	}

	public static void setArrayTemporaryOrder(ArrayList<Object[]> x) {
		arrayTemporaryOrder = x;
	}
}// end class