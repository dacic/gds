package application.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map.Entry;

import application.Main;
import application.model.Barcode;
import application.model.Preferences;
import application.model.SQLConnexion;
import javafx.application.Application;
import javafx.application.Platform; 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ReglagesSQL extends Application {

	private Preferences pref;
	private Main main;
	private SQLConnexion sql;
	private Connection sqlConnnexion;

	@Override
	public void start(Stage primaryStage) throws Exception {
	}

	@FXML
	private Button backAddItemBtn;

	@FXML
	private Button testerSql;

	@FXML
	private Button synch;

	@FXML
	private Button setsave;

	@FXML
	private ProgressBar progress1 = new ProgressBar();

	@FXML
	private PasswordField mdptxt;

	@FXML
	private TextField usertxt;

	@FXML
	private TextField porttxt;

	@FXML
	private TextField urltxt;

	@FXML
	private TextField bddtxt;

	@FXML
	public void backBtnAction(ActionEvent e) {
		Stage stage = (Stage) backAddItemBtn.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void testBtn(ActionEvent e) {

		try {
			sql = new SQLConnexion(pref);
			sql.connect();
		} catch (Exception sqlE) {
			pref.setUtiliserBaseSql(false);
			main.getAfficheurMsg().afficheErreur("Error sql: ", sqlE.getMessage().concat(" erreur connection sql "));
		}

		try {
			if (sql.testerSiBddExiste()) {
				ImageView imageView = new ImageView(
						new Image(getClass().getResource("/images/bddgreen.png").toExternalForm()));
				imageView.setFitHeight(30);
				imageView.setFitWidth(30);
				testerSql.setGraphic(imageView);
				pref.setUtiliserBaseSql(true);
				main.setPref(pref);
				main.getConfig().sauvgarderConfiguration(pref);
			} else {
				pref.setUtiliserBaseSql(false);
			}
		} catch (Exception e2) {
			main.getAfficheurMsg().afficheErreur("SQL Error: ", e2.getMessage().concat(" erreur connection sql "));
			pref.setUtiliserBaseSql(false);
		}

	}

	@FXML
	void initialize() {
		main = Main.getMain();
		pref = main.getPref();
		lireLesPreferencesActuelles();
		testerSql.setDisable(true);

		try {
			sqlConnnexion = new SQLConnexion(main.getPref()).connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		progress1.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
		progress1.setVisible(false);

	}

	@FXML
	private void savesettings(ActionEvent e) {

		if (!porttxt.getText().trim().isEmpty() && !urltxt.getText().trim().isEmpty()
				&& !usertxt.getText().trim().isEmpty() && !mdptxt.getText().trim().isEmpty()) {

			pref.setSqlurl(urltxt.getText());
			pref.setPort(Integer.valueOf(porttxt.getText()));
			pref.setSqlbdd(bddtxt.getText());
			pref.setSqluser(usertxt.getText());
			pref.setSqlpass(mdptxt.getText());
			testerSql.setDisable(false);
		}
	}

	private void lireLesPreferencesActuelles() {
		urltxt.setText(pref.getSqlurl());
		porttxt.setText(String.valueOf(pref.getPort()));
		usertxt.setText(pref.getSqluser());
		bddtxt.setText(pref.getSqlbdd());
		mdptxt.setText("000000");
	}

	public void startSync() {
		Runnable task = new Runnable() {
			public void run() {
				runTask();
			}
		};
		Thread backgroundThread = new Thread(task);
		backgroundThread.setDaemon(true);
		backgroundThread.start();
	}

	public void runTask() {

		progress1.setVisible(true); 
		
		int i = 1;
		int max = main.getLaMapduStcok().size();

		for (Iterator<Entry<Barcode, Integer>> it = main.getLaMapduStcok().entrySet().iterator(); it.hasNext();) {
		 

			Entry<Barcode, Integer> entry = it.next();
			i++;
		 			
		 if (i == max){
			updateProgress(i,max);}
			ajouterEan(entry.getKey().getBarcode(), String.valueOf(entry.getValue()), entry.getKey().getDescription());

		} 
	}

	public void ajouterEan(final String ean, final String qty, final String desrcription) {
		
		int fqty = 0;
		try {
			fqty = Integer.parseInt(qty);
		} catch (NumberFormatException nb) {

		}
		if (verifierSiExiste(ean)) {

			String sql = "UPDATE stock SET qty = ? WHERE ean = ?";
			try {
				PreparedStatement preparedStatement = sqlConnnexion.prepareStatement(sql);
				preparedStatement.setInt(1, fqty);
				preparedStatement.setString(2, ean);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
			}
		} else {
			String sql = "INSERT INTO stock(ean, description,qty) VALUES(?,?,?)";
			try {
				PreparedStatement preparedStatement = sqlConnnexion.prepareStatement(sql);
				preparedStatement.setString(1, ean);
				preparedStatement.setInt(3, fqty);
				preparedStatement.setString(2, desrcription);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
			}
		}
	}

	private boolean verifierSiExiste(String ean) {
		boolean existe = false;
		String sql = "SELECT ean FROM stock where ean=?";
		try {
			PreparedStatement preparedStatement = sqlConnnexion.prepareStatement(sql);
			preparedStatement.setString(1, ean);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				existe = true;
			}
		} catch (SQLException e) {

		}
		return existe;
	}
  private void updateProgress(final int i,final int max){
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				progress1.setProgress(i/max);
				
			}					
			});
  }
}