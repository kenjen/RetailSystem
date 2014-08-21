package gui;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;

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

import data.CustomerOrder;
import data.Finance;
import data.StockOrder;

public class ProfitLossPanel extends JPanel{
	private ArrayList<Finance> finances = new ArrayList<Finance>();
	private static final long serialVersionUID = 1L;

	public ProfitLossPanel() {
		setLayout(new GridBagLayout());
		
		//generate profit/loss report
		for(CustomerOrder order:Shop.getCustomerOrders()){
			finances.add(new Finance(order.getTotalNet(),order.getCreationDate(),"Customer Order",false));
		}
		for(StockOrder order:Shop.getStockOrders()){
			finances.add(new Finance(order.getTotal(),order.getDate(),"Stock Order",true));
		}
		
		final CategoryDataset chartData = createDataset1();
	    // create the chart...
	    final JFreeChart chart = ChartFactory.createBarChart(
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
	    chart.setBackgroundPaint(Color.white);
//	    chart.getLegend().setAnchor(Legend.SOUTH);

	    // get a reference to the plot for further customisation...
	    final CategoryPlot plot = chart.getCategoryPlot();
	    plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
	    plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

	    final CategoryAxis domainAxis = plot.getDomainAxis();
	    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
	    final ValueAxis axis2 = new NumberAxis("Secondary");
	    plot.setRangeAxis(1, axis2);

	    final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
	    renderer2.setToolTipGenerator(new StandardCategoryToolTipGenerator());
	    plot.setRenderer(1, renderer2);
	    plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
	    // OPTIONAL CUSTOMISATION COMPLETED.

	    // add the chart to a panel...
	    final ChartPanel chartPanel = new ChartPanel(chart);
	    //chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    add(chartPanel);


		
	}
	
	/**
	 * Creates a sample dataset.
	 *
	 * @return  The dataset.
	 */
	private CategoryDataset createDataset1() {

	    // row keys...
	    final String expense = "Expense";
	    final String profit = "Profit";

	    
	    
	    
	    
	    
	    

	    
	    
	    
	    
	    DateTime now = DateTime.now();
	    DateTime lastMonthDateTime = DateTime.now().minusMonths(1);
	    DateTime monthBeforeLastMonthDateTime = DateTime.now().minusMonths(2);
	    
	    
	    
	    
	    // column keys...
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    System.out.println("now = "+now.toString());
	    System.out.println("last month = "+lastMonthDateTime.toString());
	    System.out.println("last month-1 = "+monthBeforeLastMonthDateTime.toString());
	    
	    int currentYear = now.getYear();
	    int currentMonth=now.getMonthOfYear();
	    int lastMonth = lastMonthDateTime.getMonthOfYear();
	    int monthBeforeLastMonth = monthBeforeLastMonthDateTime.getMonthOfYear();
	    int currentWeek = now.getWeekyear();
	    String strCurrentYear = String.valueOf(currentYear);
	    String strCurrentMonth = getMonthName(currentMonth);
	    String strCurrentWeek = "Current Week";
	    String strLastMonth = getMonthName(lastMonth);
	    String strMonthBeforeLastMonth = getMonthName(monthBeforeLastMonth);
	    
	    DateTime startDateOfThisMonth = now.withDayOfMonth(1);
	    DateTime endDateOfThisMonth = now.plusMonths(1).minusDays(1);
	    DateTime startDateOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
	    DateTime endDateOfLastMonth = now.withDayOfWeek(1).minusDays(1);
	    DateTime startDateOfMonthBeforeLastMonth = now.minusMonths(2).withDayOfMonth(1);
	    DateTime endDateOfMonthBwforeLastMonth = now.minusMonths(1).withDayOfWeek(1).minusDays(1);
	    

	    // create the dataset...
	    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	    try {
		dataset.addValue(calculateExpencesForSpecifiedPeriod(sdf.parse("01/01/"+strCurrentYear), sdf.parse("31/12/"+strCurrentYear)), expense, strCurrentYear);
		dataset.addValue(calculateExpencesForSpecifiedPeriod(startDateOfMonthBeforeLastMonth.toDate(), endDateOfMonthBwforeLastMonth.toDate()), expense, strMonthBeforeLastMonth);
		dataset.addValue(calculateExpencesForSpecifiedPeriod(startDateOfLastMonth.toDate(), endDateOfLastMonth.toDate()), expense, strLastMonth);
		dataset.addValue(calculateExpencesForSpecifiedPeriod(startDateOfThisMonth.toDate(), endDateOfThisMonth.toDate()), expense, strCurrentMonth);
	    //TODO
		dataset.addValue(500, expense, strCurrentWeek);

	    dataset.addValue(calculateProfitForSpecifiedPeriod(sdf.parse("01/01/"+strCurrentYear), sdf.parse("31/12/"+strCurrentYear)), profit, strCurrentYear);
	    dataset.addValue(calculateProfitForSpecifiedPeriod(startDateOfMonthBeforeLastMonth.toDate(), endDateOfMonthBwforeLastMonth.toDate()), profit, strMonthBeforeLastMonth);
	    dataset.addValue(calculateProfitForSpecifiedPeriod(startDateOfLastMonth.toDate(), endDateOfLastMonth.toDate()), profit, strLastMonth);
	    dataset.addValue(calculateProfitForSpecifiedPeriod(startDateOfThisMonth.toDate(), endDateOfThisMonth.toDate()), profit, strCurrentMonth);
	    //TODO
	    dataset.addValue(300, profit, strCurrentWeek);
	    
	    } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return dataset;

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
	
	//returns the expences for specified period
		public double calculateExpencesForSpecifiedPeriod(Date start, Date end){
			double total = 0;
			for(Finance finance:finances){
				if(finance.isExpense()==true && finance.getDate().before(end) && finance.getDate().after(start)){
					total+=finance.getAmount();
				}
			}
			return total;
			
		}

}
