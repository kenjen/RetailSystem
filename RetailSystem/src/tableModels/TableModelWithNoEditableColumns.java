package tableModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TableModelWithNoEditableColumns extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private String columnNames[];
	private Object[][] data;
	
	public TableModelWithNoEditableColumns(Object[][] objectPassed, String[] columnNamesPassed){
		data = objectPassed;
		columnNames = columnNamesPassed;
	}
	public TableModelWithNoEditableColumns(){
		
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		//Make sure that if the user changes the value to null (deletes everything from cell) the integer remains parsed to int
		//otherwise an exception is generated
		if(columnIndex != columnNames.length-1){
			return data[rowIndex][columnIndex];
		}else{
			if(data[rowIndex][columnIndex] != null){
				return data[rowIndex][columnIndex];
			}else{
				return 0;
			}
		}
	}
	
	public Class<?> getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	 }
		
	//allow edits ONLY on last column
	public boolean isCellEditable(int row, int col) {
        /*if (col < 2) {
            return false;
        } else {
            return true;
        }*/
		if(col == columnNames.length-1){
			return false;
		}else{
			return true;
		}
    }
	
	//allow saves ONLY on last column
    public void setValueAt(Object value, int row, int col) {
    	//if(col == columnNames.length-1){
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    	//}
    }
    
    //remove row
    public void removeRow(int row){
    	List<Object[]> list = new ArrayList<Object[]>(Arrays.asList(data));
    	list.remove(row);
    	Object[][] array2 = list.toArray(new Object[][]{});
    	data = array2;
    	//calling fireTableDataChanged() gives error if data is basically null
    	if(data.length != 0){
    		fireTableDataChanged();
    	}
    }
    
}//end class ProductTableModel