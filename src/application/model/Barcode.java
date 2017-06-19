package application.model;


public class Barcode {
  
	/**
	 * 
	 */
	private String barcode;
	private String description ="-"; 
	private String qty_min="000";   

	public Barcode(final String barcode, final String description){
		this.barcode = barcode;
		this.description=description;  
	}
	
	public Barcode(final String barcode, final String description, String qty_min){
		this.barcode = barcode;
		this.description=description;  
		this.qty_min=qty_min;
	}
	
	
	public Barcode(String barcode){
		this.barcode = barcode; 
	}
	
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() { 
		return description;
	}
	
 
	

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int  getQty_min(){
		int x=-1;
		if (this.qty_min.equals("---")){
			return -1;
		}
		try {
			x=Integer.parseInt(this.qty_min);
		} catch (NumberFormatException e){
			
		}
		return x;
	}
	
	public String getStrQty_min(){	 
		return this.qty_min;
	}
	
	public void setQty_min(String qty){ 
		if (qty.length()==1){
			qty="00".concat(qty);
		} else if (qty.length()==2){
			qty="0".concat(qty);
		}  
		 this.qty_min=qty;
	} 
	
	public void removeQty_min() {
		this.qty_min="---";
	}
	 
}
