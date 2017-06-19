package application.model;

import javafx.beans.property.SimpleStringProperty;

//objet edition pour le tableau de la class edition 

public class EditionsObj{
	
	private SimpleStringProperty barcode;
	private final SimpleStringProperty ref;
	private final SimpleStringProperty now;
	private final SimpleStringProperty alerte;
	private final SimpleStringProperty cmd;
	public EditionsObj(final String barcode, final String ref,final String now,final String alerte, final String cmd){
		this.barcode = new SimpleStringProperty(barcode);
		this.ref=new SimpleStringProperty(ref);
		this.now=new SimpleStringProperty(now);
		this.alerte=new SimpleStringProperty(alerte);
		this.cmd=new SimpleStringProperty(cmd);		
	}
	public String getRef() {
		return ref.get();
	}
	public void setRef(String ref) {
		this.ref.set(ref);
	}
	public String getNow() {
		return now.get();
	}
	public void setNow(String now) {
		this.now.set(now);
	}
	public String getAlerte() {
		return alerte.get();
	}
	public void setAlerte(String alerte) {
		this.alerte.set(alerte);
	}
	public String getCmd() {
		return cmd.get();
	}
	public void setCmd(String cmd) {
		this.cmd.set(cmd);
	}
	public String getBarcode() {
		return barcode.get();
	}
	public void setBarcode(String barcode) {
		this.barcode.set(barcode);
	}
	public int calculCmd(){  // c
		int alrt = Integer.parseInt(this.alerte.get());
		int stock =Integer.parseInt(this.now.get());
		
		if (alrt > stock){
			return alrt-stock;
		}else {
			return 0;	
		}		
	}
}