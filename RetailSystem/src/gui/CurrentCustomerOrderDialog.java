package gui;

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
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;
import tableModels.UneditableTableModel;

public class CurrentCustomerOrderDialog extends JDialog implements ActionListener  {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JButton btnOk = new JButton("Ok");
	private JScrollPane scrollPane;
	private JPanel contentPanePanel = new JPanel();
	private static ArrayList<Object[]> passedList;
	private JButton btnClearOrder = new JButton("Clear Order");
	private Object[][] listData;
	private JButton btnCancel = new JButton("No, Back");
	private JButton btnOrder = new JButton("Yes, Submit");
	int choice;
	
	public CurrentCustomerOrderDialog() {
		
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
		setModal(true);
		setTitle("Order confirmation");
		toFront();
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
		panel.add(btnClearOrder,"wrap, growx");
		contentPanePanel.add(panel,"pushx,wrap,alignx left, aligny top");
		contentPanePanel.add(btnOk,"pushx, alignx center");
		btnOk.addActionListener(this);
		btnClearOrder.addActionListener(this);

		setLayout(new GridBagLayout());
		setContentPane(contentPanePanel);
		setModal(true);
		toFront();
		setTitle("Current order");
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
			String columnNames[] = { "Id", "Name", "Supplier", "Category", "Price",	"Discounted?", "Quantity", "Amount to Order"};
			Object[][] objects = new Object[0][8];
			UneditableTableModel model = new UneditableTableModel(objects, columnNames);
			table=new JTable(model);
			scrollPane.getViewport().add(table);
			scrollPane.repaint();
			CustomerOrderPanel.setArrayCurrentOrder(new ArrayList<Object[]>());
			table.setAutoCreateRowSorter(false);
		}else if(e.getSource() .equals(btnOrder)){
			choice=1;
			setVisible(false);
		}
	}
	
	
	
	public void createTable(ArrayList<Object[]> list){
		passedList=list;
		String columnNames[] = { "Id", "Name", "Supplier", "Category", "Price",	"Discounted?", "Quantity", "Amount to Order"};
		//create from array list an array of objects
		Object[][] objects = new Object[list.size()][7];
		listData = new Object[list.size()][8];
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
			
		}
		
		UneditableTableModel model = new UneditableTableModel(listData, columnNames);
		table = new JTable(model);
		if(table.getRowCount() > 0)
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseAdapter(){
			//selects the row at right click
			public void mousePressed(MouseEvent e){
				if(e.isMetaDown()){
					JTable source = (JTable)e.getSource();
	                int row = source.rowAtPoint( e.getPoint() );
	                int column = source.columnAtPoint( e.getPoint() );

	                if (! source.isRowSelected(row)){
	                    source.changeSelection(row, column, false, false);
	                }
	                JPopupMenu m = mouseMenu();
	                m.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		
		//table.setComponentPopupMenu(popup);
		scrollPane.getViewport().add(table);
		scrollPane.repaint();
	}
	
	public JPopupMenu mouseMenu(){
		JPopupMenu popup = new JPopupMenu();
		JMenuItem removeSelected = new JMenuItem("Remove Selected");
		popup.add(removeSelected);
		removeSelected.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				int row = table.getSelectedRow();
				UneditableTableModel model = (UneditableTableModel) table.getModel();
				if(model.getRowCount()>1){
					model.removeRow(row);
					model.fireTableDataChanged();
				}else{
					String columnNames[] = { "Id", "Name", "Supplier", "Category", "Price",	"Discounted?", "Quantity", "Amount to Order"};
					Object[][] objects = new Object[0][8];
					UneditableTableModel mod = new UneditableTableModel(objects, columnNames);
					table=new JTable(mod);
					scrollPane.getViewport().add(table);
					scrollPane.repaint();
					table.setAutoCreateRowSorter(false);
				}
		        passedList.remove(row);
			}
		});
		return popup;
	}

}
