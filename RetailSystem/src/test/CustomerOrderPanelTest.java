package test;

import static org.junit.Assert.*;
import gui.CustomerOrderPanel;
import gui.Shop;
import gui.CustomerOrderPanel.ComboBoxKeyListener;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.Customer;
import data.CustomerOrder;
import data.Product;
import data.ProductToOrder;
import data.Staff;
import data.Supplier;

public class CustomerOrderPanelTest {
	static Customer customer = null;
	static Staff staff = null;
	static Supplier supplier = null;
	static Product product = null;
	static Product product1 = null;
	static ProductToOrder productToOrder = null;
	static CustomerOrder customerOrder;
	static ArrayList<ProductToOrder> productsToOrder = null;
	static ArrayList<CustomerOrder> customerOrders = null;
	static ArrayList<Customer> customers = new ArrayList<Customer>();
	private static ArrayList<String> arrayCustomerNames;
	private static ArrayList<String> arrayProductNames;
	private Object[][] availableProductsArray ;
	
	static JComboBox comboSelectCustomer;
	static JComboBox comboSearchForProducts;
	final static int PRODUCT_1_PRICE =15;
	final static int PRODUCT_1_AMOUNT = 10;
	final static int PRODUCT_2_PRICE =20;
	final static int PRODUCT_2_AMOUNT = 100;
	final static int FIRST_CUSTOMER_ORDER_ID = 1;
	final static int FIRST_PRODUCT_ID = 1;
	final int AMOUNT_TO_DECREASE = 5;
	final int QUANTITY_AFTER_DECREASE = 45;
	
	@BeforeClass
	public static void setUp() throws Exception {
		customer = new Customer("Jimmy","surname","address","mobile","phone");
		staff = new Staff("name","surname",12.00,"username","password");
		productsToOrder = new ArrayList<ProductToOrder>();
		supplier = new Supplier("name","address");
		product = new Product("name","category",50,PRODUCT_1_PRICE,supplier, true, 2);
		product1 = new Product("Apple","food",50,PRODUCT_2_PRICE,supplier, true, 3);
		productToOrder = new ProductToOrder(product,PRODUCT_1_AMOUNT);
		productsToOrder.add(productToOrder);
		customers.add(customer);	
		customerOrder = new CustomerOrder(customer,staff,productsToOrder);
		customerOrders = new ArrayList<CustomerOrder>();
		customerOrders.add(customerOrder);
		Shop.getProducts().add(product);
		Shop.getProducts().add(product1);
		comboSelectCustomer = new JComboBox();
		comboSearchForProducts = new JComboBox();
		Shop shop = new Shop();
		shop.populateStaffMembers();
		shop.populateCustomers();
		shop.populateSuppliers();
		shop.populateProducts();
		shop.populateCustomerOrders();
		shop.populateStockOrders();
	}
	
	//returns an array of product names
		public String[] getProductNamesForComboBox(){
			ArrayList<String> names = new ArrayList<String>();
			for(Product product:Shop.getProducts()){
				boolean found = false;
				innerFor:for(String s:names){
					if(product.getName().equalsIgnoreCase(s)){
						found=true;
						break innerFor;
					}
				}
				if(found==false){
					names.add(product.getName());
				}
			}
			String[] arrayToSort = new String[names.size()];
			arrayToSort = (String[]) names.toArray(new String[names.size()]);
			Arrays.sort(arrayToSort);
			return arrayToSort;
		}
		
	
	@Test
	public void customerOrder_addACustomerToTheArrayListOfCustomerOrders_customerOrderInArray(){
		assertEquals(1, customerOrders.size());
	}
	
	@Test
	public void customerOrder_verifyCustomerOrder_expectedValues(){
		
		assertEquals("Jimmy", customerOrders.get(0).getCustomer().getCustomerFName());
		assertEquals(FIRST_CUSTOMER_ORDER_ID, customerOrders.get(0).getId());
		assertEquals(FIRST_PRODUCT_ID, customerOrders.get(0).getProducts().get(0).getId());
	}
	
	@Test
	public void customerOrder_TotalGrossValue_sumOfProductsOrdered(){
		assertEquals((PRODUCT_1_AMOUNT*PRODUCT_1_PRICE*1.21), customerOrders.get(0).getTotalGross(), .0002);
	}
	
