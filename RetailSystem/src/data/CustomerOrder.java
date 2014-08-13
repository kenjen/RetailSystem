package data;

import java.util.ArrayList;
import java.util.Date;


public class CustomerOrder {
	
	
	private static int nextId;
	private int id;
	private Date creationDate;
	private Customer customer;
	private Staff staff;
	private ArrayList<ProductToOrder> products;
	private double totalGross;
	private double totalNet;
	private boolean complete = false;
	
	
	public CustomerOrder(){
		
	}
	
	public CustomerOrder(Customer customer, Staff staff, ArrayList<ProductToOrder> products) {
		this.creationDate = new Date();				
		nextId++;
		this.id = nextId;
		this.staff=staff;
		this.customer = customer;
		this.products=products;
		double totalNet = 0;
		for(ProductToOrder product:products){
			totalNet+=product.getPrice()*product.getAmount();
		}
		this.totalNet = totalNet;
		this.totalGross = totalNet*1.21;
	}
	
	
  

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
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


	public ArrayList<ProductToOrder> getProducts() {
		return products;
	}


	public void setProducts(ArrayList<ProductToOrder> products) {
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