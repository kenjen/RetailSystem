package data;

import java.text.DecimalFormat;

public class ProductToOrder {

	private int id;
	private String name;
	private Supplier supplier;
	private double price;
	private boolean discounted;
	private String category;
	private int amount;
		
	public ProductToOrder(int id, String name, Supplier supplier, String category, double price, boolean discounted, int amount) {
		this.id = id;
		this.name = name;
		this.supplier = supplier;
		this.price = price;
		this.discounted=discounted;
		this.category = category;
		this.amount=amount;
	}
	
	public ProductToOrder(){
		
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

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isDiscounted() {
		return discounted;
	}

	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public String toHtmlString(){
		DecimalFormat df = new DecimalFormat("#.00");
		String returnable ="<tr><td>"+id+"</td><td>"+name+"</td><td>"+supplier.getSupplierName()+"</td><td>"+category+"</td><td>"+price+"</td><td>"+amount+"</td><td>"+df.format(amount*price)+"</td></tr>";
		return returnable;
		
	}
	

}
