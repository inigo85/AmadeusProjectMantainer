package com.bayesforecast.persistence;

import java.sql.SQLException;
import java.util.List;

import com.bayesforecsast.model.*;

public interface IDatabaseFacade {

	public List<Project> getProjects() throws SQLException;

	public List<ProjectComment> getProjectComments(String userName) throws SQLException;

	public void updateProjectComment(Integer id_project, String comment,
			String userName) throws SQLException;

	public void deleteProjecComment(Integer id) throws SQLException;

	public void addProjectComment(ProjectComment projectComment, String userName)
			throws SQLException;

	public Integer getLastInsertedCommentIndex() throws SQLException;

	public List<User> getUsers() throws SQLException;

	public void insertUser(User user) throws SQLException;

	public void updateUser(Integer id, User userInfo) throws SQLException;

	public void deleteUser(Integer userId) throws SQLException;

	public User getUserByUserName(String userName) throws SQLException;
}
