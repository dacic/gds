package application.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream; 
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import application.Main;
import application.model.Barcode;  
import application.model.InterfaceEan;
import application.model.ObjHistorique;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage; 

public class GestionBDDCsv implements InterfaceEan { 
	/**
	 * 
	 */ 
	private Main main;
	private final File fichierBDD =new File ("GDS/GestionDeStockBDD.csv");
	private final File fichierHistorique =new File ("GDS/historique.csv");
	private final Format formatDate = new SimpleDateFormat("dd-MMM-yy");
	
	
	public GestionBDDCsv(){
		main=Main.getMain();
	}
 
    /**
	 * 
	 * Lecture base de donn�es csv
	 */
	public Map<Barcode, Integer> lectureBaseDeDonneesArticles() {
		Map<Barcode, Integer> baseArticles = new HashMap<>();
	    if (fichierBDD.exists()) {
	        FileReader fr = null;        
	        try {
	            fr = new FileReader(fichierBDD);
	        } catch (FileNotFoundException e) {
	        	main.getAfficheurMsg().afficheErreur("erreur","Fichier de Base de données introuvable");
	        }
	        
	        BufferedReader br = new BufferedReader(fr);
	        String line = " ";
	        try {
	            while ((line = br.readLine()) != null) { 
	                String[] RowData = line.split(","); 
	                String ean = RowData[0];
	                String description = RowData[1];
	                String qty_min=RowData[3];
	                 int x=0;
	                 try {
	                    x = Integer.parseInt(RowData[2]);
	                } catch (Exception e) {
	                	main.getAfficheurMsg().afficheErreur("Erreur", ("erreur lecture base de donnée csv \n fichier corrompu, quantité erronée").concat(fichierBDD.getName().toString()));    			                
	                }
	                baseArticles.put(new Barcode(ean, description,qty_min),x);
	            }

	        } catch (Exception e) {
	        	main.getAfficheurMsg().afficheErreur("Erreur", ("erreur lecture base de donnée csv \n fichier corrompu").concat(fichierBDD.getName().toString())); 
	        } finally {
	            try {
	                br.close();
	                fr.close();
	            } catch (Exception e) {
	            	main.getAfficheurMsg().afficheErreur("Erreur",("Erreur fermeture de fichier  ").concat(fichierBDD.getName().toString()));
	            }
	        }
	    }
	    return baseArticles;
	}
	
	
    /**
	 * this method saves  
	 *ArrayList of {@link Barcode} to database csv file
	 * @param File database file
	 * @param path The path to the application folder
	 */
	public void enregistrerLaBase(final Map<Barcode, Integer> baseArticles) {
	 
	    try { 
	            if (!fichierBDD.exists()) {
	                fichierBDD.createNewFile();
	            }
	            FileWriter writer = new FileWriter(fichierBDD);	            
	            baseArticles.forEach((k,v)->{		 
							 try {
								writer.append(k.getBarcode()).append(",").append(k.getDescription()).append(",").append(String.valueOf(v)).append(",").append(k.getStrQty_min()).append("\n");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}				 
				 });
	            writer.flush();
	            writer.close();
	    } catch (IOException e) {  
	    	
	    	main.getAfficheurMsg().afficheErreur("Erreur", ("erreur e/s lors de l'écriture du ficier \n").concat(fichierBDD.getName().toString()));
	    }
	}
	
	public void enregistrerHistorique(final ArrayList<ObjHistorique> historique) {
		if (!fichierHistorique.exists()) {
			try {
				fichierHistorique.createNewFile();
			} catch (IOException e) {
			}
		}
		try {
			FileOutputStream fos = new FileOutputStream(fichierHistorique, false);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(historique);
			out.flush();
			out.close();
		} catch (IOException e) {

		}  
	}

 
	@SuppressWarnings("unchecked") // au pire retourne historique vide 
	public final ArrayList<ObjHistorique> lireHistorique() {
		ArrayList<ObjHistorique> listHistorique=new ArrayList<ObjHistorique>();;		 
		if (fichierHistorique.exists()) {
			try {
				FileInputStream fis = new FileInputStream(fichierHistorique);
				ObjectInputStream in = new ObjectInputStream(fis);
				listHistorique = (ArrayList<ObjHistorique>) in.readObject();
				in.close();
			} catch (Exception e) { 
			}  
		}  
		return listHistorique; 
	}


