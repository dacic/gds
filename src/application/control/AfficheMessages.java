package application.control;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * Affichage les fen�tres de messages (erreurs, informations, avertissement, confirmations)
 * @author DACIC Enis
 * @author Projet NFA019
 * permet de creer un seul objet Alert par type de message et non pour chaque message 1 objet 
 */
public class AfficheMessages {
	
	final Alert erreur = new Alert(AlertType.ERROR);
	final Alert info = new Alert(AlertType.INFORMATION); 
	final Alert warn = new Alert(AlertType.WARNING); 
    /**
	 * 
	 */
	public void afficheur(Alert alert,String titre, String msg) {	 
		alert.setTitle(titre);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	public void afficheErreur(final String titre, final String msg) {		
		afficheur(erreur, titre, msg);
	}
	public void afficheInfo(final String titre, final String msg) {
		afficheur(info, titre, msg);
	}
	public void afficheWarning(final String titre, final String msg) {		
		afficheur(warn, titre, msg);
	} 

}
