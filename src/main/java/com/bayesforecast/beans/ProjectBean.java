package com.bayesforecast.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import org.primefaces.event.RowEditEvent;
import com.bayesforecast.persistence.DatabaseFacade;
import com.bayesforecsast.model.Project;


@ManagedBean
public class ProjectBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Project> projectList;

	private List<Project> filteredList;
	private DatabaseFacade db;
	private Project selectedProject;

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}

	public ProjectBean() {
		db = DatabaseFacade.getInstance();
		try {
			projectList = db.getProjects();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onEdit(RowEditEvent event) throws SQLException {
		Project project;
		DatabaseFacade db = DatabaseFacade.getInstance();
		project = (Project) event.getObject();
		int id_project = project.getId();
		String comment = project.getComments();
		db.updateProjectComment(id_project, comment);

	}

	public void onCancel(RowEditEvent event) {

	}

	public String logOut() {
		ExternalContext tmpEC;
		Map<?, ?> sMap;
		tmpEC = FacesContext.getCurrentInstance().getExternalContext();
		sMap = tmpEC.getSessionMap();
		LoginBean user = (LoginBean) sMap.get("LoginBean");
		return user.logout();
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public List<Project> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<Project> filteredList) {
		this.filteredList = filteredList;
	}

	public void addProject() {
		projectList.add(new Project());
	}

	public void updateEvents() throws NamingException, SQLException {
		db = DatabaseFacade.getInstance();
		projectList = db.getProjects();

	}

}
