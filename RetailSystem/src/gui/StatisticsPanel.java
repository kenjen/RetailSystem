package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;

import data.CustomerOrder;
import data.Product;
import data.ProductToOrder;

public class StatisticsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	JLabel lblDatesTitle, label1, label2, lblProductsTitle, lblTextualStatistic;
	private String[] comboDatesData, comboProductsAData, comboProductsBData;
	private DateTime startThisMonth, endThisMonth, startThisWeek, endThisWeek, startLastMonth, endLastMonth, startLastWeek, endLastWeek, endBeforeLastWeek, startBeforeLastWeek;
	private DateTime selectedDateStart, selectedDateEnd;
	private UtilDateModel modelStart = new UtilDateModel();
	private JDatePanelImpl datePanelStart = new JDatePanelImpl(modelStart);
	private JDatePickerImpl datePickerStart = new JDatePickerImpl(datePanelStart);
	private UtilDateModel modelEnd = new UtilDateModel();
	private JDatePanelImpl datePanelEnd = new JDatePanelImpl(modelEnd);
	private JDatePickerImpl datePickerEnd = new JDatePickerImpl(datePanelEnd);
	private JComboBox<String> comboProductsA, comboProductsB, comboDates;
	private JButton btnDisplayProductsFromDatePicker = new JButton("Generate");
	private final DefaultCategoryDataset defaultCategoryDataSet = new DefaultCategoryDataset();
	private ChartPanel chartPanel;
	private JFreeChart chart;
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private int currentYear, productASalesCount, productBSalesCount;
	private ActionListener comboDatesListener, btnDisplayProductsFromDatePickerListener;
	private boolean isProductAInChart = false, isProductBInChart = false;
	final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	JComboBox<String> comboProducts;
	JComboBox<String> comboProducts1;

	//Constructor
	public StatisticsPanel() {
		
		generateComboBoxesData();
		initializeDateVariablesForChartData();
		createPanelComponents();
		createListenersForComponents();
		comboDates.addActionListener(comboDatesListener);
		comboProductsA .addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					updateChart();
				}
			}
	
		});
		
		comboProductsB .addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					updateChart();
				}
			}
		});
		btnDisplayProductsFromDatePicker.addActionListener(btnDisplayProductsFromDatePickerListener);
		
		//set initial values for user editable fields
		comboDates.setSelectedIndex(0);
		datePickerEnd.getModel().setValue(null);
		datePickerStart.getModel().setValue(null);
		

		//add components to panel
		setLayout(new MigLayout());
		add(lblProductsTitle, ",wrap ");
		add(comboProductsA, "span, split");
		add(comboProductsB, "wrap");
		add(lblDatesTitle);
		add(comboDates,"span, wrap, gapy 20");
		add(label1);
		add(datePickerStart, "split 3");
		add(label2);
		add(datePickerEnd, "wrap");
		add(btnDisplayProductsFromDatePicker,"wrap, gapy 30");
		add(chartPanel, "span, split");
		add(lblTextualStatistic,"aligny top");
	}//end constructor

	private void createListenersForComponents() {
		// add action listener to combo box items
			
		//clear datepickers if a nonNull value has been selected
		comboDatesListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboDates.getSelectedIndex() != 0){
					updateChart();
					datePickerEnd.getModel().setValue(null);
					datePickerStart.getModel().setValue(null);
					lblTextualStatistic.setText("");
				}
			}
		};
		
		btnDisplayProductsFromDatePickerListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//if datepickers have value, then generateData using these 2 dates
				//otherwise run the updateChart method that is dealing only with periods based on the selected value in comboDates
				if (datePickerStart.getModel().getValue() != null && datePickerEnd.getModel().getValue() != null) {
					selectedDateStart = new DateTime((Date) datePickerStart.getModel().getValue()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
					selectedDateEnd = new DateTime(datePickerEnd.getModel().getValue()).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
					comboDates.getModel().setSelectedItem(comboDates.getItemAt(0));
					
					//validate dates
					if (selectedDateEnd.isBefore(selectedDateStart.getMillis()) || selectedDateStart.isAfterNow()) {
						JOptionPane.showMessageDialog(StatisticsPanel.this, "Invalid dates selected");
						datePickerEnd.getModel().setValue(null);
						datePickerStart.getModel().setValue(null);
						return;
					}
					
					//get relevant orders
					ArrayList<CustomerOrder> cusOrders = new ArrayList<CustomerOrder>();
					for (CustomerOrder c : Shop.getCustomerOrders()) {
						if (c.getCreationDate().before(selectedDateEnd.toDate()) && c.getCreationDate().after(selectedDateStart.toDate()))
							cusOrders.add(c);
					}
					
					defaultCategoryDataSet.clear();
					generateData(cusOrders);
				} else {
					updateChart();
				}
			}
		};
		
	}//end createListenersForComponents()

	private void createPanelComponents() {
		comboDates = new JComboBox<String>(comboDatesData);
		comboProductsA = new JComboBox<String>(comboProductsAData);
		comboProductsB = new JComboBox<String>(comboProductsBData);

		lblDatesTitle = new JLabel("Date period:");
		   lblDatesTitle.setFont(Shop.TITLE_FONT);
		   lblDatesTitle.setForeground(Shop.TITLE_COLOR);
		label1 = new JLabel("Or pick a period between:");
		label2 = new JLabel(" and ");
		lblProductsTitle = new JLabel("Choose product(s): ");
		   lblProductsTitle.setForeground(Shop.TITLE_COLOR);
		   lblProductsTitle.setFont(Shop.TITLE_FONT);
		lblTextualStatistic = new JLabel();

		chart = ChartFactory.createBarChart("Number of products sold", "Products", "Number of products",
				defaultCategoryDataSet, PlotOrientation.VERTICAL, true, true, false);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 430));
		// set dimension for bars
		CategoryPlot categoryPlot = chart.getCategoryPlot();
		BarRenderer br = (BarRenderer) categoryPlot.getRenderer();
		br.setMaximumBarWidth(.15);
		// display values above bars
		br.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		br.setBaseItemLabelsVisible(true);
	}

	private void initializeDateVariablesForChartData() {
		DateTime now = DateTime.now();
		currentYear = now.getYear();
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
	}

	private void generateComboBoxesData() {
		comboDatesData = new String[] { "", "Last 3 weeks","Last 2 months", "Current year"};
		comboProductsAData = getProductNamesWithId();
		comboProducts = new JComboBox<String>(comboProductsAData );
		
		// add an empty item at the beginning of the array
		comboProductsBData = new String[comboProductsAData.length + 1];
		comboProductsBData[0] = "";
		for (int i = 1; i < comboProductsAData.length + 1; i++) {
			comboProductsBData[i] = comboProductsAData[i - 1];
		}
		comboProducts1 = new JComboBox<String>(comboProductsBData);
	}

	/**
	 * Based on selected value from comboDates, extracts all ordered products in an arrayList from CustomerOrders that correspond to
	 * the selected period. Note: The chart data is cleared even if no products have been found.
	 */
	private void updateChart() {
	//	String comboDatesItem = (String) comboDates.getSelectedItem().toString();
		ArrayList<CustomerOrder> cusOrders = new ArrayList<CustomerOrder>();

		if (comboDates.getSelectedItem() == "Current year") {
			defaultCategoryDataSet.clear();
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
				generateDataD(defaultCategoryDataSet, cusOrders);
			}

			else if((comboDates.getSelectedItem()=="Last 2 months")){
				defaultCategoryDataSet.clear();
				// set the date and display chart

				generateMonthlyData(defaultCategoryDataSet);
			}	
			else if(comboDates.getSelectedItem()=="Last 3 weeks"){
				defaultCategoryDataSet.clear();
				generateWeeklyData(defaultCategoryDataSet);
			}	
			
	}//end updateChart()

	/**
	 * Based on all products in Shop.getProducts(), it returns a String array of concatenated product name + id
	 * @return
	 */
	private String[] getProductNamesWithId() {
		ArrayList<String> toPopulate = new ArrayList<String>();
		for (Product p : Shop.getProducts()) {
			String name = p.getName();
			int id = p.getId();
			toPopulate.add(name + " " + id);
		}

		String[] returnable = new String[toPopulate.size()];
		for (int i = 0; i < returnable.length; i++) {
			returnable[i] = toPopulate.get(i);
		}
		Arrays.sort(returnable);
		return returnable;
	}//end getProductNamesWithId()

	/**
	 * Updates chart data based on selected product from comboProductsA and comboProductsB
	 * @param cusOrders represents the data which is used in calculating the chart values.
	 */
	public void generateData(ArrayList<CustomerOrder> cusOrders) {
		String selectedProduct = comboProductsA.getSelectedItem().toString();
		String[] arraySplitSelectedProduct = selectedProduct.split(" ");
		int selectedProductID = Integer.parseInt(arraySplitSelectedProduct[arraySplitSelectedProduct.length - 1]);
		productASalesCount = 0;
		productBSalesCount = 0;
		isProductAInChart = false;
		isProductBInChart = false;
		lblTextualStatistic.setText("");

		// Add the chart column for first combobox selected product, if any has
		// been ordered in the selected period
		for (int i = 0; i < cusOrders.size(); i++) {
			for (ProductToOrder pO : cusOrders.get(i).getProducts()) {
				if (pO.getId() == selectedProductID) {
					productASalesCount += pO.getAmount();
					// update chart data
					defaultCategoryDataSet.setValue(productASalesCount, "Sold", selectedProduct);
					isProductAInChart = true;
				}
			}
		}

		// Add the chart column for second combobox selected product, if any has
		// been ordered in the selected period
		String selectedProduct1 = comboProductsB.getSelectedItem().toString();
		if (!selectedProduct1.equals("")) {
			String[] arraySplitSelectedProduct1 = selectedProduct1.split(" ");
			int selectedProduct1ID = Integer
					.parseInt(arraySplitSelectedProduct1[arraySplitSelectedProduct1.length - 1]);
			for (int i = 0; i < cusOrders.size(); i++) {
				for (ProductToOrder pO : cusOrders.get(i).getProducts()) {
					if (pO.getId() == selectedProduct1ID) {
						productBSalesCount += pO.getAmount();
						// update chart data
						defaultCategoryDataSet.setValue(productBSalesCount, "Sold", selectedProduct1);
						isProductBInChart = true;
					}
				}
			}
		}
		displayTextualStatistic();
	}//end generateData()
	
	public void displayTextualStatistic(){
		if(defaultCategoryDataSet.getColumnCount() == 0)
			lblTextualStatistic.setText("We did not sell any of the speciffied product(s) in this period");
		else if(isProductAInChart == false && isProductBInChart==true)
			lblTextualStatistic.setText("<html>We did not sell any "+comboProductsA.getSelectedItem().toString()+" in this period.<br>"+
					comboProductsB.getSelectedItem()+", sales count is: "+productBSalesCount+"</html>");
		else if(isProductAInChart == true && isProductBInChart == false && comboProductsB.getSelectedItem() != "")
			lblTextualStatistic.setText("<html>We did not sell any "+comboProductsB.getSelectedItem().toString()+" in this period.<br>"+
					comboProductsA.getSelectedItem()+", sales count is: "+productASalesCount+"</html>");
		else if(comboProductsB.getSelectedItem() == "")
			lblTextualStatistic.setText(comboProductsA.getSelectedItem()+", sales count is: "+productASalesCount);
		else
			lblTextualStatistic.setText("<html>"+comboProductsA.getSelectedItem()+", sales count is: "+productASalesCount+"<br>"+
					comboProductsB.getSelectedItem()+", sales count is: "+productBSalesCount+"</html>");
	}	
	
	
	
	
	// generate data and upload data to chart

		public void generateDataD(DefaultCategoryDataset dataset, ArrayList<CustomerOrder> cusOrders ){

			String selectedProduct = comboProductsA.getSelectedItem().toString();
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
			String selectedProduct1 = comboProductsB.getSelectedItem().toString();
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
			String selectedProduct = comboProductsA.getSelectedItem().toString();
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
			String selectedProduct1 = comboProductsB.getSelectedItem().toString();
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
			String selectedProduct = comboProductsA.getSelectedItem().toString();
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
						dataset.setValue(value2, "Two weeks ago", selectedProduct);
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
			String selectedProduct1 = comboProductsB.getSelectedItem().toString();
			if(!selectedProduct1.equals("")){
				String [] arraySplitSelectedProduct1 = selectedProduct1.split(" ");
				int selectedProduct1ID = Integer.parseInt(arraySplitSelectedProduct1[arraySplitSelectedProduct1.length-1]);
				// get the total amount sold for last second week
				for(int i=0; i<cusOrdersBeforeLast.size(); i++){
					for(ProductToOrder pO : cusOrdersBeforeLast.get(i).getProducts()){
						if(pO.getId() == selectedProduct1ID){
							value5 += pO.getAmount();
							dataset.setValue(value5, "Two weeks ago", selectedProduct1);		
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
	
	
}//end Class
