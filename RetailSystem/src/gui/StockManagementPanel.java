package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import data.Product;
import data.Supplier;

public class StockManagementPanel extends JSplitPane{
	
	private DefaultListModel listModel = new DefaultListModel();
	private JList list;
	
	private JTextField textField;
	private JTextField txtEnterProductId;
	private JTextField textField_1;
	private JTextField textField_2;
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
	private JTextField textSupplier;

	private boolean productLoaded = false;
	

	public StockManagementPanel() {
		
		//setup list with products
		list = new JList(listModel);
		for(Product product : Shop.getProducts()){
			listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + " Units    " + product.getName());
		}
		
		
		//stop users from resizing panes
		//setEnabled(false);
		

		//Add scroll pane to left size with list of products
		JScrollPane scrollPane = new JScrollPane(list);
		this.setDividerLocation(300);
		setLeftComponent(scrollPane);
		
		
		
		//add panel to right hand side
		JPanel panel = new JPanel();
		setRightComponent(panel);
		panel.setLayout(new MigLayout("", "[][100px:n:100px,grow][100px:n:100px,grow][100px:100px:100px,center][100px:n:100px][grow]", "[][][][][][][][][][][][]"));
		
		txtId = new JTextField();
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
		panel.add(txtId, "cell 3 0,alignx center");
		
		//Setup button to load a products details
		JButton btnIdConfirm = new JButton("Confirm");
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
						if(product.getId() == id){
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
					textSupplier.setText(tempProduct.getSupplier().getSupplierName());
				}
			}
		});
		panel.add(btnIdConfirm, "cell 3 1");
		
		//product name fields
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setHorizontalAlignment(SwingConstants.CENTER);
		txtName.setText("Name");
		panel.add(txtName, "cell 1 3,growx");
		txtName.setColumns(10);
		
		textName = new JTextField();
		panel.add(textName, "cell 3 3,growx");
		textName.setColumns(10);
		
		JButton btnSaveName = new JButton("Save");
		panel.add(btnSaveName, "cell 5 3");
		btnSaveName.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int id = 0;
				if(productLoaded){
					String tempId = txtId.getText();
					try{
						id = Integer.parseInt(tempId);
						for(Product product : Shop.getProducts()){
							if(product.getId() == id){
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
		panel.add(txtCategory, "cell 1 5,growx");
		txtCategory.setColumns(10);
		
		textCategory = new JTextField();
		panel.add(textCategory, "cell 3 5,growx");
		textCategory.setColumns(10);
		
		JButton btnSaveCategory = new JButton("Save");
		panel.add(btnSaveCategory, "cell 5 5");
		
		btnSaveCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int id = 0;
				if(productLoaded){
					String tempId = txtId.getText();
					try{
						id = Integer.parseInt(tempId);
						for(Product product : Shop.getProducts()){
							if(product.getId() == id){
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
		panel.add(txtQuantity, "cell 1 7,growx");
		txtQuantity.setColumns(10);
		
		textQuantity = new JTextField();
		panel.add(textQuantity, "cell 3 7,growx");
		textQuantity.setColumns(10);
		
		JButton btnSaveQuantity = new JButton("Save");
		panel.add(btnSaveQuantity, "cell 5 7");
		
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
							if(product.getId() == id){
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
		panel.add(txtPrice, "cell 1 9,growx");
		txtPrice.setColumns(10);
		
		textPrice = new JTextField();
		panel.add(textPrice, "cell 3 9,growx");
		textPrice.setColumns(10);
		
		JButton btnSavePrice = new JButton("Save");
		panel.add(btnSavePrice, "cell 5 9");
		
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
							if(product.getId() == id){
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
		panel.add(txtSupplier, "cell 1 11,growx");
		txtSupplier.setColumns(10);
		
		textSupplier = new JTextField();
		panel.add(textSupplier, "cell 3 11,growx");
		textSupplier.setColumns(10);
		
		JButton btnSaveSupplier = new JButton("Save");
		panel.add(btnSaveSupplier, "cell 5 11");
		
		btnSaveSupplier.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int id = 0;
				if(productLoaded){
					String tempId = txtId.getText();
					try{
						id = Integer.parseInt(tempId);
						for(Supplier supplier : Shop.getSuppliers()){
							if(supplier.getSupplierName().equalsIgnoreCase(textSupplier.getText())){
								for(Product product : Shop.getProducts()){
									if(product.getId() == id){
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
	}
}