	@Test
	public void product_decrementProductAvalableQuantity_quantityMinusAmount(){
		Shop.getProducts().add(product);
		Shop.getProducts().add(product1);
		boolean returnable = false;
		for(Product y:Shop.getProducts()){
			if(y.getId() == FIRST_PRODUCT_ID){
				y.setQuantity(y.getQuantity()-AMOUNT_TO_DECREASE);
				if(y.getQuantity() == 0){
					y.setAvailable(false);
					y.setFlaggedForOrder(true);
				}
				returnable  = true;
				break;
			}
		}
		assertEquals(true, returnable);
		assertEquals(QUANTITY_AFTER_DECREASE, Shop.getProducts().get(0).getQuantity());
		assertEquals(true, Shop.getProducts().get(0).isAvailable());
		assertEquals(false, Shop.getProducts().get(0).isFlaggedForOrder());
	}
	
	@Test
	//populate customer names for the combo box
	public void customerName_addingCustomerNamesConcatenationToArrayListOFStrings_concatenatedValues(){
		arrayCustomerNames = new ArrayList<String>();
		arrayCustomerNames.add("");
		
		//concatenate customer names and add them to the combo box
		for (Customer customer: Shop.getCustomers()){
			if(customer.isDeleted() == false){
				String name = customer.getCustomerFName()+" "+customer.getCustomerLName();
				arrayCustomerNames.add(name);
			}
		}		
		comboSelectCustomer.removeAllItems();
		for(String x:arrayCustomerNames){
			comboSelectCustomer.addItem(x);
		}
		comboSelectCustomer.revalidate();
		int count = Shop.getCustomers().size();
		String firstCustomer = "";
		String secondCustomer = Shop.getCustomers().get(0).getCustomerFName()+" "+Shop.getCustomers().get(0).getCustomerLName();
		assertEquals(count+1, comboSelectCustomer.getItemCount());
		assertEquals(firstCustomer, comboSelectCustomer.getItemAt(0).toString());
		assertEquals(secondCustomer, comboSelectCustomer.getItemAt(1).toString());
	}
	
	@Test
	//populate product names for the combo box
	public void productNames_populateProductNamesComboBoxWithValuesOfAllProducts_atomicProductNames(){
		arrayProductNames = new ArrayList<String>();
		int counter = 0;
		for(Product product:Shop.getProducts()){
			if(product.isAvailable() && product.isDeleted()==false){
				boolean found = false;
				innerFor:for(String s:arrayProductNames){
					if(product.getName().equalsIgnoreCase(s)){
						found=true;
						break innerFor;
					}
				}
				if(found==false){
					counter++;
					arrayProductNames.add(product.getName());
				}
			}
		}
		comboSearchForProducts.removeAllItems();
		for(String x:arrayProductNames){
			comboSearchForProducts.addItem(x);
		}
		comboSearchForProducts.revalidate();
		
		assertEquals(counter, comboSearchForProducts.getItemCount());
	}
	
	@Test
	public void populateAvailableProductsArayOfObjectsForAllProducts() {
		int availableCounter = 0;
		for(Product prod : Shop.getProducts()){
			if(prod.isAvailable() && prod.isDeleted() == false)
				availableCounter ++;
		}
		availableProductsArray = new Object[availableCounter][8];
		int counter = 0;
		//make products array to feed into the table model
		DecimalFormat df = new DecimalFormat("#.00");
		for(Product product:Shop.getProducts()){
			if(product.isAvailable() && product.isDeleted()==false){
				availableProductsArray[counter][0] = product.getId();
				availableProductsArray[counter][1] = product.getName();
				availableProductsArray[counter][2] = product.getSupplier().getSupplierName();
				availableProductsArray[counter][3] = product.getCategory();
				availableProductsArray[counter][4] = Double.parseDouble(df.format(product.getMarkupPrice()));
				availableProductsArray[counter][5] = product.isDiscounted();
				
				//update the price of the array if there are orders made for this products that are yet to be sumbitted
				//if the user adds to order but does not submit it, leaving the pane and coming back will redraw the table with current products
				//therefore this will make sure that if you have products in the order that is not submitted, will reflect the quantities
				availableProductsArray[counter][6] = product.getQuantity();
				//this column will be editable
				availableProductsArray[counter][7] = 0;
				counter ++;
			}
		}
		
		boolean available, deleted;
		Product product = Shop.getProducts().get(0);
		available = product.isAvailable();
		deleted = product.isDeleted();
		product.setAvailable(true);
		product.setDeleted(false);
		assertEquals(product.getId(), (int)availableProductsArray[0][0]);
		product.setAvailable(available);
		product.setDeleted(deleted);
	}
	
	
	

}
