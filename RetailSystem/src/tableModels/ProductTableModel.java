package tableModels;

import javax.swing.table.AbstractTableModel;

public class ProductTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private String columnNames[];
	private Object[][] data;
	
	public ProductTableModel(Object[][] objectPassed, String[] columnNamesPassed){
		data = objectPassed;
		columnNames = columnNamesPassed;
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
			return true;
		}else{
			return false;
		}
    }

	//allow saves ONLY on last column
    public void setValueAt(Object value, int row, int col) {
    	//if(col == columnNames.length-1){
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    	//}
    }
	
}//end class ProductTableModel