package edu.carrollcc.cis232.SemesterProject;

import java.util.ArrayList;
import java.util.List;

public class Hospital { //REQ#5 define super class to be implemented by 2 sub classes

	private List<Doctor> doctors = new ArrayList<Doctor>();
	private List<Patient> patients = new ArrayList<Patient>();
	String hospitalName;
	
	
	public Hospital(){
		this.hospitalName = null;
		this.doctors = null;
		this.patients = null;
	}
	
	public Hospital(String name, List<Doctor> d, List<Patient> p){
		this.hospitalName = name;
		this.doctors = d;
		this.patients = p;
	}
	
	public void addDoctor(Doctor d){
		doctors.add(d);
	}
	public void addPatient(Patient p){
		patients.add(p);
	}
	
	public List<Doctor> getDoctors(){
		return doctors;
	}
	public List<Patient> getPatients(){
		return patients;
	}
	
	public void setDoctors(List<Doctor> d){
		this.doctors = d;
	}
	
	public void setPatients(List<Patient> p){
		this.patients = p;
	}
}
