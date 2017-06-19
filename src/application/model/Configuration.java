package application.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import application.Main;  

/**
 * Enregistrement et lecture de {@link Preferences} dans/du fichies de configuration {@link Serializable} .config  
 * @author DACIC Enis
 * @author Projet NFA019
 */
public class Configuration implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private final File dossierDeBase = new File("GDS");
	private final File fichierConfiguration = new File("GDS/.config");
	private final File fichierBDD =new File ("GDS/GestionDeStockBDD.csv");
	private final File historique =new File ("GDS/historique.csv");
	private String clef ="pas de Clef Aes "; 
	private Main main;
	
	 
	/**
	 * Constructeur par d�faut v�rifie si le fichier .config existe 
	 * si non cr�ation de la clef AES 128 bits
	 *            
	 */
	public Configuration(){	
		main=Main.getMain();
		if (!dossierDeBase.exists()){
			dossierDeBase.mkdirs();
		}
		if(!fichierBDD.exists()){
			try {
				fichierBDD.createNewFile();
			} catch (IOException e) {
				main.getAfficheurMsg().afficheErreur("Erreur", "dossier en lecture seule, impossible d'écrire");				
			}
		}
		if (!fichierConfigExiste()){
			clef = genererClef();
			Preferences  pref = new Preferences(true, true, true, false, clef);
			this.sauvgarderConfiguration(pref);
		}
		if(!historique.exists()){
			try {
				historique.createNewFile();
			} catch (IOException e) {
				main.getAfficheurMsg().afficheErreur("Erreur", e.getMessage().toString());				
			}
		}
	}

	/**
	 * Lecture des options du fichier de configuration .config
	 * @return {@link Preferences} 
	 */
	public Preferences lireLaConfiguration() {
		/**
		 * definition valeurs par d�faut
		 */
		Preferences preferences = new Preferences(true, true, true,false, clef);
 
		if (fichierConfiguration.exists()) {
			try {
				FileInputStream fis = new FileInputStream(fichierConfiguration);
				ObjectInputStream in = new ObjectInputStream(fis);

				preferences = (Preferences) in.readObject();
				in.close();

			} catch (Exception e) {
	
				main.getAfficheurMsg().afficheErreur("erreur", "fichier de configuration inutilisable\nutilisation des options par défaut");

				preferences = new Preferences(true, true, true,false,clef);
			}
		}  
		return preferences;
	}

	/**
	 * Enregistrement des options dans le fichier de configuration .config
	 * @param Preferences
	 *            
	 */
	public void sauvgarderConfiguration(Preferences config) { 
		if (!fichierConfiguration.exists()) {
			try {
				fichierConfiguration.createNewFile();
			} catch (IOException e) {

			}
		}
		try {
			FileOutputStream fos = new FileOutputStream(fichierConfiguration, false);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(config);
			out.flush();
			out.close();
		} catch (IOException e) {

		}
	}

	
	/**
	 * Test si le fichier .config existe, 1ere execution
	 *@return Boolean            
	 */
	public final boolean fichierConfigExiste() { 
		return fichierConfiguration.exists();
	}
	
	
	private String genererClef() {
		KeyGenerator gen = null;
		String clef ="pas de Clef Aes ";
		try {
			gen = KeyGenerator.getInstance("AES/ECB/NoPadding");
			gen.init(128); /* 128-bit AES */
			SecretKey secret = gen.generateKey(); 
			 byte[] binary = secret.getEncoded();
			clef = String.format("%032X", new BigInteger(+1, binary));	
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
		return clef;
	}
	public void  rendreLesFichierEnRW(Boolean rw){
		fichierConfiguration.setWritable(rw);
		fichierBDD.setWritable(rw);
		historique.setWritable(rw);
	}
}
