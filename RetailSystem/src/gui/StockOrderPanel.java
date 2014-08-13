package gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;






import data.Customer;
import data.Supplier;
import net.miginfocom.swing.MigLayout;

public class StockOrderPanel extends JPanel{

	public StockOrderPanel() {
		setLayout(new MigLayout());
		ArrayList<String> supplierNames = new ArrayList<String>();
		for ( Supplier supplier: Shop.getSuppliers()){
			String name = supplier.getSupplierName();
			supplierNames.add(name);
		}
	
        
		JPanel jpanel = new JPanel();
		JLabel supplier = new JLabel("Supplier : ");
        jpanel.add(supplier);
        
              
             
       JComboBox selectSupplier = new JComboBox(supplierNames.toArray());
       add(selectSupplier);
       
       JButton selectSupplier2 = new JButton("Select");
		add(selectSupplier2, "wrap");
		selectSupplier2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        
       
	});
	}

}