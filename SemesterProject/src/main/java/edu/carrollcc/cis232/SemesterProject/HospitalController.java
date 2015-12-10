package edu.carrollcc.cis232.SemesterProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

import edu.carrollcc.cis232.SemesterProject.Doctor.Specialty;
import edu.carrollcc.cis232.SemesterProject.Inpatient.InpatientAilment;
import edu.carrollcc.cis232.SemesterProject.Outpatient.OutpatientAilment;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


public class HospitalController implements Initializable {

	@FXML 
	private Button addNewDoc;
	@FXML 
	private Button addNewPat;
	
	@FXML 
	private TableColumn<Patient,String> ailment;
	@FXML 
	private TableColumn<Patient,Integer> age;
	@FXML 
	private TableColumn<Patient,String> patientName;
	@FXML 
	private TableColumn<Patient,Integer> patientID;
	@FXML 
	private TableView<Patient> Patients;
	
	@FXML 
	private TableColumn<Doctor,String> doctorName;
	@FXML 
	private TableColumn<Doctor,Integer> doctorID;
	@FXML 
	private TableColumn<Doctor,String> shift;
	@FXML 
	private TableColumn<Doctor,String> specialty;
	@FXML 
	private TableView<Doctor> Doctors;
	
	@FXML
	private TextField newDocID;
	@FXML
	private TextField newDocName;
	@FXML
	private TextField newDocShift;
	@FXML
	private TextField newDocSpecialty;
	
	@FXML
	private TextField newPatID;
	@FXML
	private TextField newPatName;
	@FXML
	private TextField newPatAilment;
	@FXML
	private TextField newPatAge;
	@FXML
	private RadioButton inButton;
	@FXML
	private RadioButton outButton;
	@FXML
	private ToggleGroup group;
	
	@FXML
	private TextField delDoctor;
	@FXML
	private TextField delPatient;
	@FXML
	private Button delDoctorButton;
	@FXML
	private Button delPatientButton;
	@FXML
	private Label hospitalName;
	
