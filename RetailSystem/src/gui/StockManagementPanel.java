package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import net.miginfocom.swing.MigLayout;
import data.JsonExample;
import data.Product;
import data.Supplier;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class StockManagementPanel extends JSplitPane{
	
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list;
	
	private JTextField txtId;
	private JTextField txtName;
	private JTextField txtCategory;
	private JTextField txtQuantity;
	private JTextField txtThreshold;
	private JTextField txtPrice;
	private JTextField txtDiscountedAmount;
	private JTextField txtDiscountedPrice;
	private JTextField txtSupplier;
	private JTextField textName;
	private JTextField textCategory;
	private JTextField textQuantity;
	private JTextField textThreshold;
	private JTextField textPrice;
	private JTextField textDiscountedPrice;
	private JComboBox<String> comboSelectSupplier;
	private static JComboBox<String> comboSelectId;

	private boolean productLoaded = false;

	private JTextField txtFlaggedForOrder;
	private JButton btnCreateNewProduct;
	private JButton btnDisplayProducts;
	private JButton btnDisplayLowStock;
	private JButton btnDisplayDeletedStock;
	private JButton btnDisplayAllProducts;
	private JButton btnRestoreDefaultProducts;
	private JButton btnFlagForOrder;
	private JButton btnDiscountProduct;
	private JButton btnDeleteProduct;
	private JButton btnRestoreProduct;
	
	private NumberFormat formatter;
	
	

	public StockManagementPanel() {
		
		setupList();
		formatter = NumberFormat.getCurrencyInstance();
		
		//Add scroll pane to left size with list of products
		JScrollPane scrollPane = new JScrollPane(list);
		//this.setDividerLocation(300);
		setLeftComponent(scrollPane);
		
		
		//add panel to right hand side
		JPanel panel = new JPanel();
		setRightComponent(panel);
		panel.setLayout(new MigLayout("", "[][170px:n,grow][grow][70px:n,grow][::30px][100px:n,grow][100px:n,grow]", "[][20px:n][][][][][][grow][][grow][][grow][][grow][][grow][][grow][][grow][][grow][][grow][][30px:n,grow]"));
		
		
		//drop down menu  to enter id of product
		ArrayList<Integer> idValues = new ArrayList<Integer>();
		idValues.add(0);
		for (Product product : Shop.getProducts()){
			int id = product.getId();
			idValues.add(id);
		}
		comboSelectId = new JComboBox(idValues.toArray());
		refreshCombo(Shop.getProducts());
		panel.add(comboSelectId, "cell 3 3, alignx right");//TODO
		comboSelectId.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectId);
		
				
		//Button to load a products details
		JButton btnIdConfirm = new JButton("Load Details");
		panel.add(btnIdConfirm, "cell 5 3, alignx left");
		btnIdConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = 0;
				id = Integer.parseInt((String) comboSelectId.getSelectedItem());
				loadProductDetails(id, Shop.getProducts());
			}
		});
		
		
		//display all products button
		btnDisplayProducts = new JButton("Display Stock");
		panel.add(btnDisplayProducts, "cell 1 1, growx");
		btnDisplayProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setupList();
				refreshCombo(Shop.getProducts());
			}
		});
		
		
		//Button to display products with stock levels below threshold
		btnDisplayLowStock = new JButton("Display Low Stock");
		panel.add(btnDisplayLowStock, "cell 1 2, growx");
		btnDisplayLowStock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayLowStock(Shop.getProducts());
			}
		});
		
		
		//Button to display deleted products
		btnDisplayDeletedStock = new JButton("Display Deleted Stock");
		panel.add(btnDisplayDeletedStock, "cell 1 3, growx");
		btnDisplayDeletedStock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayDeletedStock(Shop.getProducts());
			}
		});
				
		
		//Button to display all products including deleted products
		btnDisplayAllProducts = new JButton("Display All Stock");
		panel.add(btnDisplayAllProducts, "cell 1 4, growx");
		btnDisplayAllProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayAllProducts(Shop.getProducts());
			}
		});
		
		
		//Button to reset products to defaults
		btnRestoreDefaultProducts = new JButton("Restore Defaults");
		panel.add(btnRestoreDefaultProducts, "cell 1 5, growx");
		btnRestoreDefaultProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetToDefaultValues(Shop.getProducts(), false);
				saveDetails();
				setupList();
				refreshCombo(Shop.getProducts());
			}
		});
		
		
		//text displaying the flagged for order if true
		txtFlaggedForOrder = new JTextField();
		txtFlaggedForOrder.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtFlaggedForOrder.setForeground(Color.RED);
		txtFlaggedForOrder.setEditable(false);
		txtFlaggedForOrder.setVisible(false);
		txtFlaggedForOrder.setHorizontalAlignment(SwingConstants.CENTER);
		txtFlaggedForOrder.setText("FLAGGED FOR ORDER");
		panel.add(txtFlaggedForOrder, "cell 4 4 3 1,growx");
		txtFlaggedForOrder.setColumns(10);
		
		
		//product name fields
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setHorizontalAlignment(SwingConstants.CENTER);
		txtName.setText("Name");
		panel.add(txtName, "cell 3 6,growx");
		txtName.setColumns(10);
		textName = new JTextField();
		panel.add(textName, "cell 5 6,growx");
		textName.setColumns(10);
		textName.setFocusTraversalKeysEnabled(false);
		textName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent k) {
				if(k.getKeyCode() == KeyEvent.VK_TAB){
					textCategory.requestFocusInWindow();
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
		
		
		//product category fields
		txtCategory = new JTextField();
		txtCategory.setHorizontalAlignment(SwingConstants.CENTER);
		txtCategory.setEditable(false);
		txtCategory.setText("Category");
		panel.add(txtCategory, "cell 3 8,growx");
		txtCategory.setColumns(10);
		textCategory = new JTextField();
		panel.add(textCategory, "cell 5 8,growx");
		textCategory.setColumns(10);
		textCategory.setFocusTraversalKeysEnabled(false);
		textCategory.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent k) {
				if(k.getKeyCode() == KeyEvent.VK_TAB){
					textQuantity.requestFocusInWindow();
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
		
		
		//product quantity fields
		txtQuantity = new JTextField();
		txtQuantity.setText("Quantity");
		txtQuantity.setEditable(false);
		txtQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtQuantity, "cell 3 10,growx");
		txtQuantity.setColumns(10);
		textQuantity = new JTextField();
		panel.add(textQuantity, "cell 5 10,growx");
		textQuantity.setColumns(10);
		textQuantity.setFocusTraversalKeysEnabled(false);
		textQuantity.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent k) {
				if(k.getKeyCode() == KeyEvent.VK_TAB){
					textThreshold.requestFocusInWindow();
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
		
		
		//product threshold fields
		txtThreshold = new JTextField();
		txtThreshold.setText("Threshold");
		txtThreshold.setEditable(false);
		txtThreshold.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtThreshold, "cell 3 12,growx");
		txtThreshold.setColumns(10);
		textThreshold = new JTextField();
		panel.add(textThreshold, "cell 5 12,growx");
		textThreshold.setColumns(10);
		textThreshold.setFocusTraversalKeysEnabled(false);
		textThreshold.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent k) {
				if(k.getKeyCode() == KeyEvent.VK_TAB){
					textPrice.requestFocusInWindow();
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
		
		
		//product price fields
		txtPrice = new JTextField();
		txtPrice.setHorizontalAlignment(SwingConstants.CENTER);
		txtPrice.setEditable(false);
		txtPrice.setText("Price");
		panel.add(txtPrice, "cell 3 14,growx");
		txtPrice.setColumns(10);
		textPrice = new JTextField();
		panel.add(textPrice, "cell 5 14,growx");
		textPrice.setColumns(10);
		textPrice.setFocusTraversalKeysEnabled(false);
		textPrice.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent k) {
				if(k.getKeyCode() == KeyEvent.VK_TAB){
					comboSelectSupplier.requestFocusInWindow();
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
		
		
		//product discounted price fields
		txtDiscountedPrice = new JTextField();
		txtDiscountedPrice.setHorizontalAlignment(SwingConstants.CENTER);
		txtDiscountedPrice.setEditable(false);
		txtDiscountedPrice.setText("Discounted Price");
		panel.add(txtDiscountedPrice, "cell 3 16,growx");
		txtDiscountedPrice.setColumns(10);
		textDiscountedPrice = new JTextField();
		panel.add(textDiscountedPrice, "cell 5 16,growx");
		textDiscountedPrice.setColumns(10);
		txtDiscountedAmount = new JTextField();
		txtDiscountedAmount.setHorizontalAlignment(SwingConstants.CENTER);
		txtDiscountedAmount.setEditable(false);
		txtDiscountedAmount.setText("");
		panel.add(txtDiscountedAmount, "cell 6 16");
		txtDiscountedAmount.setColumns(5);
		
		
		//product supplier fields
		txtSupplier = new JTextField();
		txtSupplier.setHorizontalAlignment(SwingConstants.CENTER);
		txtSupplier.setEditable(false);
		txtSupplier.setText("Supplier");
		panel.add(txtSupplier, "cell 3 18,growx");
		txtSupplier.setColumns(10);
		ArrayList<String> supplierNames = new ArrayList<String>();
		supplierNames.add("");
		for (Supplier supplier : Shop.getSuppliers()){
			String name = supplier.getSupplierName();
			supplierNames.add(name);
		}
		comboSelectSupplier = new JComboBox(supplierNames.toArray());
		panel.add(comboSelectSupplier, "cell 5 18");
		comboSelectSupplier.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectSupplier);
		
		
		JButton btnSaveAll = new JButton("Save All");
		panel.add(btnSaveAll, "cell 3 20, growx, center");
		btnSaveAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAll();
			}
		});
		
		
		//create new product button
		btnCreateNewProduct = new JButton("New Product");
		panel.add(btnCreateNewProduct, "cell 5 20, growx, center");
		btnCreateNewProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Product product = createNewProduct(false);
				if(product!=null){
					Shop.getProducts().add(product);
				}
				saveDetails();
				setupList();
				refreshCombo(Shop.getProducts());
				//comboSelectId.setSelectedItem(product.getId());
					
			}
		});
		
		
		//discount product by percentage
		btnDiscountProduct = new JButton("Discount");
		panel.add(btnDiscountProduct, "cell 3 22, growx, center");
		btnDiscountProduct.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(productLoaded){
					int id = Integer.parseInt((String) comboSelectId.getSelectedItem());
					discountProduct(id, Shop.getProducts(), false);
				}
			}
		});
		
		
		//Delete product button
		btnDeleteProduct = new JButton("Delete...");
		panel.add(btnDeleteProduct, "cell 5 22, growx, center");
		btnDeleteProduct.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt((String) comboSelectId.getSelectedItem());
				deleteProduct(id, Shop.getProducts(), false);
				clearProductDetails();
				saveDetails();
				setupList();
				refreshCombo(Shop.getProducts());
			}
		});
		
		
		//mark product as low stock and needing orders
		btnFlagForOrder = new JButton("Flag For Order");
		panel.add(btnFlagForOrder, "cell 3 24, growx, center");
		btnFlagForOrder.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt((String) comboSelectId.getSelectedItem());
				flagForOrder(id, Shop.getProducts());
				saveAll();
			}
		});
		
		
		//Restore product button
		btnRestoreProduct = new JButton("Restore...");
		panel.add(btnRestoreProduct, "cell 5 24, growx, center");
		btnRestoreProduct.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					String tempId = JOptionPane.showInputDialog("Enter id of product to restore");
					int id = Integer.parseInt(tempId);
					restoreProduct(id, Shop.getProducts());
				}catch(NumberFormatException nfe){
					System.out.println("number entered not an integer");
				}
				saveDetails();
				setupList();
				refreshCombo(Shop.getProducts());
			}
		});
	}
	
	
	//Clears the right pane of any product details
	public void clearProductDetails(){
		comboSelectId.setSelectedIndex(0);
		textName.setText("");
		textCategory.setText("");
		textQuantity.setText("");
		textThreshold.setText("");
		textPrice.setText("");
		txtDiscountedAmount.setText("");
		textDiscountedPrice.setText("");
		txtFlaggedForOrder.setVisible(false);
		comboSelectSupplier.setSelectedIndex(0);
	}
	
	
	public Product createNewProduct(boolean testing){
		//TODO popup displaying why not created
		if(productLoaded){
			//clear fields to add new details
			productLoaded = false;
			clearProductDetails();
			textName.requestFocus();
			return null;
		}else{
			//take in new details and create new product
			if(!textName.equals("")){
				if(!(textCategory.getText().equals(""))){
					try{
						Integer.parseInt(textQuantity.getText());
						try{
							Double.parseDouble(textPrice.getText());
							try{
								Integer.parseInt(textThreshold.getText());
								if(!testing){
									if(comboSelectSupplier.getSelectedIndex()>0){
										for(Supplier supplier : Shop.getSuppliers()){
											if(supplier.getSupplierName().equals((String)comboSelectSupplier.getSelectedItem())){
												Product product = new Product(textName.getText(), textCategory.getText(), Integer.parseInt(textQuantity.getText()), Double.parseDouble(textPrice.getText()), supplier, true, 20);
												product.setLowStockOrder(Integer.parseInt(textThreshold.getText()));
												clearProductDetails();
												System.out.println("Product Created Succesfully");
												return product;
											}
										}
									}else{
										System.out.println("No supplier selected");
									}
								}else{
									Product product = new Product(textName.getText(), textCategory.getText(), Integer.parseInt(textQuantity.getText()), Double.parseDouble(textPrice.getText()), new Supplier(), true, 20);
									product.setLowStockOrder(Integer.parseInt(textThreshold.getText()));
									return product;
								}
							}catch(NumberFormatException nfe){
								System.out.println("number format exception");
							}
						}catch(NumberFormatException nfe){
							System.out.println("number format exception");
						}
					}catch(NumberFormatException nfe){
						System.out.println("number format exception");
					}
				}else{
					System.out.println("Blank category");
				}
			}else{
				System.out.println("Blank Name");
			}
		}
		return null;
	}
	
	
	public void deleteProduct(int id, ArrayList<Product> products, boolean testing){
		if(productLoaded){
			Product remove = null;
			for(Product product : products){
				if(product.getId() == id && !(product.isDeleted())){
					int selectedOption = 0;
					if(testing){
						selectedOption = JOptionPane.YES_OPTION;
					}else{
						selectedOption = JOptionPane.showConfirmDialog(null, 
								"Are you sure you wish to delete product?",
								"Warning",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
					}
					if (selectedOption == JOptionPane.YES_OPTION) {
						//check if user wants to mark as deleted or completly remove record
						Object[] options = {"Mark", "Remove"};
						int markAsDeleted;
						if(testing){
							markAsDeleted = JOptionPane.NO_OPTION;
						}
						else{
							markAsDeleted = JOptionPane.showOptionDialog(null,
									"Mark as deleted or permanently remove?",
									"Warning",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.WARNING_MESSAGE,
									null,     //do not use a custom Icon
									options,  //the titles of buttons
									options[0]); //default button title
						}
						if(markAsDeleted == JOptionPane.YES_OPTION){
							product.setDeleted(true);
						}else{
							remove = product;
						}
					}
				}
			}

			if(remove != null){
				products.remove(remove);
			}
		}
	}
	
	
	public int displayAllProducts(ArrayList<Product> products){
		listModel.clear();
		list = new JList<String>(listModel);
		int counter = 0;
		for(Product product : products){
			String deleted = "";
			if(product.isDeleted()){
				deleted = "  *DELETED*";
			}
			listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + "/" + product.getLowStockOrder() + " " + " Units    " + product.getName() + deleted);
			
			counter++;
		}
		return counter;
	}
	
	public void discountProduct(int id, ArrayList<Product> products, boolean testing){
		for(Product product : products){
			if(product.getId() == id && !(product.isDeleted())){
				String input = null;
				if(testing){
					input = "33";
				}else{
					input = JOptionPane.showInputDialog("Enter percentage to discount \n"+"Pevious discount "+product.getDiscountedPercentage()+"%");
				}
				if(input!=null){
					try{
						double inputD = Double.parseDouble(input);
						String test = String.format("%.2f", inputD);
						Double percent = Double.parseDouble(test);
						product.setDiscountedPercentage(percent);
						if(percent==0){
							product.setDiscounted(false);
						}else{
							product.setDiscounted(true);
						}
						int tempId = Integer.parseInt((String) comboSelectId.getSelectedItem());
						loadProductDetails(tempId, Shop.getProducts());
					}catch(NumberFormatException nfe){
						System.out.println("Entered value not a valid integer");
					}
				}
			}
		}
	}
	
	
	public int displayDeletedStock(ArrayList<Product> products){
		listModel.clear();
		list = new JList<String>(listModel);
		int counter = 0;
		for(Product product : products){
			if(product.isDeleted()){
				counter++;
				listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + "/" + product.getLowStockOrder() + " " + " Units    " + product.getName());
			}
		}
		return counter;
	}
	
	
	public int displayLowStock(ArrayList<Product> products){
		listModel.clear();
		list = new JList<String>(listModel);
		int counter = 0;
		for(Product product : products){
			if((!product.isDeleted()) && product.getQuantity()<=product.getLowStockOrder()){
				counter++;
				listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + "/" + product.getLowStockOrder() + " " + " Units    " + product.getName());
			}
		}
		return counter;
	}
	
	
	public void flagForOrder(int id, ArrayList<Product> products){
		if(productLoaded){
			//int id = Integer.parseInt((String) comboSelectId.getSelectedItem());
			for(Product product : products){
				if(product.getId() == id && !(product.isDeleted())){
					if(product.isFlaggedForOrder()){
						product.setFlaggedForOrder(false);
						txtFlaggedForOrder.setVisible(false);
					}else{
						product.setFlaggedForOrder(true);
						txtFlaggedForOrder.setVisible(true);
					}
				}
			}
		}
	}
	
	
	public void loadProductDetails(int id, ArrayList<Product> products){
		Product tempProduct = null;
		boolean productExists = false;
		for(Product product : products){
			if(product.getId() == id && !(product.isDeleted())){
				productExists = true;
				tempProduct = product;
				productLoaded = true;
				break;
			}
		}
		if(!productExists){
			System.out.println("*****This Id Does Not Match A Product*****");
		}else{
			textName.setText(tempProduct.getName());
			textCategory.setText(tempProduct.getCategory());
			textQuantity.setText(""+tempProduct.getQuantity());
			textThreshold.setText(""+tempProduct.getLowStockOrder());
			textPrice.setText(""+tempProduct.getPrice());
			Double discountPrice = tempProduct.getPrice() - (tempProduct.getPrice()*(tempProduct.getDiscountedPercentage()/100));
			DecimalFormat df = new DecimalFormat("#.##");
			textDiscountedPrice.setText(""+(df.format(discountPrice)));
			txtFlaggedForOrder.setVisible(tempProduct.isFlaggedForOrder());
			txtDiscountedAmount.setText(tempProduct.getDiscountedPercentage() + "%");
			int index = 0;

			for(Supplier supplier : Shop.getSuppliers()){
				index++;
				if(supplier.getSupplierName().equals(tempProduct.getSupplier().getSupplierName())){
					comboSelectSupplier.setSelectedIndex(index);
				}
			}
		}
	}
	
	
	//refresh combo box
	public static int refreshCombo(ArrayList<Product> products){
		ArrayList<Integer> idValues = new ArrayList<Integer>();
		idValues.add(0);
		for (Product product : products){
			if(!product.isDeleted()){
				int id = product.getId();
				idValues.add(id);
			}
		}
		comboSelectId.removeAllItems();
		int counter = 0;
		for(Integer current : idValues){
			String s = current.toString();
			comboSelectId.addItem(s);
			counter++;
		}
		return counter;
	}
	
	
	//resets the product list to original hard coded values
	public void resetToDefaultValues(ArrayList<Product> products, boolean testing){
		int selectedOption = 0;
		if(testing){
			selectedOption = JOptionPane.YES_OPTION;
		}else{
			selectedOption = JOptionPane.showConfirmDialog(null, 
					"Are you sure you wish to restore product defaults?", 
					"Warning", 
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE); 
		}
		if (selectedOption == JOptionPane.YES_OPTION) {
			products.clear();
			Product.setNextId(0);
			Product p1;
			Product p2;
			Product p3;
			Product p4;
			Product p5;
			
			//used for junit tests
			if(testing){
				p1 = new Product("Pear", "Food", 70, 0.23, null, true, 80);
				p2 = new Product("Coat", "Clothing", 50, 29.99, null, true, 10);
				p3 = new Product("Trousers", "Clothing", 80, 40.0, null, true, 15);
				p4 = new Product("Ham", "Food", 120, 4.50, null, true, 60);
				p5 = new Product("Broom", "Hygene", 20, 12.0, null, true, 3);
			}else{
				p1 = new Product("Pear", "Food", 70, 0.23, Shop.getSuppliers().get(0), true, 80);
				p2 = new Product("Coat", "Clothing", 50, 29.99, Shop.getSuppliers().get(1), true, 10);
				p3 = new Product("Trousers", "Clothing", 80, 40.0, Shop.getSuppliers().get(1), true, 15);
				p4 = new Product("Ham", "Food", 120, 4.50, Shop.getSuppliers().get(0), true, 60);
				p5 = new Product("Broom", "Hygene", 20, 12.0, Shop.getSuppliers().get(2), true, 3);
			}
			products.add(p1);
			products.add(p2);
			products.add(p3);
			products.add(p4);
			products.add(p5);
		}
	}
	
	
	public void restoreProduct(int id, ArrayList<Product> products){
		for(Product product : products){
			if(product.getId() == id && product.isDeleted()){
				product.setDeleted(false);
			}
		}
	}
	
	
	public void saveAll(){
		System.out.println("Saving Product Details");
		saveCategory();
		saveName();
		savePrice();
		saveSupplier();
		saveThreshold();
		saveQuantity();
		
		saveDetails();
		setupList();
		refreshCombo(Shop.getProducts());
		clearProductDetails();
	}
	
	
	public void saveCategory(){
		if(productLoaded){
			int id = Integer.parseInt((String) comboSelectId.getSelectedItem());
			for(Product product : Shop.getProducts()){
				if(product.getId() == id && !(product.isDeleted())){
					product.setCategory(textCategory.getText());
				}
			}
		}
	}
	
	
	public void saveDetails(){
		
		JsonExample.clearList("resources/products.json");
		for(Product product : Shop.getProducts()){
			JsonExample.saveProductToFile(product);
		}
		System.out.println("Finished saving products");
	}
	
	
	public void saveName(){
		if(productLoaded){
			int id = Integer.parseInt((String) comboSelectId.getSelectedItem());
			for(Product product : Shop.getProducts()){
				if(product.getId() == id && !(product.isDeleted())){
					product.setName(textName.getText());
				}
			}
		}
	}
	
	
	public void savePrice(){
		double price = 0;
		if(productLoaded){
			int id = Integer.parseInt((String) comboSelectId.getSelectedItem());
			String tempPrice = textPrice.getText();
			try{
				price = Double.parseDouble(tempPrice);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						String test = String.format("%.2f", price);
						price = Double.parseDouble(test);
						product.setPrice(price);
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
	}
	
	
	public void saveSupplier(){
		if(productLoaded){
			int id = Integer.parseInt((String) comboSelectId.getSelectedItem());
			for(Supplier supplier : Shop.getSuppliers()){
				if(supplier.getSupplierName().equals((String)comboSelectSupplier.getSelectedItem())){
					for(Product product : Shop.getProducts()){
						if(product.getId() == id && !(product.isDeleted())){
							product.setSupplier(supplier);
						}
					}
				}
			}
		}
	}
	
	
	public void saveThreshold(){
		int threshold = 0;
		if(productLoaded){
			int id = Integer.parseInt((String) comboSelectId.getSelectedItem());
			String tempThreshold = textThreshold.getText();
			try{
				threshold = Integer.parseInt(tempThreshold);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						product.setLowStockOrder(threshold);
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
	}
	
	
	public void saveQuantity(){
		int quantity = 0;
		if(productLoaded){
			int id = Integer.parseInt((String) comboSelectId.getSelectedItem());
			String tempQuantity = textQuantity.getText();
			try{
				quantity = Integer.parseInt(tempQuantity);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						product.setQuantity(quantity);
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
	}
	
	
	public void setupList(){
		listModel.clear();
		list = new JList(listModel);
		for(Product product : Shop.getProducts()){
			if(!product.isDeleted()){
				listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + "/" + product.getLowStockOrder() + " " + " Units    " + product.getName());
			}
		}
	}
	
	
	/*
	 * 
	private JTextField textName;
	private JTextField textCategory;
	private JTextField textQuantity;
	private JTextField textThreshold;
	private JTextField textPrice;
	private JTextField textDiscountedPrice;
	*/
	
	public String getDisplayedName(){
		return textName.getText();
	}
	
	public String getDisplayedCategory(){
		return textCategory.getText();
	}
	
	public String getDisplayedQuantity(){
		return textQuantity.getText();
	}
	
	public String getDisplayedThreshold(){
		return textThreshold.getText();
	}
	
	public String getDisplayedPrice(){
		return textPrice.getText();
	}
	
	public String getDisplayedDiscountPrice(){
		return textDiscountedPrice.getText();
	}

	public String getDisplayedDiscountPercent(){
		return txtDiscountedAmount.getText();
	}
	
	public boolean isFlaggedForOrderVisible(){
		return txtFlaggedForOrder.isVisible();
	}
	
	public void setDisplayedName(String str){
		textName.setText(str);
	}
	
	public void setDisplayedCategory(String str){
		textCategory.setText(str);
	}
	
	public void setDisplayedQuantity(String str){
		textQuantity.setText(str);
	}
	
	public void setDisplayedThreshold(String str){
		textThreshold.setText(str);
	}
	
	public void setDisplayedPrice(String str){
		textPrice.setText(str);
	}
	
	public void setDisplayedDiscountPrice(String str){
		textDiscountedPrice.setText(str);
	}

	public void setDisplayedDiscountPercent(String str){
		txtDiscountedAmount.setText(str);
	}
	
	public void setProductLoaded(boolean productLoaded) {
		this.productLoaded = productLoaded;
	}
}