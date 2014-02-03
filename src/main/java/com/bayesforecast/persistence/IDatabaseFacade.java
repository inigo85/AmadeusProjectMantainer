package com.bayesforecast.persistence;

import java.sql.SQLException;
import java.util.List;

import com.bayesforecsast.model.*;

public interface IDatabaseFacade {

	public List<Project> getProjects() throws SQLException;

	public List<ProjectComment> getProjectComments() throws SQLException;

	public void updateProjectComment(Integer id_project, String comment)
			throws SQLException;

	public void deleteProjecComment(Integer id) throws SQLException;

	public void addProjectComment(ProjectComment projectComment)
			throws SQLException;

	public Integer getLastInsertedCommentIndex() throws SQLException;

	public User login(String user, String password) throws SQLException;
}
