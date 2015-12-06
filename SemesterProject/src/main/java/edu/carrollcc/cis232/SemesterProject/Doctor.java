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

public class Doctor extends Hospital  { //REQ#6 extends implemented superclass
	
	public enum Specialty  {
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
	
	Doctor(){
		this.doctorID = new SimpleIntegerProperty();
		this.doctorName = new SimpleStringProperty();
		this.specialty = new SimpleStringProperty();
		this.shift = new SimpleStringProperty();
	}
	
	private Doctor(int doctorID, String doctorName, String specialty, String shift){
		this.doctorID = new SimpleIntegerProperty(doctorID);
		this.doctorName = new SimpleStringProperty(doctorName);

		try {
			if(verifySpecialty(specialty) == true){
				this.specialty = new SimpleStringProperty(specialty);
			}
		} 
		catch (InvalidSpecialtyException e) {//REQ#11 catch at least one exception and handle it
			
		}
		
		this.shift = new SimpleStringProperty(shift);
	}
	
	public boolean verifySpecialty (String in) throws InvalidSpecialtyException{
		String trimmed = in.trim();
		for (Specialty s: Specialty.values()){
			if(trimmed.equalsIgnoreCase(s.name())){
				return true;
			}
		}
		throw new InvalidSpecialtyException(trimmed);
	}
		
		
	
	
	public List<Doctor> getDoctors(){ //REQ # 8 use sql db to retrieve data
		final String URL = "jdbc:derby:Doctor";
    	Connection conn;
		Statement stmt;
		List<Doctor> ll = new LinkedList<Doctor>();
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
