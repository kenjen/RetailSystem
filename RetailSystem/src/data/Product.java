package data;
import java.util.ArrayList;


public class Product {
	
	private int id;
	private String name;
	private Supplier supplier;
	private boolean available;
	private double price;
	private boolean deleted;
	private boolean discounted;
	private double discountedPercentage;
	private int quantity;
	private int lowStockOrder;
	private String category;
	
	private ArrayList<Product> products = new ArrayList<Product>();
	
	public Product(int id, String name, String category,  int quantity, double price, Supplier supplier){
		this.name = name;
		this.category = category;
		this.quantity = quantity;
		this.price = price;
		this.supplier = supplier;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getCategory(){
		return category;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	
	public double getPrice(){
		return price;
	}
	
	public void setPrice(){
		this.price = price;
	}

}
