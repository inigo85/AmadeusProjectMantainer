package com.bayesforecast.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import com.bayesforecast.util.CurrentDate;
import com.bayesforecsast.model.Project;
import com.bayesforecsast.model.ProjectComment;
import com.bayesforecsast.model.User;

public class DatabaseFacade implements IDatabaseFacade {

	private static DatabaseFacade dbFacade;
	private DataSource ds;

	/**
	 * Constructor privado de DatabaseFacade (patrón Singleton).
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 *             en caso de algún problema en la conexión a la base de datos
	 * 
	 */
	private DatabaseFacade() throws ClassNotFoundException, SQLException,
			NamingException {

		Context initialContext = new InitialContext();
		Context envCtx = (Context) initialContext.lookup("java:comp/env");
		ds = (DataSource) envCtx.lookup("jdbc/pmantainer");
	}

	/**
	 * Método "getInstance" correspondiente al patrón Singleton, para manejar
	 * únicamente una instancia de la fachada.
	 * 
	 * @throws SQLException
	 *             , NamingException, ClassNotFoundException
	 * 
	 * @return la instancia de la clase
	 * 
	 */
	public static DatabaseFacade getInstance() {
		if (dbFacade == null) {
			try {
				dbFacade = new DatabaseFacade();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dbFacade;
	}

	@Override
	public List<Project> getProjects() throws SQLException {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		List<Project> projectList = new ArrayList<Project>();
		try {
			connection = ds.getConnection();
			String sql;
			sql = "select x.co_gen_project, x.na_comment, x.id_project, x.dt_comment_date, x.in_comment_type, "
					+ "name from (select a.co_gen_project, na_comment, b.id_project, dt_comment_date, "
					+ "case when in_comment_type='I' then 'Inserted' when in_comment_type='U' "
					+ "then 'Edited' end as in_comment_type, id_user from(select a.co_gen_project, "
					+ "na_comment,  dt_comment_date, in_comment_type, id_user from(select  "
					+ "co_gen_project from amadeusit.art_d_project where co_gen_project is not null) a "
					+ "left join amadeusit.bys_project_comment b using(co_gen_project) "
					+ "group by a.co_gen_project, na_comment, dt_comment_date, in_comment_type, "
					+ "id_user order by a.co_gen_project) a inner join (select min(id_project) "
					+ "id_project, co_gen_project from amadeusit.art_d_project group by 2) "
					+ "b on a.co_gen_project = b.co_gen_project group by 1,2,3,4,5,6) x "
					+ "left join amadeusit.svt_user on x.id_user=id group by 1,2,3,4,5,6";
			st = connection.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				Project project = new Project();
				project.setId(rs.getInt("id_project"));
				project.setComments(rs.getString("na_comment"));
				project.setCode(rs.getString("co_gen_project"));
				project.setDate(rs.getDate("dt_comment_date"));
				if (rs.getString("in_comment_type") != null) {
					project.setType(rs.getString("in_comment_type").charAt(0));
				}
				project.setUser(rs.getString("name"));
				projectList.add(project);
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			DatabaseUtil.close(st);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(connection);
		}
		return projectList;
	}

	@Override
	public void updateProjectComment(Integer id_project, String comment,
			String userName) throws SQLException {
		Connection connection = null;
		PreparedStatement st = null;
		String sql = "";
		User user = null;
		char type = 'U'; // Update
		try {
			connection = ds.getConnection();
			user = getUserByUserName(userName);
			sql = "update amadeusit.bys_project_comment "
					+ "set na_comment = ?, dt_comment_date = ?, "
					+ " in_comment_type = ?, id_user = ? where id = ?";
			st = connection.prepareStatement(sql);
			st.setString(1, comment);
			st.setDate(2, new Date(CurrentDate.getCurrentDate().getTime()));
			st.setString(3, String.valueOf(type));
			st.setInt(4, Integer.valueOf(user.getId()));
			st.setInt(5, id_project);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			DatabaseUtil.close(st);
			DatabaseUtil.close(connection);
		}

	}

	@Override
	public List<ProjectComment> getProjectComments(String userName)
			throws SQLException {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		List<ProjectComment> projectCommentList = new ArrayList<ProjectComment>();
		User user = null;
		try {
			connection = ds.getConnection();
			String sql;
			user = getUserByUserName(userName);
			sql = "select * from amadeusit.bys_project_comment"
					+ " where co_gen_project is not null and id_user = ?";
			st = connection.prepareStatement(sql);
			st.setInt(1, Integer.valueOf(user.getId()));
			rs = st.executeQuery();
			while (rs.next()) {
				ProjectComment project = new ProjectComment();
				project.setId(rs.getInt("id"));
				project.setComment(rs.getString("na_comment"));
				project.setProjectCode(rs.getString("co_gen_project"));
				project.setDate(rs.getDate("dt_comment_date"));
				if (rs.getString("in_comment_type") != null) {
					project.setType(rs.getString("in_comment_type").charAt(0));
				}
				project.setUserId(rs.getInt("id_user"));
				projectCommentList.add(project);
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			DatabaseUtil.close(st);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(connection);
		}
		return projectCommentList;
	}

	@Override
	public void deleteProjecComment(Integer id) throws SQLException {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = ds.getConnection();
			String sql;
			sql = "DELETE FROM amadeusit.bys_project_comment WHERE id= ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(conn, st);
		}

	}

	@Override
	public void deleteUser(Integer userId) throws SQLException {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = ds.getConnection();
			String sql;
			sql = "DELETE FROM amadeusit.svt_user WHERE id= ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, userId);
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(conn, st);
		}

	}

	@Override
	public void addProjectComment(ProjectComment projectComment, String userName)
			throws SQLException {
		Connection conn = null;
		PreparedStatement st = null;
		User user = null;
		char type = 'I'; // insertion
		try {
			conn = ds.getConnection();
			user = getUserByUserName(userName);
			String sql;
			sql = "INSERT INTO amadeusit.bys_project_comment"
					+ "(co_gen_project, na_comment, dt_comment_date, in_comment_type, id_user) "
					+ "VALUES(?, ?, ?, ?, ?);";
			st = conn.prepareStatement(sql);
			st.setString(1, projectComment.getProjectCode());
			st.setString(2, projectComment.getComment());
			st.setDate(3, new Date(CurrentDate.getCurrentDate().getTime()));
			st.setString(4, String.valueOf(type));
			st.setInt(5, Integer.valueOf(user.getId()));
			st.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(conn, st);
		}

	}

	@Override
	public Integer getLastInsertedCommentIndex() throws SQLException {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int id = -1;
		try {
			conn = ds.getConnection();
			String sql;
			sql = "select last_value from amadeusit.bys_project_comment_id_seq";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			if (rs.next()) {
				id = rs.getInt("last_value");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(conn, st, rs);
		}
		return id;
	}

	@Override
	public List<User> getUsers() throws SQLException {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		List<User> userList = new ArrayList<User>();
		try {
			connection = ds.getConnection();
			String sql;
			sql = "select * from amadeusit.svt_user";
			st = connection.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(String.valueOf(rs.getInt("id")));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setType(rs.getString("type").charAt(0));
				userList.add(user);
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			DatabaseUtil.close(st);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(connection);
		}
		return userList;
	}

	@Override
	public void insertUser(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = ds.getConnection();
			String sql;
			sql = "INSERT INTO amadeusit.svt_user(name, password, type) VALUES(?, ?, ?);";
			st = conn.prepareStatement(sql);
			st.setString(1, user.getName());
			st.setString(2, user.getPassword());
			st.setString(3, String.valueOf(user.getType()));
			st.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(conn, st);
		}

	}

	@Override
	public void updateUser(Integer id, User userInfo) throws SQLException {
		Connection connection = null;
		PreparedStatement st = null;
		String sql = "";
		try {
			connection = ds.getConnection();
			sql = "update amadeusit.svt_user set name = ?,  type = ? where id = ?";
			st = connection.prepareStatement(sql);
			st.setString(1, userInfo.getName());
			st.setString(2, String.valueOf(userInfo.getType()));
			st.setInt(3, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			DatabaseUtil.close(st);
			DatabaseUtil.close(connection);
		}

	}

	@Override
	public User getUserByUserName(String userName) throws SQLException {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		User user = null;
		try {
			connection = ds.getConnection();
			String sql;
			sql = "select * from amadeusit.svt_user" + " where name = ?";
			st = connection.prepareStatement(sql);
			st.setString(1, userName);
			rs = st.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(String.valueOf(rs.getInt("id")));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password").replace("'", "''"));
				user.setType(rs.getString("type").charAt(0));
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			DatabaseUtil.close(st);
			DatabaseUtil.close(rs);
			DatabaseUtil.close(connection);
		}
		return user;
	}

}
