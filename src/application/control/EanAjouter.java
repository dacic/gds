package application.control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField; 
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import application.Main;

public class EanAjouter extends Application {
	private Main main;

	@FXML
	void initialize() {
		main = Main.getMain();
		main.setEanAjouterView(this);		 

		returnToBarcodeView();
		
		sqlimage.setVisible(main.getPref().isUtiliserBaseSql());
		sqlLabel.setVisible(main.getPref().isUtiliserBaseSql());
	 

		final ChangeListener<Boolean> focusListener = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					returnToBarcodeView();
				}

			}
		};

		labelAjout.setOnMouseClicked(e -> {
			// TODO msg + effacer le dernier ajout?
			returnToBarcodeView();
		});

		checkautoscan.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				descriptionTextField.setDisable(newValue);
				if (newValue) {
					qtyTextField.setText("1");
				}
				returnToBarcodeView();
			}
		});
 

		descriptionTextField.focusedProperty().addListener(focusListener);
		qtyTextField.focusedProperty().addListener(focusListener);
		labelAjout.focusedProperty().addListener(focusListener);

		qtyTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					if (!Character.isDigit(qtyTextField.getText().charAt(0))) {
						main.getAfficheurMsg().afficheErreur("erreur", "chiffre incorrect");
								qtyTextField.setText("1");
						return;
					} else {
						returnToBarcodeView();
					}

				}
			}
		});
		
		barcodeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					if (!Character.isDigit(barcodeTextField.getText().charAt(0))) {
						main.getAfficheurMsg().afficheErreur("erreur",
								"Merci d'activer la touche 'verr num'  de votre clavier.\n(verouillage du clavier numérique)");
						barcodeTextField.setText("");
						return;
					}
				}
				if (checkautoscan.isSelected()) {
					if (keyEvent.getCode() == KeyCode.ENTER) {
						ajoutEanBtn.fire();
					}
				}
				returnToBarcodeView();
			}
		});

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}

	@FXML
	private TextArea labelAjout;

	@FXML
	private TextField barcodeTextField;

	@FXML
	private TextField qtyTextField;

	@FXML
	private TextArea descriptionTextField;

	@FXML
	private Button ajoutEanBtn;

	@FXML
	private ImageView sqlimage;
	
	@FXML
	private Label sqlLabel;

	@FXML
	private Button backAddItemBtn;

	@FXML
	private CheckBox checkautoscan;

	@FXML
	public void ajouterEan(ActionEvent e) {
		String ean = barcodeTextField.getText();
		String description = descriptionTextField.getText();
		String ancienDescription =  main.getGestionEan().getDescription(ean);
		
		if ((barcodeTextField.getText().length()==0)||(qtyTextField.getText().length()==0)){
			return;
		}
        if ((description == null)&&(ancienDescription.length()>0)){
        	description=ancienDescription;
        } else if (description.length()>0){
        	main.getGestionEan().modifierEanDescription(ean, description);
        }
        
        
		main.getGestionEan().ajouterEan(ean, qtyTextField.getText(),
				description.replace("\n", " "));
		

		final String eanAjoutee = ean.concat(" x ").concat(String.valueOf(qtyTextField.getText()).concat("  ").concat(main.getEanAjouterView().getDescriptionTextArea().getText()));
		
		labelAjout.setText(
				eanAjoutee
				.concat("\n")
				.concat(main.getEanAjouterView().getDescriptionAjoutees().getText()));
		barcodeTextField.setText("");
		

		if (checkautoscan.isSelected()) {
			qtyTextField.setText("1");
		} else {
			qtyTextField.setText("");
		}
		descriptionTextField.setText("");
		returnToBarcodeView();

	}

	@FXML
	public void backBtnAction(ActionEvent e) {
		Stage stage = (Stage) backAddItemBtn.getScene().getWindow();
		stage.close();
	}

	public TextField getBarcodeTextField() {
		return barcodeTextField;
	}

	public TextField getQtyTextField() {
		return qtyTextField;
	}

	public TextArea getDescriptionTextArea() {
		return descriptionTextField;
	}

	public TextArea getDescriptionAjoutees() {
		return labelAjout;
	}

	private void returnToBarcodeView() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				barcodeTextField.requestFocus();
			}
		});
	}
	
	public ImageView getSqlImage(){
		return sqlimage;
	}

}
