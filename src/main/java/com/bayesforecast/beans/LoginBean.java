package com.bayesforecast.beans;

import java.io.Serializable;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import com.bayesforecast.persistence.DatabaseFacade;
import com.bayesforecsast.model.User;

/**
 * Bean implementation class ValidateBean.
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2662757956069613054L;

	/**
	 * Login user.
	 */
	private String login;

	/**
	 * Password user.
	 */
	private String password;

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private HttpSession session = null;

	/**
	 * Empty constructor.
	 */
	public LoginBean() {

	}

	/**
	 * Get the login user.
	 * 
	 * @return login login user.
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Set the login user.
	 * 
	 * @param login
	 *            login user.
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Get the password user.
	 * 
	 * @return password password user.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password user.
	 * 
	 * @param password
	 *            password user.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public HttpSession getS() {
		return session;
	}

	public void setS(HttpSession session) {
		this.session = session;
	}

	public String obtainLogin() throws NamingException, SQLException {
		DatabaseFacade db = DatabaseFacade.getInstance();
		User user;
		user = db.getUserByUserName(this.login);
		if (user != null) {
			setId(Integer.valueOf(user.getId()));
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put("username", this.login);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(false);
			if (BCrypt.checkpw(this.password, user.getPassword())) {
				if (user.getType() == 'A') {
					return "admin.xhtml?faces-redirect=true";
				} else {
					return "main.xhtml?faces-redirect=true";
				}
			}
		}
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"User not allowed to access this system", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "failed";

	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();
		return "login.xhtml?faces-redirect=true";
	}

}
