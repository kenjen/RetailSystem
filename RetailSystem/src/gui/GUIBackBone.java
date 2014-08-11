package gui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.*;

public class GUIBackBone  extends JFrame{
	
	//create the tabbed Pane
	JTabbedPane tabbedPane = new JTabbedPane();
	private CustomerOrderPanel panelCustomerOrders = new CustomerOrderPanel(); 
	private ManagementPanel panelManagement = new ManagementPanel();
	private StockManagementPanel panelStockManagement = new StockManagementPanel();
	private StaffPanel panelStaff = new StaffPanel();
	private CustomerPanel panelCustomer = new CustomerPanel();
	private SupplierPanel panelSupplier = new SupplierPanel();
	
	
	public GUIBackBone(){
			
		tabbedPane.add("StockManagement", panelStockManagement);
		tabbedPane.add("CustomerOrders", panelCustomerOrders);
		tabbedPane.addTab("Management", panelManagement);
		tabbedPane.addTab("Staff", panelStaff);
		tabbedPane.addTab("Customer", panelCustomer);
		tabbedPane.addTab("Supplier", panelSupplier);
		

		getContentPane().add(tabbedPane);

	}
}
