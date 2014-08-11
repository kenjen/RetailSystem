package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class testguipanel extends JFrame {

	private JPanel contentPane;
	private DefaultListModel listModel = new DefaultListModel();
	private JList list;
	ArrayList<Product> products = new ArrayList<Product>();

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Product p1 = new Product("Pear", "Food", 100, 0.23, new Supplier(01, "tesco", "stillorgan"), true, 22);
		Product p2 = new Product("Coat", "Clothing", 50, 29.99, new Supplier(02, "tesco", "dundrum"), true, 10);
		Product p3 = new Product("Trousers", "Clothing", 80, 40.0, new Supplier(03, "tesco", "rathfarnham"), true, 15);
		Product p4 = new Product("Ham", "Food", 120, 4.50, new Supplier(04, "tesco", "blackrock"), true, 60);
		Product p5 = new Product("Broom", "Hygene", 20, 12.0, new Supplier(05, "tesco", "city center"), true, 3);
		
		products.add(p1);
		products.add(p2);
		products.add(p3);
		products.add(p4);
		products.add(p5);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 434, 262);
		contentPane.add(tabbedPane);
		
		JPanel panelStock = new JPanel();
		tabbedPane.addTab("Stock Management", null, panelStock, null);
		panelStock.setLayout(new MigLayout("", "[][][][][][][]", "[]"));
		
		JButton btnNewButton = new JButton("Add Products");
		btnNewButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				for(Product product : products){
					listModel.addElement(product.getQuantity() + "    " + product.getName());
				}
			}
			
		});
		panelStock.add(btnNewButton, "cell 6 0");
		
		list = new JList(listModel);
		JScrollPane scrollPane = new JScrollPane(list);
		panelStock.add(scrollPane, "cell 0 0");
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_2, null);
	}
}
