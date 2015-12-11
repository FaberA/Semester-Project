package edu.carrollcc.cis232.SemesterProject;

import java.util.List;
import java.util.ListIterator;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Hospital { //REQ#5 define super class to be implemented by 2 sub classes
	
	private StringProperty hospitalName;
	
	public String getHospitalName(){
		return hospitalNameProperty().get();
	}
	public void setHospitalName(String name){
		hospitalNameProperty().set(name);;
	}
	public StringProperty hospitalNameProperty(){
		if(hospitalName == null){
			hospitalName = new SimpleStringProperty(this,"doctorName");
		}
		return hospitalName;
	}
	
	private ListProperty<Doctor> doctors;
	
	public ObservableList<Doctor> getDoctors(){
		return doctorsProperty().get();
	}
	public void setDoctors(ObservableList<Doctor> d){
		doctorsProperty().set(d);;
	}
	public ListProperty<Doctor> doctorsProperty(){
		if(doctors == null){
			doctors = new SimpleListProperty<Doctor>(this,"doctors");
		}
		return doctors;
	}
	
	private ListProperty<Patient> patients;
	
	public ObservableList<Patient> getPatients(){
		return patientsProperty().get();
	}
	public void setPatients(ObservableList<Patient> p){
		patientsProperty().set(p);;
	}
	public ListProperty<Patient> patientsProperty(){
		if(patients == null){
			patients = new SimpleListProperty<Patient>(this,"patients");
		}
		return patients;
	}
	
	public Hospital(){
		this.hospitalName = new SimpleStringProperty();;
		this.doctors = new SimpleListProperty<Doctor>();
		this.patients = new SimpleListProperty<Patient>();
	}
	
	public Hospital(String name, List<Doctor> d, List<Patient> p){
		this.hospitalName = new SimpleStringProperty(name);
		ObservableList<Doctor> doctorList = FXCollections.observableArrayList(d);
		this.doctors = new SimpleListProperty<Doctor>(doctorList);
		ObservableList<Patient> patientList = FXCollections.observableArrayList(p);
		this.patients = new SimpleListProperty<Patient>(patientList);
	}
	
	public void addDoctor(Doctor d){
		doctors.add(d);
	}
	public void addPatient(Patient p){
		patients.add(p);
	}
	public void deleteDoctor(String doctorID){
		String search = doctorID;
		ListIterator<Doctor> iter = this.getDoctors().listIterator();
		while(iter.hasNext()){
			Doctor d = iter.next();
			if(Integer.toString(d.getDoctorID())!= null && Integer.toString(d.getDoctorID()).contains(search)){
				iter.remove();
			}
		}
	}
	public void deletePatient(String patientID){
		String search = patientID;
		ListIterator<Patient> iter = this.getPatients().listIterator();
		while(iter.hasNext()){
			Patient p = iter.next();
			if(Integer.toString(p.getPatientID())!= null && Integer.toString(p.getPatientID()).contains(search)){
				iter.remove();
			}
		}
	}
}
