package data;

import gui.Shop;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonExample {

	/**
	 * Returns the whole list of products stored in /resources/products.json
	 * @return
	 */
	public static ArrayList<Product> readProductsFromFile() {
		Scanner in=null;
		try {
			in = new Scanner(new FileReader("resources/products.json"));
			ObjectMapper mapper = new ObjectMapper();
			while (in.hasNextLine()) {
				Product product = mapper
						.readValue(in.nextLine(), Product.class);
				System.out.println(product.toString());
				Shop.getProducts().add(product);
			}
			return Shop.getProducts();
		} catch (EOFException eof){
			eof.printStackTrace();
			return null;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			in.close();
		}
		return null;
	}

	/**
	 * Saves the product in /resources/products.json as a Json object
	 * @param product
	 */
	public static void saveProductToFile(Product product) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(product) + "\n";
			FileWriter writer = new FileWriter("resources/products.json", true);
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	/**
	 * Returns the whole list of customers stored in /resources/customers.json
	 * @return
	 */
	public static ArrayList<Customer> readCustomersFromFile() {
		Scanner in=null;
		try {
			in = new Scanner(new FileReader("resources/customers.json"));
			ObjectMapper mapper = new ObjectMapper();
			while (in.hasNextLine()) {
				Customer customer = mapper
						.readValue(in.nextLine(), Customer.class);
				System.out.println(customer.toString());
				Shop.getCustomers().add(customer);
			}
			return Shop.getCustomers();
		} catch (EOFException eof){
			eof.printStackTrace();
			return null;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			in.close();
		}
		return null;
	}
	
	/**
	 * Saves the customer in /resources/customers.json as a Json object
	 * @param customer
	 */
	public static void saveCustomerToFile(Customer customer) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(customer) + "\n";
			FileWriter writer = new FileWriter("resources/customers.json", true);
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Returns the whole list of suppliers stored in /resources/suppliers.json
	 * @return
	 */
	public static ArrayList<Supplier> readSuppliersFromFile() {
		Scanner in=null;
		try {
			in = new Scanner(new FileReader("resources/suppliers.json"));
			ObjectMapper mapper = new ObjectMapper();
			while (in.hasNextLine()) {
				Supplier supplier = mapper
						.readValue(in.nextLine(), Supplier.class);
				System.out.println(supplier.toString());
				Shop.getSuppliers().add(supplier);
			}
			return Shop.getSuppliers();
		} catch (EOFException eof){
			eof.printStackTrace();
			return null;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			in.close();
		}
		return null;
	}

	/**
	 * Saves the suppliers in /resources/suppliers.json as a Json object
	 * @param supplier
	 */
	public static void saveSupplierToFile(Supplier supplier) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(supplier) + "\n";
			FileWriter writer = new FileWriter("resources/suppliers.json", true);
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the whole list of StockOrders stored in /resources/stockOrders.json
	 * @return
	 */
	
	public static ArrayList<StockOrder> readStockOrdersFromFile() {
		Scanner in=null;
		try {
			in = new Scanner(new FileReader("resources/stockOrders.json"));
			ObjectMapper mapper = new ObjectMapper();
			while (in.hasNextLine()) {
				StockOrder stockOrder = mapper
						.readValue(in.nextLine(), 	StockOrder.class);
				System.out.println(stockOrder.toString());
				Shop.getStockOrders().add(stockOrder);
			}
			return Shop.getStockOrders();
		} catch (EOFException eof){
			eof.printStackTrace();
			return null;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			in.close();
		}
		return null;
	}

	/**
	 * Saves the stock order in /StockOrders/products.json as a Json object
	 * @param product
	 */
	public static void saveStockOrdersToFile(StockOrder stockOrder) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(stockOrder) + "\n";
			FileWriter writer = new FileWriter("resources/stockOrders.json", true);
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the whole list of staff stored in /resources/staff.json
	 * @return
	 */
	public static ArrayList<Staff> readStaffFromFile() {
		Scanner in=null;
		try {
			in = new Scanner(new FileReader("resources/staff.json"));
			ObjectMapper mapper = new ObjectMapper();
			while (in.hasNextLine()) {
				Staff staff = mapper
						.readValue(in.nextLine(), Staff.class);
				System.out.println(staff.toString());
				Shop.getStaffMembers().add(staff);
			}
			return Shop.getStaffMembers();
		} catch (EOFException eof){
			eof.printStackTrace();
			return null;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			in.close();
		}
		return null;
	}

	/**
	 * Saves the staff in /resources/staff.json as a Json object
	 * @param supplier
	 */
	public static void saveStaffToFile(Staff staff) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(staff) + "\n";
			FileWriter writer = new FileWriter("resources/staff.json", true);
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Saves the product in file location (e.g. "resources/products.json") as a Json object
	 * @param product
	 */
	public static void clearList(String file){
		try{
			String result = "";
			FileWriter writer = new FileWriter(file);
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
