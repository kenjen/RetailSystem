package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.*;

public class GUIBackBone  extends JFrame{
	
	//create the tabbed Pane
	JTabbedPane tabbedPane = new JTabbedPane();
	private CustomerOrderPanel panelCustomerOrders = new CustomerOrderPanel(); 
	private StockOrderPanel panelStockOrder = new StockOrderPanel();
	private StockManagementPanel panelStockManagement = new StockManagementPanel();
	private StaffPanel panelStaff = new StaffPanel();
	private CustomerPanel panelCustomer = new CustomerPanel();
	private SupplierPanel panelSupplier = new SupplierPanel();
	private static boolean userTypeAdmin = false;
	
	
	public GUIBackBone(boolean isAdmin){
			if(isAdmin == true){
				userTypeAdmin = true;
				tabbedPane.addTab("StockManagement", panelStockManagement);
				tabbedPane.addTab("CustomerOrders", panelCustomerOrders);
				tabbedPane.addTab("Management", panelStockOrder);
				tabbedPane.addTab("Staff", panelStaff);
				tabbedPane.addTab("Customer", panelCustomer);
				tabbedPane.addTab("Supplier", panelSupplier);
			}else{
				tabbedPane.addTab("StockManagement", panelStockManagement);
				tabbedPane.addTab("CustomerOrders", panelCustomerOrders);
				tabbedPane.addTab("Management", panelStockOrder);
				tabbedPane.addTab("Customer", panelCustomer);
				tabbedPane.addTab("Supplier", panelSupplier);
			}
			
			

			JPanel contentPane = new JPanel();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(tabbedPane, BorderLayout.CENTER);
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(1024,768);

	}
}
