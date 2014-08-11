package gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import data.Customer;
import data.Product;
import data.Staff;
import data.Supplier;
//import data.Product;
import data.StockOrder;
//import data.CustomerOrder;



public class Shop {
	private ArrayList<Staff> staffMembers = new ArrayList<Staff>();
	private ArrayList<Customer> customers = new ArrayList<Customer>();
	private ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	private ArrayList<Product> products = new ArrayList<Product>();
	private ArrayList<StockOrder> stockOrders = new ArrayList<StockOrder>();
	//private ArrayList<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
	
	public static void main(String[] args) {
		new Shop();
	}
	public Shop() {
		//populate data
		populateCustomers();
		//populateSuppliers();
		//populateStaffMembers();
		//populateProducts();
		populateStockOrders();
		//populateCustomerOrders();
		
		//run login
		//Login login = new Login();
		//login.drawFrame();
		
	}

	public void populateCustomers(){
		Customer c1 = new Customer("Dave", "Foley", "23 Main Street", "0873727345", "012398045");
		Customer c2 = new Customer("Jim", "McDonald", "6 High Street", "0877603240", "017839295");
		Customer c3 = new Customer("Carl", "Lenny", "54 Shelbyville Drive", "0867839022", "014039202");
		Customer c4 = new Customer("Leonard", "Cooper", "16 Pasadena Avenue", "0850393939", "018280304");
		Customer c5 = new Customer("Roy", "Moss", "3 Rehnham Way", "0872349299", "013202033");
		customers.add(c1);
		customers.add(c2);
		customers.add(c3);
		customers.add(c4);
		customers.add(c5);
		
		for(Customer customer:customers){
			System.out.println(customer.getCustomerID());
		}
		
	}
	
	public void populateSuppliers(){
		
	}
	
	public void populateStaffMembers(){
		
	}
	
	public void populateProducts(){
		
	}
	
	public void populateStockOrders(){
		//not completed as waiting for products to be populated and for staff to be passed from login to shop
		
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		ArrayList<Product> productsToOrder = new ArrayList<Product>();
		try {
			StockOrder stockOrder = new StockOrder(sd.parse("10/08/2014 13:36"), productsToOrder, new Staff("kian", "jennings", 300, "kJennings", "help"));
			stockOrders.add(stockOrder);
			System.out.println("Order placed with id " + stockOrder.getId());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			StockOrder stockOrder = new StockOrder(sd.parse("10/08/2014 13:36"), productsToOrder, new Staff("kian", "jennings", 300, "kJennings", "help"));
			stockOrders.add(stockOrder);
			System.out.println("Order placed with id " + stockOrder.getId());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void populateCustomerOrders(){
		
	}

	

}
