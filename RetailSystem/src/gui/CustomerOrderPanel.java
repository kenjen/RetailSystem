package gui;

import java.awt.Component;
import java.awt.TrayIcon.MessageType;
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
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import data.Customer;
import data.Product;

//this class deals with customer ordering only.
public class CustomerOrderPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JComboBox comboSelectCustomer;
	private JButton selectCustomer;
	private Customer selectedCustomer;
	private JLabel productListLabel;

	public CustomerOrderPanel() {
		setLayout(new MigLayout());
		ArrayList<String> customerNames = new ArrayList<String>();
		for ( Customer customer: Shop.getCustomers()){
			String name = customer.getCustomerFName()+" "+customer.getCustomerLName();
			customerNames.add(name);
		}
		
		JLabel lblCustomer = new JLabel("Customer:");
		add(lblCustomer);
		
		comboSelectCustomer = new JComboBox(customerNames.toArray());
		add(comboSelectCustomer);
		comboSelectCustomer.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectCustomer);
		
		selectCustomer = new JButton("Select");
		add(selectCustomer, "wrap");
		selectCustomer.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String concatenatedName = comboSelectCustomer.getSelectedItem().toString();
				if(getCustomerFromConcatenatedName(concatenatedName) != null){
					selectedCustomer = getCustomerFromConcatenatedName(concatenatedName);
				}else{
					JOptionPane.showMessageDialog(CustomerOrderPanel.this, "No such customer in the list");
				}
			}
			
		});
		
		productListLabel = new JLabel("Product list:");
		add(productListLabel, "wrap");
		
		Object myProducts[][] = new Object[Shop.getProducts().size()][8];
		int counter = 0;
		//make products array to feed into the table model
		for(Product product:Shop.getProducts()){
			System.out.println(product.getId());
			myProducts[counter][0] = product.getId();
			myProducts[counter][1] = product.getName();
			myProducts[counter][2] = product.getSupplier().getSupplierName();
			myProducts[counter][3] = product.getCategory();
			myProducts[counter][4] = product.getPrice();
			myProducts[counter][5] = product.isDiscounted();
			myProducts[counter][6] = product.getQuantity();
			myProducts[counter][7] = 0;
			counter ++;
		}
		
		String columnNames[] = {"Id","Name","Supplier","Category","Price","Discounted?","Quantity","Amount to Order"};
		ProductTableModel productsTableModel = new ProductTableModel(myProducts, columnNames);
		JTable productsTable = new JTable(productsTableModel);
		productsTable.setAutoCreateRowSorter(true);
		JScrollPane scrollPane = new JScrollPane(productsTable);
		
		add(scrollPane, "span 3, grow, push");
	}
	
	public Customer getCustomerFromConcatenatedName(String name){
		for(Customer customer:Shop.getCustomers()){
			String thisFullName = customer.getCustomerFName()+" "+customer.getCustomerLName();
			if(thisFullName.equalsIgnoreCase(name)){
				return customer;
			}
		}
		return null;
	}
	

}

class ProductTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private String columnNames[];
	private Object[][] data;
	
	public ProductTableModel(Object[][] objectPassed, String[] columnNamesPassed){
		data = objectPassed;
		columnNames = columnNamesPassed;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		//MAKE
		if(columnIndex != columnNames.length-1){
			return data[rowIndex][columnIndex];
		}else{
			if(data[rowIndex][columnIndex] != null){
				return data[rowIndex][columnIndex];
			}else{
				return 0;
			}
		}
	}
	
	 public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	 }
		
	//allow edits ONLY on last column
	public boolean isCellEditable(int row, int col) {
        /*if (col < 2) {
            return false;
        } else {
            return true;
        }*/
		if(col == columnNames.length-1){
			return true;
		}else{
			return false;
		}
    }

	//allow saves ONLY on last column
    public void setValueAt(Object value, int row, int col) {
    	if(col == columnNames.length-1){
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    	}
    }
	
}
