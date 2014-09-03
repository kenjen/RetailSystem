package data;

import gui.Shop;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.codehaus.jackson.map.ObjectMapper;

public class Json {

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
				Shop.getProducts().add(product);
				//repopulate the supplier for this product as the supplier might be different than the one saved
				for(Supplier s:Shop.getSuppliers()){
					if(s.getSupplierId() == product.getSupplier().getSupplierId()){
						product.setSupplier(s);
						break;
					}
				}
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
				//repopulate Stock Orders with Staff members
				for(Staff staff:Shop.getStaffMembers()){
					if(staff.getId() == stockOrder.getStaff().getId()){
						stockOrder.setStaff(staff);
					}
				}
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
		if(in.hasNextLine()){	
			while (in.hasNextLine()) {
				Staff staff = mapper
						.readValue(in.nextLine(), Staff.class);
				Shop.getStaffMembers().add(staff);
			}
			return Shop.getStaffMembers();
		}
		else{
			return null;
		}
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
	 * clears the contents onf the file specified in the "file" param. If successful TRUE is returned, otherwise, FALSE
	 * @param file
	 * @return
	 */
	public static boolean clearList(String file){
		try{
			new FileWriter(new File(file));
			return true;
		}catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
			return false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Saves the customer order in resources/customerOrder.json as a Json object
	 * @param product
	 */
	public static void saveCustomerOrdersToFile(CustomerOrder customerOrder) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(customerOrder) + "\n";
			FileWriter writer = new FileWriter("resources/customerOrders.json",true);
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the whole list of products stored in /resources/products.json
	 * @return
	 */
	public static ArrayList<CustomerOrder> readCustomerOrdersFromFile() {
		Scanner in=null;
		try {
			//if this is the first time to read from the file then create it
			try{
				new FileReader("resources/customerOrders.json");
			}catch(FileNotFoundException fnfe){
				new FileWriter(new File("resources/customerOrders.json"));
			}
			
			
			in = new Scanner(new FileReader("resources/customerOrders.json"));
			ObjectMapper mapper = new ObjectMapper();
			while (in.hasNextLine()) {
				CustomerOrder custOrder = mapper
						.readValue(in.nextLine(), CustomerOrder.class);
				//repopulate Customer order's customers and staff members in case they have been edited
				for(Staff staff:Shop.getStaffMembers()){
					if (staff.getId() == custOrder.getStaff().getId()){
						custOrder.setStaff(staff);
					}
				}
				for(Customer customer:Shop.getCustomers()){
					if(customer.getCustomerID() == custOrder.getCustomer().getCustomerID()){
						custOrder.setCustomer(customer);
					}
				}
				Shop.getCustomerOrders().add(custOrder);
			}
			return Shop.getCustomerOrders();
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
	 * Returns the whole list of Finance stored in /resources/finance.json
	 * @return
	 */
	public static ArrayList<Finance> readFinanceFromFile() {
		Scanner in=null;
		try {
			//if this is the first time to read from the file then create it
			try{
				new FileReader("resources/finance.json");
			}catch(FileNotFoundException fnfe){
				new FileWriter(new File("resources/finance.json"));
			}
			
			
			in = new Scanner(new FileReader("resources/finance.json"));
			ObjectMapper mapper = new ObjectMapper();
			while (in.hasNextLine()) {
				Finance finance = mapper
						.readValue(in.nextLine(), Finance.class);
				Shop.getFinancialRecords().add(finance);
			}
			return Shop.getFinancialRecords();
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
	 * Saves the customer order in resources/customerOrder.json as a Json object
	 * @param product
	 */
	public static void saveFinanceToFile(Finance finance) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(finance) + "\n";
			FileWriter writer = new FileWriter("resources/finance.json",true);
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
