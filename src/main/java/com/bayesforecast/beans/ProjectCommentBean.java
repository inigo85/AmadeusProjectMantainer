package com.bayesforecast.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import com.bayesforecast.persistence.DatabaseFacade;
import com.bayesforecsast.model.Project;
import com.bayesforecsast.model.ProjectComment;

public class ProjectCommentBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ProjectComment> projectCommentList;
	private DatabaseFacade db;
	private String id;
	private String selectedProjectCode;

	public String getSelectedProjectCode() {
		return selectedProjectCode;
	}

	public void setSelectedProjectCode(String selectedProjectCode) {
		this.selectedProjectCode = selectedProjectCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProjectCommentBean() {
		db = DatabaseFacade.getInstance();
		try {
			projectCommentList = db.getProjectComments(getCommentUserName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<ProjectComment> getProjectCommentList() {
		return projectCommentList;
	}

	public void setProjectCommentList(List<ProjectComment> projectCommentList) {
		this.projectCommentList = projectCommentList;
	}

	public void onEdit(RowEditEvent event) throws SQLException, NamingException {
		ProjectComment projectComment;
		DatabaseFacade db = DatabaseFacade.getInstance();
		projectComment = (ProjectComment) event.getObject();
		String userName = getCommentUserName();
		if (projectComment.getProjectCode() != null) {
			db.updateProjectComment(projectComment.getId(),
					projectComment.getComment(), userName);
		} else {
			// A�ado �ltimo projectCode seleccionado del "Autocomplete"
			projectComment.setProjectCode(selectedProjectCode);
			db.addProjectComment(projectComment, userName);
			projectComment.setId(db.getLastInsertedCommentIndex());
		}
		refreshDatatable();

	}

	public String getCommentUserName() {
		String userName = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("username");
		return userName;
	}

	public void onCancel(RowEditEvent event) {
		// Verificar si al cancelar no se ha introducido nada
		ProjectComment projectComment;
		projectComment = (ProjectComment) event.getObject();
		if (projectComment.getProjectCode() == null
				&& projectComment.getComment() == null) {
			projectCommentList.remove(projectCommentList.size() - 1);
		}
	}

	public void onDelete(String id) throws NamingException, SQLException {
		int cont = 0;
		db.deleteProjecComment(Integer.valueOf(id));
		for (ProjectComment p : projectCommentList) {
			if (p.getId().compareTo(Integer.valueOf(id)) == 0)
				break;
			cont++;
		}
		projectCommentList.remove(cont);
		refreshDatatable();
	}

	private void refreshDatatable() throws NamingException, SQLException {
		ExternalContext tmpEC;
		Map<?, ?> sMap;
		tmpEC = FacesContext.getCurrentInstance().getExternalContext();
		sMap = tmpEC.getSessionMap();
		ProjectBean projectBean = (ProjectBean) sMap.get("projectBean");
		projectBean.updateEvents();
	}

	public void addComment() {
		projectCommentList.add(new ProjectComment());
	}

	public void setCommentId(String project_comment_id) {
		this.id = project_comment_id;
	}

	public void handleSelect(SelectEvent event) {
		Project p = (Project) event.getObject();
		selectedProjectCode = p.getCode();
	}

}
