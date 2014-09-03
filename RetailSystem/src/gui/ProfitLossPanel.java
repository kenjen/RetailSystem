package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import tableModels.UneditableTableModel;
import data.CustomerOrder;
import data.Finance;
import data.StockOrder;

public class ProfitLossPanel extends JPanel{
	private ArrayList<Finance> finances = new ArrayList<Finance>();
	private static final long serialVersionUID = 1L;
	private JLabel lblWrittenReport = new JLabel();
	private double expenseCurrentYear;
	private double expenseCurrentWeek;
	private double expenseCurrentMonth;
	private double expenseLastMonth;
	private double expenseMonthBeforeLastMonth;
	private double profitCurrentYear;
	private double profitCurrentMonth;
	private double profitCurrentWeek;
	private double profitLastMonth;
	private double profitMonthBeforeLastMonth;
	private String strCurrentYear;
	private String strCurrentMonth;
	private String strCurrentWeek;
	private String strLastMonth;
	private String strMonthBeforeLastMonth;
	private DecimalFormat df = new DecimalFormat("#.00");
	private JButton btnAddExpense = new JButton("Add Extra Expense");
	private ArrayList<Object[]> tableData = new ArrayList<Object[]>();
	private UneditableTableModel tableModel; 
	private JTable table = new JTable();
	private JScrollPane scrollPane = new JScrollPane(table);
	private CategoryDataset chartData;
	private ChartPanel chartPanel;
	private JFreeChart chart;
	private DefaultCategoryDataset dataset;
    // row keys...
    private final String expense = "Expense";
    private final String profit = "Income";
	
    public ProfitLossPanel(){
    	
    }
    
	public void createPanel() {
		setLayout(new MigLayout());
		generateFinanceData();
		loadChartData();
		
		// create and add the chart to a panel...
	    createChart();
	    add(chartPanel, "alignx right, pushx");

	    // add the written finance report
	    add(lblWrittenReport,"aligny top, alignx left, pushx ,wrap");
	    lblWrittenReport.setText(generateHtmlString());
	    
	    //add the option to add expense
	    JLabel lblExpenses = new JLabel("Recorded expenses:");
	    lblExpenses.setFont(Shop.TITLE_FONT);
	    lblExpenses.setForeground(Shop.TITLE_COLOR);
	    add(lblExpenses,"split 2");
	    add(btnAddExpense, "gapx 50, wrap");
	    add(scrollPane,"span, grow, push");
	    drawTable();
	    btnAddExpense.addActionListener(new AddExpenseHandler());
	    
	}
	
	public void createChart() {
	    // create the chart...
	   JFreeChart thisChart = ChartFactory.createBarChart(
	        "Financial report",        // chart title
	        "Period",               // domain axis label
	        "Amount",                  // range axis label
	        chartData,                 // data
	        PlotOrientation.VERTICAL,
	        true,                     // include legend
	        true,                     // tooltips?
	        false                     // URL generator?  Not required...
	    );

	    // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	   thisChart.setBackgroundPaint(Color.white);

	    // get a reference to the plot for further customisation...
	    CategoryPlot plot = thisChart.getCategoryPlot();
	    plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
	    plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

	    CategoryAxis domainAxis = plot.getDomainAxis();
	    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
	    ValueAxis axis2 = new NumberAxis("Secondary");
	    plot.setRangeAxis(1, axis2);

	    LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
	    renderer2.setToolTipGenerator(new StandardCategoryToolTipGenerator());
	    plot.setRenderer(1, renderer2);
	    plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
	    
	    // add the chart to a panel...
	    chartPanel = new ChartPanel(thisChart);
	}

	//makes the table with previous miscelaneous expenses
	public void drawTable() {
		String columnNames[] = {"Id","Admin name","Date","Comments","Amount"};
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		tableData = new ArrayList<Object[]>();
		for(Finance finance : Shop.getFinancialRecords()){
			Object[] x = new Object[5];
			x[0] = finance.getId();
			x[1] = GUIBackBone.getLoggedStaffMember().getName()+", "+GUIBackBone.getLoggedStaffMember().getSurname();
			x[2] = sdf.format(finance.getDate());
			x[3] = finance.getDescription();
			x[4] = finance.getAmount();
			tableData.add(x);
		}
		tableModel = new UneditableTableModel(tableData.toArray(new Object[tableData.size()][]), columnNames);
		table.setModel(tableModel);
		scrollPane.getViewport().add(table);
		scrollPane.repaint();
		
	}//end drawTable

