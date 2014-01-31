package com.bayesforecsast.model;





public class Project {
	private Integer id;
	private String code;
	private String comments;
	private String status;
	private boolean isInScope;
	private boolean belongsToAProgram;


	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
