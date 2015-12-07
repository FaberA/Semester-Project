package edu.carrollcc.cis232.SemesterProject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
    include using either the StringBuilder or StringTokenizer classes. (Comment with REQ#2)

 *
 */

public class App extends Application {
	final static String DOCTOR_URL = "jdbc:derby:Doctor;create=true";
	final static String PATIENT_URL = "jdbc:derby:Patient;create=true";
	
	@Override
	public void start(Stage stage){ //REQ #9 produce GUI with javafx
		
		Parent parent = null;
		try{
			parent = FXMLLoader.load(getClass().getResource("Hospital.fxml"));
		}
		catch(IOException e){ 
			System.out.println("Error in parent declaration.");
		}
		
		Scene scene = new Scene(parent);
		
		stage.setTitle("Patient Registry Manager by Faber Alarcon");
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main( String[] args )
    {
        System.out.println( "Faber Alarcon - Semester Project" ); // REQ#1 display your name when it runs
        createDB();
        launch(args);
    }

	public static void addDoctor(Connection conn, int id, String name, String specialty, String shift){
    	
    	try{
    		PreparedStatement stmt;
    		
    		String sql = String.format("INSERT INTO Doctors (doctorID, doctorName, specialty, shift)"
    			   + " VALUES (?,?,?,?)");
    		stmt = conn.prepareStatement(sql);
    		stmt.setInt(1,id);
    		stmt.setString(2, name);
    		stmt.setString(3, specialty);
    		stmt.setString(4, shift);
    		stmt.execute();
    	}
    	catch (SQLException e){
    		System.out.println("Error in addDoctor");
    	}
    }
	
	public static void addPatient(Connection conn, int id, String name, int age, String ailment){
    	try{
    		PreparedStatement stmt;
    		
    		String sql = String.format("INSERT INTO Patients (patientID, patientName, age, ailment)"
    			   + " VALUES (?,?,?,?)");
    		stmt = conn.prepareStatement(sql);
    		stmt.setInt(1,id);
    		stmt.setString(2, name);
    		stmt.setInt(3, age);
    		stmt.setString(4, ailment);
    		stmt.execute();
    	}
    	catch (SQLException e){
    		System.out.println("Error in addPatient");
    	}
    }
	
	public static void createDB(){ //REQ #7 use sql db to store data
		Connection conn;
		Statement stmt;
		try {
			conn = DriverManager.getConnection(DOCTOR_URL);
			stmt = conn.createStatement();
			String dropTable = "DROP TABLE Doctors";
			stmt.execute(dropTable);
			System.out.println("Doctors table dropped.");				
			conn.close();
		} 
		catch (SQLException e) {
			System.out.println("Doctors table does not exist.");
		}
		
		try {
			conn = DriverManager.getConnection(DOCTOR_URL);
			stmt = conn.createStatement();
			String sql = "CREATE TABLE Doctors" +
						 "( doctorID INT NOT NULL PRIMARY KEY," +
						 "  doctorName CHAR(50),"     +
						 "  specialty CHAR(50)," +
						 "  shift CHAR(10) )"; 
			stmt.execute(sql);
				
			addDoctor(conn, 0, "Bill Gentry",     "Cardiologist",   "Day");
			addDoctor(conn, 1, "Will Bentry",     "Pediatrician",   "Day");
			addDoctor(conn, 2, "Jonah Sentry",    "Surgeon",        "Night");
			addDoctor(conn, 3, "Ezekiel Houdini", "Emergency",      "Day");
			addDoctor(conn, 4, "Jillian Hart",    "Cardiologist",   "Night");
				
			conn.close();
			System.out.println("Doctor DB created succesfully.");
		} 
		
		catch (SQLException e) {
			System.out.println("Error creating Doctor DB");
		}
		try {
			conn = DriverManager.getConnection(PATIENT_URL);
			stmt = conn.createStatement();
			String dropTable = "DROP TABLE Patients";
			stmt.execute(dropTable);
			conn.close();
			System.out.println("Patients table dropped.");
		} 
		catch (SQLException e) {
			System.out.println("Patients table does not exist.");
		}
		
		try {
			conn = DriverManager.getConnection(PATIENT_URL);
			stmt = conn.createStatement();
			String sql = "CREATE TABLE Patients" +
						 "( patientID INT NOT NULL PRIMARY KEY," +
					     "  patientName CHAR(50),"               +
						 "  age INT,"                            +
						 "  ailment CHAR(50) )"; 
			stmt.execute(sql);
				
			addPatient(conn, 0, "Helga Ylvis",    50,    "Trauma");
			addPatient(conn, 1, "Yvonne Beckley", 44,    "Emergency");
			addPatient(conn, 2, "Henry Doe",      26,    "Surgery");
			addPatient(conn, 3, "James Hearth",   15,    "Tumor");
			addPatient(conn, 4, "Kelly Blue",     65,    "Trauma");
				
			conn.close();
			System.out.println("Patient DB created succesfully.");
		} 
		
		catch (SQLException e) {
			System.out.println("Error creating Patient DB");
		}
	}
}