	@Override
	public void ajouterEan(final String ean, final String qtyStr, String desrcription) {

	final int qty= Integer.parseInt(qtyStr);
	
	int old_qty=0; 	 
 
 
	    for(Iterator<Entry<Barcode, Integer>> it =  main.getLaMapduStcok().entrySet().iterator(); it.hasNext(); ) {
	        Entry<Barcode, Integer> entry = it.next();
	        if(entry.getKey().getBarcode().equals(ean)) {
	        	if (desrcription==null || desrcription.length() <1){      
	        		desrcription = entry.getKey().getDescription()==null?" ":(desrcription=entry.getKey().getDescription());

	        	} 
	        
	        	old_qty=entry.getValue();         	
	        	it.remove();
	        }
	    } 
		main.getLaMapduStcok().put(new Barcode(ean,desrcription), old_qty+qty); 
		 
		if (main.getEanAjouterView()!=null){ 
	         Platform.runLater(new Runnable() { //runOnUiThread?
	             @Override
	             public void run() {
			main.getEanAjouterView().getBarcodeTextField().clear(); 
			main.getEanAjouterView().getDescriptionTextArea().clear();
	             }
	          });
		            }
	 
		enregistrerLaBase(main.getLaMapduStcok());
	}


	@Override
	public void suprimerEan(final String ean, String qtyStr) {
		 

		
		final int qty= Integer.parseInt(qtyStr); 
		int old_qty=0; 
		Boolean trouvee=false;
		String description=" ";
		
	    for(Iterator<Entry<Barcode, Integer>> it =  main.getLaMapduStcok().entrySet().iterator(); it.hasNext(); ) {
	        Entry<Barcode, Integer> entry = it.next();
	        if(entry.getKey().getBarcode().equals(ean)) {
	        	trouvee=true;
	        	old_qty=entry.getValue(); 
	        	description=entry.getKey().getDescription();
	        	if ((old_qty-qty) >-2){ //garde les qty 0 pour le commande
	        		 it.remove();
		            	if ((old_qty-qty) <= entry.getKey().getQty_min()){
			        		main.getAfficheurMsg().afficheWarning("Erreur", ("quantité min atteinte, pensez a réapprovisionner"));
			        	}
	        		Barcode barcode = new Barcode(ean,description);
	        		main.getLaMapduStcok().put(barcode, old_qty-qty); 
	        		//main.getAfficheurMsg().afficheInfo("Info", (("suppression " + qty).concat(" x ").concat(ean)));
	            	enregistrerLaBase(main.getLaMapduStcok()); 
	            	main.getHistorique().add(new ObjHistorique(barcode,qtyStr,formatDate.format(new Date())));
	            	enregistrerHistorique(main.getHistorique());
	            	
	            	if (main.getEanSupprimerView()!=null){
	            		main.getEanSupprimerView().getBarcodeDelTextField().clear();
	            		main.getEanSupprimerView().getQtyTextFieldDel().clear();
	            	}
	        		return;
	        	} else if ((old_qty-qty) <=-1){
	        		main.getAfficheurMsg().afficheErreur("Erreur", ("impossible, quantité restante " + old_qty));
	        		return;
	        	}
	        }  
	    }    
    	if(!trouvee){
         	main.getAfficheurMsg().afficheErreur("Erreur", (ean.concat("  introuvable")));}
	}
	

	public void modifierDesriptionEanCsv(String barcode,String newDescription) {
		// TODO Auto-generated method stub
		 for (Iterator<Entry<Barcode, Integer>> it = main.getLaMapduStcok().entrySet().iterator(); it
					.hasNext();) {
				Entry<Barcode, Integer> entry = it.next();
				if (entry.getKey().getBarcode().equals(barcode)){						
					entry.getKey().setDescription(newDescription); 
				}
			}
			this.enregistrerLaBase(main.getLaMapduStcok());		
	}
	
	public String getDesriptionEanCsv(String barcode) { 
		String desc=" ";
		 for (Iterator<Entry<Barcode, Integer>> it = main.getLaMapduStcok().entrySet().iterator(); it
					.hasNext();) {
				Entry<Barcode, Integer> entry = it.next();
				if (entry.getKey().getBarcode().equals(barcode)){						
					desc = entry.getKey().getDescription(); 
				}
			}
			return desc;
	}
	
	public void modifierQtyMinCsv(String barcode,String qtyMin) { 
		 for (Iterator<Entry<Barcode, Integer>> it = main.getLaMapduStcok().entrySet().iterator(); it
					.hasNext();) {
				Entry<Barcode, Integer> entry = it.next();
				if (entry.getKey().getBarcode().equals(barcode)){	 					
					entry.getKey().setQty_min(qtyMin); 
				}
			}
			this.enregistrerLaBase(main.getLaMapduStcok());
		
	}
	
	public void modifierQtyMinCsvPourTous(String qtyMin,ProgressBar progressBar,Stage stage) { 
	

	}
 
}
