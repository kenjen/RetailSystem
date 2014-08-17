package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.Staff;

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
	private static Staff loggedStaffMember;
	private int previousTabIndex = 0;
	
	
	public GUIBackBone(boolean isAdmin, Staff loggedStaffMember){
			if(isAdmin == true){
				userTypeAdmin = true;
				this.loggedStaffMember = loggedStaffMember;
				tabbedPane.addTab("StockManagement", panelStockManagement);
				tabbedPane.addTab("CustomerOrders", panelCustomerOrders);
				tabbedPane.addTab("StockOrders", panelStockOrder);
				tabbedPane.addTab("Staff", panelStaff);
				tabbedPane.addTab("Customer", panelCustomer);
				tabbedPane.addTab("Supplier", panelSupplier);
				tabbedPane.setIconAt(0, new ImageIcon(getClass().getResource("/CheckList.png")));
				tabbedPane.setIconAt(1, new ImageIcon(getClass().getResource("/Cart.png")));
				tabbedPane.setIconAt(2, new ImageIcon(getClass().getResource("/Icon.png")));
				tabbedPane.setIconAt(3, new ImageIcon(getClass().getResource("/Staff.png")));
				tabbedPane.setIconAt(4, new ImageIcon(getClass().getResource("/Customer.png")));
				tabbedPane.setIconAt(5, new ImageIcon(getClass().getResource("/Supplier.png")));
				
				//TODO testing save
				tabbedPane.addChangeListener(new ChangeListener() {
			        @Override
					public void stateChanged(ChangeEvent e) {
			           saveTabDetails(previousTabIndex);
			           previousTabIndex = tabbedPane.getSelectedIndex();
			        }
			    });
			}else{
				this.loggedStaffMember = loggedStaffMember;
				tabbedPane.addTab("StockManagement", panelStockManagement);
				tabbedPane.addTab("CustomerOrders", panelCustomerOrders);
				tabbedPane.addTab("Management", panelStockOrder);
				tabbedPane.addTab("Customer", panelCustomer);
				tabbedPane.addTab("Supplier", panelSupplier);
				tabbedPane.setIconAt(0, new ImageIcon(getClass().getResource("/CheckList.png")));
				tabbedPane.setIconAt(1, new ImageIcon(getClass().getResource("/Cart.png")));
				tabbedPane.setIconAt(2, new ImageIcon(getClass().getResource("/Icon.png")));
				tabbedPane.setIconAt(3, new ImageIcon(getClass().getResource("/Customer.png")));
				tabbedPane.setIconAt(4, new ImageIcon(getClass().getResource("/Supplier.png")));
			}
			
			

			JPanel contentPane = new JPanel();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(tabbedPane, BorderLayout.CENTER);
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(1024,768);
		setTitle("RetailSystem");
		setLocationRelativeTo(null);
	}


	public static Staff getLoggedStaffMember() {
		return loggedStaffMember;
	}
	
	//TODO testing save
	public void saveTabDetails(int index){
		if(index==0){
			panelStockManagement.saveDetails();
		}
	}


	public static void setLoggedStaffMember(Staff loggedStaffMember) {
		GUIBackBone.loggedStaffMember = loggedStaffMember;
	}
}
