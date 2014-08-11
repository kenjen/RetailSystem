package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StockOrder {
	
	private static int nextId = 100;
	
	private int id;
	private Date date;
	private double total;
	private ArrayList<Product> products;
	private Staff staff;
	private Date expectedDeliveryDate;

	public StockOrder(Date date, ArrayList<Product> products, Staff staff) {
		this.id = nextId;
		nextId++;
		int totalProd = 0;
		for(Product product : products){
			totalProd++;
		}
		this.total = totalProd;
		this.date = date;
		this.total = total;
		this.products = products;
		this.staff = staff;
		/*
		 * If all products in stock expected delivery date is two days from date
		 */
		//expectedDeliveryDate = date.
		String temp = date.toString();
		//int t = temp.charAt(1)
	}

		//How To use dates
		/*SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		date = new Date();
		try {
			date = sd.parse("22/06/2012 16:12");
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
	
	public void printInvoice(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Date getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

}
