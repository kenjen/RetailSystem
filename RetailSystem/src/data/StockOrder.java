package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StockOrder {
	
	int id;
	Date date;
	double total;
	ArrayList<Product> products = new ArrayList<Product>();
	Staff staff;
	Date expectedDeliveryDate;

	public StockOrder(int id, Date date, double total, ArrayList<Product> products, Staff staff) {
		this.id = id;
		this.date = date;
		this.total = total;
		this.products = products;
		this.staff = staff;
		/*
		 * If all products in stock expected delivery date is two days from date
		 */
	}

		//How To use dates
		/*SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		date = new Date();
		try {
			date = sd.parse("22/06/2012 16:12");
		} catch (ParseException e) {
			e.printStackTrace();
		}*/

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
