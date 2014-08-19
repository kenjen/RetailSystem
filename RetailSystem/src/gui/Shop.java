package gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.Customer;
import data.CustomerOrder;
import data.JsonExample;
import data.Product;
import data.ProductToOrder;
import data.Staff;
//import data.Product;
import data.StockOrder;
import data.Supplier;



public class Shop {
	private static ArrayList<Staff> staffMembers = new ArrayList<Staff>();
	private static ArrayList<Customer> customers = new ArrayList<Customer>();
	private static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	private static ArrayList<Product> products = new ArrayList<Product>();
	private static ArrayList<StockOrder> stockOrders = new ArrayList<StockOrder>();
	private static ArrayList<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
	
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
		populateSuppliers();
		populateStaffMembers();
		populateProducts();
		populateStockOrders();
		populateCustomerOrders();
		printCustomerOrderInvoice();
		updateStockOrderDevilveryDates();
		
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
			System.out.println(customer.getCustomerID()+customer.getCustomerLName());
		}
		
	}
	
	public static void editCustomer(int customerID, String customerFName, String customerLName,
			String customerAddress, String customerMobile, String customerHome){
		for(Customer customer:customers){
			if(customer.getCustomerID()==customerID){
				customer.setCustomerFName(customerFName);
				customer.setCustomerLName(customerLName);
				customer.setCustomerAddress(customerAddress);
				customer.setCustomerMobile(customerMobile);
				customer.setCustomerHome(customerHome);
			}
		}
		System.out.println(customerID+customerFName);
	}
	
	public static void deleteCustomer(int customerID){
		for(Customer customer:customers){
			if(customer.getCustomerID()==customerID){
				customer.setDeleted(true);
			}
			
		}
	}
	
	public static void deleteStaff(String Username){
		for(Staff staff :staffMembers){
			if(staff.getUsername()==Username){
				staff.setDeleted(true);
			}
			
		}
	}
	
	public static void EditDetails(String name, String surname, double salary, String username, String password){
		for(Staff staff :staffMembers){
			if(staff.getUsername().equals(username)){
				
				//TODO: change the username for only one staff member
						
				//System.out.println("Edit Details loop");
				staff.setName(name);
				staff.setSurname(surname);
				staff.setSalary(salary);
				
				staff.setPassword(password);
				//staff.setUsername(username);

				System.out.println("User Details have been Edited: " + username);
			}
		}	
	}

	
	
	public void populateSuppliers(){
		
		suppliers = JsonExample.readSuppliersFromFile();
		if(suppliers == null){
			System.out.println("reached null loop");
		Supplier supplier1 = new Supplier(123,"Doyle's", "St.Stephens,Dublin");
		Supplier supplier2 = new Supplier(234,"Profi", "Baldara, Ashbourne");
		Supplier supplier3 = new Supplier(345,"Jane LTD", "Kileen, Cork");
		Supplier supplier4 = new Supplier(656,"G&M", "Hunter's Lane, Navan");
		suppliers.add(supplier1);
		suppliers.add(supplier2);
		suppliers.add(supplier3);
		suppliers.add(supplier4);
		}
	}
	
	public void populateStaffMembers(){
		
		staffMembers = JsonExample.readStaffFromFile();
		if(staffMembers == null){
			System.out.println("null Loop");
			
		Staff admin = new Staff("admin","admin",0,"admin","admin");
		admin.setAdmin(true);
		Staff john = new Staff("Jhn","Doe",15.23,"JohnDoe","Firefly");
		Staff mick = new Staff("Mick","Green",8.65,"MickGreen","Avalanche");
		Staff angela = new Staff("Angela","Blue",23.5,"AngelaBlue","Onyx");
		staffMembers.add(admin);
		staffMembers.add(john);
		staffMembers.add(mick);
		staffMembers.add(angela);
		//JsonExample.saveStaffToFile(john);
		
		}

		for(Staff staff : staffMembers){
			Staff.setNextId(staff.getId());
		}

		System.out.println("Staff members populated");
		
		//Save each staff member details to a json file
		//StaffPersistency.saveStaffToFile(john);
	}
	
	public void populateProducts(){
			products = JsonExample.readProductsFromFile();
			
			//if error occured during load, load default products
			if(products==null){
				System.out.println("REACHED NULL LOOP");
				Product p1 = new Product("Pear", "Food", 70, 0.23, suppliers.get(0), true, 80);
				Product p2 = new Product("Coat", "Clothing", 50, 29.99, suppliers.get(1), true, 10);
				Product p3 = new Product("Trousers", "Clothing", 80, 40.0, suppliers.get(1), true, 15);
				Product p4 = new Product("Ham", "Food", 120, 4.50, suppliers.get(0), true, 60);
				Product p5 = new Product("Broom", "Hygene", 20, 12.0, suppliers.get(3), true, 3);
				
				products = new ArrayList<Product>();
				products.add(p1);
				products.add(p2);
				products.add(p3);
				products.add(p4);
				products.add(p5);
			}
			for(Product product : products){
				Product.setNextId(product.getId());
			}
	}
	
	public void populateStockOrders(){
		
		stockOrders = JsonExample.readStockOrdersFromFile();
		if(stockOrders == null){
			
			System.out.println("reached null loop");
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			ArrayList<ProductToOrder> toOrder = new ArrayList<ProductToOrder>();
			toOrder.add(new ProductToOrder(Shop.getProducts().get(0), 20));
			
			try {
				StockOrder stockOrder = new StockOrder(sd.parse("10/08/2014 13:36"), toOrder, staffMembers.get(2));
				stockOrders.add(stockOrder);
				System.out.println("Order placed with id " + stockOrder.getId());
			} catch (ParseException e) {
				e.printStackTrace();
				System.out.println("Order not placed as error with date");
			}
			
			toOrder = new ArrayList<ProductToOrder>();
			toOrder.add(new ProductToOrder(Shop.getProducts().get(1), 5));
			toOrder.add(new ProductToOrder(Shop.getProducts().get(2), 5));
			
			try {
				StockOrder stockOrder = new StockOrder(sd.parse("26/07/2014 10:15"), toOrder, staffMembers.get(2));
				stockOrders.add(stockOrder);
				System.out.println("Order placed with id " + stockOrder.getId());
			} catch (ParseException e) {
				e.printStackTrace();
				System.out.println("Order not placed as error with date");
			}
			
			toOrder = new ArrayList<ProductToOrder>();
			toOrder.add(new ProductToOrder(Shop.getProducts().get(3), 5));
			toOrder.add(new ProductToOrder(Shop.getProducts().get(3), 5));
			
			try {
				StockOrder stockOrder = new StockOrder(sd.parse("06/08/2014 13:00"), toOrder, staffMembers.get(2));
				stockOrders.add(stockOrder);
				System.out.println("Order placed with id " + stockOrder.getId());
			} catch (ParseException e) {
				e.printStackTrace();
				System.out.println("Order not placed as error with date");
			}
		}
			
	}
	
	
	public void populateCustomerOrders(){
		
		/*CustomerOrder customerOrder1 = new CustomerOrder(1111, "11/08/14", 5.00, 5.65);
		CustomerOrder customerOrder2 = new CustomerOrder(1112, "10/08/14", 30.00, 32.65);
		CustomerOrder customerOrder3 = new CustomerOrder(1113, "09/08/14", 56.70, 59.35);
		CustomerOrder customerOrder4 = new CustomerOrder(1114, "08/08/14", 3.00, 7.85);
		
		customerOrders.add(customerOrder1);
		customerOrders.add(customerOrder2);
		customerOrders.add(customerOrder3);
		customerOrders.add(customerOrder4);*/
		
	}
	
	public void updateStockOrderDevilveryDates(){
		for(int i = 0; i<stockOrders.size(); i++){
			if(stockOrders.get(i).getExpectedDeliveryDate().before(new Date())){
				stockOrders.get(i).setCompleted(true);
			}
		}
	}
	
	public void printCustomerOrderInvoice() {
		for(CustomerOrder order: customerOrders){
			if (order.isComplete() == true){
			System.out.println("-----Invoice-------");
			System.out.println("Order ID : " + order.getId() );
			System.out.println("Customer ID : " + order.getCustomer());
			System.out.println("Order received: " + order.getCreationDate());
			System.out.println("Products ordered : " + order.getProducts());
			System.out.println("Gross Total : " + order.getTotalGross());
			System.out.println("Net Total (VAT @ 10%) : " + order.getTotalNet());
			}
		}
			
		}
		

	public static ArrayList<Customer> getCustomers() {
		
		return customers;
	}

	public static void setCustomers(ArrayList<Customer> customers) {
		Shop.customers = customers;
	}

	public static ArrayList<Product> getProducts() {
		return products;
	}

	public static void setProducts(ArrayList<Product> products) {
		Shop.products = products;
	}

	public static ArrayList<Staff> getStaffMembers() {
		return staffMembers;
	}

	public static void setStaffMembers(ArrayList<Staff> staffMembers) {
		Shop.staffMembers = staffMembers;
	}

	public static ArrayList<Supplier> getSuppliers() {
		return suppliers;
	}

	public static void setSuppliers(ArrayList<Supplier> suppliers) {
		Shop.suppliers = suppliers;
	}

	public static ArrayList<StockOrder> getStockOrders() {
		return stockOrders;
	}

	public static void setStockOrders(ArrayList<StockOrder> stockOrders) {
		Shop.stockOrders = stockOrders;
	}

	public static ArrayList<CustomerOrder> getCustomerOrders() {
		return customerOrders;
	}

	public static void setCustomerOrders(ArrayList<CustomerOrder> customerOrders) {
		Shop.customerOrders = customerOrders;
	}

}
