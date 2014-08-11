package gui;

import java.util.ArrayList;

import data.Customer;
import data.Staff;
import data.Supplier;
import data.Product;
import data.StockOrder;
import data.CustomerOrder;

public static void main(String[] args) {
	new Shop();
}

public class Shop {
	private ArrayList<Staff> staffMembers;
	private ArrayList<Customer> customers;
	private ArrayList<Supplier> suppliers;
	private ArrayList<Product> products;
	private ArrayList<StockOrder> stockOrders;
	private ArrayList<CustomerOrder> customerOrders;
	

	public Shop() {
		//populate data
		populateCustomers();
		populateSuppliers();
		populateStaffMembers();
		populateProducts();
		populateStockOrders();
		populateCustomerOrders();
		
		//run login
		Login login = new Login();
		login.drawFrame();
		
	}

	public void populateCustomers(){
		
	}
	
	public void populateSuppliers(){
		
	}
	
	public void populateStaffMembers(){
		
	}
	
	public void populateProducts(){
		
	}
	
	public void populateStockOrders(){
		
	}
	
	public void populateCustomerOrders(){
		
	}

	

}
