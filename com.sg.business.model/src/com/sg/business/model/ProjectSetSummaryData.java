package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

public class ProjectSetSummaryData {

	public int total;

	public int finished;

	public int processing;

	public int processing_normal;

	public int processing_delay;

	public int processing_advance;

	public int finished_normal;

	public int finished_delay;

	public int finished_advance;
	
	public int cost_normal;
	
	public int cost_over;
	
	public List<ProjectProvider> subOrganizationProjectProvider = new ArrayList<ProjectProvider>();
	
	public List<ProjectProvider> subChargerProjectProvider = new ArrayList<ProjectProvider>();
	
}
