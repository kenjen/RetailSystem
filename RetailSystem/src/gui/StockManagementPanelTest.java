package gui;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.Product;
import data.Supplier;

public class StockManagementPanelTest {
	
	StockManagementPanel panel;
	ArrayList<Product> products = new ArrayList<Product>();
	Product p1;

	@Before
	public void setUp() throws Exception {
		panel = new StockManagementPanel();
		p1 = new Product("name", "category",  30, 12.0, null, true, 20);
		p1.setId(1);
		panel.setProductLoaded(true);
		products = new ArrayList<Product>();
		products.clear();
		products.add(p1);
	}

	@Test
	public void testClearProductDetails() {
		panel.setDisplayedName("test");
		panel.setDisplayedCategory("test");
		panel.setDisplayedQuantity("test");
		panel.setDisplayedThreshold("test");
		panel.setDisplayedPrice("test");
		panel.setDisplayedDiscountPrice("test");
		panel.setDisplayedDiscountPercent("test");
		panel.clearProductDetails();
		if(panel.getDisplayedName().length()!=0){
			fail("Name not cleared");
		}else if(panel.getDisplayedCategory().length()!=0){
			fail("Category not cleared");
		}else if(panel.getDisplayedQuantity().length()!=0){
			fail("Quantity not cleared");
		}else if(panel.getDisplayedThreshold().length()!=0){
			fail("Threshold not cleared");
		}else if(panel.getDisplayedPrice().length()!=0){
			fail("Price not cleared");
		}else if(panel.getDisplayedDiscountPrice().length()!=0){
			fail("Discounted price not cleared");
		}else if(panel.getDisplayedDiscountPercent().length()!=0){
			fail("Discounted percent not cleared");
		}
	}

	@Test
	public void testCreateNewProduct() {
		panel.setDisplayedName("test");
		panel.setDisplayedCategory("test");
		panel.setDisplayedQuantity("20");
		panel.setDisplayedThreshold("20");
		panel.setDisplayedPrice("19.99");
		panel.setProductLoaded(false);
		Product p = panel.createNewProduct(true);
		if(p==null){
			fail("Product not created sucesfully");
		}
	}

	@Test
	public void testDeleteProduct() {
		Product p2 = new Product("name", "category",  30, 12.0, null, true, 20);
		p2.setId(2);
		products.add(p2);
		int before = products.size();
		panel.deleteProduct(1, products, true);
		assertEquals(before-1, products.size());
	}

	@Test
	public void testDisplayAllProducts() {
		assertEquals(1, panel.displayAllProducts(products));
		Product p2 = new Product("name", "category",  30, 12.0, null, true, 20);
		p2.setId(2);
		products.add(p2);
		Product p3 = new Product("name", "category",  30, 12.0, null, true, 20);
		p3.setId(3);
		products.add(p3);
		assertEquals(3, panel.displayAllProducts(products));
	}

	@Test
	public void testDiscountProduct() {
		panel.setProductLoaded(true);
		panel.discountProduct(1, products, true);
		assertEquals(products.get(0).getDiscountedPercentage(), 33d, .001);
	}

	@Test
	public void testDisplayDeletedStock() {
		assertEquals(0, panel.displayDeletedStock(products));
		Product p2 = new Product("name", "category",  30, 12.0, null, true, 20);
		p2.setId(2);
		products.add(p2);
		Product p3 = new Product("name", "category",  30, 12.0, null, true, 20);
		p3.setId(3);
		products.add(p3);
		assertEquals(0, panel.displayDeletedStock(products));
		products.get(0).setDeleted(true);
		assertEquals(1, panel.displayDeletedStock(products));
		products.get(1).setDeleted(true);
		products.get(2).setDeleted(true);
		assertEquals(3, panel.displayDeletedStock(products));
	}

	@Test
	public void testDisplayLowStock() {
		assertEquals(0, panel.displayLowStock(products));
		Product p2 = new Product("name", "category",  30, 12.0, null, true, 20);
		p2.setId(2);
		products.add(p2);
		Product p3 = new Product("name", "category",  30, 12.0, null, true, 20);
		p3.setId(3);
		products.add(p3);
		assertEquals(0, panel.displayLowStock(products));
		products.get(0).setLowStockOrder(300);
		assertEquals(1, panel.displayLowStock(products));
		products.get(1).setLowStockOrder(300);
		products.get(2).setLowStockOrder(300);
		assertEquals(3, panel.displayLowStock(products));
	}

	@Test
	public void testFlagForOrder() {
		boolean first = products.get(0).isFlaggedForOrder();
		panel.flagForOrder(1, products);
		boolean second = products.get(0).isFlaggedForOrder();
		assertNotEquals(first, second);
		panel.flagForOrder(1, products);
		first = products.get(0).isFlaggedForOrder();
		assertNotEquals(first, second);
		assertTrue(second);
	}

	@Test
	public void testLoadProductDetails() {
		panel.loadProductDetails(1, products);
		assertEquals(panel.getDisplayedName(), products.get(0).getName());
		assertEquals(panel.getDisplayedCategory(), products.get(0).getCategory());
		assertEquals(panel.getDisplayedQuantity(), Integer.toString(products.get(0).getQuantity()));
		assertEquals(panel.getDisplayedThreshold(), Integer.toString(products.get(0).getLowStockOrder()));
		assertEquals(panel.getDisplayedPrice(), Double.toString(products.get(0).getPrice()));
		assertEquals(Double.parseDouble(panel.getDisplayedDiscountPrice()), (double)products.get(0).getPrice() - (products.get(0).getPrice()*(products.get(0).getDiscountedPercentage()/100)), .0001);
		assertEquals(panel.getDisplayedDiscountPercent(), products.get(0).getDiscountedPercentage() + "%");
		assertEquals(panel.isFlaggedForOrderVisible(), products.get(0).isFlaggedForOrder());
	}

	@Test
	public void testRefreshCombo() {
		assertEquals(2, panel.refreshCombo(products));
		Product p2 = new Product("name", "category",  30, 12.0, null, true, 20);
		p2.setId(2);
		products.add(p2);
		Product p3 = new Product("name", "category",  30, 12.0, null, true, 20);
		p3.setId(3);
		products.add(p3);
		assertEquals(4, panel.refreshCombo(products));
	}

	@Test
	public void testResetToDefaultValues() {
		assertEquals(1, products.size());
		panel.resetToDefaultValues(products, true);
		assertEquals(5, products.size());
	}

	@Test
	public void testRestoreProduct() {
		p1.setDeleted(true);
		assertTrue(products.get(0).isDeleted());
		panel.restoreProduct(1, products);
		assertFalse(products.get(0).isDeleted());
	}

}