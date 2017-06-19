package application;

import java.io.IOException;
import java.net.URL; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import application.control.AfficheMessages;
import application.control.EanAjouter; 
import application.control.EanSupprimer;
import application.control.GestionEan;
import application.model.Barcode;
import application.model.Configuration;
import application.model.ObjHistorique;
import application.model.Preferences; 
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button; 
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Main extends Application { 

	private Configuration config;
	private Preferences pref;
	private GestionEan gestionEan;
	private AfficheMessages msg;
	private Map<Barcode, Integer> laMapduStcok;
	private ArrayList<ObjHistorique> historique;
	private EanAjouter eanAjouter;
	private EanSupprimer eanSupprimer; 

	private static Main main;
	

	@Override
	public void start(final Stage primaryStage) {

		main=this;
		setLaMapduStcok(new HashMap<>());
		msg = new AfficheMessages();
		
		try {
		config = new Configuration(); 		
		pref=config.lireLaConfiguration();
		gestionEan = new GestionEan(pref);
		config.rendreLesFichierEnRW(true);
		} catch (Exception e) {
			main.getAfficheurMsg().afficheErreur("Erreur", "dossier en lecture seule, impossible d'écrire");
			Platform.exit();
		    System.exit(0);
		}

		try {
			primaryStage.setTitle("Gestion de Stock");
			final FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/views/MainXml.fxml"));
			final BorderPane rootLayout = (BorderPane) loader.load();
			final Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	@FXML
	public void btnAction(final ActionEvent e) {
		if (e.getSource() == ajoutEanBtn) {
			lanceLaSceneDepuisUrl(application.control.EanAjouter.class.getResource("/views/EanAjouter.fxml"), "Ajouter article");
		} else if (e.getSource() == supprimeEanBtn) {
			lanceLaSceneDepuisUrl(application.control.EanSupprimer.class.getResource("/views/EanSupprimer.fxml"), "Supprimer article");
		} else if (e.getSource() == creationEanBtn) {
			lanceLaSceneDepuisUrl(application.control.EanInventaire.class.getResource("/views/EanInventaire.fxml"), "Inventaire");
		} else if (e.getSource() == rechercherEanBtn) {
			lanceLaSceneDepuisUrl(application.control.EanRechercher.class.getResource("/views/EanRechercher.fxml"), "Rechercher article");
		} else if (e.getSource() == historiqueBtn) {
			lanceLaSceneDepuisUrl(application.control.Historique.class.getResource("/views/Historique.fxml"), "Historique des sorties");
		} else if (e.getSource() == aideBtn) {
			lanceLaSceneDepuisUrl(application.control.Editions.class.getResource("/views/Editions.fxml"), "Editions");
		} else if (e.getSource() == reglagesBtn) {
			lanceLaSceneDepuisUrl(application.control.Reglages.class.getResource("/views/Reglages.fxml"), "RÈglages");
		} else if (e.getSource() == aproposBtn) {
			lanceLaSceneDepuisUrl(application.control.Apropos.class.getResource("/views/Apropos.fxml"), "A propos");
		} else {
			main.getAfficheurMsg().afficheErreur("Erreur", "erreur d'identification du boutton d'appel (btnAction)");
		}
	}

	public static void lanceLaSceneDepuisUrl(final URL url, final String title) {
		try {
			final Parent root1 = (Parent) FXMLLoader.load(url);
			final Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle(title);
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (Exception e2) { 
			main.getAfficheurMsg().afficheErreur("Erreur", e2.getMessage().toString());
		}
	}

	@FXML
	private Button ajoutEanBtn;

	@FXML
	private Button supprimeEanBtn;

	@FXML
	private Button rechercherEanBtn;

	@FXML
	private Button creationEanBtn;

	@FXML
	private Button aideBtn;

	@FXML
	private Button historiqueBtn;

	@FXML
	private Button reglagesBtn;

	@FXML
	private Button aproposBtn; 

	@FXML
	private VBox root;


	@FXML
	private void rechargerMain() throws Exception {
		final Scene scene = root.getScene();
		scene.setRoot(FXMLLoader.load(Main.class.getResource("view/MainXml.fmxl")));
	}

	@Override
	public void stop(){
		config.rendreLesFichierEnRW(false);
	}

	public GestionEan getGestionEan() {
		return gestionEan;
	}

	public AfficheMessages getAfficheurMsg() {
		return msg;
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public Preferences getPref() {
		return pref;
	}

	public void setPref(Preferences pref) {
		this.pref = pref;
	}

	public void setGestionEan(GestionEan gestionEan) {
		this.gestionEan = gestionEan;
	}

	public Map<Barcode, Integer> getLaMapduStcok() {
		return laMapduStcok;
	}

	public void setLaMapduStcok(Map<Barcode, Integer> laMapduStcok) {
		this.laMapduStcok = laMapduStcok;
	}
	public static Main getMain() {
		return main;
	}

	public static void setMain(Main main) {
		Main.main = main;
	}
	public ArrayList<ObjHistorique> getHistorique() {
		return historique;
	}

	public void setHistorique(ArrayList<ObjHistorique> historique) {
		this.historique = historique;
	}

	public EanAjouter getEanAjouterView() {
		return eanAjouter;
	}

	public void setEanAjouterView(EanAjouter eanAjouter) {
		this.eanAjouter = eanAjouter;
	}
	public EanSupprimer getEanSupprimerView() {
		return eanSupprimer;
	}

	public void setEanSupprimerView(EanSupprimer eanSupprimer) {
		this.eanSupprimer = eanSupprimer;
	}

}
