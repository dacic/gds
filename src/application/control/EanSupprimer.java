package application.control;

import application.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField; 
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class EanSupprimer extends Application {
	Main main;

	@Override
	public void start(final Stage primaryStage) throws Exception {
			
	}
	
	@FXML
	void initialize() {
	main=Main.getMain();
	main.setEanSupprimerView(this);
	
	sqlimagesuprim.setVisible(main.getPref().isUtiliserBaseSql());
	sqlLabelsuprim.setVisible(main.getPref().isUtiliserBaseSql());

	barcodeDelTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent keyEvent) {
			if (keyEvent.getCode() == KeyCode.ENTER) {
			 if (!Character.isDigit(barcodeDelTextField.getText().charAt(0)) ){ 
	             main.getAfficheurMsg().afficheErreur("erreur","Merci d'activer la touche 'verr num'  de votre clavier.\n(verouillage du clavier numérique)");
	             barcodeDelTextField.setText("");
	             return;
			 	} else {
			 	qtyTextFieldDel.setText("1");
			 	delEanBtn.fire();
			 	qtyTextFieldDel.setText("");
			 	}
			} 
			
			returnToBarcodeView();
		}
	}); 
	}
	


	@FXML private TextField qtyTextFieldDel;
    
    @FXML private TextField barcodeDelTextField;
      
    @FXML private Button backDelItemBtn;
    
    @FXML private Button delEanBtn;
    
	@FXML
	private ImageView sqlimagesuprim;
	
	@FXML
	private Label sqlLabelsuprim;
    
    @FXML
    public void backBtnAction(ActionEvent e){ 	
        Stage stage = (Stage) backDelItemBtn.getScene().getWindow();     
        stage.close();
    }
    
    @FXML
    public void supprimerEan(ActionEvent e){  
    	if ((barcodeDelTextField.getText().length()==0)||(qtyTextFieldDel.getText().length()==0)){
			return;
		}

    	try {
    	main.getGestionEan().supprimerEan(
    			barcodeDelTextField.getText(), 
    			qtyTextFieldDel.getText());
    	} catch (IndexOutOfBoundsException e2e){ 
    	} 
    } 
    
    public TextField getQtyTextFieldDel() {
		return qtyTextFieldDel;
	}

	public TextField getBarcodeDelTextField() {
		return barcodeDelTextField;
	}
	
	private void returnToBarcodeView(){
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		        barcodeDelTextField.requestFocus();
		    }
		});
	}
	
	public ImageView getSqlImqge(){
		return sqlimagesuprim;
	}


}
