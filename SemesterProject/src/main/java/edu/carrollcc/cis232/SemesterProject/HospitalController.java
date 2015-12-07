package edu.carrollcc.cis232.SemesterProject;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import edu.carrollcc.cis232.SemesterProject.Doctor.Specialty;
import edu.carrollcc.cis232.SemesterProject.Inpatient.InpatientAilment;
import edu.carrollcc.cis232.SemesterProject.Outpatient.OutpatientAilment;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
	
	Boolean isOutpatient = false;
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
			Patients.getItems().setAll(p.getPatients());//default to inpatient
			Doctors.getItems().setAll(d.getDoctors());
	  
		addNewDoc.setOnAction(new EventHandler<ActionEvent>() {//REQ # 8 use sql db to retrieve data
            @Override
            public void handle(ActionEvent event) {
            	final String DB_URL = "jdbc:derby:Doctor";
            	Connection conn;
        		PreparedStatement stmt;
        		
        		if(!newDocShift.getText().equalsIgnoreCase("Day") || newDocShift.getText().equalsIgnoreCase("Night")){
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
                Doctors.getItems().setAll(d.getDoctors());
            }
        });
		
		addNewPat.setOnAction(new EventHandler<ActionEvent>() {//REQ # 8 use sql db to retrieve data
            @Override
            public void handle(ActionEvent event) {
            	final String DB_URL = "jdbc:derby:Patient";
            	Connection conn;
        		PreparedStatement stmt;
        		
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
                Patient p = new Inpatient() ;
                Patients.getItems().setAll(p.getPatients());
            }
        });
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

		         if (group.getSelectedToggle() != null) {
		             if(group.getSelectedToggle().getUserData().toString().equals("Outpatient")){
		            	System.out.println(group.getSelectedToggle().getUserData().toString());
		            	isOutpatient = true;
		             }
		             else{
		            	isOutpatient = false;
		             }
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
