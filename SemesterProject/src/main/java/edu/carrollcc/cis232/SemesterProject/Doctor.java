package edu.carrollcc.cis232.SemesterProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Doctor extends Hospital  { //REQ#6 extends implemented superclass
	
	public enum Specialty  { // if any specialties want to be added just add value to this enum
		EMERGENCY, CARDIOLOGIST, PEDIATRICIAN, SURGEON, ONCOLOGIST;
	} 
	
	private IntegerProperty doctorID;
	public int getDoctorID(){
		return IDProperty().get();
	}
	public void setDoctorID(int doctorID){
		IDProperty().set(doctorID);;
	}
	public IntegerProperty IDProperty(){
		if(doctorID == null){
			doctorID = new SimpleIntegerProperty(this,"doctorID");
		}
		return doctorID;
	}
	
	private StringProperty doctorName;
	
	public String getDoctorName(){
		return nameProperty().get();
	}
	public void setDoctorName(String name){
		nameProperty().set(name);;
	}
	public StringProperty nameProperty(){
		if(doctorName == null){
			doctorName = new SimpleStringProperty(this,"doctorName");
		}
		return doctorName;
	}
	
	private StringProperty specialty;
	
	public String getSpecialty(){
		return specialtyProperty().get();
	}
	public void setSpecialty(String specialty){
		specialtyProperty().set(specialty);;
	}
	public StringProperty specialtyProperty(){
		if(specialty == null){
			specialty = new SimpleStringProperty(this,"specialty");
		}
		return specialty;
	}
	
	private StringProperty shift;
	public String getShift(){
		return shiftProperty().get();
	}
	public void setShift(String shift){
		shiftProperty().set(shift);;
	}
	public StringProperty shiftProperty(){
		if(shift == null){
			shift = new SimpleStringProperty(this,"shift");
		}
		return shift;
	}
	
	Doctor(){ // no arg constructor
		this.doctorID = new SimpleIntegerProperty();
		this.doctorName = new SimpleStringProperty();
		this.specialty = new SimpleStringProperty();
		this.shift = new SimpleStringProperty();
	}
	
	public Doctor(int doctorID, String doctorName, String specialty, String shift){ // normal constructor
		this.doctorID = new SimpleIntegerProperty(doctorID);
		this.doctorName = new SimpleStringProperty(doctorName);
		this.specialty = new SimpleStringProperty(specialty);
		this.shift = new SimpleStringProperty(shift);
	}
		
	public ObservableList<Doctor> getDoctors(){ //REQ # 8 use sql db to retrieve data
		final String URL = "jdbc:derby:Doctor";
    	Connection conn;
		Statement stmt;
		ObservableList<Doctor> ll = FXCollections.observableArrayList();
		ResultSet rs;
    		try {
    			conn = DriverManager.getConnection(URL);
    			stmt = conn.createStatement();
    			
    			String getList = "SELECT * FROM Doctors "
    					+ "ORDER BY doctorID DESC";
    			rs = stmt.executeQuery(getList);
    			while(rs.next()){
    				int doctorID = rs.getInt(1);
    				String doctorName = rs.getString(2);
    				String specialty = rs.getString(3);
    				String shift = rs.getString(4);
    				Doctor row = new Doctor(doctorID,doctorName,specialty,shift);
    				ll.add(row);
    			}
    		} 
    		catch (SQLException e) {
    			e.printStackTrace();
    			System.out.println("Error in getDoctors.");
    		}
    	super.setDoctors(ll);
		return ll;
	}
}
