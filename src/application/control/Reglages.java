package application.control;
 
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue; 
import javafx.event.ActionEvent;
import application.Main;
import application.model.Configuration;
import application.model.Preferences;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class Reglages extends Application { 
	private Preferences pref;
	private Configuration config;
	private Main main;


	@Override
	public void start(Stage primaryStage) throws Exception { 
	}

	@FXML
	private Button backAddItemBtn;

	@FXML
	CheckBox utiliserSon;

	@FXML
	CheckBox utiliserAide;

	@FXML
	CheckBox utiliserSql;

	@FXML
	public void backBtnAction(ActionEvent e) {
		Stage stage = (Stage) backAddItemBtn.getScene().getWindow();
		stage.close();
	}

	@FXML
	void initialize() {
		main=Main.getMain();
		setConfig(main.getConfig());
		setPref(main.getPref());
		lireLesPreferencesActuelles();
		utiliserSon.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				getPref().setUtiliserEffetSonore(new_val);
				config.sauvgarderConfiguration(pref);
			}
		});
 
		utiliserAide.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				getPref().setAfficherMessageAide(new_val);
				config.sauvgarderConfiguration(pref);
			 }
		});
		utiliserSql.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				if (!old_val){
					application.Main.lanceLaSceneDepuisUrl(application.control.ReglagesSQL.class.getResource("/views/ReglagesSQL.fxml"), "Réglages SQL");
					getPref().setUtiliserBaseSql(new_val);
					config.sauvgarderConfiguration(pref);
				} else {
					getPref().setUtiliserBaseSql(new_val);
					config.sauvgarderConfiguration(pref);
				}
			}
		});
	}

	private void lireLesPreferencesActuelles() {
		utiliserSon.selectedProperty().setValue(pref.isUtiliserEffetSonore()); 
		utiliserAide.selectedProperty().setValue(pref.isAfficherMessageAide());
		utiliserSql.selectedProperty().setValue(pref.isUtiliserBaseSql());
	}
	
	public Preferences getPref() {
		return pref;
	}

	public void setPref(Preferences pref) {
		this.pref = pref;
	}
	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}
}
