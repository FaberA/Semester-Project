package edu.carrollcc.cis232.SemesterProject;

import edu.carrollcc.cis232.SemesterProject.Doctor.Specialty;

public class InvalidSpecialtyException extends Exception { //REQ #12 implement and handle custom exception
	private static final long serialVersionUID = 1L;
	String temp = null;
	public InvalidSpecialtyException(String in) {
		System.out.println("Error! " + in + " is an invalid specialty! These are valid specialties:");
		for(Specialty s: Specialty.values()){
			System.out.println(s);
		}
		this.temp = in;
	}	
}
