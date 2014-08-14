package gui;



import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import tableModels.ProductTableModel;
import data.Product;
import data.Supplier;

public class StockOrderPanel extends JPanel{
	
	private JComboBox comboSuppliers;
	//private JButton btnSeclectSupplier;
	private Supplier selectedSupplier;
	//private JLabel lblActiveSupplierText = null;
	//private JLabel lblActiveSupplier = null;
	private JLabel lblProductList;
	private JTable tableProducts;
	private JScrollPane scrollPane;
	

	public StockOrderPanel() {
		setLayout(new MigLayout());
		ArrayList<String> supplierNames = new ArrayList<String>();
		//populates the suppliers combo box
		for ( Supplier supplier: Shop.getSuppliers()){
			String name = supplier.getSupplierName();
			supplierNames.add(name);
		}
	
        
		JPanel jpanel = new JPanel();
		jpanel.setBackground(Color.BLUE);
        jpanel.setOpaque(true);
        
		JLabel supplier = new JLabel("Supplier:");
		add(supplier, "split 3");
        //jpanel.add(supplier);
        setVisible(true);  
        
        
        
       comboSuppliers = new JComboBox(supplierNames.toArray());
       add(comboSuppliers, "wrap");
       comboSuppliers.setEditable(true);
		AutoCompleteDecorator.decorate(comboSuppliers);
		comboSuppliers.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					//adf
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
		});
		 /*btnSeclectSupplier = new JButton("Select");
			add(btnSeclectSupplier, "wrap");
			btnSeclectSupplier.addActionListener(new ActionListener(){

				
				public void actionPerformed(ActionEvent e) {
					String choice = comboSuppliers.getSelectedItem().toString();
							
					if(getSupplierFromChoice(choice) != null){
						
					
						
						selectedSupplier = getSupplierFromChoice(choice);
						lblActiveSupplier.setText(selectedSupplier.getSupplierName());
					}
				}
			});*/
			
				/*JLabel lblActiveSupplierText = new JLabel("Active Supplier: ");
				JLabel lblActiveSupplier = new JLabel("");
				add(lblActiveSupplierText, "gapx 20px");
				add(lblActiveSupplier, "wrap");*/

				
			
		
				
					
				lblProductList = new JLabel("Supplier's Product list:");
				add(lblProductList, "wrap");
				
				Object[][] myProducts = new Object[Shop.getProducts().size()][9];
				int counter = 0;
				
					for(Product products: Shop.getProducts()){
					
						myProducts[counter][0] = products.getId();
						myProducts[counter][1] = products.getName();
						myProducts[counter][2] = products.getCategory();
						myProducts[counter][3] = products.getPrice();
						myProducts[counter][4] = products.getSupplier().getSupplierName();
						myProducts[counter][5] = products.getQuantity();
						myProducts[counter][6] = products.isAvailable();
						myProducts[counter][7] = products.isFlaggedForOrder();
						
						myProducts[counter][8] = 0;
						counter ++;
					}
				
					String columnNames[] = {"Id","Name","Category","Price","Supplier","Quantity","In Shop?", "Required","Amount to Order"};					
				ProductTableModel productsTableModel = new ProductTableModel(myProducts, columnNames);
				
				tableProducts = new JTable(productsTableModel);
				tableProducts.setAutoCreateRowSorter(true);
				
				scrollPane = new JScrollPane(tableProducts);
				//tableProducts.setCellSelectionEnabled(true);
				tableProducts.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener(){

					
					public void valueChanged(ListSelectionEvent e) {
						int row = tableProducts.getSelectedRow();
						tableProducts.requestFocus();
						//tableProducts.editCellAt(row, 7);
						tableProducts.changeSelection(row, 8, false, false);
					}
					
				});
				
				
				add(scrollPane,"span, grow, push, wrap");

				
				
			}
        
       

		
		
	
	public void populateProductsTable(String forSupplier){
		boolean supplierFound = false;
		for(Supplier supplier:Shop.getSuppliers()){
			if(supplier.getSupplierName().equalsIgnoreCase(forSupplier)){
				supplierFound = true;
				break;
			}
		}
		
		String columnNames[] = {"Id","Name","Category","Price","Supplier","Quantity","In Shop?", "Required","Amount to Order"};
		
		//display all products from all suppliers
		if(supplierFound != true){
						
			Object[][] myProducts = new Object[Shop.getProducts().size()][9];
			int counter = 0;
			
				for(Product products: Shop.getProducts()){
				
					myProducts[counter][0] = products.getId();
					myProducts[counter][1] = products.getName();
					myProducts[counter][2] = products.getCategory();
					myProducts[counter][3] = products.getPrice();
					myProducts[counter][4] = products.getSupplier().getSupplierName();
					myProducts[counter][5] = products.getQuantity();
					myProducts[counter][6] = products.isAvailable();
					myProducts[counter][7] = products.isFlaggedForOrder();
					
					myProducts[counter][8] = 0;
					counter ++;
				}
			
				
			ProductTableModel productsTableModel = new ProductTableModel(myProducts, columnNames);
			tableProducts = new JTable(productsTableModel);
			tableProducts.setAutoCreateRowSorter(true);
			scrollPane.getViewport().add(tableProducts);
			StockOrderPanel.this.repaint();
		}else{
			////////////////////////////////////////
				
			lblProductList = new JLabel("Supplier's Product list:");
			add(lblProductList, "wrap");
			
			int counterForThisSuppliersProducts = 0;
			for(Product products: Shop.getProducts()){
				if(products.getSupplier().getSupplierName().equalsIgnoreCase(forSupplier)){
					counterForThisSuppliersProducts++;
				}
			}
			Object[][] myProducts = new Object[counterForThisSuppliersProducts][9];
			int counter = 0;
			
				for(Product products: Shop.getProducts()){
				
					if(products.getSupplier().getSupplierName().equalsIgnoreCase(forSupplier)){
						myProducts[counter][0] = products.getId();
						myProducts[counter][1] = products.getName();
						myProducts[counter][2] = products.getCategory();
						myProducts[counter][3] = products.getPrice();
						myProducts[counter][4] = products.getSupplier().getSupplierName();
						myProducts[counter][5] = products.getQuantity();
						myProducts[counter][6] = products.isAvailable();
						myProducts[counter][7] = products.isFlaggedForOrder();
						
						myProducts[counter][8] = 0;
						counter ++;
					}
				}
			
				
			ProductTableModel productsTableModel = new ProductTableModel(myProducts, columnNames);
			tableProducts = new JTable(productsTableModel);
			tableProducts.setAutoCreateRowSorter(true);
			scrollPane.getViewport().add(tableProducts);
			StockOrderPanel.this.repaint();
		}
	}
}