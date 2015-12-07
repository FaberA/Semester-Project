package edu.carrollcc.cis232.SemesterProject;

import edu.carrollcc.cis232.SemesterProject.Inpatient.Ailment;

public class InvalidInAilmentException extends Exception {//REQ #12 implement and handle custom exception
	private static final long serialVersionUID = 1L;
	String temp = null;
	public InvalidInAilmentException(String in) {
		System.out.println("Error!" + in + " is an invalid ailment for this patient type! These are valid ailments:");
		for(Ailment a: Ailment.values()){
			System.out.println(a);
		}
		this.temp = in;
	}	
}
