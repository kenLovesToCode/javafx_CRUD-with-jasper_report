package application;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TestController implements Initializable{

	@FXML
	private AnchorPane anchorpane_parent;

	@FXML
	private Pane pane_top;

	@FXML
	private Label label_title;

	@FXML
	private AnchorPane anchorpane_center;

	@FXML
	private AnchorPane anchorpane_left;

	@FXML
	private JFXTextField txt_firstname;

	@FXML
	private JFXTextField txt_lastname;

	@FXML
	private JFXComboBox<String> combo_gender;

	@FXML
	private JFXComboBox<String> combo_position;

	@FXML
	private JFXButton btn_position;

	@FXML
	private JFXButton btn_save;

	@FXML
	private AnchorPane anchorpane_right;

	@FXML
	private TableView<Test> tblview;

	@FXML
	private TableColumn<Test, String> column_firstname;

	@FXML
	private TableColumn<Test, String> column_lastname;

	@FXML
	private TableColumn<Test, String> column_gender;

	@FXML
	private TableColumn<Test, String> column_position;
	
	@FXML
    private TableColumn<Test, Integer> column_ID;

	@FXML
	private JFXButton btn_print_preview;
	
	@FXML
    private JFXButton btn_edit;

    @FXML
    private JFXButton btn_delete;

    @FXML
    private JFXButton btn_add_new;

	private String agender [] = {"Male", "Female"};
	private FXMLLoader loader;
	private String query, firstname, lastname, gender, position;
	DataAccessObject dao;
	private DBConnection database;
	private Connection connect;
	private Map<String, Object> map;
	private boolean EDIT=false, ADD=true;
	private int ID;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dao = new DataAccessObject();
		initGender();
		btn_position.setOnAction(e->{
			showPosition();
		});
		
		combo_position.setOnMouseClicked(e->{
			initPosition();
		});
		
		btn_save.setOnAction(e->{
			saveAccount();
		});
		btn_print_preview.setOnAction(e->{
			printReport();
		});
		btn_edit.setOnAction(e->{
			ADD = false;
			EDIT = true;
			editAccount();
		});
		btn_add_new.setOnAction(e->{
			EDIT = false;
			ADD = true;
			insertNewAccount();
		});
		btn_delete.setOnAction(e->{
			deleteAccount();
		});
		

		initPosition();
		combo_gender.getSelectionModel().select(0);
		combo_position.getSelectionModel().select(0);
		
		refreshTable();
		
	}
	
	public void printReport() {
		database = new DBConnection();
		connect = database.getConnection();
		map = new HashMap<String, Object>();
		
		Report.createReport(connect, map, dao.getReport("account_report", "report_jasper"));
		Report.showReport();
	}
	
	private void initTable() {
		column_firstname.setCellValueFactory(cell->cell.getValue().getpFirstname());
		column_lastname.setCellValueFactory(cell->cell.getValue().getpLastname());
		column_gender.setCellValueFactory(cell->cell.getValue().getpGender());
		column_position.setCellValueFactory(cell->cell.getValue().getpPosition());
		column_ID.setCellValueFactory(cell->cell.getValue().getpID().asObject());
	}
	
	private void refreshTable() {
		initTable();
		query = "SELECT a.firstname, a.lastname, a.gender, p.position, a.account_ID FROM account as a " + 
				"JOIN positions as p ON a.position_ID=p.position_ID " + 
				"ORDER BY a.firstname";
		tblview.setItems(dao.getAccountsData(query));
		
	}
	
	private void saveAccount() { // for saving
		
		firstname = txt_firstname.getText();
		lastname = txt_lastname.getText();
		gender = combo_gender.getSelectionModel().getSelectedItem();
		position = combo_position.getSelectionModel().getSelectedIndex()+1+""; // plus 1 since index starts with 0 and primary key starts with 1
		
		if(EDIT) { // if edit button is pressed
			query = "UPDATE account SET firstname='"+firstname+"', lastname='"+lastname+"', gender='"+gender+"', position_ID="+position+" WHERE account_ID="+ID+"";   
		}else if(ADD){ // if add button is pressed
			query = "INSERT INTO account VALUES(null, '"+firstname+"', '"+lastname+"', '"+gender+"', "+position+");";
		}
		
		dao.saveData(query);
		
		txt_firstname.setText("");
		txt_lastname.setText("");
		combo_gender.getSelectionModel().select(0);
		combo_position.getSelectionModel().select(0);
		
		refreshTable();
		
		ADD = true;
	}
	
	private void deleteAccount() {
		Test selected = tblview.getSelectionModel().getSelectedItem();
		ID = selected.getpID().get();
		query = "DELETE FROM account WHERE account_ID="+ID+"";
		dao.saveData(query);
		refreshTable();
	}
	
	private void editAccount() { // for updating existing account
		Test selected = tblview.getSelectionModel().getSelectedItem();
		ID = selected.getpID().get();
		txt_firstname.setText(selected.getpFirstname().get());
		txt_lastname.setText(selected.getpLastname().get());
		combo_gender.getSelectionModel().select(selected.getpGender().get());
		combo_position.getSelectionModel().select(selected.getpPosition().get());
	}
	
	private void insertNewAccount() { // for adding new account
		txt_firstname.setText("");
		txt_lastname.setText("");
		combo_gender.getSelectionModel().select(0);
		combo_position.getSelectionModel().select(0);
	}
	
	private void initGender() {
		List<String> list = new ArrayList<String>();

		// foreach loop
		for(String content:agender) {
			list.add(content);
		}
		
		// convert list to observable list
		ObservableList obList = FXCollections.observableArrayList(list);
		combo_gender.setItems(obList);

	}
	
	private void initPosition() {
		combo_position.getSelectionModel().clearSelection();
		query = "SELECT position FROM positions";
		combo_position.setItems(dao.getPositionComboBox(query));
	}
	
	private void showPosition() {
		try {
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Position.fxml"));
			PositionController controller = new PositionController();
			loader.setController(controller);
			loader.load();
			Scene scene = new Scene(loader.getRoot());
			scene.getStylesheets().add(getClass().getResource("Position.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}








