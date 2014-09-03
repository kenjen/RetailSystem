package gui;

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
import org.jfree.chart.plot.PlotOrientation;
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
	
	
	public StatisticsPanel() { 
		
		setLayout(new MigLayout());
		// create combobox to allow selection of the period 
		String[] items = {"Current week",  "Current month", "Current year","Last week", "Last month"};
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
		endThisMonth = now.plusMonths(1).withDayOfMonth(1).minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		startLastMonth = now.minusMonths(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		endLastMonth = now.withDayOfMonth(1).minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		startThisWeek = now.dayOfWeek().withMinimumValue().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		endThisWeek = now.dayOfWeek().withMaximumValue().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		startLastWeek = now.minusDays(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		endLastWeek = now.withDayOfWeek(1).minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		
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
				false, true, false);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 400));
		add(chartPanel);

	}
	
	private void updateChart() {
		String menuItem = (String)menu.getSelectedItem().toString();

		if(menuItem.equalsIgnoreCase("Current week")){
			dataset.clear();
			totalValues.clear();
			// set the date and display chart 
			for(CustomerOrder c: Shop.getCustomerOrders()){
				if(c.getCreationDate().before(endThisWeek.toDate()) && c.getCreationDate().after(startThisWeek.toDate())){
					generateData(dataset);
					break;
				}
			}
		}	
		else if(menu.getSelectedItem()=="Current month"){
			dataset.clear();
			totalValues.clear();
			// set the date and display chart
			for(CustomerOrder c: Shop.getCustomerOrders()){
				if(c.getCreationDate().before(endThisMonth.toDate()) && c.getCreationDate().after(startThisMonth.toDate())){
					generateData(dataset);
					break;
				}
			}
		}

		else if(menu.getSelectedItem()=="Current year"){
			dataset.clear();
			totalValues.clear();
			// set the date and display chart
			for(CustomerOrder c: Shop.getCustomerOrders()){
				try {
					if(c.getCreationDate().before(sdf.parse("31/12/"+currentYear)) && c.getCreationDate().after(sdf.parse("01/01/"+currentYear))){
						generateData(dataset);
						break;
					}
				} 
				catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		else if(menu.getSelectedItem()=="Last month"){
			dataset.clear();
			totalValues.clear();
			// set the date and display chart
			for(CustomerOrder c: Shop.getCustomerOrders()){
				if(c.getCreationDate().before(endLastMonth.toDate()) && c.getCreationDate().after(startLastMonth.toDate())){
					generateData(dataset);
					break;
				}
			}
		}	
		else if(menu.getSelectedItem()=="Last week"){
			dataset.clear();
			totalValues.clear();
			// set the date and display chart
			for(CustomerOrder c: Shop.getCustomerOrders()){
				if(c.getCreationDate().before(endLastWeek.toDate()) && c.getCreationDate().after(startLastWeek.toDate())){
					generateData(dataset);
					break;
				}
			}
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
	public void generateData(DefaultCategoryDataset dataset){
		String selectedProduct = comboProducts.getSelectedItem().toString();
		String [] arraySplitSelectedProduct = selectedProduct.split(" ");
		int selectedProductID = Integer.parseInt(arraySplitSelectedProduct[arraySplitSelectedProduct.length-1]);
		
		// get the data from customer orders
		int value = 0;
		int value1 = 0;
		for(int i=0; i<Shop.getCustomerOrders().size(); i++){
			for(ProductToOrder pO : Shop.getCustomerOrders().get(i).getProducts()){
				// set up counter for each product
				for(Product product : Shop.getProducts()){
					if(product.getId()==pO.getId() && product.getId() == selectedProductID){
						value += pO.getAmount();
						// upload data to chart
						dataset.setValue(value, "Sold", selectedProduct);
					}
				}
			}
		}
		
		String selectedProduct1 = comboProducts1.getSelectedItem().toString();
		if(!selectedProduct1.equals("")){
			String [] arraySplitSelectedProduct1 = selectedProduct1.split(" ");
			int selectedProduct1ID = Integer.parseInt(arraySplitSelectedProduct1[arraySplitSelectedProduct1.length-1]);
			for(int i=0; i<Shop.getCustomerOrders().size(); i++){
				for(ProductToOrder pO : Shop.getCustomerOrders().get(i).getProducts()){
					for(Product product : Shop.getProducts()){
						if(product.getId()==pO.getId() && product.getId() == selectedProduct1ID){
							value1 += pO.getAmount();
							// upload data to chart
							dataset.setValue(value1, "Sold", selectedProduct1);
						}
					}
				}
			}
		}
	}
}