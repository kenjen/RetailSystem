package test;

import static org.junit.Assert.*;
import gui.Shop;
import gui.StaffPanel;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.Product;
import data.Staff;

public class StaffPanelTest {
	
	Shop shop;
	
	public StaffPanelTest(){
		shop = new Shop();
		shop.populateStaffMembers();
	}
	
	public Staff getStaffName(String name) {
		for (Staff staff : Shop.getStaffMembers()) {
			String sName = staff.getName();
			if (sName.equalsIgnoreCase(name)) {
				return staff;
			}
		}
		return null;
	}
	
	public Staff getStaffbyID(String string) {
		for (Staff staff : Shop.getStaffMembers()) {
			if (staff.getUsername().equalsIgnoreCase(string)) {
				return staff;
			}
		}
		return null;
	}
	
	@Test
	public void testGetStaffbyID() {
		Staff staff = new Staff("Alibaba", "Jaffar", 15, "goldenKingdom", "Jaffar");
		shop.getStaffMembers().add(staff);
		assertEquals(shop.getStaffMembers().get(shop.getStaffMembers().size()-1), getStaffbyID("goldenKingdom"));
	}

	@Test
	public void testGetStaffName() {
		Staff staff = new Staff("Alibaba", "Jaffar", 15, "Alibaba", "Jaffar");
		shop.getStaffMembers().add(staff);
		assertEquals(shop.getStaffMembers().get(shop.getStaffMembers().size()-1), getStaffName("Alibaba"));
	}

}
