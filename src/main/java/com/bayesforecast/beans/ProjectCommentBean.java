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
			projectCommentList = db.getProjectComments();
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

	public void onEdit(RowEditEvent event) throws SQLException {
		ProjectComment projectComment;
		DatabaseFacade db = DatabaseFacade.getInstance();
		projectComment = (ProjectComment) event.getObject();
		if (projectComment.getProjectCode() != null) {
			db.updateProjectComment(projectComment.getId(),
					projectComment.getComment());
		} else {
			// Añado último projectCode seleccionado del "Autocomplete"
			projectComment.setProjectCode(selectedProjectCode);
			db.addProjectComment(projectComment);
			projectComment.setId(db.getLastInsertedCommentIndex());
		}

	}

	public void onCancel(RowEditEvent event) {

	}

	public void onDelete(String id) throws NamingException, SQLException {
		int cont = 0;
		db.deleteProjecComment(Integer.valueOf(id));
		for(ProjectComment p:projectCommentList){
			if(p.getId().compareTo(Integer.valueOf(id))==0)
				break;
			cont++;
		}
		projectCommentList.remove(cont);
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
