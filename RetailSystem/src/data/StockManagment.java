package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StockManagment {
	
	ArrayList<Product> products;
	ArrayList<Product> productsToOrder = new ArrayList<Product>();
	Staff staff;
	ArrayList<StockOrder> stockOrders = new ArrayList<StockOrder>();
	
	
	public StockManagment(ArrayList<Product> products, Staff staff) {
		this.products = products;
		this.staff = staff;
	}
	
	public StockOrder addOrder(ArrayList<Product> products, Staff staff){
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		try {
			date = sd.parse("22/06/2012 16:12");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		StockOrder order = new StockOrder(date, productsToOrder, staff);
		stockOrders.add(order);
		return order;
		
	}
	
}
