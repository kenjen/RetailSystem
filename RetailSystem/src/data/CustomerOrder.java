package data;

import java.util.ArrayList;


public class CustomerOrder {
	
	

	private int id;
	private String creationDate;
	private Customer customer;
	private Staff staff;
	private ArrayList<Product> products;
	private double totalGross;
	private double totalNet;
	private boolean complete = false;
	
	
	
	
	public CustomerOrder(int id, String creationDate, double totalGross, double totalNet) {
		this.id = id;
		this.creationDate = creationDate;				
		this.totalGross = totalGross;
		this.totalNet = totalNet;
		
		
		
	}
	
	
  

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Staff getStaff() {
		return staff;
	}


	public void setStaff(Staff staff) {
		this.staff = staff;
	}


	public ArrayList<Product> getProducts() {
		return products;
	}


	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}


	public double getTotalGross() {
		return totalGross;
	}


	public void setTotalGross(double totalGross) {
		this.totalGross = totalGross;
	}


	public double getTotalNet() {
		return totalNet;
	}


	public void setTotalNet(double totalNet) {
		this.totalNet = totalNet;
	}




	public boolean isComplete() {
		return complete;
	}




	public void setComplete(boolean complete) {
		this.complete = complete;
	}

}