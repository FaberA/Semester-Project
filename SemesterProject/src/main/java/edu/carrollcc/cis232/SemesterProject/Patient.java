package edu.carrollcc.cis232.SemesterProject;

import java.util.List;

public interface Patient { // REQ # 3 define interface to be used by two classes && REQ#10 use polymorphism
	
	public String getAilment();
	
	public void setAilment(String a);
	
	public List<Patient> getPatients();
}
