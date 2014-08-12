package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JTextPane;

import data.Product;
import data.Supplier;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import javax.swing.JSplitPane;

import java.awt.GridLayout;

public class testguipanel extends JFrame {

	private JPanel contentPane;
	private DefaultListModel listModel = new DefaultListModel();
	private JList list;
	ArrayList<Product> products = new ArrayList<Product>();
	private JTextField textField;
	private JTextField txtEnterProductId;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField txtId;
	private JTextField txtName;
	private JTextField txtCategory;
	private JTextField txtQuantity;
	private JTextField txtPrice;
	private JTextField txtSupplier;
	private JTextField textName;
	private JTextField textCategory;
	private JTextField textQuantity;
	private JTextField textPrice;
	private JTextField textSupplier;
	
	private boolean productLoaded = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testguipanel frame = new testguipanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public testguipanel() {
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//setup test products
		Product p1 = new Product("Pear", "Food", 100, 0.23, new Supplier(01, "tesco", "stillorgan"), true, 22);
		Product p2 = new Product("Coat", "Clothing", 50, 29.99, new Supplier(02, "tesco", "dundrum"), true, 10);
		Product p3 = new Product("Trousers To Test Size", "Clothing", 80, 40.0, new Supplier(03, "tesco", "rathfarnham"), true, 15);
		Product p4 = new Product("Ham", "Food", 120, 4.50, new Supplier(04, "tesco", "blackrock"), true, 60);
		Product p5 = new Product("Broom", "Hygene", 20, 12.0, new Supplier(05, "tesco", "city center"), true, 3);
		products.add(p1);
		products.add(p2);
		products.add(p3);
		products.add(p4);
		products.add(p5);
		
		//setup list with products
		list = new JList(listModel);
		for(Product product : products){
			listModel.addElement("Id=" + product.getId() + "   " + product.getQuantity() + " Units    " + product.getName());
		}
		
		//setup tabbed pane
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setSize(800, 600);
		contentPane.add(tabbedPane);
		
		
		
		//split pane
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		tabbedPane.addTab("New tab", null, splitPane, null);
		
		//Add scroll pane to left size with list of products
		JScrollPane scrollPane_1 = new JScrollPane(list);
		splitPane.setLeftComponent(scrollPane_1);
		
		//add panel to right hand side
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new MigLayout("", "[][100px:n:100px,grow][100px:n:100px,grow][100px:100px:100px][100px:n:100px][grow]", "[][][][][][][][][][][][]"));
		
		txtId = new JTextField();
		txtId.setHorizontalAlignment(SwingConstants.CENTER);
		txtId.setColumns(20);
		txtId.setText("Enter Id Of Product");
		txtId.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				txtId.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				//Do Nothing
			}
			
		});
		panel.add(txtId, "cell 0 0 6 1,alignx center");
		
		//Setup button to load a products details
		JButton btnIdConfirm = new JButton("Confirm");
		btnIdConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean successful = true;
				int id = 0;
				Product tempProduct = null;
				String tempId = txtId.getText();
				try{
					id = Integer.parseInt(tempId);
				}catch(NumberFormatException nfe){
					successful = false;
					System.out.println("*****Entered a non integer value*****");
				}
				boolean productExists = false;
				if(successful){
					for(Product product : products){
						if(product.getId() == id){
							productExists = true;
							tempProduct = product;
							productLoaded = true;
							break;
						}
					}
				}
				if(!productExists){
					System.out.println("*****This Id Does Not Match A Product*****");
				}else{
					textName.setText(tempProduct.getName());
					textCategory.setText(tempProduct.getCategory());
					textQuantity.setText(""+tempProduct.getQuantity());
					textPrice.setText(""+tempProduct.getPrice());
					textSupplier.setText(tempProduct.getSupplier().getSupplierName());
				}
			}
		});
		panel.add(btnIdConfirm, "cell 3 1");
		
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setHorizontalAlignment(SwingConstants.CENTER);
		txtName.setText("Name");
		panel.add(txtName, "cell 1 3,growx");
		txtName.setColumns(10);
		
		textName = new JTextField();
		panel.add(textName, "cell 3 3,growx");
		textName.setColumns(10);
		
		JButton btnSaveName = new JButton("Save");
		panel.add(btnSaveName, "cell 5 3");
		
		txtCategory = new JTextField();
		txtCategory.setHorizontalAlignment(SwingConstants.CENTER);
		txtCategory.setEditable(false);
		txtCategory.setText("Category");
		panel.add(txtCategory, "cell 1 5,growx");
		txtCategory.setColumns(10);
		
		textCategory = new JTextField();
		panel.add(textCategory, "cell 3 5,growx");
		textCategory.setColumns(10);
		
		JButton btnSaveCategory = new JButton("Save");
		panel.add(btnSaveCategory, "cell 5 5");
		
		txtQuantity = new JTextField();
		txtQuantity.setText("Quantity");
		txtQuantity.setEditable(false);
		txtQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtQuantity, "cell 1 7,growx");
		txtQuantity.setColumns(10);
		
		textQuantity = new JTextField();
		panel.add(textQuantity, "cell 3 7,growx");
		textQuantity.setColumns(10);
		
		JButton btnSaveQuantity = new JButton("Save");
		panel.add(btnSaveQuantity, "cell 5 7");
		
		txtPrice = new JTextField();
		txtPrice.setHorizontalAlignment(SwingConstants.CENTER);
		txtPrice.setEditable(false);
		txtPrice.setText("Price");
		panel.add(txtPrice, "cell 1 9,growx");
		txtPrice.setColumns(10);
		
		textPrice = new JTextField();
		panel.add(textPrice, "cell 3 9,growx");
		textPrice.setColumns(10);
		
		JButton btnSavePrice = new JButton("Save");
		panel.add(btnSavePrice, "cell 5 9");
		
		txtSupplier = new JTextField();
		txtSupplier.setHorizontalAlignment(SwingConstants.CENTER);
		txtSupplier.setEditable(false);
		txtSupplier.setText("Supplier");
		panel.add(txtSupplier, "cell 1 11,growx");
		txtSupplier.setColumns(10);
		
		textSupplier = new JTextField();
		panel.add(textSupplier, "cell 3 11,growx");
		textSupplier.setColumns(10);
		
		JButton btnSaveSupplier = new JButton("Save");
		panel.add(btnSaveSupplier, "cell 5 11");
	}
}
