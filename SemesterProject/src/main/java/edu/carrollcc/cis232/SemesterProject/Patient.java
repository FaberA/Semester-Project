package edu.carrollcc.cis232.SemesterProject;

import javafx.collections.ObservableList;

public interface Patient { // REQ # 3 define interface to be used by two classes && REQ#10 use polymorphism
	
	public String getAilment();
	
	public void setAilment(String a);
	
	public ObservableList<Patient> getPatients();

	public int getPatientID();
}
