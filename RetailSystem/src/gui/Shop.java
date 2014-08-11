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
	private static ArrayList<Staff> staffMembers = new ArrayList<Staff>();
	private static ArrayList<Customer> customers = new ArrayList<Customer>();
	private static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	private static ArrayList<Product> products = new ArrayList<Product>();
	private static ArrayList<StockOrder> stockOrders = new ArrayList<StockOrder>();
	//private ArrayList<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
	
	//Initialize s of Type Supplier
	Supplier s;
		
	public static void main(String[] args) {
		new Shop(true);
	}
	
	public Shop(){
		
	}
	
	public Shop(boolean run) {
		//populate data
		populateCustomers();
		//populateSuppliers();
		populateStaffMembers();
		populateProducts();
		populateStockOrders();
		//populateCustomerOrders();
		
		//run login
		Login login = new Login(staffMembers);
		login.drawFrame();
		
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
		Staff admin = new Staff("admin","admin",0,"admin","admin");
		admin.setAdmin(true);
		Staff john = new Staff("John","Doe",15.23,"JohnDoe","Firefly");
		Staff mick = new Staff("Mick","Green",8.65,"MickGreen","Avalanche");
		mick.setDeleted(true);
		Staff angela = new Staff("Angela","Blue",23.5,"AngelaBlue","Onyx");
		staffMembers.add(admin);
		staffMembers.add(john);
		staffMembers.add(mick);
		staffMembers.add(angela);

		System.out.println("Staff members populated");
	}
	
	public void populateProducts(){
		
		Product p1 = new Product("Pear", "Food", 100, 0.23, s, true, 22);
		Product p2 = new Product("Coat", "Clothing", 50, 29.99, s, true, 10);
		Product p3 = new Product("Trousers", "Clothing", 80, 40.0, s, true, 15);
		Product p4 = new Product("Ham", "Food", 120, 4.50, s, true, 60);
		Product p5 = new Product("Broom", "Hygene", 20, 12.0, s, true, 3);
		
		products.add(p1);
		products.add(p2);
		products.add(p3);
		products.add(p4);
		products.add(p5);
		
		for(Product product : products){
			System.out.println("Product: "+product.getName()+" Category: "+product.getCategory()+
					" quantity: "+product.getQuantity()+" Price: "+product.getPrice()+
					" Supplier: "+product.getSupplier()+
					" Availability: "+product.isAvailable() +
					"Low Stock Order: "+product.getLowStockOrder());
		}
		
	}
	
	public void populateStockOrders(){
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		ArrayList<Product> productsToOrder = new ArrayList<Product>();
		try {
			StockOrder stockOrder1 = new StockOrder(sd.parse("10/08/2014 13:36"), productsToOrder, new Staff("kian", "jennings", 300, "kJennings", "help"));
			stockOrders.add(stockOrder1);
			System.out.println("Order placed with id " + stockOrder1.getId());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			StockOrder stockOrder1 = new StockOrder(sd.parse("10/08/2014 13:36"), productsToOrder, new Staff("kian", "jennings", 300, "kJennings", "help"));
			stockOrders.add(stockOrder1);
			System.out.println("Order placed with id " + stockOrder1.getId());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public void populateCustomerOrders(){
		
	}

	

}
