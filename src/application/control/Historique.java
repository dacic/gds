package application.control;

import java.util.ArrayList; 
import java.util.Collections;
import application.Main;
import application.model.ObjHistorique;
import javafx.application.Application;
import javafx.collections.FXCollections; 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage; 

public class Historique extends Application {
	 
	private Main main;
	
	@FXML
	void initialize() {
	main=Main.getMain();
	ArrayList<String> convertObj= new ArrayList<>();
	for ( ObjHistorique obj:main.getHistorique()){
		 convertObj.add(obj.getDate().concat("\n").concat(String.valueOf(obj.getQty()).concat(" x ").concat(obj.getBarcode()).concat("\n").concat(obj.getDescription())));
	} 
	Collections.reverse(convertObj);
	list.setItems( FXCollections.observableArrayList(convertObj));
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
 
	}
	
    @FXML private Button backAddItemBtn;
    
    @FXML private ListView<String> list;
      
    
    @FXML
    public void backBtnAction(ActionEvent e){ 	
        Stage stage = (Stage) backAddItemBtn.getScene().getWindow();     
        stage.close();
    }
 
}
