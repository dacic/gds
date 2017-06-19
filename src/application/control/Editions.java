package application.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Map.Entry;
import application.Main;
import application.model.EditionsObj;
import application.model.Barcode;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 */

public class Editions extends Application {

	private Main main = Main.getMain();
	private TableColumn<EditionsObj, String> colonneBarcode, colonneQtyCmd, colonneRef, colonneQtyAlert, colonneQtyNow;
	private int minCmd = 0;

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.show();

	}

	@SuppressWarnings("unchecked") // bug report JDK-6227971 : generic
									// inferrence bug in varargs

	@FXML
	public void initialize() {
		main = Main.getMain();
		final GestionEan gestionEan = main.getGestionEan();

		final ArrayList<EditionsObj> list = new ArrayList<>();

		for (Iterator<Entry<Barcode, Integer>> it = main.getLaMapduStcok().entrySet().iterator(); it.hasNext();) {
			Entry<Barcode, Integer> entry = it.next();
			final EditionsObj appObj = new EditionsObj(entry.getKey().getBarcode(), entry.getKey().getDescription(),
					entry.getValue().toString(), String.valueOf(entry.getKey().getQty_min()),
					String.valueOf(entry.getValue() > entry.getKey().getQty_min() ? 0
							: entry.getKey().getQty_min() - entry.getValue()));
			list.add(appObj);
		}

		ObservableList<EditionsObj> data = FXCollections.observableArrayList(list);
		tableau.setEditable(true);
		tableau.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		colonneBarcode = new TableColumn<EditionsObj, String>("Code barres");
		colonneBarcode.setMinWidth(100);
		colonneBarcode.setCellValueFactory(new PropertyValueFactory<EditionsObj, String>("barcode"));
		colonneBarcode.setCellFactory(TextFieldTableCell.forTableColumn());
		colonneBarcode.setStyle("-fx-alignment: CENTER;");
		colonneBarcode.setEditable(false);

		colonneRef = new TableColumn<EditionsObj, String>("Réference, description articles");
		colonneRef.setMinWidth(300);
		colonneRef.setCellValueFactory(new PropertyValueFactory<EditionsObj, String>("ref"));
		colonneRef.setCellFactory(TextFieldTableCell.forTableColumn());
		colonneRef.setStyle("-fx-alignment: CENTER;");
		colonneRef.setOnEditCommit(new EventHandler<CellEditEvent<EditionsObj, String>>() {
			@Override
			public void handle(CellEditEvent<EditionsObj, String> t) {

				((EditionsObj) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRef(t.getNewValue());

				gestionEan.modifierEanDescription(colonneBarcode.getCellData(t.getTablePosition().getRow()),
						t.getNewValue());
			}
		}

		);

		colonneQtyNow = new TableColumn<EditionsObj, String>("quantité \nactuelle");
		colonneQtyNow.setMinWidth(60);
		colonneQtyNow.setCellValueFactory(new PropertyValueFactory<EditionsObj, String>("now"));
		colonneQtyNow.setCellFactory(TextFieldTableCell.forTableColumn());
		colonneQtyNow.setStyle("-fx-alignment: CENTER;");
		colonneQtyNow.setEditable(false);

		colonneQtyAlert = new TableColumn<EditionsObj, String>("alerte stock\nminimum");
		colonneQtyAlert.setMinWidth(60);
		colonneQtyAlert.setCellValueFactory(new PropertyValueFactory<EditionsObj, String>("alerte"));
		colonneQtyAlert.setCellFactory(TextFieldTableCell.forTableColumn());
		colonneQtyAlert.setStyle("-fx-alignment: CENTER;");
		colonneQtyAlert.setOnEditCommit(new EventHandler<CellEditEvent<EditionsObj, String>>() {
			@Override
			public void handle(CellEditEvent<EditionsObj, String> t) {
				if (estNombreEntier(t.getNewValue())) {
					((EditionsObj) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setAlerte(t.getNewValue());

					gestionEan.modifierQtyMinCsv(colonneBarcode.getCellData(t.getTablePosition().getRow()),
							t.getNewValue());

					((EditionsObj) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCmd((String
							.valueOf(t.getTableView().getItems().get(t.getTablePosition().getRow()).calculCmd())));

					tableau.refresh();
				} else {
					tableau.refresh();
					main.getAfficheurMsg().afficheInfo("Modification alerte stock min.",
							"quantité ".concat(t.getNewValue().concat(" est erronée")));
				}
			}
		}

		);

		colonneQtyCmd = new TableColumn<EditionsObj, String>("quantité à\ncommander");
		colonneQtyCmd.setMinWidth(60);
		colonneQtyCmd.setCellValueFactory(new PropertyValueFactory<EditionsObj, String>("cmd"));
		colonneQtyCmd.setCellFactory(TextFieldTableCell.forTableColumn());
		colonneQtyCmd.setStyle("-fx-alignment: CENTER;");
		colonneQtyCmd.setOnEditCommit(new EventHandler<CellEditEvent<EditionsObj, String>>() {
			@Override
			public void handle(CellEditEvent<EditionsObj, String> t) {

				((EditionsObj) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCmd(t.getNewValue());

				colonneQtyCmd.getTableView().getItems().get(t.getTablePosition().getRow()).setCmd(t.getNewValue());
			}
		});

		tableau.setItems(data);
		tableau.getColumns().addAll(colonneBarcode, colonneRef, colonneQtyAlert, colonneQtyNow, colonneQtyCmd);

		allQtyCheckBox.setSelected(false);
		allQtyText.setDisable(true);

		allQtyCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				allQtyText.setDisable(!new_val);
			}
		});

		checkcmd.setSelected(false);
		cmdText.setDisable(true);

		checkcmd.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				cmdText.setDisable(!new_val);
			}
		});

		cmdText.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {

					try {
						minCmd = Integer.parseInt(cmdText.getText());
					} catch (NumberFormatException nb) {
						main.getAfficheurMsg().afficheInfo("Erreur",
								"nombre incorrect : ".concat(cmdText.getText()));
						 return;
					} 
					for (EditionsObj obj : list) { 
						if (checkcmd.isSelected() && Integer.parseInt(obj.getNow()) <= minCmd) {
							if (Integer.valueOf(obj.getAlerte()) > minCmd ){
								obj.setCmd(obj.getAlerte());
							} else {
								obj.setCmd(String.valueOf((minCmd)-Integer.parseInt(obj.getNow())));
							}
						}
					}
					tableau.getItems().clear();
					ObservableList<EditionsObj> data = FXCollections.observableArrayList(list);
					tableau.setItems(data);
				}
			}

		});

		allQtyText.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					final String qtyAll = allQtyText.getText();
					if (estNombreEntier(qtyAll)) {

						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Définir stock min");
						alert.setHeaderText(null);
						alert.setContentText("définir la même valeur du stock minimum pour les articles");

						ProgressBar progressBar = new ProgressBar();
						ProgressIndicator progressIndicator = new ProgressIndicator();
						progressBar.setProgress(0);

						FlowPane root = new FlowPane();
						root.setPadding(new Insets(10));
						root.setHgap(10);
						root.getChildren().addAll(progressBar, progressIndicator);

						Stage dialogStage = new Stage();
						dialogStage.initStyle(StageStyle.DECORATED);
						dialogStage.setResizable(false);
						dialogStage.initModality(Modality.APPLICATION_MODAL);

						Scene scene = new Scene(root);
						dialogStage.setScene(scene);

						progressBar.setProgress(0);

						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == ButtonType.OK) {
							dialogStage.show();

							Task<Void> task = new Task<Void>() {
								@Override
								public Void call() {
									int i = 0;
									int max = main.getLaMapduStcok().size();

									for (Iterator<Entry<Barcode, Integer>> it = main.getLaMapduStcok().entrySet()
											.iterator(); it.hasNext();) {
										Entry<Barcode, Integer> entry = it.next();
										entry.getKey().setQty_min(qtyAll);

										updateProgress(i, max);
									}

									return null;
								}

								@Override
								protected void succeeded() {
									super.succeeded();
									dialogStage.hide();
									main.getGestionEan().enregistrerLaBase(main.getLaMapduStcok());
									main.getAfficheurMsg().afficheInfo("Modification de l'alerte stock min.",
											"modification faite");

									Stage stage = (Stage) backAddItemBtn.getScene().getWindow();
									stage.close();
								}

							};

							progressBar.progressProperty().bind(task.progressProperty());
							new Thread(task).start();
						}
					} else {
						main.getAfficheurMsg().afficheInfo("Erreur",
								"nombre incorrect: ".concat(qtyAll));
					}
				}
			}
		});

	}

	@FXML
	private Button backAddItemBtn;

	@FXML
	private Button copier;

	@FXML
	public TableView<EditionsObj> tableau;

	@FXML
	private CheckBox allQtyCheckBox;

	@FXML
	private CheckBox checkcmd;

	@FXML
	private TextField allQtyText;

	@FXML
	private TextField cmdText;

	@FXML
	public void backBtnAction(ActionEvent e) {
		Stage stage = (Stage) backAddItemBtn.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void copierAction(ActionEvent e) {

		StringBuilder copieStr = new StringBuilder();
		try {
			for (EditionsObj o : tableau.getItems()) {
				if (Integer.parseInt(colonneQtyCmd.getCellData(o)) > 0) {
					copieStr.append(colonneBarcode.getCellData(o).concat(" - ").concat(
							colonneRef.getCellData(o).concat(" x ").concat(colonneQtyCmd.getCellData(o)).concat("\n")));
				}
			}
		} catch (Exception nullOrNumberFormat) {
			main.getAfficheurMsg().afficheErreur("Erreur", nullOrNumberFormat.getMessage().toString());
		}

		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(copieStr.toString());
		clipboard.setContent(content);

	}

	private static boolean estNombreEntier(String str) {
		if (str == null) {
			return false;
		}
		if (str.equals("-1")) {
			return true;
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
}
