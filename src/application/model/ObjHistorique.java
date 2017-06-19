package application.model;

import java.io.Serializable; 

public class ObjHistorique implements Serializable {

	/**
	 * 
	 */
	String barcode;
	String description;
	String date;
	String qty; 
	
	private static final long serialVersionUID = 1L;
	
	public ObjHistorique(Barcode barcode, String qty,String date){
		this.barcode=barcode.getBarcode();
		this.description=barcode.getDescription();
		this.qty=qty; 
		this.date=date;		
	}
	
	public String getBarcode() {
		return barcode;
	}

	public String getDescription() {
		return description;
	} 

	public String getDate() {
		return date;
	}
 

	public String getQty() {
		return qty;
	} 
}

