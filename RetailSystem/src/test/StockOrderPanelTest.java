package test;


import static org.junit.Assert.*;

import gui.CustomerOrderPanel;
import gui.Shop;
import gui.CustomerOrderPanel.ComboBoxKeyListener;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.junit.Before;
import org.junit.Test;

import data.Customer;
import data.CustomerOrder;
import data.Product;
import data.ProductToOrder;
import data.Staff;
import data.StockOrder;
import data.Supplier;
import gui.Shop;
import gui.StockOrderPanel;


public class StockOrderPanelTest {
	

	static Supplier supplier = null;
	static Staff staff = null;
	static Product product = null;
	static Product product1 = null;
	static StockOrder stockOrder = null;
	static ProductToOrder productToOrder = null;
	static ArrayList<StockOrder> stockOrders = null;
	static ArrayList<ProductToOrder> productsToOrder = null;
	static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	static Date date = null;
	JComboBox comboSelectSupplier;
	final static int PRODUCT_1_PRICE =15;
	final static int PRODUCT_1_AMOUNT = 10;
	final static int PRODUCT_2_PRICE =20;
	final static int PRODUCT_2_AMOUNT = 100;
	final static int FIRST_CUSTOMER_ORDER_ID = 1;
	final static int FIRST_PRODUCT_ID = 1;
	final int AMOUNT_TO_DECREASE = 5;
	final int QUANTITY_AFTER_DECREASE = 45;
	
	
	
	@Before
	public void setUp() throws Exception {

		
		supplier = new Supplier("Doyle's", "St.Stephens,Dublin");
		staff = new Staff("name", "surname", 300, "username", "password");	
		productsToOrder = new ArrayList<ProductToOrder>();
		product =  new Product("name", "category",  30, PRODUCT_1_PRICE,supplier, true, 2);
		product1 =  new Product("name", "category",  30, PRODUCT_2_PRICE,supplier, true, 3);
		productToOrder = new ProductToOrder(product,PRODUCT_1_AMOUNT);
		productsToOrder.add(productToOrder);
		suppliers.add(supplier);
		stockOrder = new StockOrder(date, productsToOrder, staff);
		stockOrders = new ArrayList<StockOrder>();
		stockOrders.add(stockOrder);
		Shop.getProducts().add(product);
		Shop.getProducts().add(product1);
		
		
	}

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
	public void stockOrder_addASupplierToTheArrayListOfStockOrders_stockOrderInArray(){
		assertEquals(1, stockOrders.size());
	}
	

	@Test
	public void testSaveDetails() {
		stockOrder.setId(100);
		stockOrder.setProductsToOrder(productsToOrder);
		stockOrder.setStaff(staff);
		
				
		if(stockOrder.getProductsToOrder().lastIndexOf(stockOrder)==0) {
			fail("ID not Saved");
		}
		
	}
	
	@Test
	public void supplierName_addingSupplierNamesConcatenationToArrayListOFStrings_concatenatedValues(){
		ArrayList<String> supplierNames = new ArrayList<String>();
		supplierNames.add("");
		
		
		for (Supplier supplier: suppliers){
			String name = supplier.getSupplierName();
			supplierNames.add(name);
		}
		
		comboSelectSupplier = new JComboBox(supplierNames.toArray());
		comboSelectSupplier.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectSupplier);
		
		assertEquals(2, supplierNames.size());
		assertEquals("Joe surname", supplierNames.get(1));
		assertEquals("Joe surname", comboSelectSupplier.getItemAt(1));
	}

	@Test
	public void testChangeStockOrderToComplete() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProductNamesForComboBox() {
		fail("Not yet implemented");
	}

	@Test
	public void testDisplayErrorMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testDisplayOrderTable() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testDisplayProductsTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetArrayTemporaryOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetArrayTemporaryOrder() {
		fail("Not yet implemented");
	}

}