	//generates a String to be used as a HTML table
	public String generateHtmlString() {
	    String styleGreen = "background-color:\"green\"";
	    String styleRed = "background-color:red";
	    String htmlString = "<html><head>"
	    		+ "<style>"
	    		+ "table{background-color:white}"
	    		+ "</style>"
	    		+ "</head>"
	    		+ "<body>"
	    			+ "<table border='1'>"
			    		+ "<tr>"
				    		+ "<td rowspan='3'>"+strCurrentYear+"</td>"
				    		+ "<td style='color:red'>EUR "+df.format(expenseCurrentYear)+"</td>"
			    		+ "</tr>"
			    		+ "<tr>"
				    		+ "<td style='color:blue'>EUR "+df.format(profitCurrentYear)+"</td>"
			    		+ "</tr>"
			    		+ "<tr>";
    		if(profitCurrentYear-expenseCurrentYear > 0){
    			htmlString+="<td style='font-size:12px; border:1px solid black;"+styleGreen+"'>EUR "+df.format(profitCurrentYear-expenseCurrentYear)+"</td>";
    		}else{
    			htmlString+="<td style='font-size:12px; border:1px solid black;"+styleRed+"'>EUR "+df.format(profitCurrentYear-expenseCurrentYear)+"</td>";
    		}
			htmlString+= "</tr>"
			    		+ "<tr>"
				    		+ "<td rowspan='3'>"+strMonthBeforeLastMonth+"</td>"
				    		+ "<td style='color:red'>EUR "+df.format(expenseMonthBeforeLastMonth)+"</td>"
			    		+ "</tr>"
			    		+ "<tr>"
				    		+ "<td style='color:blue'>EUR "+df.format(profitMonthBeforeLastMonth)+"</td>"
			    		+ "</tr>"
			    		+ "<tr>";
    		if(profitMonthBeforeLastMonth-expenseMonthBeforeLastMonth > 0){
    			htmlString+="<td style='font-size:12px; border:1px solid black;"+styleGreen+"'>EUR "+df.format(profitMonthBeforeLastMonth-expenseMonthBeforeLastMonth)+"</td>";
    		}else{
    			htmlString+="<td style='font-size:12px; border:1px solid black;"+styleRed+"'>EUR "+df.format(profitMonthBeforeLastMonth-expenseMonthBeforeLastMonth)+"</td>";
    		}
    		htmlString+= "</tr>"
			    		+ "</tr>"
			    		+ "<tr>"
				    		+ "<td rowspan='3'>"+strLastMonth+"</td>"
				    		+ "<td style='color:red'>EUR "+df.format(expenseLastMonth)+"</td>"
			    		+ "</tr>"
			    		+ "<tr>"
			    			+ "<td style='color:blue'>EUR "+df.format(profitLastMonth)+"</td>"
			    		+ "</tr>"
			    		+ "<tr>";
			if(profitLastMonth-expenseLastMonth > 0){
				htmlString+="<td style='font-size:12px; border:1px solid black;"+styleGreen+"'>EUR "+df.format(profitLastMonth-expenseLastMonth)+"</td>";
			}else{
				htmlString+="<td style='font-size:12px; border:1px solid black;"+styleRed+"'>EUR "+df.format(profitLastMonth-expenseLastMonth)+"</td>";
			}
			htmlString+= "</tr>"
			    		+ "</tr>"
			    		+ "<tr>"
				    		+ "<td rowspan='3'>"+strCurrentMonth+"</td>"
				    		+ "<td style='color:red'>EUR "+df.format(expenseCurrentMonth)+"</td>"
			    		+ "</tr>"
			    		+ "<tr>"
				    		+ "<td style='color:blue'>EUR "+df.format(profitCurrentMonth)+"</td>"
			    		+ "</tr>"
			    		+ "<tr>";
			if(profitCurrentMonth-expenseCurrentMonth > 0){
				htmlString+="<td style='font-size:12px; border:1px solid black;"+styleGreen+"'>EUR "+df.format(profitCurrentMonth-expenseCurrentMonth)+"</td>";
			}else{
				htmlString+="<td style='font-size:12px; border:1px solid black;"+styleRed+"'>EUR "+df.format(profitCurrentMonth-expenseCurrentMonth)+"</td>";
			}
			htmlString+= "</tr>"
			    		+ "</tr>"
			    		+ "<tr>"
				    		+ "<td rowspan='3'>"+strCurrentWeek+"</td>"
				    		+ "<td style='color:red'>EUR "+df.format(expenseCurrentWeek)+"</td>"
			    		+ "</tr>"
			    		+ "<tr>"
				    		+ "<td style='color:blue'>EUR "+df.format(profitCurrentWeek)+"</td>"
			    		+ "</tr>"
			    		+ "<tr>";
			if(profitCurrentWeek-expenseCurrentWeek > 0){
				htmlString+="<td style='font-size:12px; border:1px solid black;"+styleGreen+"'>EUR "+df.format(profitCurrentWeek-expenseCurrentWeek)+"</td>";
			}else{
				htmlString+="<td style='font-size:12px; border:1px solid black;"+styleRed+"'>EUR "+df.format(profitCurrentWeek-expenseCurrentWeek)+"</td>";
			}
			htmlString+= "</tr>"
			    		+ "</tr>"
		    		+ "</table>"
	    		+ "</body>"
	    		+ "</html>";
		return htmlString;
	}

