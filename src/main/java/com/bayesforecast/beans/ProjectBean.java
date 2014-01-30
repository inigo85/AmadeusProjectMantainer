package com.bayesforecast.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ManagedBean;

import com.bayesforecast.persistence.DatabaseFacade;
import com.bayesforecsast.modelo.Project;


@ManagedBean
public class ProjectBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Project> projectList;
	private DatabaseFacade db;
	
	
	public ProjectBean(){
		db = DatabaseFacade.getInstance();
		try {
			projectList = db.getProjects();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

}
