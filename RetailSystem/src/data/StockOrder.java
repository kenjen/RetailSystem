package data;

import gui.Shop;

import java.util.ArrayList;
import java.util.Date;

public class StockOrder {
	
	private static int nextId = 100;
	
	private int id;
	private Date date;
	private double total;
	private Staff staff;
	private Date expectedDeliveryDate;
	private ArrayList<ProductToOrder> productsToOrder;
	private boolean completed = false;

	public StockOrder(Date date, ArrayList<ProductToOrder> productsToOrder, Staff staff) {
		this.id = nextId;
		nextId++;
		this.date = date;
		this.staff = staff;
		this.productsToOrder = productsToOrder;
		double tempTotal = 0;
		for(ProductToOrder product:productsToOrder){
			tempTotal +=product.getPrice()*product.getAmount();
		}
		this.total=tempTotal;
		this.completed=false;
	}	
	
	public StockOrder(ArrayList<ProductToOrder> products, Staff staff){
		productsToOrder = products;
		this.staff=staff;
		this.date = new Date();
		nextId++;
		this.id=nextId;
		double tempTotal = 0;
		for(ProductToOrder product:products){
			tempTotal +=product.getPrice()*product.getAmount();
		}
		this.total=tempTotal;
		this.completed=false;
	}
	
	/*public String getInvoice(){
		//TODO individual price, total price
		String invoice = "\n \n" + "****** INVOICE ******" + "\n";
		invoice = invoice + "Ordered by " + staff.getName() + "\n" + "Date - " + date.toString() + "\n";
		int i=0;
		for(StockOrder stockOrder : Shop.getStockOrders()){
			invoice = invoice + Integer.parseInt(productAmounts.get(i)) + "      " + product.getName() + "\n";
			i++;
		}
		return invoice;
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

	public ArrayList<ProductToOrder> getProductsToOrder() {
		return productsToOrder;
	}

	public void setProductsToOrder(ArrayList<ProductToOrder> productsToOrder) {
		this.productsToOrder = productsToOrder;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

}
