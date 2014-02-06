package com.bayesforecsast.model;

import java.sql.Date;

public class Project {
	private Integer id;
	private String code;
	private String comments;
	private String status;
	private boolean isInScope;
	private boolean belongsToAProgram;
	private Date date;
	private char type;
	private String strType;
	private String user;

	public String getStrType() {
		return strType;
	}

	public void setStrType(String strType) {
		this.strType = strType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		if (type == 'I'){
			this.strType = "Inserted";
		}else{
			this.strType = "Updated";
		}
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

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
