package test;

import static org.junit.Assert.*;
import gui.Shop;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.Test;

import data.Customer;
import data.CustomerOrder;
import data.Finance;
import data.Json;
import data.Product;
import data.ProductToOrder;
import data.Staff;
import data.StockOrder;
import data.Supplier;

public class ShopTest {
	
	private static ArrayList<Staff> staffMembers = new ArrayList<Staff>();
	private static ArrayList<Customer> customers = new ArrayList<Customer>();
	private static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	private static ArrayList<Product> products = new ArrayList<Product>();
	private static ArrayList<StockOrder> stockOrders = new ArrayList<StockOrder>();
	private static ArrayList<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
	private static ArrayList<Finance> financialRecords = new ArrayList<Finance>();
	
	@Test
	public void populateFinancesFromJSonFile_sizeNotNull() {
		financialRecords = Json.readFinanceFromFile();
		assertTrue(financialRecords.size() > 0);
	}
	
	@Test
	public void populateCustomersFromHardCode_sizeNotNull() {
		//customers = Json.readCustomersFromFile();

		if (customers.size() == 0) {
			Customer c1 = new Customer("Dave", "Foley", "23 Main Street",
					"0873727345", "012398045");
			Customer c2 = new Customer("Jim", "McDonald", "6 High Street",
					"0877603240", "017839295");
			Customer c3 = new Customer("Carl", "Lenny", "54 Shelbyville Drive",
					"0867839022", "014039202");
			Customer c4 = new Customer("Leonard", "Cooper",
					"16 Pasadena Avenue", "0850393939", "018280304");
			Customer c5 = new Customer("Roy", "Moss", "3 Rehnham Way",
					"0872349299", "013202033");
			
		customers = new ArrayList<Customer>();
			customers.add(c1);
			customers.add(c2);
			customers.add(c3);
			customers.add(c4);
			customers.add(c5);

		}
		
		for (Customer customer : customers) {
			Customer.setNextId(customer.getCustomerID());
		}
		assertTrue(customers.size() > 0);
	}
	
	@Test
	public void populateCustomersFromJSonFile_sizeNotNull() {
		customers = Json.readCustomersFromFile();

		assertTrue(customers.size() > 0);
	}
	
	@Test
public void populateSuppliersFromJSonFile_sizeNotNull(){
		
		suppliers = Json.readSuppliersFromFile();
		if(suppliers.size() == 0){
			System.out.println("reached null loop");
			
		Supplier supplier1 = new Supplier("Doyle's", "St.Stephens,Dublin");
		Supplier supplier2 = new Supplier("Profi", "Baldara, Ashbourne");
		Supplier supplier3 = new Supplier("Jane LTD", "Kileen, Cork");
		Supplier supplier4 = new Supplier("G&M", "Hunter's Lane, Navan");
		suppliers.add(supplier1);
		suppliers.add(supplier2);
		suppliers.add(supplier3);
		suppliers.add(supplier4);
		}
		for(Supplier supplier:suppliers){
			supplier.setGeneratedId(supplier.getSupplierId());
		}
		assertTrue(suppliers.size() > 0);
	}
	
	@Test
	public void populateStaffMembersFromJSonFile_sizeNotNull() throws FileNotFoundException {
		
		staffMembers = Json.readStaffFromFile();
		
		if (staffMembers.size() == 0) {
			System.out.println("reached null loop");
			
			staffMembers = new ArrayList<Staff>();

		Staff admin = new Staff("admin", "admin", 0, "admin", "admin");
		admin.setAdmin(true);
		Staff john = new Staff("John", "Doe", 15.23, "JohnDoe", "Firefly");
		Staff mick = new Staff("Mick", "Green", 8.65, "MickGreen", "Avalanche");
		Staff angela = new Staff("Angela", "Blue", 23.5, "AngelaBlue", "Onyx");
		staffMembers.add(admin);
		staffMembers.add(john);
		staffMembers.add(mick);
		staffMembers.add(angela);
		
		for(Staff s : staffMembers){
			if(s.getId() > Staff.getNextId()){
				Staff.setNextId(s.getId());
			}
		}
	}
		assertTrue(staffMembers.size() > 0);
	}
	
	@Test
	public void populateProductsFromJSonFile_sizeNotNull() {
		products = Json.readProductsFromFile();
		
		// if error occured during load, load default products
		if (products.size() == 0) {
			System.out.println("REACHED NULL LOOP");
			Product p1 = new Product("Pear", "Food", 70, 0.23,
					suppliers.get(0), true, 80);
			Product p2 = new Product("Coat", "Clothing", 50, 29.99,
					suppliers.get(1), true, 10);
			Product p3 = new Product("Trousers", "Clothing", 80, 40.0,
					suppliers.get(1), true, 15);
			Product p4 = new Product("Ham", "Food", 120, 4.50,
					suppliers.get(0), true, 60);
			Product p5 = new Product("Broom", "Hygene", 20, 12.0,
					suppliers.get(3), true, 3);
			products = new ArrayList<Product>();
			products.add(p1);
			products.add(p2);
			products.add(p3);
			products.add(p4);
			products.add(p5);
		}
		for (Product product : products) {
			Product.setNextId(product.getId());
		}
		assertTrue(products.size() > 0);
	}
	
	@Test
	public void populateCustomerOrdersFromJSonFile_sizeNotNull() {
		customerOrders = Json.readCustomerOrdersFromFile();
		assertTrue(customerOrders.size() > 0);
	}
	
	@Test
	public void populateStockOrdersFromJSonFile_sizeNotNull() {
		
		stockOrders = Json.readStockOrdersFromFile();
		
		if(stockOrders.size() == 0){
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		ArrayList<ProductToOrder> toOrder = new ArrayList<ProductToOrder>();
		toOrder.add(new ProductToOrder(Shop.getProducts().get(0), 20));

		try {
			StockOrder stockOrder = new StockOrder(
					sd.parse("10/08/2014 13:36"), toOrder, staffMembers.get(2));
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
			StockOrder stockOrder = new StockOrder(
					sd.parse("26/07/2014 10:15"), toOrder, staffMembers.get(2));
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
			StockOrder stockOrder = new StockOrder(
					sd.parse("06/08/2014 13:00"), toOrder, staffMembers.get(2));
			stockOrders.add(stockOrder);
			System.out.println("Order placed with id " + stockOrder.getId());
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("Order not placed as error with date");
		}
		
		}
		for(StockOrder stockOrder : stockOrders){
			StockOrder.setNextId(stockOrder.getId());
		}
		
		assertTrue(stockOrders.size() > 0);
	}
	
}
