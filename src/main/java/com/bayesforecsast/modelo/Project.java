package com.bayesforecsast.modelo;

import java.sql.SQLException;
import java.util.List;
import com.bayesforecast.persistence.DatabaseFacade;



public class Project {

	private String code;
	private String comments;
	private String status;
	private boolean isInScope;
	private boolean belongsToAProgram;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isInScope() {
		return isInScope;
	}

	public void setInScope(boolean isInScope) {
		this.isInScope = isInScope;
	}

	public boolean isBelongsToAProgram() {
		return belongsToAProgram;
	}

	public void setBelongsToAProgram(boolean belongsToAProgram) {
		this.belongsToAProgram = belongsToAProgram;
	}

}
