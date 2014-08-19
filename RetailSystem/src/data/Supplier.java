package data;

public class Supplier {
	private int supplierId;
	private static int generatedId = 100;
	private String supplierName;
	private String supplierAddress;
	private boolean supplierDeleted=false;

	public Supplier(String name, String address) {
		generatedId++;
		this.supplierId=generatedId;
		this.supplierName=name;
		this.supplierAddress=address;
		
	}
	
	public Supplier(){
		this.supplierId=0;
		this.supplierName="";
		this.supplierAddress="";
	}
	public static int getGeneratedId() {
		return generatedId;
	}
	public static void setGeneratedId(int generatedId) {
		Supplier.generatedId = generatedId;
	}
	public boolean isSupplierDeleted() {
		return supplierDeleted;
	}
	public void setSupplierDeleted(boolean supplierDeleted) {
		this.supplierDeleted = supplierDeleted;
	}
	public int getSupplierId() {
		return supplierId;
		
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

	
	
}
