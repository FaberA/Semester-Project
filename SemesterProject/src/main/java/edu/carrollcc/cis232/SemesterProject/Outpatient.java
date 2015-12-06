package edu.carrollcc.cis232.SemesterProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Outpatient extends Hospital implements Patient { // REQ#4 implements defined interface && REQ#6 extends implemented superclass && REQ#10 use polymorphism
	
	private IntegerProperty patientID;
	public int getPatientID(){
		return IDProperty().get();
	}
	public void setPatientID(int patientID){
		IDProperty().set(patientID);;
	}
	public IntegerProperty IDProperty(){
		if(patientID == null){
			patientID = new SimpleIntegerProperty(this,"patientID");
		}
		return patientID;
	}
	
	private IntegerProperty age;
	public int getAge(){
		return IDProperty().get();
	}
	public void setAge(int age){
		AgeProperty().set(age);;
	}
	public IntegerProperty AgeProperty(){
		if(age == null){
			age = new SimpleIntegerProperty(this,"age");
		}
		return age;
	}
	
	private StringProperty patientName;
	public String getPatientName(){
		return nameProperty().get();
	}
	public void setPatientName(String patientName){
		nameProperty().set(patientName);;
	}
	public StringProperty nameProperty(){
		if(patientName == null){
			patientName = new SimpleStringProperty(this,"patientName");
		}
		return patientName;
	}
	
	private StringProperty ailment;
	@Override
	public String getAilment(){
		return ailmentProperty().get();
	}
	@Override
	public void setAilment(String ailment){
		ailmentProperty().set(ailment);;
	}
	public StringProperty ailmentProperty(){
		if(ailment == null){
			ailment = new SimpleStringProperty(this,"ailment");
		}
		return ailment;
	}
	
	Outpatient(){
		this.patientID = new SimpleIntegerProperty();
		this.patientName = new SimpleStringProperty();
		this.ailment = new SimpleStringProperty();
	}
	
	private Outpatient(int patientID, String patientName, String ailment){
		this.patientID = new SimpleIntegerProperty(patientID);
		this.patientName = new SimpleStringProperty(patientName);
		this.ailment = new SimpleStringProperty(ailment);
	}
	
	@Override
	public List<Patient> getPatients(){ //REQ # 8 use sql db to retrieve data
		final String URL = "jdbc:derby:Patient";
    	Connection conn;
		Statement stmt;
		List<Patient> ll = new LinkedList<Patient>();
		ResultSet rs;
    		try {
    			conn = DriverManager.getConnection(URL);
    			stmt = conn.createStatement();
    			
    			String getList = "SELECT * FROM Patients "
    					+ "ORDER BY patientID DESC";
    			rs = stmt.executeQuery(getList);
    			while(rs.next()){
    				int patientID = rs.getInt(1);
    				String patientName = rs.getString(2);
    				String ailment = rs.getString(3);
    				
    				Patient row = new Outpatient(patientID,patientName,ailment);
    				ll.add(row);
    			}
    		} 
    		catch (SQLException e) {
    			e.printStackTrace();
    			System.out.println("Error in getPatients.");
    		}
    	super.setPatients(ll);
		return ll;
	}

}
