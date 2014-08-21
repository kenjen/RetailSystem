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
	private static boolean userTypeAdmin = false;
	private static Staff loggedStaffMember;
	private int previousTabIndex = 0;
	private JLabel lblLogout = new JLabel();

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
			//tabbedPane.addTab("Finance", panelProfit);
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
			tabbedPane.setTabComponentAt(6, lblLogout);

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
				if (tabbedPane.getSelectedIndex() == 6
						|| (tabbedPane.getSelectedIndex() == 5 && GUIBackBone.loggedStaffMember
								.isAdmin() == false)) {
					System.out.println("selected index - "
							+ tabbedPane.getSelectedIndex());
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
					System.out.println("Admin Click");
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
					System.out.println("Not Admin Click");
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
		setSize(1024, 768);
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
		panelStockManagement.setupList();
		panelStockOrder.displayOrderTable(0);
		panelStockOrder.displayProductsTable("", "");
		panelStockOrder.repaint();
		panelCustomerOrders.displayOrderTable(false, 0);
		panelCustomerOrders.displayProductsTable("");

		panelCustomer.saveDetails();

		panelSupplier.saveDetails();
		panelStockOrder.saveDetails();

	}

	public static void setLoggedStaffMember(Staff loggedStaffMember) {
		GUIBackBone.loggedStaffMember = loggedStaffMember;
	}
}
