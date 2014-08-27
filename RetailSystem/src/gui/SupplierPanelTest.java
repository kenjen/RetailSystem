package gui;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.Supplier;

public class SupplierPanelTest {
	SupplierPanel supplierPanel;
	ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	Supplier supplier;

	@Before
	public void setUp() {
		 //throws Exception
		supplierPanel = new SupplierPanel();
		supplier = new Supplier("name", "address");
		supplier.setSupplierId(111);
		suppliers = new ArrayList<Supplier>();
		suppliers.clear();
		suppliers.add(supplier);
	}

	
	@Test
	public void testAddSupplier() {
		supplierPanel.setSupplierName("test");
		supplierPanel.setSupplierAddress("test");
		supplierPanel.createSupplier(supplierPanel.getSupplierName(),supplierPanel.getSupplierAddress());
		if((supplier.getSupplierName().length()==0)&&(supplier.getSupplierAddress().length()==0)){
			fail("Supplier not saved");
		}
	}

	@Test
	public void testAddEditedSupplier() {
	//	supplierPanel.
		//fail("Not yet implemented");
	}

	@Test
	public void testSaveDetails() {
		fail("Not yet implemented");
	}

}
