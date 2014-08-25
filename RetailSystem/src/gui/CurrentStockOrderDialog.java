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
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;
import tableModels.TableModelWithLastColEditable;
import tableModels.UneditableTableModel;

public class CurrentStockOrderDialog extends JDialog implements ActionListener  {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JButton btnOk = new JButton("Ok");
	private JScrollPane scrollPane;
	private JPanel contentPanePanel = new JPanel();
	private static ArrayList<Object[]> passedList;
	private JButton btnClearOrder = new JButton("Clear Order");
	//private JButton btnRemoveSelected = new JButton ("Remove selected");
	
	
	public CurrentStockOrderDialog() {
		
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
		panel.add(btnClearOrder,"wrap, growx");
		contentPanePanel.add(panel,"pushx,wrap,alignx left, aligny top");
		contentPanePanel.add(btnOk,"pushx, alignx center");
		btnOk.addActionListener(this);
		btnClearOrder.addActionListener(this);

		setLayout(new GridBagLayout());
		setContentPane(contentPanePanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnOk)){
			this.dispose();
		}else if(e.getSource().equals(btnClearOrder)){
			String columnNames[] = { "Id", "Name", "Category", "Price", "Supplier",	"Quantity", "In Shop?", "Required", "Amount to Order"};
			Object[][] objects = new Object[0][9];
			UneditableTableModel model = new UneditableTableModel(objects, columnNames);
			table=new JTable(model);
			scrollPane.getViewport().add(table);
			scrollPane.repaint();
			StockOrderPanel.setArrayTemporaryOrder(new ArrayList<Object[]>());
			table.setAutoCreateRowSorter(false);
		}
	}
	
	public void drawTable(ArrayList<Object[]> list){
		passedList=list;
		String columnNames[] = { "Id", "Name", "Category", "Price", "Supplier",	"Quantity", "In Shop?", "Required", "Amount to Order"};
		Object[][] objects = new Object[list.size()][];
		
		//list.toArray(array type and length of the actual list) returns an array of the same array type and length
		Object[][] products = passedList.toArray(objects);
		int counter = 0;
		for(Object[] object:products){
			products[counter][0] = (int) object[0];
			products[counter][1] = (String) object[1];
			products[counter][2] = (String) object[2];
			products[counter][3] = (double) object[3];
			products[counter][4] = (String) object[4];
			products[counter][5] = (int) object[5];
			products[counter][6] = (boolean) object[6];
			products[counter][7] = (boolean) object[7];
			products[counter][8] = (int) object[8];
			counter++;
		}
		
		UneditableTableModel model = new UneditableTableModel(products, columnNames);
		table = new JTable(model);
		if(table.getRowCount() > 0)
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseAdapter(){
			//selects the row at right click
			public void mousePressed(MouseEvent e){
				if(e.isPopupTrigger()){
					JTable source = (JTable)e.getSource();
	                int row = source.rowAtPoint( e.getPoint() );
	                int column = source.columnAtPoint( e.getPoint() );

	                System.out.println("Selecting row in table");
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
					String columnNames[] = { "Id", "Name", "Category", "Price", "Supplier",	"Quantity", "In Shop?", "Required", "Amount to Order"};
					Object[][] objects = new Object[0][9];
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
