package tableModels;

public class UneditableTableModel extends TableModelWithLastColEditable{
	
	public UneditableTableModel(Object[][] objectPassed, String[] columnNamesPassed){
		super(objectPassed, columnNamesPassed);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

}
