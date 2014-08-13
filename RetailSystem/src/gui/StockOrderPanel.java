package gui;



import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import tableModels.ProductTableModel;
import data.Customer;
import data.Product;
import data.Supplier;
import net.miginfocom.swing.MigLayout;

public class StockOrderPanel extends JPanel{
	
	private JComboBox selectSupplier;
	private JButton selectSupplier2;
	private Supplier selectedSupplier;
	private JLabel lblActiveSupplierText = null;
	private JLabel lblActiveSupplier = null;
	private JLabel productListLabel;
	private JTable productsTable;
	

	public StockOrderPanel() {
		setLayout(new MigLayout());
		ArrayList<String> supplierNames = new ArrayList<String>();
		for ( Supplier supplier: Shop.getSuppliers()){
			String name = supplier.getSupplierName();
			supplierNames.add(name);
		}
	
        
		JPanel jpanel = new JPanel();
		jpanel.setBackground(Color.BLUE);
        jpanel.setOpaque(true);
        
		JLabel supplier = new JLabel("Supplier:");
		add(supplier, "split");
        jpanel.add(supplier);
        setVisible(true);  
        
        
        
       selectSupplier = new JComboBox(supplierNames.toArray());
       add(selectSupplier);
       selectSupplier.setEditable(true);
		AutoCompleteDecorator.decorate(selectSupplier);
       
		 selectSupplier2 = new JButton("Select");
			add(selectSupplier2, "wrap");
			selectSupplier2.addActionListener(new ActionListener(){

				
				public void actionPerformed(ActionEvent e) {
					String choice = selectSupplier.getSelectedItem().toString();
							
					if(getSupplierFromChoice(choice) != null){
						
					
						
						selectedSupplier = getSupplierFromChoice(choice);
						lblActiveSupplier.setText(selectedSupplier.getSupplierName());
					}
				}
			});
			
				JLabel lblActiveSupplierText = new JLabel("Active Supplier: ");
				JLabel lblActiveSupplier = new JLabel("");
				add(lblActiveSupplierText, "gapx 20px");
				add(lblActiveSupplier, "wrap");

				
			
		
				
				ArrayList<String> productNames = new ArrayList<String>();
				for (Product product: Shop.getProducts() ){
					String name = product.getName();
					productNames.add(name);
				}
					
				productListLabel = new JLabel("Supplier's Product list:");
				add(productListLabel, "wrap");
				
				Object[][] myProducts = new Object[Shop.getSuppliers().size()+1][7];
				int counter = 0;
				
				
					for(Product products: Shop.getProducts()){
					
					
						myProducts[counter][0] = products.getId();
						myProducts[counter][1] = products.getName();
						myProducts[counter][2] = products.getCategory();
						myProducts[counter][3] = products.getPrice();
						myProducts[counter][4] = products.getQuantity();
						
						myProducts[counter][5] = 0;
						counter ++;
					}
				
					
				String columnNames[] = {"Id","Name","Category","Price","Quantity","Amount to Order"};
				ProductTableModel productsTableModel = new ProductTableModel(myProducts, columnNames);
				
				JTable productList = new JTable();
				productList.add(jpanel);
				productsTable = new JTable(productsTableModel);
				productsTable.add(jpanel);
				productsTable.setAutoCreateRowSorter(true);
				
				JScrollPane scrollPane = new JScrollPane(productsTable);
				productsTable.setCellSelectionEnabled(true);
				productsTable.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener(){

					
					public void valueChanged(ListSelectionEvent e) {
						int row = productsTable.getSelectedRow();
						productsTable.changeSelection(row, 5, false, false);
						productsTable.requestFocus();
						productsTable.editCellAt(row, 5);
					}
					
				});
				
				
				add(scrollPane);

				
				
			}
        
       

		
		
	
	
	
	private Supplier getSupplierFromChoice(String name) {
		for(Supplier supplier :Shop.getSuppliers()){
			String thisFullName = supplier.getSupplierName();
			if(thisFullName.equalsIgnoreCase(getName())){
				return supplier;
			}
		}
		return null;
		
	}
	public static void main(String[] args){
    	StockOrderPanel gui = new StockOrderPanel();  
    }

}