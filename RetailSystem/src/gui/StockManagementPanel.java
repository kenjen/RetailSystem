package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
import java.util.ArrayList;

public class StockManagementPanel extends JSplitPane{
	
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list;
	
	private JTextField txtId;
	private JTextField txtName;
	private JTextField txtCategory;
	private JTextField txtQuantity;
	private JTextField txtPrice;
	private JTextField txtSupplier;
	private JTextField textName;
	private JTextField textCategory;
	private JTextField textQuantity;
	private JTextField textPrice;
	private JButton btnFlagForOrder;
	private JButton btnDeleteProduct;
	private JComboBox<String> comboSelectSupplier;

	private boolean productLoaded = false;
	private JTextField txtFlaggedForOrder;
	private JButton btnCreateNewProduct;
	private JButton btnDisplayAllProducts;
	private JButton btnDisplayLowStock;
	
	
	

	public StockManagementPanel() {
		
		//setup list with products
		list = new JList<String>(listModel);
		for(Product product : Shop.getProducts()){
			if(!product.isDeleted()){
				listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + "/" + product.getLowStockOrder() + " " + " Units    " + product.getName());
			}
		}
		

		//Add scroll pane to left size with list of products
		JScrollPane scrollPane = new JScrollPane(list);
		this.setDividerLocation(300);
		setLeftComponent(scrollPane);
		
		
		
		//add panel to right hand side
		JPanel panel = new JPanel();
		setRightComponent(panel);
		panel.setLayout(new MigLayout("", "[][100px:n][100px:n:100px,grow][100px:n:100px,grow][100px:100px:100px,grow,center][100px:n:100px][grow]", "[][20px:n][][][50px:n][][][][][][][][][][][50px:n][][20px:n][][20px:n][]"));
		
		txtId = new JTextField();
		panel.add(txtId, "cell 4 2,alignx center");
		txtId.setHorizontalAlignment(SwingConstants.CENTER);
		txtId.setColumns(20);
		txtId.setText("Enter Id");
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
		
