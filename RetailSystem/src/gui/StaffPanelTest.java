package gui;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.Product;
import data.Staff;

public class StaffPanelTest {
	
	StaffPanel panel;
	ArrayList<Staff> staff;
	Staff s1;
	
	@Before
	public void setUp() throws Exception {
		panel = new StaffPanel();
		s1 = new Staff("name", "surname", 20.00, "username", "password");
		s1.setId(1);
		staff = new ArrayList<Staff>();
		staff.clear();
		staff.add(s1);
	}

	@Test
	public void testStaffPanel() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStaffbyID() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveDetails() {
		
		s1.setName("test");
		s1.setSurname("test");
		s1.setSalary(5.5);
		s1.setUsername("test");
		s1.setPassword("password");
		
		panel.saveDetails();
		
		if(s1.getName().length()==0){
			fail("Name not Saved");
		}
		else if(s1.getSurname().length()==0){
			fail("Surname not saved");
		}
		else if(s1.getUsername().length()==0){
			fail("UserName not saved");
		}
		else if(s1.getPassword().length()==0){
			fail("Password not saved");
		}
		
		//fail("Not yet implemented");
	}

	@Test
	public void testGetStaffName() {
		fail("Not yet implemented");
	}

}
