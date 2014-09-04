package gui;

import java.awt.Color;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;

import data.CustomerOrder;
import data.Product;
import data.ProductToOrder;

public class StatisticsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChartPanel chartPanel;
	ArrayList<ProductToOrder> products = new ArrayList<ProductToOrder>();
	ArrayList<Integer> totalValues = new ArrayList<Integer>();
	String[] arrayofSortedProducts;
	private DateTime startThisMonth;
	private DateTime endThisMonth;
	private DateTime startThisWeek;
	private DateTime endThisWeek;
	private DateTime startLastMonth;
	private DateTime endLastMonth;
	private DateTime startLastWeek;
	private DateTime endLastWeek;
	JComboBox<String> comboProducts;
	JComboBox<String> comboProducts1;
	JComboBox<?> menu;
	final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	int currentYear;
	private DateTime startBeforeLastWeek;
	private DateTime endBeforeLastWeek;
	public StatisticsPanel() { 

		setLayout(new MigLayout());
		// create combobox to allow selection of the period 
		String[] items = {"Current year","Last 3 weeks", "Last 2 months"};
		menu = new JComboBox<Object>(items);
		arrayofSortedProducts = getProductNamesWithId();
		comboProducts = new JComboBox<String>(arrayofSortedProducts);
		String[] combo2ArrayData = new String[arrayofSortedProducts.length+1];
		combo2ArrayData[0] = "";
		
		for(int i=1; i<arrayofSortedProducts.length+1; i++){
			combo2ArrayData[i] = arrayofSortedProducts[i-1];
		}
		comboProducts1 = new JComboBox<String>(combo2ArrayData);
		
		menu.setSelectedIndex(0);
		add(menu);
		add(comboProducts);
		add(comboProducts1,"wrap");

		// create data format

		DateTime now = DateTime.now();
		DateTime.now().minusMonths(1);
		currentYear = now.getYear();
		// initialize date variables
		startThisMonth = now.withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		endThisMonth = now.plusMonths(1).withDayOfMonth(1).minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
		startLastMonth = now.minusMonths(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		endLastMonth = now.withDayOfMonth(1).minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
		startThisWeek = now.dayOfWeek().withMinimumValue().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		endThisWeek = now.dayOfWeek().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
		startLastWeek = now.dayOfWeek().withMinimumValue().minusDays(7).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		endLastWeek = startLastWeek.withDayOfWeek(7).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
		startBeforeLastWeek = now.dayOfWeek().withMinimumValue().minusDays(14).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		endBeforeLastWeek = startBeforeLastWeek.withDayOfWeek(7).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);

		// add action listener to combo box items
		menu.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e){
				updateChart();
			}

		});

		comboProducts.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					updateChart();
				}
			}
		});
		
		comboProducts1.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					updateChart();
				}
			}
		});

		// create chart
		JFreeChart chart = ChartFactory.createBarChart("Number of products sold",
				"Products", "Number of products", dataset, PlotOrientation.VERTICAL,
				true, true, false);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 400));
		CategoryPlot categoryPlot = chart.getCategoryPlot();
		BarRenderer br = (BarRenderer) categoryPlot.getRenderer();
		br.setMaximumBarWidth(.15);
		add(chartPanel);

	}

	private void updateChart() {
		menu.getSelectedItem().toString();
		ArrayList<CustomerOrder> cusOrders = new ArrayList<CustomerOrder>();

		if(menu.getSelectedItem()=="Current year"){
			dataset.clear();
			totalValues.clear();
			// set the date and display chart
			for(CustomerOrder c: Shop.getCustomerOrders()){
				try {
					if(c.getCreationDate().before(sdf.parse("31/12/"+currentYear)) && c.getCreationDate().after(sdf.parse("01/01/"+currentYear))){
						cusOrders.add(c);
					}
				} 
				catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			generateData(dataset, cusOrders);
			
		}

		//TODO
		else if(menu.getSelectedItem()=="Last 2 months"){
			dataset.clear();
			// set the date and display chart

			generateMonthlyData(dataset);
		}	
		else if(menu.getSelectedItem()=="Last 3 weeks"){
			dataset.clear();
				generateWeeklyData(dataset);
		}	
	}

	private String[] getProductNamesWithId() {
		ArrayList<String> toPopulate = new ArrayList<String>();
		for (Product p : Shop.getProducts()){
			String name = p.getName();
			int id = p.getId();
			toPopulate.add(name + " " + id);
		}

		String[] returnable = new String[toPopulate.size()];
		for(int i=0; i< returnable.length; i++){
			returnable[i] = toPopulate.get(i);
		}
		Arrays.sort(returnable);
		return returnable;
	}

	// generate data and upload data to chart

	public void generateData(DefaultCategoryDataset dataset, ArrayList<CustomerOrder> cusOrders ){

		String selectedProduct = comboProducts.getSelectedItem().toString();
		String [] arraySplitSelectedProduct = selectedProduct.split(" ");
		int selectedProductID = Integer.parseInt(arraySplitSelectedProduct[arraySplitSelectedProduct.length-1]);

		// get the data from customer orders
		int value = 0;
		int value1 = 0;
		
		for(int i=0; i<cusOrders.size(); i++){
			for(ProductToOrder pO : cusOrders.get(i).getProducts()){
				// set up counter for each product
				//for(Product product : Shop.getProducts()){
				if(pO.getId() == selectedProductID){

					value += pO.getAmount();
					// upload data to chart
					dataset.setValue(value, "Current year", selectedProduct);

				}
			}
		}
		String selectedProduct1 = comboProducts1.getSelectedItem().toString();
		if(!selectedProduct1.equals("")){
			String [] arraySplitSelectedProduct1 = selectedProduct1.split(" ");
			int selectedProduct1ID = Integer.parseInt(arraySplitSelectedProduct1[arraySplitSelectedProduct1.length-1]);
			for(int i=0; i<cusOrders.size(); i++){
				for(ProductToOrder pO : cusOrders.get(i).getProducts()){
					//for(Product product : cusOrders.){
						if(pO.getId() == selectedProduct1ID){
							value1 += pO.getAmount();
							
							dataset.setValue(value1, "Current year", selectedProduct1);		
							}
							
							
						}
				}
			}
	}
	
	// generate last two months data for chart
	public void generateMonthlyData(DefaultCategoryDataset dataset ){
		// store the data from customer orders in new array list
		ArrayList<CustomerOrder> cusOrdersThis = new ArrayList<CustomerOrder>();
		ArrayList<CustomerOrder> cusOrdersLast = new ArrayList<CustomerOrder>();
		
		for( CustomerOrder c:	Shop.getCustomerOrders()){
			if(c.getCreationDate().before(endThisMonth.toDate()) && c.getCreationDate().after(startThisMonth.toDate())){
				cusOrdersThis.add(c);
			}
			else if(c.getCreationDate().before(endLastMonth.toDate()) && c.getCreationDate().after(startLastMonth.toDate())){
				cusOrdersLast.add(c);
				
			}
			
		}
		
		// get the first selected product and its id
		String selectedProduct = comboProducts.getSelectedItem().toString();
		String [] arraySplitSelectedProduct = selectedProduct.split(" ");
		int selectedProductID = Integer.parseInt(arraySplitSelectedProduct[arraySplitSelectedProduct.length-1]);

		// create variables to store the data set values to compare
		int value = 0;
		int value1 = 0;
		int value2 = 0;
		int value3 = 0;
		
		// get the total amount sold for last month
		for(int i=0; i<cusOrdersLast.size(); i++){
			for(ProductToOrder pO : cusOrdersLast.get(i).getProducts()){
				if(pO.getId() == selectedProductID){
					value1 += pO.getAmount();
					// upload data to chart
					dataset.setValue(value1, "Last month", selectedProduct);
				}
			}
		}
		// get the total amount sold for this month
		for(int i=0; i<cusOrdersThis.size(); i++){
			for(ProductToOrder pO : cusOrdersThis.get(i).getProducts()){
				if(pO.getId() == selectedProductID){
					value += pO.getAmount();
					// upload data to chart
					dataset.setValue(value, "This month", selectedProduct);
				}
			}
		}
		
		// get the second selected product and its id
		String selectedProduct1 = comboProducts1.getSelectedItem().toString();
		if(!selectedProduct1.equals("")){
			String [] arraySplitSelectedProduct1 = selectedProduct1.split(" ");
			int selectedProduct1ID = Integer.parseInt(arraySplitSelectedProduct1[arraySplitSelectedProduct1.length-1]);
			// get the total amount sold for last month
			for(int i=0; i<cusOrdersLast.size(); i++){
				for(ProductToOrder pO : cusOrdersLast.get(i).getProducts()){
					if(pO.getId() == selectedProduct1ID){
						value2 += pO.getAmount();
						dataset.setValue(value2, "Last month", selectedProduct1);		
					}
				}
			}
			// get the total amount sold for this month
			for(int i=0; i<cusOrdersThis.size(); i++){
				for(ProductToOrder pO : cusOrdersThis.get(i).getProducts()){
					if(pO.getId() == selectedProduct1ID){
						value3 += pO.getAmount();
						dataset.setValue(value3, "This month", selectedProduct1);		
					}
				}
			}
		}
	}
	
	// generate three weeks data for chart
	public void generateWeeklyData(DefaultCategoryDataset dataset ){
		// store the data from customer orders in new array list
		ArrayList<CustomerOrder> cusOrdersThis = new ArrayList<CustomerOrder>();
		ArrayList<CustomerOrder> cusOrdersLast = new ArrayList<CustomerOrder>();
		ArrayList<CustomerOrder> cusOrdersBeforeLast = new ArrayList<CustomerOrder>();
		
		for( CustomerOrder c:	Shop.getCustomerOrders()){
			if(c.getCreationDate().before(endThisWeek.toDate()) && c.getCreationDate().after(startThisWeek.toDate())){
				cusOrdersThis.add(c);
			}
			else if(c.getCreationDate().before(endLastWeek.toDate()) && c.getCreationDate().after(startLastWeek.toDate())){
				cusOrdersLast.add(c);
				
			}
			else if(c.getCreationDate().before(endBeforeLastWeek.toDate()) && c.getCreationDate().after(startBeforeLastWeek.toDate())){
				cusOrdersBeforeLast.add(c);
				
			}
			
		}
		// get the first selected product and its id
		String selectedProduct = comboProducts.getSelectedItem().toString();
		String [] arraySplitSelectedProduct = selectedProduct.split(" ");
		int selectedProductID = Integer.parseInt(arraySplitSelectedProduct[arraySplitSelectedProduct.length-1]);

		// create variables to store the data set values to compare
		int value = 0;
		int value1 = 0;
		int value2 = 0;
		int value3 = 0;
		int value4 = 0;
		int value5 = 0;
		// get the total amount sold for last second week
		for(int i=0; i<cusOrdersBeforeLast.size(); i++){
			for(ProductToOrder pO : cusOrdersBeforeLast.get(i).getProducts()){
				if(pO.getId() == selectedProductID){
					value2 += pO.getAmount();
					// upload data to chart
					dataset.setValue(value2, "Second last week", selectedProduct);
				}
			}
		}
		// get the total amount sold for last week
		for(int i=0; i<cusOrdersLast.size(); i++){
			for(ProductToOrder pO : cusOrdersLast.get(i).getProducts()){
				if(pO.getId() == selectedProductID){
					value1 += pO.getAmount();
					// upload data to chart
					dataset.setValue(value1, "Last week", selectedProduct);
				}
			}
		}
		// get the total amount sold for this week
		for(int i=0; i<cusOrdersThis.size(); i++){
			for(ProductToOrder pO : cusOrdersThis.get(i).getProducts()){
				if(pO.getId() == selectedProductID){
					value += pO.getAmount();
					// upload data to chart
					dataset.setValue(value, "This week", selectedProduct);
				}
			}
		}
		// get the second selected product and its id
		String selectedProduct1 = comboProducts1.getSelectedItem().toString();
		if(!selectedProduct1.equals("")){
			String [] arraySplitSelectedProduct1 = selectedProduct1.split(" ");
			int selectedProduct1ID = Integer.parseInt(arraySplitSelectedProduct1[arraySplitSelectedProduct1.length-1]);
			// get the total amount sold for last second week
			for(int i=0; i<cusOrdersBeforeLast.size(); i++){
				for(ProductToOrder pO : cusOrdersBeforeLast.get(i).getProducts()){
					if(pO.getId() == selectedProduct1ID){
						value5 += pO.getAmount();
						dataset.setValue(value5, "Second last week", selectedProduct1);		
					}
				}
			}
			// get the total amount sold for last week
			for(int i=0; i<cusOrdersLast.size(); i++){
				for(ProductToOrder pO : cusOrdersLast.get(i).getProducts()){
					if(pO.getId() == selectedProduct1ID){
						value4 += pO.getAmount();
						dataset.setValue(value4, "Last week", selectedProduct1);		
					}
				}
			}
			// get the total amount sold for this week
			for(int i=0; i<cusOrdersThis.size(); i++){
				for(ProductToOrder pO : cusOrdersThis.get(i).getProducts()){
					if(pO.getId() == selectedProduct1ID){
						value3 += pO.getAmount();
						// upload data to chart
						dataset.setValue(value3, "This week", selectedProduct1);
					}
				}
			}
		}
	}
}
