package gui;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.JsonExample;
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
	private JButton btnLogout = new JButton();
	private static JPanel panel2;
	private static int counter = 0;
	
	
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
			
			panelStockOrder.addFocusListener(new FocusAdapter(){

				@Override
				public void focusGained(FocusEvent e) {
					if(e.getSource() == JPanel.class){
						System.out.println("Jpanel");
					}
				}
			});
			
		JLayeredPane lPane = new JLayeredPane();
		final JPanel panel2 = new JPanel();
		setLayout(new GridBagLayout());
		add(lPane);
		setContentPane(lPane);
		setSize(1024,768);
		
		btnLogout.setIcon(new ImageIcon("resources/Logout_closed.png"));
		btnLogout.setBorder(null);
		btnLogout.setContentAreaFilled(false);
		btnLogout.setBorderPainted(false);
		btnLogout.setSize(48, 48);
		btnLogout.setOpaque(false);
		tabbedPane.setSize(this.getWidth(),this.getHeight());
		panel2.setBounds(this.getWidth()-50,0,48,48);
		panel2.setOpaque(true);
		panel2.setLayout(new FlowLayout());
		panel2.setBorder(null);
		panel2.add(btnLogout);
		lPane.add(tabbedPane, new Integer(1), 0);
		lPane.add(panel2, new Integer(1), 0);
		lPane.setBorder(null);
		
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("RetailSystem");
		
		//When frame is closed, save all details
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				saveTabDetails(0);
				
				//Remove the contents of staff.json file
				//JsonExample.clearList("resources/staff.json");
			}
			
		});
		
		//since we are using fixed bounds for the logout button, at each window resize recalculate and redraw the component
		addComponentListener(new ComponentAdapter(){

			@Override
			public void componentResized(ComponentEvent arg0) {
				JFrame frame = GUIBackBone.this;
				counter++;
				if(counter > 3){
					System.out.println("width: "+GUIBackBone.this.getWidth());
					System.out.println("width: "+GUIBackBone.this.getHeight());
					int width = frame.getWidth();
					int height = frame.getHeight();
					panel2.setBounds(width-50,0,48,48);
					tabbedPane.setSize(width,height);
				}
			}
			
		});
		
		btnLogout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int x = JOptionPane.showConfirmDialog(GUIBackBone.this, "Are you sure you want to log out?","Log Out?", JOptionPane.YES_NO_OPTION);
				if(x==JOptionPane.YES_OPTION){
					// run login
					Login login = new Login(Shop.getStaffMembers());
					login.drawFrame();
					saveTabDetails(0);
					GUIBackBone.this.dispose();
				}
				
			}
			
		});
		btnLogout.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnLogout.setIcon(new ImageIcon("resources/Logout.png"));
				btnLogout.repaint();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				btnLogout.setIcon(new ImageIcon("resources/Logout_closed.png"));
				btnLogout.repaint();
			}
			
		});
	}


	public static Staff getLoggedStaffMember() {
		return loggedStaffMember;
	}
	
	public void saveTabDetails(int index){
		panelStockManagement.saveDetails();
		panelStockManagement.setupList();
		panelStockOrder.displayOrderTable(0);
		panelStockOrder.displayProductsTable("", "");
		panelStockOrder.repaint();
		panelCustomerOrders.displayOrderTable(false,0);
		panelCustomerOrders.displayProductsTable("");

		panelCustomer.saveDetails();

		panelSupplier.saveDetails();
		panelStockOrder.saveDetails();

	}

	public static void setLoggedStaffMember(Staff loggedStaffMember) {
		GUIBackBone.loggedStaffMember = loggedStaffMember;
	}
}
