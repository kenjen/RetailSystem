package data;
import java.util.ArrayList;


public class Product {
	
	private static int nextId;
	private int id;
	private String name;
	private Supplier supplier;
	private boolean available = false;
	private double price;
	private boolean deleted = false;
	private boolean discounted;
	private double discountedPercentage;
	private int quantity;
	private int lowStockOrder;
	private String category;
	
	private ArrayList<Product> products = new ArrayList<Product>();
	
	public Product(){
		
	}
	
	public Product(String name, String category,  int quantity, double price, Supplier supplier, boolean available, int lowStockOrder){
		this.name = name;
		this.category = category;
		this.quantity = quantity;
		this.price = price;
		this.supplier = supplier;
		this.available = available;
		this.lowStockOrder = lowStockOrder;
		nextId++;
		this.id = nextId;
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
	
	public void setPrice(double price){
		this.price = price;
	}
	
	public boolean upDateProducts(){
		return false;
	}
	
	public double applyDiscount(double amount){
		this.discountedPercentage = amount /100;
		this.discounted = true;
		return this.price * discountedPercentage - price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDiscounted() {
		return discounted;
	}

	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
	}

	public double getDiscountedPercentage() {
		return discountedPercentage;
	}

	public void setDiscountedPercentage(double discountedPercentage) {
		this.discountedPercentage = discountedPercentage;
	}

	public int getLowStockOrder() {
		return lowStockOrder;
	}

	public void setLowStockOrder(int lowStockOrder) {
		this.lowStockOrder = lowStockOrder;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

}
