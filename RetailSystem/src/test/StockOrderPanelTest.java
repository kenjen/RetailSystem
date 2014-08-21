package test;


import static org.junit.Assert.*;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import data.Product;
import data.ProductToOrder;
import data.Staff;
import data.StockOrder;
import data.Supplier;
import gui.StockOrderPanel;


public class StockOrderPanelTest {
	
	StockOrderPanel panel;
	ArrayList<StockOrder> stockOrders;
	StockOrder so1;
	Product po1;
	ArrayList<ProductToOrder> pto1;
	Supplier sup01;
	Staff staff1;
	Date da1;

	@Before
	public void setUp() throws Exception {
		panel = new StockOrderPanel();
		sup01 = new Supplier("Doyle's", "St.Stephens,Dublin");	
		staff1 = new Staff("name", "surname", 300, "username", "password");		
		po1 = new Product("name", "category",  30, 12.0, null, true, 20);
		ArrayList<ProductToOrder> pto1 = new ArrayList<ProductToOrder>(100);
		so1 = new StockOrder(da1, pto1, staff1);
		so1.setId(1);
		panel.setStockOrderLoaded(true);
		stockOrders = new ArrayList<StockOrder>();
		stockOrders.clear();
		stockOrders.add(so1);
		
	}

	

	@Test
	public void testSaveDetails() {
		so1.setId(100);
		so1.setProductsToOrder(pto1);
		so1.setStaff(staff1);
		
		panel.saveDetails();
		
		if(so1.getProductsToOrder().lastIndexOf(so1)==0) {
			fail("ID not Saved");
		}
		

		//assertequals

		//fail("Not yet implemented");
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
