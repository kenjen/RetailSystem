package data;

import java.util.ArrayList;
import java.util.Date;

public class StockOrder {
	
	private static int nextId = 100;
	
	private int id;
	private Date date;
	private double total;
	private ArrayList<Product> products;
	private ArrayList<String> productAmounts;
	private Staff staff;
	private Date expectedDeliveryDate;

	public StockOrder(Date date, ArrayList<Product> products, ArrayList<String> amountToOrder, Staff staff) {
		this.id = nextId;
		nextId++;
		int totalProd = 0;
		for(Product product : products){
			totalProd++;
		}
		this.productAmounts = amountToOrder;
		this.total = totalProd;
		this.date = date;
		this.total = total;
		this.products = products;
		this.staff = staff;
	}	
	
	public String getInvoice(){
		String invoice = "\n \n" + "****** INVOICE ******" + "\n";
		invoice = invoice + "Ordered by " + staff.getName() + "\n" + "Date - " + date.toString() + "\n";
		int i=0;
		for(Product product : products){
			invoice = invoice + Integer.parseInt(productAmounts.get(i)) + "      " + product.getName() + "\n";
			i++;
		}
		return invoice;
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
