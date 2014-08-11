package data;

//import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerOrder {

	private int id;
	private Date creationDate;
	private Customer customer;
	private Staff staff;
	private Product products;
	private double totalGross;
	private double totalNet;
	
	
	
	public CustomerOrder(int id, Date creationDate, Customer customer, Staff staff, Product products, double totalGross, double totalNet) {
		this.id = id;
		this.customer = customer;
		this.creationDate = creationDate;		
		this.staff = staff;
		this.products = products;
		this.totalGross = totalGross;
		this.totalNet = totalNet;
		
		
		
	}
	
	String date= "2013-09-06T14:15:11.557";
    DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
   // Date d=df.parse(date); {
    //df=new SimpleDateFormat("yyyy-MMM-dd hh:mm");
    //System.out.println(df.format(d));
   // }

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Staff getStaff() {
		return staff;
	}


	public void setStaff(Staff staff) {
		this.staff = staff;
	}


	public Product getProducts() {
		return products;
	}


	public void setProducts(Product products) {
		this.products = products;
	}


	public double getTotalGross() {
		return totalGross;
	}


	public void setTotalGross(double totalGross) {
		this.totalGross = totalGross;
	}


	public double getTotalNet() {
		return totalNet;
	}


	public void setTotalNet(double totalNet) {
		this.totalNet = totalNet;
	}

}