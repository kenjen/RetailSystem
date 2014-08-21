package data;

import java.util.Date;

public class Finance {
	private int id;
	private static int nextId;
	private Date date;
	private String description;
	private double amount;
	private boolean isExpense;

	public Finance() {
		
	}
	
	public Finance(double amount, Date date, String description, boolean isExpense){
		nextId++;
		this.amount=amount;
		this.date = date;
		this.description = description;
		this.id=nextId;
		this.isExpense=isExpense;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static int getNextId() {
		return nextId;
	}

	public static void setNextId(int nextId) {
		Finance.nextId = nextId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isExpense() {
		return isExpense;
	}

	public void setExpense(boolean isExpense) {
		this.isExpense = isExpense;
	}
}
