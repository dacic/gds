package application.control;

import java.util.Map; 
import application.Main;
import application.model.Barcode;
import application.model.FormaterBarcode;
import application.model.Preferences;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

/**
 * La gestion des {@link Barcode}: lecture et enregistrement depuis la base de donn�es {@link GestionBDDCsv}
 * @author DACIC Enis
 * @author Projet NFA019
 */
public final class GestionEan { 
	private Preferences pref;
	private GestionBDDCsv gestionBDDCsv;
	private GestionBDDSql gestionBDDSql;  
	private Main main=Main.getMain();
	private FormaterBarcode formaterBarcode;
	/** 
	 *  
	 * Constructor.
	 * Barcode Engine, manage barcode records from {@link GestionBDDCsv} database
	 * 
	 */
	public GestionEan(Preferences pref){   
		this.pref=pref;	 
		gestionBDDCsv = new GestionBDDCsv();
		if (pref.isUtiliserBaseSql()){
			gestionBDDSql = new GestionBDDSql();
		}
		formaterBarcode = new FormaterBarcode(); 
		main.setLaMapduStcok(gestionBDDCsv.lectureBaseDeDonneesArticles());
		main.setHistorique(gestionBDDCsv.lireHistorique());
	}
	
	/**Ajouter code-barres � la BDD
	 * @param barcode 
	 * 
	 */
	public final void ajouterEan(final String eanBrut,final String qtyStr,final String desrcription){	 
		if (eanBrut==null || eanBrut.length() <1 || eanBrut.length()> 13 ){
			main.getAfficheurMsg().afficheWarning("Erreur", ("erreur code barres 0 ou plus de 13 caractéres"));
			return;
		}
		
		if (!estNombreEntier(eanBrut)){
				main.getAfficheurMsg().afficheWarning("Erreur", ("le code barres : ".concat(eanBrut).concat(" \nest incorrect, 8 ou 13 chiffres")));
				return;
		} 
		
		if (!estNombreEntier(qtyStr)){
				main.getAfficheurMsg().afficheWarning("Erreur", ("la quantité ".concat(qtyStr).concat(" \nest incorrecte, chiffre reel, positif")));
				return;
		}
	
		final String ean= formaterBarcode.formatCode(eanBrut); 
		
		if (pref.isUtiliserBaseSql()){		
			gestionBDDSql.ajouterEan(ean,qtyStr,desrcription);
		}
			gestionBDDCsv.ajouterEan(ean,qtyStr,desrcription);  	
	
	}
	
	public final void supprimerEan(final String eanBrut,final String qtyStr){	 
		if (eanBrut==null || eanBrut.length() <1 || eanBrut.length()> 13 ){
			main.getAfficheurMsg().afficheWarning("Erreur", ("erreur code barres 0 ou plus de 13 caractères"));
			return;
		}
		if (!estNombreEntier(eanBrut)){
			main.getAfficheurMsg().afficheWarning("Erreur", ("le code barres : ".concat(eanBrut).concat(" \nest incorrect, 8 ou 13 chiffres")));
			return;
		} 
		
		if (!estNombreEntier(qtyStr)){
			main.getAfficheurMsg().afficheWarning("Erreur", ("la quantité ".concat(qtyStr).concat(" \nest incorrecte, chiffre reel, positif")));
			return;
		}
		final String ean= formaterBarcode.formatCode(eanBrut);
		 
		if (pref.isUtiliserBaseSql()){
			gestionBDDSql.suprimerEan(ean,qtyStr);
		}
			gestionBDDCsv.suprimerEan(ean,qtyStr);  	

	}
	
	public void enregistrerLaBase(final Map<Barcode, Integer> baseArticles) {
		if (pref.isUtiliserBaseSql()){
			gestionBDDSql.enregistrerLaBase(baseArticles);
		}
			gestionBDDCsv.enregistrerLaBase(baseArticles);
	}
	
	public void modifierEanDescription(String barcode, String newDescription){
		 	gestionBDDCsv.modifierDesriptionEanCsv(barcode,newDescription);
		if (pref.isUtiliserBaseSql()){
			gestionBDDSql.modifierDesriptionEanSql(barcode, newDescription);
		}		
	}
	
	public void modifierQtyMinCsv(String barcode, String qtyMin){			
		gestionBDDCsv.modifierQtyMinCsv(barcode, qtyMin);
		if (pref.isUtiliserBaseSql()){
			gestionBDDSql.modifierDesriptionEanSql(barcode,qtyMin);
		}	
	}
	
	public void modifierQtyMinCsvPourTous(String qtyMin,ProgressBar progressBar,Stage stage) {
		gestionBDDCsv.modifierQtyMinCsvPourTous( qtyMin, progressBar,stage);
		if (pref.isUtiliserBaseSql()){
			gestionBDDSql.modifierQtyMinCsvPourTous(qtyMin, progressBar);
			//TODO secodrary progress sql
		}
	}
	
	public String getDescription(String ean){
		return gestionBDDCsv.getDesriptionEanCsv(ean);	
	}
	public GestionBDDSql getGestionBDDSql() {
		return gestionBDDSql;
	}
	
    private static boolean estNombreEntier(String str) {
        if (str == null) {
            return false;
        }
        if (str.contains("-")) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
 
}