		//display all products button
		btnDisplayAllProducts = new JButton("Display All Products");
		panel.add(btnDisplayAllProducts, "cell 1 1");
		btnDisplayAllProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setupList();
			}
		});
		
		btnDisplayLowStock = new JButton("Display Low Stock");
		panel.add(btnDisplayLowStock, "cell 1 2");
		btnDisplayLowStock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				listModel.clear();
				list = new JList<String>(listModel);
				for(Product product : Shop.getProducts()){
					if((!product.isDeleted()) && product.getQuantity()<=product.getLowStockOrder()){
						listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + "/" + product.getLowStockOrder() + " " + " Units    " + product.getName());
					}
				}
			}
		});
		
		//Setup button to load a products details
		JButton btnIdConfirm = new JButton("Confirm");
		panel.add(btnIdConfirm, "cell 4 3");
		btnIdConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
					textPrice.setText(""+tempProduct.getPrice());
					txtFlaggedForOrder.setVisible(tempProduct.isFlaggedForOrder());
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
		});
		
		txtFlaggedForOrder = new JTextField();
		txtFlaggedForOrder.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtFlaggedForOrder.setForeground(Color.RED);
		txtFlaggedForOrder.setEditable(false);
		txtFlaggedForOrder.setVisible(false);
		txtFlaggedForOrder.setHorizontalAlignment(SwingConstants.CENTER);
		txtFlaggedForOrder.setText("FLAGGED FOR ORDER");
		panel.add(txtFlaggedForOrder, "cell 3 4 3 1,growx");
		txtFlaggedForOrder.setColumns(10);
		
		//product name fields
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setHorizontalAlignment(SwingConstants.CENTER);
		txtName.setText("Name");
		panel.add(txtName, "cell 2 6,growx");
		txtName.setColumns(10);
		
		textName = new JTextField();
		panel.add(textName, "cell 4 6,growx");
		textName.setColumns(10);
		
		JButton btnSaveName = new JButton("Save");
		panel.add(btnSaveName, "cell 6 6");
		btnSaveName.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
		});
		
		//product category fields
		txtCategory = new JTextField();
		txtCategory.setHorizontalAlignment(SwingConstants.CENTER);
		txtCategory.setEditable(false);
		txtCategory.setText("Category");
		panel.add(txtCategory, "cell 2 8,growx");
		txtCategory.setColumns(10);
		
		textCategory = new JTextField();
		panel.add(textCategory, "cell 4 8,growx");
		textCategory.setColumns(10);
		
		JButton btnSaveCategory = new JButton("Save");
		panel.add(btnSaveCategory, "cell 6 8");
		
		btnSaveCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
		});
		
		//product quantity fields
		txtQuantity = new JTextField();
		txtQuantity.setText("Quantity");
		txtQuantity.setEditable(false);
		txtQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtQuantity, "cell 2 10,growx");
		txtQuantity.setColumns(10);
		
		textQuantity = new JTextField();
		panel.add(textQuantity, "cell 4 10,growx");
		textQuantity.setColumns(10);
		
		JButton btnSaveQuantity = new JButton("Save");
		panel.add(btnSaveQuantity, "cell 6 10");
		
		btnSaveQuantity.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
		});
		
		//product price fields
		txtPrice = new JTextField();
		txtPrice.setHorizontalAlignment(SwingConstants.CENTER);
		txtPrice.setEditable(false);
		txtPrice.setText("Price");
		panel.add(txtPrice, "cell 2 12,growx");
		txtPrice.setColumns(10);
		
		textPrice = new JTextField();
		panel.add(textPrice, "cell 4 12,growx");
		textPrice.setColumns(10);
		
		JButton btnSavePrice = new JButton("Save");
		panel.add(btnSavePrice, "cell 6 12");
		
		btnSavePrice.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
							}
						}
					}catch(NumberFormatException nfe){
						System.out.println("number entered not an integer");
					}
				}
			}
		});
		
		//product supplier fields
		txtSupplier = new JTextField();
		txtSupplier.setHorizontalAlignment(SwingConstants.CENTER);
		txtSupplier.setEditable(false);
		txtSupplier.setText("Supplier");
		panel.add(txtSupplier, "cell 2 14,growx");
		txtSupplier.setColumns(10);
		
		ArrayList<String> supplierNames = new ArrayList<String>();
		supplierNames.add("");
		for (Supplier supplier : Shop.getSuppliers()){
			String name = supplier.getSupplierName();
			supplierNames.add(name);
		}
		comboSelectSupplier = new JComboBox(supplierNames.toArray());
		panel.add(comboSelectSupplier, "cell 4 14");
		comboSelectSupplier.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectSupplier);
		
		JButton btnSaveSupplier = new JButton("Save");
		panel.add(btnSaveSupplier, "cell 6 14");
		
		btnSaveSupplier.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
		});
		
		//mark product as low stock and needing orders
		btnFlagForOrder = new JButton("Flag For Order");
		panel.add(btnFlagForOrder, "cell 3 16");
		btnFlagForOrder.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
		});
		
		//Delete product button
		btnDeleteProduct = new JButton("Delete");
		panel.add(btnDeleteProduct, "cell 5 16");
		btnDeleteProduct.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
									product.setDeleted(true);
									System.out.println("Product deleted succesfully");
									setupList();
								}
							}
						}
					}catch(NumberFormatException nfe){
						System.out.println("number entered not an integer");
					}
				}
			}
		});
		
		//create new product button
		btnCreateNewProduct = new JButton("New Product");
		panel.add(btnCreateNewProduct, "cell 4 20");
		btnCreateNewProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(productLoaded){
					//clear fields to add new details
					productLoaded = false;
					txtId.setText("");
					textName.setText("");
					textCategory.setText("");
					textQuantity.setText("");
					textPrice.setText("");
					txtFlaggedForOrder.setVisible(false);
					comboSelectSupplier.setSelectedIndex(0);
				}else{
					//take in new details and create new product
					if(!textName.equals("")){
						if(!(textCategory.getText().equals(""))){
							try{
								Integer.parseInt(textQuantity.getText());
								try{
									Integer.parseInt(textPrice.getText());
									if(comboSelectSupplier.getSelectedIndex()>0){
										for(Supplier supplier : Shop.getSuppliers()){
											if(supplier.getSupplierName().equals((String)comboSelectSupplier.getSelectedItem())){
												Product product = new Product(textName.getText(), textCategory.getText(), Integer.parseInt(textQuantity.getText()), Integer.parseInt(textPrice.getText()), supplier, true, 20);
												Shop.getProducts().add(product);
												txtId.setText(""+product.getId());
												textName.setText("");
												textCategory.setText("");
												textQuantity.setText("");
												textPrice.setText("");
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
						}else{
							System.out.println("Blank category");
						}
					}else{
						System.out.println("Blank Name");
					}
					setupList();
				}
			}
		});
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
	
	public void focusGained(FocusEvent fe) {
		setupList();
		
		ArrayList<String> supplierNames = new ArrayList<String>();
		supplierNames.add("");
		for (Supplier supplier : Shop.getSuppliers()){
			String name = supplier.getSupplierName();
			supplierNames.add(name);
		}
		comboSelectSupplier = new JComboBox(supplierNames.toArray());
		add(comboSelectSupplier, "cell 1 6");
		comboSelectSupplier.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectSupplier);
	}
}