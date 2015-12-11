package edu.carrollcc.cis232.SemesterProject;

import edu.carrollcc.cis232.SemesterProject.Outpatient.OutpatientAilment;

public class InvalidOutAilmentException extends Exception {//REQ #12 implement and handle custom exception
	private static final long serialVersionUID = 1L;
	String temp = null;
	public InvalidOutAilmentException(String in) {
		System.out.println("Error! " + in + " is an invalid ailment for this patient type! These are valid ailments:");
		for(OutpatientAilment a: OutpatientAilment.values()){//print out all valid outpatient ailments
			System.out.println(a);
		}
		this.temp = in;
	}	
}
