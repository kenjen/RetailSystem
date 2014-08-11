package data;

public class Staff {
	private static int nextId;
	private int id;
	private String name;
	private String surname;
	private double salary;
	private int holidayDaysLeft;
	private boolean admin = false;
	private String username;
	private String password;

	public Staff() {
		
	}
	
	public Staff(String name, String surname, double salary, String username, String password) {
		this.name=name;
		this.surname=surname;
		this.salary=salary;
		this.username=username;
		this.password=password;
		this.holidayDaysLeft = 21;
		nextId ++;
		this.id=nextId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int getHolidayDaysLeft() {
		return holidayDaysLeft;
	}

	public void setHolidayDaysLeft(int holidayDaysLeft) {
		this.holidayDaysLeft = holidayDaysLeft;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
