package gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RefineryUtilities;

import data.CustomerOrder;
import data.Finance;
import data.Product;
import data.ProductToOrder;

public class StatisticsPanel extends JPanel {
	 private CategoryDataset dataset ;
	 private JFreeChart chart ;
	 private ChartPanel chartPanel;
	 private double[][] data;
	private Object arrayCurrentOrder;
	
	private ArrayList<Finance> sales = new ArrayList<Finance>();
	ArrayList<ProductToOrder> products = new ArrayList<ProductToOrder>();
	private ProductToOrder order ;
	private int orders;
	
	 
	public StatisticsPanel() { 
		
	   		refresh();
			
	}
	
	public void refresh(){
		ArrayList<String> names = new ArrayList<String>();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		//int total = 0;
		String name="";
		order = new ProductToOrder();
	
//	 		for (Product p: Shop.getProducts()){
//				for(int i = 0; i < Shop.getCustomerOrders().size(); i++){ 
//					for(ProductToOrder pO: Shop.getCustomerOrders().get(i).getProducts()){
//					
//					  		if(p.getName().equalsIgnoreCase(pO.getName())){
//					  			total= pO.getAmount();	
//					  			System.out.println(total);
//					  			dataset.setValue(total, "Sold", pO.getName());
//					}	
//					}
//					}
//			//	break;
//		  }
		
		ArrayList<Integer> totalValues = new ArrayList<Integer>();
	
		for(Product product : Shop.getProducts()){
			totalValues.add(0);
		}

		for(int i=0; i<Shop.getCustomerOrders().size(); i++){
			for(ProductToOrder pO : Shop.getCustomerOrders().get(i).getProducts()){
				//System.out.println(pO.getName()+", "+pO.getAmount());
				int counter = 0;
				for(Product product : Shop.getProducts()){
					//totalValues.get(counter) = totalValues.get(counter) + pO.getAmount();
					if(product.getName().equals(pO.getName())){
						int newTotal = totalValues.get(counter) + pO.getAmount();
						totalValues.set(counter, Integer.valueOf(newTotal));
						System.out.println(product.getName() + " " + counter + " - " + newTotal);
						dataset.setValue(newTotal, "Sold", pO.getName());
					}
					counter++;
				}
				
			}
		}

		JFreeChart chart = ChartFactory.createBarChart("Number of products sold",
		"Products", "Number of products", dataset, PlotOrientation.VERTICAL,
		false, true, false);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 400));
		add(chartPanel);
	}

	
	}
