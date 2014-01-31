package com.bayesforecast.persistence;

import java.sql.SQLException;
import java.util.List;

import com.bayesforecsast.model.*;

public interface IDatabaseFacade {

	public List<Project> getProjects() throws SQLException;

	public void updateProjectComment(Integer id_project, String comment) throws SQLException;
	
	public User login(String user, String password);
}
