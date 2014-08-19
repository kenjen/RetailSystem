package gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.Product;

public class StockManagementPanelTest {
	
	StockManagementPanel panel;

	@Before
	public void setUp() throws Exception {
		panel = new StockManagementPanel();
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
		Product p = panel.createNewProduct(true);
		if(p==null){
			fail("Product not created sucesfully");
		}
	}

	@Test
	public void testDeleteProduct() {
		fail("Not yet implemented");
	}

	@Test
	public void testDisplayAllProducts() {
		fail("Not yet implemented");
	}

	@Test
	public void testDiscountProduct() {
		fail("Not yet implemented");
	}

	@Test
	public void testDisplayDeletedStock() {
		fail("Not yet implemented");
	}

	@Test
	public void testDisplayLowStock() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlagForOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoadProductDetails() {
		fail("Not yet implemented");
	}

	@Test
	public void testRefreshCombo() {
		fail("Not yet implemented");
	}

	@Test
	public void testResetToDefaultValues() {
		fail("Not yet implemented");
	}

	@Test
	public void testRestoreProduct() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveCategory() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveDetails() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveName() {
		fail("Not yet implemented");
	}

	@Test
	public void testSavePrice() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveSupplier() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveThreshold() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveQuantity() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetupList() {
		fail("Not yet implemented");
	}

}