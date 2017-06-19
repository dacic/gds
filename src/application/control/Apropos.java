package application.control;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage; 

public class Apropos extends Application {
	 
	
	@Override
	public void start(Stage primaryStage) throws Exception {
 
	}
	
    @FXML private Button backAddItemBtn;
      
    
    @FXML
    public void backBtnAction(ActionEvent e){ 	
        Stage stage = (Stage) backAddItemBtn.getScene().getWindow();     
        stage.close();
    }
 
}
