package application;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataAccessObject {

	private DBConnection database = new DBConnection();
	private ResultSet rs;
	private PreparedStatement pstmt;
	private Connection connect;
	
	public DataAccessObject() {
		
	}
	
	public void saveData(String query) {
		try {
			connect = database.getConnection(); // get connection 
			pstmt = connect.prepareStatement(query);
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			database.close(connect, pstmt, null);
		}
	}
	
	public ObservableList<Position> getPositionData(String query){
		ObservableList<Position> list = FXCollections.observableArrayList();
		try {
			connect = database.getConnection();
			pstmt = connect.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new Position(rs.getString(1)));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ObservableList<Test> getAccountsData(String query){
		ObservableList list = FXCollections.observableArrayList();
		try {
			connect = database.getConnection();
			pstmt = connect.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new Test(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ObservableList<String> getPositionComboBox(String query){
		ObservableList list = FXCollections.observableArrayList();
		try {
			connect = database.getConnection();
			pstmt = connect.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getString(1));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public InputStream getReport(String report_name, String column_name) {
		InputStream input = null;
		String query = "SELECT "+column_name+" FROM reports WHERE report_name='"+report_name+"'";
		try {
			
			connect = database.getConnection();
			pstmt = connect.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				input = rs.getBinaryStream(1);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return input;
	}
	
}









