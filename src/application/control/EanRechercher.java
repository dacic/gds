package application.control;

import java.util.ArrayList; 
import java.util.Iterator;
import java.util.Map.Entry;
import application.Main;
import application.model.Barcode; 
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML; 
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage; 

public class EanRechercher extends Application {

	private Main main;

	@FXML
	void initialize() {
		main=Main.getMain();

		ArrayList<String> barcode= new ArrayList<>();
		ArrayList<String> descritpion= new ArrayList<>();   
		ArrayList<String> resultList = new ArrayList<>();
		ArrayList<String> qty = new ArrayList<>();


		for(Iterator<Entry<Barcode, Integer>> it =  main.getLaMapduStcok().entrySet().iterator(); it.hasNext(); ) {
			Entry<Barcode, Integer> entry = it.next();
			barcode.add(entry.getKey().getBarcode());
			descritpion.add(entry.getKey().getDescription());
			qty.add(entry.getValue().toString());
		}
		
		barcodeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
				 if (!Character.isDigit(barcodeTextField.getText().charAt(0)) ){ 
		             main.getAfficheurMsg().afficheErreur("erreur","Merci d'activer la touche 'verr num'  de votre clavier.\n(verouillage du clavier numérique)");
		             barcodeTextField.setText("");
		             return;
				 	}  
				} 				
				returnToBarcodeView();
			}
		}); 
 
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			resultList.clear();
			if (newValue.length()>=1){ //min 1 caractere? selon taille de la base
				for (int x=0;x<barcode.size();x++){
					if (estNombreEntier(newValue)){
						if (barcode.get(x).contains(newValue.toLowerCase()) || barcode.get(x).contains(newValue.toUpperCase())){
							resultList.add(barcode.get(x).concat(" x ").concat(qty.get(x).concat("\n").concat(descritpion.get(x))));
						}
					} else { 

						if (descritpion.get(x).contains(newValue.toLowerCase()) || descritpion .get(x).contains(newValue.toUpperCase())){
							resultList.add(barcode.get(x).concat(" x ").concat(qty.get(x).concat("\n").concat(descritpion.get(x))));
						}
					}
				}				
			}
			searchResult.setItems(FXCollections.observableArrayList(resultList)); 
		});
	} 




	@Override
	public void start(Stage primaryStage) throws Exception {

	}

	@FXML private Button backAddItemBtn;

	@FXML private Button rechercherEanBtn;

	@FXML private TextField barcodeTextField;

	@FXML private ListView<String> searchResult;


	@FXML
	public void backBtnAction(ActionEvent e){ 	
		Stage stage = (Stage) backAddItemBtn.getScene().getWindow();     
		stage.close();
	}

	@FXML
	public void rechercherEan(ActionEvent e){ 	 
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
	
	private void returnToBarcodeView(){
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		        barcodeTextField.requestFocus();
		    }
		});
	}

}
