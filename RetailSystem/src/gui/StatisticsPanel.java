package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	private DateTime startThisMonth;
	private DateTime endThisMonth;
	private DateTime startThisWeek;
	private DateTime endThisWeek;
	private DateTime startLastMonth;
	private DateTime endLastMonth;
	private DateTime startLastWeek;
	private DateTime endLastWeek;
	public StatisticsPanel() { 
		setLayout(new MigLayout());
		// create combobox to allow selection of the period 
		String[] items = {"Current week",  "Current month", "Current year","Last week", "Last month"};
		JComboBox<?> menu = new JComboBox<Object>(items);
		menu.setSelectedIndex(0);
		add(menu);
		
		// create data format
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DateTime now = DateTime.now();
		DateTime.now().minusMonths(1);
		final int currentYear = now.getYear();
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
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		menu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JComboBox<?> menu = (JComboBox<?>)e.getSource();
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

		});

		// create chart
		JFreeChart chart = ChartFactory.createBarChart("Number of products sold",
				"Products", "Number of products", dataset, PlotOrientation.VERTICAL,
				false, true, false);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 400));
		add(chartPanel);

	}
	
	// generate data and upload data to chart
	public void generateData(DefaultCategoryDataset dataset){
		for(Product product : Shop.getProducts()){
			totalValues.add(0);
		}
		// get the data from customer orders
		for(int i=0; i<Shop.getCustomerOrders().size(); i++){
			for(ProductToOrder pO : Shop.getCustomerOrders().get(i).getProducts()){
				// set up counter for each product
				int counter = 0;
				for(Product product : Shop.getProducts()){
					if(product.getName().equals(pO.getName())){
						// get the amount sold and add it up for each product
						int newTotal = totalValues.get(counter) + pO.getAmount();
						totalValues.set(counter, Integer.valueOf(newTotal));
						// upload data to chart
						dataset.setValue(newTotal, "Sold", pO.getName());
					}
					counter++;
				}
			}
		}
	}
}