	//creates a dataset for the Chart
	public void loadChartData() {

	    // create the dataset...
	    dataset = new DefaultCategoryDataset();
		dataset.addValue(expenseCurrentYear, expense, strCurrentYear);
		dataset.addValue(expenseMonthBeforeLastMonth, expense, strMonthBeforeLastMonth);
		dataset.addValue(expenseLastMonth, expense, strLastMonth);
		dataset.addValue(expenseCurrentMonth, expense, strCurrentMonth);
		dataset.addValue(expenseCurrentWeek, expense, strCurrentWeek);

	    dataset.addValue(profitCurrentYear, profit, strCurrentYear);
	    dataset.addValue(profitMonthBeforeLastMonth, profit, strMonthBeforeLastMonth);
	    dataset.addValue(profitLastMonth, profit, strLastMonth);
	    dataset.addValue(profitCurrentMonth, profit, strCurrentMonth);
	    dataset.addValue(profitCurrentWeek, profit, strCurrentWeek);
	    
	    chartData = dataset;

	}
	
	public void updateChartData(){
		dataset.setValue(expenseCurrentYear, expense, strCurrentYear);
		dataset.setValue(expenseMonthBeforeLastMonth, expense, strMonthBeforeLastMonth);
		dataset.setValue(expenseLastMonth, expense, strLastMonth);
		dataset.setValue(expenseCurrentMonth, expense, strCurrentMonth);
		dataset.setValue(expenseCurrentWeek, expense, strCurrentWeek);

	    dataset.setValue(profitCurrentYear, profit, strCurrentYear);
	    dataset.setValue(profitMonthBeforeLastMonth, profit, strMonthBeforeLastMonth);
	    dataset.setValue(profitLastMonth, profit, strLastMonth);
	    dataset.setValue(profitCurrentMonth, profit, strCurrentMonth);
	    dataset.setValue(profitCurrentWeek, profit, strCurrentWeek);
	}
	
	//returns the month name based on integer input
	public String getMonthName(int x){
		switch (x){
		case 1:{
			return "January";
		}case 2:{
			return "February";
		}case 3:{
			return "March";
		}case 4:{
			return "April";
		}case 5:{
			return "May";
		}case 6:{
			return "June";
		}case 7:{
			return "July";
		}case 8:{
			return "August";
		}case 9:{
			return "September";
		}case 10:{
			return "October";
		}case 11:{
			return "November";
		}case 12:{
			return "December";
		}default:{
			return null;
		}
		}
	}
	
	public void refreshChart(){
		generateFinanceData();
		updateChartData();
		lblWrittenReport.setText(generateHtmlString());
	}
	
	//returns the profit for specified period
	public double calculateProfitForSpecifiedPeriod(Date start, Date end){
		double total = 0;
		for(Finance finance:finances){
			if(finance.isExpense()==false && finance.getDate().before(end) && finance.getDate().after(start)){
				total+=finance.getAmount();
			}
		}
		return total;
		
	}
	
	//returns the expenses for specified period
	public double calculateExpencesForSpecifiedPeriod(Date start, Date end){
		double total = 0;
		for(Finance finance:finances){
			if(finance.isExpense()==true && finance.getDate().before(end) && finance.getDate().after(start)){
				total+=finance.getAmount();
			}
		}
		return total;
		
	}

