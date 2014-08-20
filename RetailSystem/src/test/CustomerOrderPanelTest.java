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
	JComboBox comboSelectCustomer;
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
	public void customerName_addingCustomerNamesConcatenationToArrayListOFStrings_concatenatedValues(){
		ArrayList<String> customerNames = new ArrayList<String>();
		customerNames.add("");
		
		//concatenate customer names and add them to the combo box
		for (Customer customer:customers){
			String name = customer.getCustomerFName()+" "+customer.getCustomerLName();
			customerNames.add(name);
		}
		
		comboSelectCustomer = new JComboBox(customerNames.toArray());
		comboSelectCustomer.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectCustomer);
		
		assertEquals(2, customerNames.size());
		assertEquals("Jimmy surname", customerNames.get(1));
		assertEquals("Jimmy surname", comboSelectCustomer.getItemAt(1));
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
	
	
	
	
	
	

}
