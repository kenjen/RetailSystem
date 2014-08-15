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
import data.Product;
import data.Supplier;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
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
	
	
	

	public StockManagementPanel() {
		
		setupList();
		
		
		//Add scroll pane to left size with list of products
		JScrollPane scrollPane = new JScrollPane(list);
		//TODO marker to locate set divider
		this.setDividerLocation(300);
		setLeftComponent(scrollPane);
		
		
		//add panel to right hand side
		JPanel panel = new JPanel();
		setRightComponent(panel);
		panel.setLayout(new MigLayout("", "[][100px:n][10px,grow][100px:n:100px,grow][100px:n:100px,grow][100px:100px:100px,grow,center][100px:n:100px][grow]", "[][20px:n][][][][][][][][][][][][][][][][][][50px:n][][20px:n][][20px:n][]"));
		
		
		//text field to enter id of product
		txtId = new JTextField();
		panel.add(txtId, "cell 5 2, center");
		txtId.setHorizontalAlignment(SwingConstants.CENTER);
		txtId.setColumns(20);
		txtId.setFocusTraversalKeysEnabled(false);
		txtId.setText("Enter Id");
		//set text field to blank when focus gained
		txtId.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0) {
				txtId.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				//Do Nothing
			}
			
		});
		txtId.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent k) {
				if(k.getKeyCode() == KeyEvent.VK_ENTER){
					loadProductDetails();
				}else if(k.getKeyCode() == KeyEvent.VK_TAB){
					loadProductDetails();
					textName.requestFocusInWindow();
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
		
		
		//display all products button
		btnDisplayProducts = new JButton("Display Products");
		panel.add(btnDisplayProducts, "cell 1 1");
		btnDisplayProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setupList();
			}
		});
		
		
		//Button to display products with stock levels below threshold
		btnDisplayLowStock = new JButton("Display Low Stock");
		panel.add(btnDisplayLowStock, "cell 1 2");
		btnDisplayLowStock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayLowStock();
			}
		});
		
		
		//Button to display deleted products
		btnDisplayDeletedStock = new JButton("Display Deleted Products");
		panel.add(btnDisplayDeletedStock, "cell 1 3");
		btnDisplayDeletedStock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayDeletedStock();
			}
		});
				
		
		//Button to display all products including deleted products
		btnDisplayAllProducts = new JButton("Display All Products");
		panel.add(btnDisplayAllProducts, "cell 1 4");
		btnDisplayAllProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayAllProducts();
			}
		});
		
		
		//Button to reset products to defaults
		btnRestoreDefaultProducts = new JButton("Restore Defaults");
		panel.add(btnRestoreDefaultProducts, "cell 1 5");
		btnRestoreDefaultProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetToDefaultValues();
			}
		});
		
				
		//Setup button to load a products details
		JButton btnIdConfirm = new JButton("Confirm");
		panel.add(btnIdConfirm, "cell 5 3");
		btnIdConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadProductDetails();
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
		JButton btnSaveName = new JButton("Save");
		panel.add(btnSaveName, "cell 7 6");
		btnSaveName.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveName();
			}
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
		JButton btnSaveCategory = new JButton("Save");
		panel.add(btnSaveCategory, "cell 7 8");
		btnSaveCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveCategory();
			}
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
		JButton btnSaveQuantity = new JButton("Save");
		panel.add(btnSaveQuantity, "cell 7 10");
		btnSaveQuantity.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveQuantity();
			}
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
		JButton btnSaveThreshold = new JButton("Save");
		panel.add(btnSaveThreshold, "cell 7 12");
		btnSaveThreshold.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveThreshold();
			}
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
		JButton btnSavePrice = new JButton("Save");
		panel.add(btnSavePrice, "cell 7 14");
		btnSavePrice.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				savePrice();
			}
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
		panel.add(txtDiscountedAmount, "cell 7 16");
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
		JButton btnSaveSupplier = new JButton("Save");
		panel.add(btnSaveSupplier, "cell 7 18");
		btnSaveSupplier.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveSupplier();
			}
		});
		
		JButton btnSaveAll = new JButton("Save All");
		panel.add(btnSaveAll, "cell 7 19");
		btnSaveAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAll();
			}
		});
		
		
		//mark product as low stock and needing orders
		btnFlagForOrder = new JButton("Flag For Order");
		panel.add(btnFlagForOrder, "cell 4 20, center");
		btnFlagForOrder.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				flagForOrder();
			}
		});
		
		
		//discount product by percentage
		btnDiscountProduct = new JButton("Discount");
		panel.add(btnDiscountProduct, "cell 4 19, center");
		btnDiscountProduct.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				discountProduct();
			}
		});
		
		
		//Delete product button
		btnDeleteProduct = new JButton("Delete");
		panel.add(btnDeleteProduct, "cell 6 19, center");
		btnDeleteProduct.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteProduct();
			}
		});
		
		
		//Restore product button
		btnRestoreProduct = new JButton("Restore...");
		panel.add(btnRestoreProduct, "cell 6 20, center");
		btnRestoreProduct.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				restoreProduct();
			}
		});
		
		
		//create new product button
		btnCreateNewProduct = new JButton("New Product");
		panel.add(btnCreateNewProduct, "cell 5 24");
		btnCreateNewProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewProduct();
			}
		});
	}
	
	public void createNewProduct(){
		if(productLoaded){
			//clear fields to add new details
			productLoaded = false;
			txtId.setText("");
			textName.setText("");
			textCategory.setText("");
			textQuantity.setText("");
			textThreshold.setText("");
			textPrice.setText("");
			textDiscountedPrice.setText("");
			txtFlaggedForOrder.setVisible(false);
			comboSelectSupplier.setSelectedIndex(0);
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
								if(comboSelectSupplier.getSelectedIndex()>0){
									for(Supplier supplier : Shop.getSuppliers()){
										if(supplier.getSupplierName().equals((String)comboSelectSupplier.getSelectedItem())){
											Product product = new Product(textName.getText(), textCategory.getText(), Integer.parseInt(textQuantity.getText()), Double.parseDouble(textPrice.getText()), supplier, true, 20);
											product.setLowStockOrder(Integer.parseInt(textThreshold.getText()));
											Shop.getProducts().add(product);
											txtId.setText(""+product.getId());
											textName.setText("");
											textCategory.setText("");
											textQuantity.setText("");
											textThreshold.setText("");
											textPrice.setText("");
											textDiscountedPrice.setText("");
											comboSelectSupplier.setSelectedIndex(0);
											System.out.println("Product Created Succesfully");
											break;
										}
									}
								}else{
									System.out.println("No supplier selected");
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
			saveDetails();
			setupList();
		}
	}
	
	
	public void deleteProduct(){
		int id = 0;
		if(productLoaded){
			String tempId = txtId.getText();
			try{
				id = Integer.parseInt(tempId);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						
						int selectedOption = JOptionPane.showConfirmDialog(null, 
                                  "Are you sure you wish to delete product?", 
                                  "Warning", 
                                  JOptionPane.YES_NO_OPTION,
                                  JOptionPane.WARNING_MESSAGE); 
						if (selectedOption == JOptionPane.YES_OPTION) {
							//check if user wants to mark as deleted or completly remove record
							Object[] options = {"Mark", "Remove"};
							int permanentlyDelete = JOptionPane.showOptionDialog(null,
									"Mark as deleted or permanently remove?",
									"Warning",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.WARNING_MESSAGE,
									null,     //do not use a custom Icon
									options,  //the titles of buttons
									options[0]); //default button title
							if(permanentlyDelete == JOptionPane.YES_OPTION){
								product.setDeleted(true);
								System.out.println("Product marked as deleted succesfully");
								saveDetails();
								setupList();
							}else{
								Shop.getProducts().remove(product);
								System.out.println("Product deleted from system succesfully");
								saveDetails();
								setupList();
							}
						}
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
		saveDetails();
	}
	
	
	public void displayAllProducts(){
		listModel.clear();
		list = new JList<String>(listModel);
		for(Product product : Shop.getProducts()){
			String deleted = "";
			if(product.isDeleted()){
				deleted = "  *DELETED*";
			}
			listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + "/" + product.getLowStockOrder() + " " + " Units    " + product.getName() + deleted);
		}
	}
	
	public void discountProduct(){
		int id = 0;
		if(productLoaded){
			String tempId = txtId.getText();
			try{
				id = Integer.parseInt(tempId);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						String input = JOptionPane.showInputDialog("Enter percentage to discount \n"+"Pevious discount "+product.getDiscountedPercentage()+"%");
						if(input!=null){
							try{
								Double percent = Double.parseDouble(input);
								product.setDiscountedPercentage(percent);
								if(percent==0){
									product.setDiscounted(false);
								}else{
									product.setDiscounted(true);
								}
								loadProductDetails();
							}catch(NumberFormatException nfe){
								System.out.println("Entered value not a valid integer");
							}
						}
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
	}
	
	
	public void displayDeletedStock(){
		listModel.clear();
		list = new JList<String>(listModel);
		for(Product product : Shop.getProducts()){
			if(product.isDeleted()){
				listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + "/" + product.getLowStockOrder() + " " + " Units    " + product.getName());
			}
		}
	}
	
	
	public void displayLowStock(){
		listModel.clear();
		list = new JList<String>(listModel);
		for(Product product : Shop.getProducts()){
			if((!product.isDeleted()) && product.getQuantity()<=product.getLowStockOrder()){
				listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + "/" + product.getLowStockOrder() + " " + " Units    " + product.getName());
			}
		}
	}
	
	
	public void flagForOrder(){
		int id = 0;
		if(productLoaded){
			String tempId = txtId.getText();
			try{
				id = Integer.parseInt(tempId);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						if(product.isFlaggedForOrder()){
							product.setFlaggedForOrder(false);
							txtFlaggedForOrder.setVisible(false);
							System.out.println("Product Item Unflagged Successfully");
						}else{
							product.setFlaggedForOrder(true);
							txtFlaggedForOrder.setVisible(true);
							System.out.println("Product Item Flagged Successfully");
						}
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
	}
	
	
	public void loadProductDetails(){
		boolean successful = true;
		int id = 0;
		Product tempProduct = null;
		String tempId = txtId.getText();
		try{
			id = Integer.parseInt(tempId);
		}catch(NumberFormatException nfe){
			successful = false;
			System.out.println("*****Entered a non integer value*****");
		}
		boolean productExists = false;
		if(successful){
			for(Product product : Shop.getProducts()){
				if(product.getId() == id && !(product.isDeleted())){
					productExists = true;
					tempProduct = product;
					productLoaded = true;
					break;
				}
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
			DecimalFormat df = new DecimalFormat("#.###");
			textDiscountedPrice.setText(""+(df.format(discountPrice)));
			txtFlaggedForOrder.setVisible(tempProduct.isFlaggedForOrder());
			txtDiscountedAmount.setText(tempProduct.getDiscountedPercentage() + "%");
			int index = 0;
			for(Supplier supplier : Shop.getSuppliers()){
				index++;
				if(supplier.getSupplierName() == tempProduct.getSupplier().getSupplierName()){
					break;
				}
			}
			comboSelectSupplier.setSelectedIndex(index);
		}
	}
	
	
	//resets the product list to original hard coded values
	public void resetToDefaultValues(){
		Shop.getProducts().clear();
		Product.setNextId(0);
		Product p1 = new Product("Pear", "Food", 70, 0.23, Shop.getSuppliers().get(0), true, 80);
		Product p2 = new Product("Coat", "Clothing", 50, 29.99, Shop.getSuppliers().get(1), true, 10);
		Product p3 = new Product("Trousers", "Clothing", 80, 40.0, Shop.getSuppliers().get(1), true, 15);
		Product p4 = new Product("Ham", "Food", 120, 4.50, Shop.getSuppliers().get(0), true, 60);
		Product p5 = new Product("Broom", "Hygene", 20, 12.0, Shop.getSuppliers().get(2), true, 3);
		
		Shop.getProducts().add(p1);
		Shop.getProducts().add(p2);
		Shop.getProducts().add(p3);
		Shop.getProducts().add(p4);
		Shop.getProducts().add(p5);
		
		saveDetails();
		setupList();
	}
	
	
	public void restoreProduct(){
		int id = 0;
		String tempId = JOptionPane.showInputDialog("Enter id of product to restore");
		try{
			id = Integer.parseInt(tempId);
			for(Product product : Shop.getProducts()){
				if(product.getId() == id && product.isDeleted()){
					product.setDeleted(false);
					System.out.println("Product unmarked as deleted succesfully");
					setupList();
				}
			}
		}catch(NumberFormatException nfe){
			System.out.println("number entered not an integer");
		}
		saveDetails();
	}
	
	
	public void saveAll(){
		saveCategory();
		saveName();
		savePrice();
		saveSupplier();
		saveThreshold();
		saveQuantity();
		
		saveDetails();
		setupList();
	}
	
	
	public void saveCategory(){
		int id = 0;
		if(productLoaded){
			String tempId = txtId.getText();
			try{
				id = Integer.parseInt(tempId);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						product.setCategory(textCategory.getText());
						System.out.println("Category saved succesfully");
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
	}
	
	
	//TODO test save
	public void saveDetails(){
		System.out.println("Saving Products");
		BufferedWriter writer = null;
		String textToSave = "";
		int numberOfProducts = 0;
		int nextId = 0;
		for(Product product : Shop.getProducts()){
			numberOfProducts++;
			if(product.getId()>nextId){
				nextId = product.getId();
			}
			textToSave = textToSave + "\n" + product.getSupplier().getSupplierId() + " " + product.getName() + " " + product.getCategory() + " " + product.getQuantity() + " " + product.getPrice() + " " + product.isAvailable() + " " + product.getLowStockOrder() + " " + product.isDeleted() + " " + product.isDiscounted() + " " + product.getDiscountedPercentage() + " " + product.isFlaggedForOrder() + " " + product.getId() + " ";
			 
		}
		textToSave = numberOfProducts + " " + nextId + " " + textToSave;
		System.out.println(textToSave);
		
		try{
			writer = new BufferedWriter( new FileWriter( "Products.txt"));
			writer.write(textToSave);
		}catch ( IOException e){
			e.printStackTrace();
		}
		finally{
			try{
				if ( writer != null)
				writer.close( );
			} catch ( IOException e){
				e.printStackTrace();
		    }
		}
		System.out.println("Finished save");
	}
	
	
	public void saveName(){
		int id = 0;
		if(productLoaded){
			String tempId = txtId.getText();
			try{
				id = Integer.parseInt(tempId);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						product.setName(textName.getText());
						System.out.println("Name saved succesfully");
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
	}
	
	
	public void savePrice(){
		int id = 0;
		int price = 0;
		if(productLoaded){
			String tempId = txtId.getText();
			String tempPrice = textPrice.getText();
			try{
				id = Integer.parseInt(tempId);
				price = Integer.parseInt(tempPrice);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						product.setPrice(price);
						System.out.println("Price saved succesfully");
						loadProductDetails();
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
	}
	
	
	public void saveSupplier(){
		int id = 0;
		if(productLoaded){
			String tempId = txtId.getText();
			try{
				id = Integer.parseInt(tempId);
				for(Supplier supplier : Shop.getSuppliers()){
					if(supplier.getSupplierName().equals((String)comboSelectSupplier.getSelectedItem())){
						for(Product product : Shop.getProducts()){
							if(product.getId() == id && !(product.isDeleted())){
								product.setSupplier(supplier);
								System.out.println("Supplier saved successfully");
							}
						}
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
	}
	
	
	public void saveThreshold(){
		int id = 0;
		int threshold = 0;
		if(productLoaded){
			String tempId = txtId.getText();
			String tempThreshold = textThreshold.getText();
			try{
				id = Integer.parseInt(tempId);
				threshold = Integer.parseInt(tempThreshold);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						product.setLowStockOrder(threshold);
						System.out.println("Threshold saved succesfully");
					}
				}
			}catch(NumberFormatException nfe){
				System.out.println("number entered not an integer");
			}
		}
	}
	
	
	public void saveQuantity(){
		int id = 0;
		int quantity = 0;
		if(productLoaded){
			String tempId = txtId.getText();
			String tempQuantity = textQuantity.getText();
			try{
				id = Integer.parseInt(tempId);
				quantity = Integer.parseInt(tempQuantity);
				for(Product product : Shop.getProducts()){
					if(product.getId() == id && !(product.isDeleted())){
						product.setQuantity(quantity);
						System.out.println("Quantity saved succesfully");
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
}