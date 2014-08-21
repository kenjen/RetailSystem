package gui;

import gui.CustomerOrderPanel.ButtonOrderHandler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import data.ProductToOrder;
import net.miginfocom.swing.MigLayout;
import tableModels.ProductTableModel;

public class CurrentCustomerOrderDialog extends JDialog implements ActionListener  {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JButton btnOk = new JButton("Ok");
	private JScrollPane scrollPane;
	private JPanel contentPanePanel = new JPanel();
	private static ArrayList<Object[]> passedList;
	private JButton btnClearOrder = new JButton("Clear Order");
	private JButton btnRemoveSelected = new JButton ("Remove selected");
	private Object[][] listData;
	private JButton btnCancel = new JButton("No, Back");
	private JButton btnOrder = new JButton("Yes, Submit");
	int choice;
	
	public CurrentCustomerOrderDialog() {
		setModal(true);
	}
	
	public void confirmOrder(ArrayList<Object[]> list){
		scrollPane = new JScrollPane();
		scrollPane.setMaximumSize(new Dimension(1024,768));
		scrollPane.setMinimumSize(new Dimension(800,500));
		createTable(list);
		contentPanePanel.setLayout(new MigLayout());
		JLabel lab = new JLabel("Is this Order complete?");
		lab.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lab.setForeground(Color.orange);
		contentPanePanel.add(lab,"pushx,alignx center,span,wrap");
		contentPanePanel.add(scrollPane, "push, grow, span, wrap");
		contentPanePanel.add(btnCancel,"pushx, alignx right");
		contentPanePanel.add(btnOrder,"pushx, alignx left");
		btnCancel.addActionListener(this);
		btnOrder.addActionListener(this);

		setLayout(new GridBagLayout());
		setContentPane(contentPanePanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public CurrentCustomerOrderDialog(ArrayList<Object[]> list) {
		scrollPane = new JScrollPane();
		scrollPane.setMaximumSize(new Dimension(1024,768));
		scrollPane.setMinimumSize(new Dimension(800,500));
		createTable(list);
		
		contentPanePanel.setLayout(new MigLayout());
		contentPanePanel.add(scrollPane, "push, grow");
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.add(btnRemoveSelected,"growx, wrap");
		panel.add(btnClearOrder,"wrap, growx");
		contentPanePanel.add(panel,"pushx,wrap,alignx left, aligny top");
		contentPanePanel.add(btnOk,"pushx, alignx center");
		btnOk.addActionListener(this);
		btnClearOrder.addActionListener(this);
		btnRemoveSelected.addActionListener(this);

		setLayout(new GridBagLayout());
		setContentPane(contentPanePanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public int getValue(){
		return choice;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnCancel) || e.getSource().equals(btnOk)){
			choice = 0;
			setVisible(false);
		}else if(e.getSource().equals(btnClearOrder)){
			String columnNames[] = { "Id", "Name", "Supplier", "Category", "Price",	"Discounted?", "Quantity", "Amount to Order", "Remove?"};
			Object[][] objects = new Object[0][9];
			ProductTableModel model = new ProductTableModel(objects, columnNames);
			table=new JTable(model);
			scrollPane.getViewport().add(table);
			scrollPane.repaint();
			CustomerOrderPanel.setArrayCurrentOrder(new ArrayList<Object[]>());
		}else if(e.getSource().equals(btnRemoveSelected)){
			AbstractTableModel adstractModel = (AbstractTableModel) table.getModel();
			if(adstractModel.getRowCount() != 0){
				adstractModel.fireTableDataChanged();
				
				//loop through table data to find all element that have boolean set to true
				ArrayList<Object[]> newList= new ArrayList<Object[]>();
				for(Object[] object:listData){
					if((boolean) object[8] == false){
						newList.add(object);
					}
				}
				StockOrderPanel.setArrayTemporaryOrder(newList);
				createTable(newList);
			}
		}else if(e.getSource() .equals(btnOrder)){
			choice=1;
			setVisible(false);
		}
	}
	
	
	
	public void createTable(ArrayList<Object[]> list){
		passedList=list;
		String columnNames[] = { "Id", "Name", "Supplier", "Category", "Price",	"Discounted?", "Quantity", "Amount to Order", "Remove?"};
		//create from array list an array of objects
		Object[][] objects = new Object[list.size()][8];
		listData = new Object[list.size()][9];
		for(int i=0; i<list.size(); i++){
			objects[i] = list.get(i);
			listData[i][0] = objects[i][0];
			listData[i][1] = objects[i][1];
			listData[i][2] = objects[i][2];
			listData[i][3] = objects[i][3];
			listData[i][4] = objects[i][4];
			listData[i][5] = objects[i][5];
			listData[i][6] = objects[i][6];
			listData[i][7] = objects[i][7];
			listData[i][8] = false;
			
		}
		
		ProductTableModel model = new ProductTableModel(listData, columnNames);
		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		table.getColumnModel().getSelectionModel()
		.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				int row = table.getSelectedRow();
				table.requestFocus();
				table.changeSelection(row, 8, false, false);
			}

		});
		table.addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent e) {
		      if (e.getClickCount() == 2) {
		         JTable target = (JTable)e.getSource();
		         int row = target.getSelectedRow();
		         int cell = 0;
		         
		         int choice = JOptionPane.showConfirmDialog(CurrentCustomerOrderDialog.this, "Are you sure you want to remove this row?","Remove?",JOptionPane.YES_NO_OPTION);
		         if (choice==JOptionPane.YES_OPTION){
			        ProductTableModel model = (ProductTableModel) target.getModel();
			        //model.fireTableDataChanged();
			        System.out.println(model.getRowCount());
			        System.out.println(row);
			        model.removeRow(row);
			        table.repaint();
			        Object[] object = passedList.get(row);
			        passedList.remove(row);
			        
		         };
		      }
		}
		});
		scrollPane.getViewport().add(table);
		scrollPane.repaint();
	}

}
