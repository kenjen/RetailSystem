package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.Staff;

public class GUIBackBone extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// create the tabbed Pane
	JTabbedPane tabbedPane = new JTabbedPane();
	private CustomerOrderPanel panelCustomerOrders = new CustomerOrderPanel();
	private StockOrderPanel panelStockOrder = new StockOrderPanel();
	private StockManagementPanel panelStockManagement = new StockManagementPanel();
	private StaffPanel panelStaff = new StaffPanel();
	private CustomerPanel panelCustomer = new CustomerPanel();
	private SupplierPanel panelSupplier = new SupplierPanel();
	private ProfitLossPanel panelProfit = new ProfitLossPanel();
	private StatisticsPanel panelStat = new StatisticsPanel();
	private static boolean userTypeAdmin = false;
	private static Staff loggedStaffMember;
	private int previousTabIndex = 0;
	private JLabel lblLogout = new JLabel();
	private boolean isProfitPanelLoaded = false;

	public GUIBackBone(boolean isAdmin, Staff logStaff) {
		JPanel logoutPanel = new JPanel();
		lblLogout.setIcon(new ImageIcon("resources/Logout_closed.png"));
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT );
		if (isAdmin == true) {
			userTypeAdmin = true;
			loggedStaffMember = logStaff;
			tabbedPane.addTab("StockManagement", panelStockManagement);
			tabbedPane.addTab("CustomerOrders", panelCustomerOrders);
			tabbedPane.addTab("StockOrders", panelStockOrder);
			tabbedPane.addTab("Staff", panelStaff);
			tabbedPane.addTab("Customer", panelCustomer);
			tabbedPane.addTab("Supplier", panelSupplier);
			tabbedPane.addTab("Finance", panelProfit);
			tabbedPane.addTab("Statistics", panelStat);
			tabbedPane.addTab("", logoutPanel);
				
			tabbedPane.setIconAt(0,
					new ImageIcon(getClass().getResource("/CheckList.png")));
			tabbedPane.setIconAt(1,
					new ImageIcon(getClass().getResource("/Cart.png")));
			tabbedPane.setIconAt(2,
					new ImageIcon(getClass().getResource("/Icon.png")));
			tabbedPane.setIconAt(3,
					new ImageIcon(getClass().getResource("/Staff.png")));
			tabbedPane.setIconAt(4,
					new ImageIcon(getClass().getResource("/Customer.png")));
			tabbedPane.setIconAt(5,
					new ImageIcon(getClass().getResource("/Supplier.png")));
			tabbedPane.setIconAt(6,
					new ImageIcon(getClass().getResource("/Finance.png")));
			tabbedPane.setIconAt(7, 
					new ImageIcon(getClass().getResource("/Statistics.png")));
			tabbedPane.setTabComponentAt(8, lblLogout);
				

		} else {
			loggedStaffMember = logStaff;
			tabbedPane.addTab("StockManagement", panelStockManagement);
			tabbedPane.addTab("CustomerOrders", panelCustomerOrders);
			tabbedPane.addTab("Management", panelStockOrder);
			tabbedPane.addTab("Customer", panelCustomer);
			tabbedPane.addTab("Supplier", panelSupplier);
			tabbedPane.addTab("", logoutPanel);
			tabbedPane.setIconAt(0,
					new ImageIcon(getClass().getResource("/CheckList.png")));
			tabbedPane.setIconAt(1,
					new ImageIcon(getClass().getResource("/Cart.png")));
			tabbedPane.setIconAt(2,
					new ImageIcon(getClass().getResource("/Icon.png")));
			tabbedPane.setIconAt(3,
					new ImageIcon(getClass().getResource("/Customer.png")));
			tabbedPane.setIconAt(4,
					new ImageIcon(getClass().getResource("/Supplier.png")));
			tabbedPane.setTabComponentAt(5, lblLogout);

		}

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				saveTabDetails(0);
				//panelStat.refresh();
				if (tabbedPane.getSelectedIndex() == 8
						|| (tabbedPane.getSelectedIndex() == 5 && GUIBackBone.loggedStaffMember
								.isAdmin() == false)) {
					int x = JOptionPane.showConfirmDialog(GUIBackBone.this,
							"Are you sure you want to log out?", "Log Out?",
							JOptionPane.YES_NO_OPTION);
					if (x == JOptionPane.YES_OPTION) {
						// run login
						Login login = new Login(Shop.getStaffMembers());
						login.drawFrame();
						saveTabDetails(0);
		
						GUIBackBone.this.dispose();
					} else {
						tabbedPane.setSelectedIndex(previousTabIndex);
					}
				}else if(tabbedPane.getSelectedIndex() == 1){
					panelCustomerOrders.repopulateAll();
				}else if(tabbedPane.getSelectedIndex() == 2){
					panelStockOrder.repopulateAll();
				}else if(tabbedPane.getSelectedIndex() == 6){
					if(!isProfitPanelLoaded){
						panelProfit.createPanel();
						isProfitPanelLoaded = true;
					}
					panelProfit.refreshChart();
				}
				previousTabIndex = tabbedPane.getSelectedIndex();
			}
		});

		lblLogout.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblLogout.setIcon(new ImageIcon("resources/Logout.png"));
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				lblLogout.setIcon(new ImageIcon("resources/Logout_closed.png"));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (GUIBackBone.loggedStaffMember.isAdmin()) {
					int x = JOptionPane.showConfirmDialog(GUIBackBone.this,
							"Are you sure you want to log out?", "Log Out?",
							JOptionPane.YES_NO_OPTION);
					if (x == JOptionPane.YES_OPTION) {
						// run login
						Login login = new Login(Shop.getStaffMembers());
						login.drawFrame();
						saveTabDetails(0);
						GUIBackBone.this.dispose();
					} else {
						tabbedPane.setSelectedIndex(previousTabIndex);
					}
				} else {
					int x = JOptionPane.showConfirmDialog(GUIBackBone.this,
							"Are you sure you want to log out?", "Log Out?",
							JOptionPane.YES_NO_OPTION);
					if (x == JOptionPane.YES_OPTION) {
						// run login
						Login login = new Login(Shop.getStaffMembers());
						login.drawFrame();
						saveTabDetails(0);
						GUIBackBone.this.dispose();
					} else {
						tabbedPane.setSelectedIndex(previousTabIndex);
					}
				}
			}
		});

		setContentPane(tabbedPane);
		setSize(1200, 768);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("RetailSystem");

		// When frame is closed, save all details
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				saveTabDetails(0);
			}

		});

	}

	public static Staff getLoggedStaffMember() {
		return loggedStaffMember;
	}

	public void saveTabDetails(int index) {
		panelStockManagement.saveDetails();
		panelStockManagement.refreshTable();
		panelCustomer.saveDetails();
		panelSupplier.saveDetails();
	}

	public static void setLoggedStaffMember(Staff loggedStaffMember) {
		GUIBackBone.loggedStaffMember = loggedStaffMember;
	}
}
