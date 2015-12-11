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
	
	final String PATIENT_URL = "jdbc:derby:Patient";
	final String DOCTOR_URL = "jdbc:derby:Doctor";
	final String delNewDoctor = "DELETE FROM Doctors WHERE doctorID = ?";
	final String getNewDoctor = "INSERT INTO Doctors (doctorID, doctorName, specialty, shift) VALUES (?,?,?,?)";
	final String delNewPatient = "DELETE FROM Patients WHERE patientID = ?";
	final String getNewPatient = "INSERT INTO Patients (patientID, patientName, age, ailment) VALUES (?,?,?,?)";
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
			h.setDoctors(d.getDoctors()); //get initial doctors list
			h.setPatients(p.getPatients()); //get initial patients list
			h.setHospitalName("South Central"); //Arbitrary hospital name
			Patients.getItems().setAll(h.getPatients());
			Doctors.getItems().setAll(h.getDoctors());
			hospitalName.setText(h.getHospitalName());
	  
		addNewDoc.setOnAction(new EventHandler<ActionEvent>() {//REQ # 8 use sql db to retrieve data
            @Override
            public void handle(ActionEvent event) {
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
        		
        		add(DOCTOR_URL, getNewDoctor, "Doctors", newDocID, newDocName, newDocSpecialty, newDocShift);
            }
        });
		
		addNewPat.setOnAction(new EventHandler<ActionEvent>() {//REQ # 8 use sql db to retrieve data
            @Override
            public void handle(ActionEvent event) {
        		try{
        			if(Integer.parseInt(newPatAge.getText()) < 0){
            			System.out.println("Invalid age! Enter a positive number!");
            			return;
            		}
        		}
        		catch(NumberFormatException e){
        			System.out.println("Invalid input: " + newPatAge.getText() + ". Please enter an integer!");
        			return;
        		}
        		
        		try {
					verifyAilment(newPatAilment.getText());
				} 
        		catch (InvalidInAilmentException|InvalidOutAilmentException e) {//REQ#11 catch exception and handle it
        			return;
				}
        		
        		add(PATIENT_URL, getNewPatient, "Patients", newPatID, newPatName, newPatAge, newPatAilment);
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
            	delete(DOCTOR_URL, delDoctor, "Doctors", delNewDoctor);
            }
        });
		
		delPatientButton.setOnAction(new EventHandler<ActionEvent>() {//delete value from db, but leave in Hospital object for records
            @Override
            public void handle(ActionEvent event) {
            	delete(PATIENT_URL, delPatient, "Patients", delNewPatient);
            }
        });
	}
	public void delete(String db_url, TextField tfType, String type, String sql){
    	Connection conn;
		PreparedStatement stmt;
		
		String verifyDelete = "Are you sure you wish to delete the person with ID: ";
		StringBuilder sb = new StringBuilder(verifyDelete); //REQ #2 utilize StringBuilder (Would liked to have found a better utilization of this)
		sb.append(Integer.parseInt(tfType.getText()) + "?\n(Enter y/n)");
		System.out.println(sb);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = null;
		try {
			input = br.readLine();
		} 
		catch (IOException e) {
			System.out.println("Error Reading Input");
		}
		
		if(input.equalsIgnoreCase("Y")){
			try {
				conn = DriverManager.getConnection(db_url);
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, Integer.parseInt(tfType.getText()));		                 
				stmt.execute();
				
				h.deletePatient(tfType.getText());
			}
			catch (SQLException e) {
				System.out.println("Error in deleting patient.");
			}
			catch(NumberFormatException e){
				System.out.println("Error! Enter a valid integer ID");
			}
			tfType.clear();
			if(type.equals("Patients")){
				Patients.getItems().setAll(h.getPatients());
			}
			else if(type.equals("Doctors")){
				Doctors.getItems().setAll(h.getDoctors());
			}
			else{
				System.out.println("Enter only 'Patients' or 'Doctors'");
			}
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
	
	public void add(String db_url,String sql, String type, TextField id, TextField name, TextField op1, TextField op2){
		Connection conn;
		PreparedStatement stmt;
		try {
			conn = DriverManager.getConnection(db_url);
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(id.getText()));
			stmt.setString(2, name.getText());
			stmt.setInt(3, Integer.parseInt(op1.getText()));
			stmt.setString(4, op2.getText());		                 
			stmt.execute();
			
			if(type.equals("Patients")){
				if(isOutpatient){
					h.addPatient(new Outpatient(Integer.parseInt(id.getText()), name.getText(), Integer.parseInt(op1.getText()), op2.getText()));
				}
				else{
					h.addPatient(new Inpatient(Integer.parseInt(id.getText()), name.getText(), Integer.parseInt(op1.getText()), op2.getText()));
				}
			}
			else if(type.equals("Doctors")){
				h.addDoctor(new Doctor(Integer.parseInt(id.getText()), name.getText(), op1.getText(), op2.getText()));
			}
		}
		catch (SQLException e) {
    		System.out.println("Error in creating new patient!");
    	}
		catch(NumberFormatException e){
			System.out.println("Error! Enter a valid integer ID");
		}
        id.clear();
        name.clear();
        op1.clear();
        op2.clear(); 
        
        if(type.equals("Patients")){
			Patients.getItems().setAll(h.getPatients());
		}
		else if(type.equals("Doctors")){
			Doctors.getItems().setAll(h.getDoctors());
		}
		else{
			System.out.println("Enter only 'Patients' or 'Doctors'");
		}
        
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
