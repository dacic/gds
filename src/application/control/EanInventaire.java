package application.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;

import application.Main;
import application.model.Barcode;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage; 
/**
 * 
 */

public class EanInventaire extends Application {
	 
	private Main main=Main.getMain();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
	}
	
	@FXML
	void initialize() {
	main=Main.getMain();    
	ArrayList<String> resultList = new ArrayList<>();


	for(Iterator<Entry<Barcode, Integer>> it =  main.getLaMapduStcok().entrySet().iterator(); it.hasNext(); ) {
		Entry<Barcode, Integer> entry = it.next(); 
		if (entry.getValue()>0){
		resultList.add(entry.getKey().getBarcode().concat(" x ").concat(String.valueOf(entry.getValue()).concat("\n").concat(entry.getKey().getDescription())));
		}
	}

	Collections.sort(resultList); 
	list.setItems(FXCollections.observableArrayList(resultList)); 
	}
	
    @FXML private Button backAddItemBtn;
    
    @FXML private ListView<String> list;
    
    @FXML
    public void backBtnAction(ActionEvent e){ 	
        Stage stage = (Stage) backAddItemBtn.getScene().getWindow();     
        stage.close();
    }
    
}