	Boolean isOutpatient = false;
	Hospital h = new Hospital();
	@Override
	public void initialize(URL fxml, ResourceBundle resources){
			
			patientID.setCellValueFactory(
			    new PropertyValueFactory<Patient,Integer>("patientID"));
			
			patientName.setCellValueFactory(                
				new PropertyValueFactory<Patient,String>("patientName"));
			patientName.setCellFactory(                
				TextFieldTableCell.<Patient>forTableColumn());
			
			age.setCellValueFactory(
				new PropertyValueFactory<Patient,Integer>("age"));
			
			ailment.setCellValueFactory(
				new PropertyValueFactory<Patient,String>("ailment")); 
			ailment.setCellFactory(                
				TextFieldTableCell.<Patient>forTableColumn());
			

			doctorID.setCellValueFactory(
				new PropertyValueFactory<Doctor,Integer>("doctorID"));
			doctorName.setCellValueFactory(                
				new PropertyValueFactory<Doctor,String>("doctorName"));
			doctorName.setCellFactory(                
				TextFieldTableCell.<Doctor>forTableColumn());
			specialty.setCellValueFactory(
				new PropertyValueFactory<Doctor,String>("specialty")); 
			specialty.setCellFactory(                
				TextFieldTableCell.<Doctor>forTableColumn());
			shift.setCellValueFactory(
				new PropertyValueFactory<Doctor,String>("shift"));
			shift.setCellFactory(                
				TextFieldTableCell.<Doctor>forTableColumn());
			
			inButton.setUserData("Inpatient");
			outButton.setUserData("Outpatient");
			Doctor d = new Doctor();
			Patient p = null;
			if(isOutpatient){
				p = new Outpatient();
			}
			else{
				p = new Inpatient();
			}
			h.setDoctors(d.getDoctors());
			h.setPatients(p.getPatients());
			h.setHospitalName("South Central");
			Patients.getItems().setAll(h.getPatients());
			Doctors.getItems().setAll(h.getDoctors());
			hospitalName.setText(h.getHospitalName());
	  
		addNewDoc.setOnAction(new EventHandler<ActionEvent>() {//REQ # 8 use sql db to retrieve data
            @Override
            public void handle(ActionEvent event) {
            	final String DB_URL = "jdbc:derby:Doctor";
            	Connection conn;
        		PreparedStatement stmt;
        		
        		if(newDocShift.getText().equalsIgnoreCase("Day") || newDocShift.getText().equalsIgnoreCase("Night" ) ){
        			System.out.println("Thank you for entering a valid shift.");
        		}
        		else{ 
        			System.out.println("Invalid shift! Enter Day or Night!");
        			return;
        		}
        		try {
					verifySpecialty(newDocSpecialty.getText());
				} 
        		catch (InvalidSpecialtyException e) {//REQ#11 catch exception and handle it
        			return;
				}
        		try {
        			conn = DriverManager.getConnection(DB_URL);
        			
        			String getNewDoctor = "INSERT INTO Doctors (doctorID, doctorName, specialty, shift) VALUES (?,?,?,?)";
        			stmt = conn.prepareStatement(getNewDoctor);
        			stmt.setInt(1, Integer.parseInt(newDocID.getText()));
        			stmt.setString(2, newDocName.getText());	
        			stmt.setString(3, newDocSpecialty.getText());
        			stmt.setString(4, newDocShift.getText());		                 
        			stmt.execute();
        			
        			h.addDoctor(new Doctor(Integer.parseInt(newDocID.getText()), newDocName.getText(), newDocSpecialty.getText(), newDocShift.getText()));
        		}
        		catch (SQLException e) {
            		System.out.println("Error in creating new doctor.");
            	}
        		catch(NumberFormatException e){
        			System.out.println("Error! Enter a valid integer ID");
        		}
                newDocID.clear();
                newDocName.clear();
                newDocSpecialty.clear();
                newDocShift.clear();
                
                Doctor d = new Doctor();
                Doctors.getItems().setAll(h.getDoctors());
            }
        });
		
		addNewPat.setOnAction(new EventHandler<ActionEvent>() {//REQ # 8 use sql db to retrieve data
            @Override
            public void handle(ActionEvent event) {
            	final String DB_URL = "jdbc:derby:Patient";
            	Connection conn;
        		PreparedStatement stmt;
        		
        		if(Integer.parseInt(newPatAge.getText()) < 0){
        			System.out.println("Invalid age! Enter a positive number!");
        			return;
        		}
        		
        		try {
					verifyAilment(newPatAilment.getText());
				} 
        		catch (InvalidInAilmentException|InvalidOutAilmentException e) {//REQ#11 catch exception and handle it
        			return;
				}
        		
        		try {
        			conn = DriverManager.getConnection(DB_URL);
        			
        			String getNewPatient = "INSERT INTO Patients (patientID, patientName, age, ailment) VALUES (?,?,?,?)";
        			stmt = conn.prepareStatement(getNewPatient);
        			stmt.setInt(1, Integer.parseInt(newPatID.getText()));
        			stmt.setString(2, newPatName.getText());
        			stmt.setInt(3, Integer.parseInt(newPatAge.getText()));
        			stmt.setString(4, newPatAilment.getText());		                 
        			stmt.execute();
        			
        			if(isOutpatient){
        				h.addPatient(new Outpatient(Integer.parseInt(newPatID.getText()), newPatName.getText(), Integer.parseInt(newPatAge.getText()), newPatAilment.getText()));
        			}
        			else{
        				h.addPatient(new Inpatient(Integer.parseInt(newPatID.getText()), newPatName.getText(), Integer.parseInt(newPatAge.getText()), newPatAilment.getText()));
        			}
        		}
        		catch (SQLException e) {
            		System.out.println("Error in creating new patient!");
            	}
        		catch(NumberFormatException e){
        			System.out.println("Error! Enter a valid integer ID");
        		}
                newPatID.clear();
                newPatName.clear();
                newPatAge.clear();
                newPatAilment.clear();
                
                
                Patients.getItems().setAll(h.getPatients());
            }
        });
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

		         if (group.getSelectedToggle() != null) {
		             if(group.getSelectedToggle().getUserData().toString().equals("Outpatient")){
		            	isOutpatient = true;
		             }
		             else{
		            	isOutpatient = false;
		             }
		         }

		     } 
		});
		
		delDoctorButton.setOnAction(new EventHandler<ActionEvent>() {//delete value from db, but leave in Hospital object for records
            @Override
            public void handle(ActionEvent event) {
            	final String DB_URL = "jdbc:derby:Doctor";
            	Connection conn;
        		PreparedStatement stmt;
        		
        		String verifyDelete = "Are you sure you wish to delete the doctor with ID: ";
        		StringBuilder sb = new StringBuilder(verifyDelete); //REQ #2 utilize StringBuilder (Would liked to have found a better utilization of this)
        		sb.append(Integer.parseInt(delDoctor.getText()) + "?\n(Enter y/n)");
        		System.out.println(sb);
        		
        		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        		String input = null;
				try {
					input = br.readLine();
				} catch (IOException e) {
					System.out.println("Error Reading Input");
				}
        		
        		if(input.equalsIgnoreCase("Y")){
        			try {
        				conn = DriverManager.getConnection(DB_URL);
        			
        				String getNewDoctor = "DELETE FROM Doctors WHERE doctorID = ?";
        				stmt = conn.prepareStatement(getNewDoctor);
        				stmt.setInt(1, Integer.parseInt(delDoctor.getText()));		                 
        				stmt.execute();
        			}
        			catch (SQLException e) {
        				System.out.println("Error in deleting Doctor.");
        			}
        			catch(NumberFormatException e){
        				System.out.println("Error! Enter a valid integer ID");
        			}
        			delDoctor.clear();
        			Doctor d = new Doctor();
        			Doctors.getItems().setAll(d.getDoctors());
        		}
        		else if(input.equalsIgnoreCase("N")){
        			System.out.println("Delete Canceled");
        			return;
        		}
        		else{
        			System.out.println("Invalid input!");
        			return;
        		}
            }
        });
		
		delPatientButton.setOnAction(new EventHandler<ActionEvent>() {//delete value from db, but leave in Hospital object for records
            @Override
            public void handle(ActionEvent event) {
            	final String DB_URL = "jdbc:derby:Patient";
            	Connection conn;
        		PreparedStatement stmt;
        		
        		String verifyDelete = "Are you sure you wish to delete the patient with ID: ";
        		StringBuilder sb = new StringBuilder(verifyDelete); //REQ #2 utilize StringBuilder (Would liked to have found a better utilization of this)
        		sb.append(Integer.parseInt(delPatient.getText()) + "?\n(Enter y/n)");
        		System.out.println(sb);
        		
        		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        		String input = null;
				try {
					input = br.readLine();
				} catch (IOException e) {
					System.out.println("Error Reading Input");
				}
        		
				
        		if(input.equalsIgnoreCase("Y")){
        			try {
        				conn = DriverManager.getConnection(DB_URL);
        			
        				String getNewDoctor = "DELETE FROM Patients WHERE patientID = ?";
        				stmt = conn.prepareStatement(getNewDoctor);
        				stmt.setInt(1, Integer.parseInt(delPatient.getText()));		                 
        				stmt.execute();
        			}
        			catch (SQLException e) {
        				System.out.println("Error in deleting patient.");
        			}
        			catch(NumberFormatException e){
        				System.out.println("Error! Enter a valid integer ID");
        			}
        			delPatient.clear();
        			Patient p = new Inpatient();// default to inpatient to access sql db
        			Patients.getItems().setAll(p.getPatients());
        		}
        		else if(input.equalsIgnoreCase("N")){
        			System.out.println("Delete Canceled");
        			return;
        		}
        		else{
        			System.out.println("Invalid input!");
        			return;
        		}
            }
        });
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
	
	public boolean verifyAilment (String in) throws InvalidInAilmentException, InvalidOutAilmentException {
		String trimmed = in.trim();
		if(!isOutpatient){
			for (InpatientAilment a: InpatientAilment.values()){
				if(trimmed.equalsIgnoreCase(a.name())){
					return true;
				}
			}
			throw new InvalidInAilmentException(trimmed);
		}
		else{
			for (OutpatientAilment a: OutpatientAilment.values()){
				if(trimmed.equalsIgnoreCase(a.name())){
					return true;
				}
			}
			throw new InvalidOutAilmentException(trimmed);
		}
	}
}
