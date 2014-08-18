package gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
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

public class CurrentStockOrderDialog extends JDialog implements ActionListener  {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JButton btnOk = new JButton("Ok");
	private JScrollPane scrollPane;
	private JPanel contentPanePanel = new JPanel();
	private Object[][] arrayForTableWithExtraElement;
	private static ArrayList<Object[]> passedList;
	private JButton btnClearOrder = new JButton("Clear Order");
	private JButton btnRemoveSelected = new JButton ("Remove selected");
	
	
	public CurrentStockOrderDialog() {
		
	}
	
	public void currentCustomerOrder(ArrayList<Object[]> list) {
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
		setVisible(true);
	}
	
	public CurrentStockOrderDialog(ArrayList<Object[]> list) {
		
		
		scrollPane = new JScrollPane();
		scrollPane.setMaximumSize(new Dimension(1024,768));
		scrollPane.setMinimumSize(new Dimension(800,500));
		drawTable(list);
		
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
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnOk)){
			this.dispose();
		}else if(e.getSource().equals(btnClearOrder)){
			String columnNames[] = { "Id", "Name", "Category", "Price", "Supplier",	"Quantity", "In Shop?", "Required", "Amount to Order", "Remove?"};
			Object[][] objects = new Object[0][10];
			ProductTableModel model = new ProductTableModel(objects, columnNames);
			table=new JTable(model);
			scrollPane.getViewport().add(table);
			scrollPane.repaint();
			StockOrderPanel.setArrayTemporaryOrder(new ArrayList<Object[]>());
		}else if(e.getSource().equals(btnRemoveSelected)){
			AbstractTableModel adstractModel = (AbstractTableModel) table.getModel();
			if(adstractModel.getRowCount() != 0){
				adstractModel.fireTableDataChanged();
				
				//loop through table data to find all element that have boolean set to true
				ArrayList<Object[]> newList= new ArrayList<Object[]>();
				for(Object[] object:arrayForTableWithExtraElement){
					if((boolean)object[9] == false){
						newList.add(object);
					}
				}
				StockOrderPanel.setArrayTemporaryOrder(newList);
				drawTable(newList);
			}
		}
	}
	
	public void drawTable(ArrayList<Object[]> list){
		passedList=list;
		String columnNames[] = { "Id", "Name", "Category", "Price", "Supplier",	"Quantity", "In Shop?", "Required", "Amount to Order", "Remove?"};
		Object[][] objects = new Object[list.size()][];
		
		//list.toArray(array type and length of the actual list) returns an array of the same array type and length
		Object[][] products = passedList.toArray(objects);
		arrayForTableWithExtraElement = new Object[products.length][10];
		int counter = 0;
		for(Object[] object:products){
			arrayForTableWithExtraElement[counter][0] = (int) object[0];
			arrayForTableWithExtraElement[counter][1] = (String) object[1];
			arrayForTableWithExtraElement[counter][2] = (String) object[2];
			arrayForTableWithExtraElement[counter][3] = (double) object[3];
			arrayForTableWithExtraElement[counter][4] = (String) object[4];
			arrayForTableWithExtraElement[counter][5] = (int) object[5];
			arrayForTableWithExtraElement[counter][6] = (boolean) object[6];
			arrayForTableWithExtraElement[counter][7] = (boolean) object[7];
			arrayForTableWithExtraElement[counter][8] = (int) object[8];
			arrayForTableWithExtraElement[counter][9] = false;
			counter++;
		}
		
		
		ProductTableModel model = new ProductTableModel(arrayForTableWithExtraElement, columnNames);
		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		table.getColumnModel().getSelectionModel()
		.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				int row = table.getSelectedRow();
				table.requestFocus();
				table.changeSelection(row, 9, false, false);
			}

		});
		table.addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent e) {
		      if (e.getClickCount() == 2) {
		         JTable target = (JTable)e.getSource();
		         int row = target.getSelectedRow();
		         int cell = 0;
		         
		         int choice = JOptionPane.showConfirmDialog(CurrentStockOrderDialog.this, "Are you sure you want to remove this row?","Remove?",JOptionPane.YES_NO_OPTION);
		         if (choice==JOptionPane.YES_OPTION){
			        ProductTableModel model = (ProductTableModel) target.getModel();
			        //model.fireTableDataChanged();
			        System.out.println(model.getRowCount());
			        System.out.println(row);
			        model.removeRow(row);
			        table.repaint();
			        passedList.remove(row);
		         };
		      }
		}
		});
		scrollPane.getViewport().add(table);
		scrollPane.repaint();
	}
	
	public void createTable(ArrayList<Object[]> list){
		passedList=list;
		String columnNames[] = { "Id", "Name", "Supplier", "Category", "Price",	"Discounted?", "Quantity", "Amount to Order", "Remove?"};
		//create from array list an array of objects
		Object[][] objects = new Object[list.size()][8];
		Object[][] listData = new Object[list.size()][9];
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
		System.out.println(listData[0][1].toString());
		
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
		         
		         int choice = JOptionPane.showConfirmDialog(CurrentStockOrderDialog.this, "Are you sure you want to remove this row?","Remove?",JOptionPane.YES_NO_OPTION);
		         if (choice==JOptionPane.YES_OPTION){
			        ProductTableModel model = (ProductTableModel) target.getModel();
			        //model.fireTableDataChanged();
			        System.out.println(model.getRowCount());
			        System.out.println(row);
			        model.removeRow(row);
			        table.repaint();
			        passedList.remove(row);
		         };
		      }
		}
		});
		scrollPane.getViewport().add(table);
		scrollPane.repaint();
	}

}