	//Gather profit/loss data from Orders and miscellaneous expenses (Shop.getFinances())
	public void generateFinanceData(){
		finances = new ArrayList<Finance>();
		for(CustomerOrder order:Shop.getCustomerOrders()){
			finances.add(new Finance(order.getTotalNet(),order.getCreationDate(),"Customer Order",false));
		}
		for(StockOrder order:Shop.getStockOrders()){
			finances.add(new Finance(order.getTotal(),order.getDate(),"Stock Order",true));
		}
		int counter = 0;
		for(Finance finance:Shop.getFinancialRecords()){
			finances.add(finance);
			finance.setId(counter);
			counter ++;
		}
		
		//load the profit / expense values for different dates 
		DateTime now = DateTime.now();
	    DateTime lastMonthDateTime = DateTime.now().minusMonths(1);
	    DateTime monthBeforeLastMonthDateTime = DateTime.now().minusMonths(2);
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    System.out.println("now = "+now.toString());
	    System.out.println("last month = "+lastMonthDateTime.toString());
	    System.out.println("last month-1 = "+monthBeforeLastMonthDateTime.toString());
	    
	    int currentYear = now.getYear();
	    int currentMonth=now.getMonthOfYear();
	    int lastMonth = lastMonthDateTime.getMonthOfYear();
	    int monthBeforeLastMonth = monthBeforeLastMonthDateTime.getMonthOfYear();
	    strCurrentYear = String.valueOf(currentYear);
	    strCurrentMonth = getMonthName(currentMonth);
	    strCurrentWeek = "Current Week";
	    strLastMonth = getMonthName(lastMonth);
	    strMonthBeforeLastMonth = getMonthName(monthBeforeLastMonth);
	    
	    DateTime startDateOfThisMonth = now.withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	    DateTime endDateOfThisMonth = now.plusMonths(1).withDayOfMonth(1).minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	    DateTime startDateOfLastMonth = now.minusMonths(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	    DateTime endDateOfLastMonth = now.withDayOfMonth(1).minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	    DateTime startDateOfMonthBeforeLastMonth = now.minusMonths(2).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	    DateTime endDateOfMonthBwforeLastMonth = now.minusMonths(1).withDayOfMonth(1).minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	    DateTime startDateOfThisWeek = now.dayOfWeek().withMinimumValue().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	    DateTime endDateOfThisWeek = now.dayOfWeek().withMaximumValue().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	    

	    try {
	    	expenseCurrentYear = calculateExpencesForSpecifiedPeriod(sdf.parse("01/01/"+strCurrentYear), sdf.parse("31/12/"+strCurrentYear));
	    	expenseMonthBeforeLastMonth = calculateExpencesForSpecifiedPeriod(startDateOfMonthBeforeLastMonth.toDate(), endDateOfMonthBwforeLastMonth.toDate());
			expenseLastMonth = calculateExpencesForSpecifiedPeriod(startDateOfLastMonth.toDate(), endDateOfLastMonth.toDate());
	    	expenseCurrentMonth = calculateExpencesForSpecifiedPeriod(startDateOfThisMonth.toDate(), endDateOfThisMonth.toDate());
	    	expenseCurrentWeek = calculateExpencesForSpecifiedPeriod(startDateOfThisWeek.toDate(), endDateOfThisWeek.toDate());
	    	
	    	profitCurrentYear = calculateProfitForSpecifiedPeriod(sdf.parse("01/01/"+strCurrentYear), sdf.parse("31/12/"+strCurrentYear));
	    	profitMonthBeforeLastMonth = calculateProfitForSpecifiedPeriod(startDateOfMonthBeforeLastMonth.toDate(), endDateOfMonthBwforeLastMonth.toDate());
	    	profitLastMonth = calculateProfitForSpecifiedPeriod(startDateOfLastMonth.toDate(), endDateOfLastMonth.toDate());
	    	profitCurrentMonth = calculateProfitForSpecifiedPeriod(startDateOfThisMonth.toDate(), endDateOfThisMonth.toDate());
	    	profitCurrentWeek = calculateProfitForSpecifiedPeriod(startDateOfThisWeek.toDate(), endDateOfThisWeek.toDate());
	    }catch(ParseException ex){
	    	ex.printStackTrace();
	    }
	}
	
	public ArrayList<Object[]> getTableData() {
		return tableData;
	}

	public void setTableData(ArrayList<Object[]> tableData) {
		this.tableData = tableData;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public JLabel getLblWrittenReport() {
		return lblWrittenReport;
	}

	public void setLblWrittenReport(JLabel lblWrittenReport) {
		this.lblWrittenReport = lblWrittenReport;
	}

	public class AddExpenseHandler implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				new PopupDialog(ProfitLossPanel.this);
			}
		}
}
