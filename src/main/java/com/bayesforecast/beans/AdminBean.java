package com.bayesforecast.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.naming.NamingException;
import org.mindrot.jbcrypt.BCrypt;
import org.primefaces.event.RowEditEvent;
import com.bayesforecast.persistence.DatabaseFacade;
import com.bayesforecsast.model.User;

@ManagedBean
public class AdminBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4651671094737108941L;
	private List<User> userList;
	private List<User> filteredUserList;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<User> getFilteredUserList() {
		return filteredUserList;
	}

	public void setFilteredUserList(List<User> filteredUserList) {
		this.filteredUserList = filteredUserList;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	private DatabaseFacade db;

	public AdminBean() {
		db = DatabaseFacade.getInstance();
		try {
			userList = db.getUsers();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addUser() {
		userList.add(new User());
	}

	public void setUserId(String user_id) {
		this.id = user_id;
	}

	public void onEdit(RowEditEvent event) throws SQLException {
		User user;
		DatabaseFacade db = DatabaseFacade.getInstance();
		user = (User) event.getObject();
		if (user.getId() != null) {
			String hashedPasswd = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
			user.setPassword(hashedPasswd);
			db.updateUser(Integer.valueOf(user.getId()), user);
		} else {
			String hashedPasswd = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
			user.setPassword(hashedPasswd);
			db.insertUser(user);
		}

	}

	public void onCancel(RowEditEvent event) {
		// Verificar si al cancelar no se ha introducido nada
		User user;
		user = (User) event.getObject();
		if (user.getName() == null && user.getPassword() == null) {
			userList.remove(userList.size() - 1);
		}
	}

	public void onDelete(String id) throws NamingException, SQLException {
		int cont = 0;
		db.deleteUser(Integer.valueOf(id));
		for (User p : userList) {
			if (p.getId().compareTo(id) == 0)
				break;
			cont++;
		}
		userList.remove(cont);
	}

}
