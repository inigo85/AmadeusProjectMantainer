package com.bayesforecast.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
			sql = "select a.co_gen_project, na_comment, b.id_project from("
					+ "select a.co_gen_project, na_comment from "
					+ "( select  co_gen_project from amadeusit.art_d_project "
					+ "where co_gen_project is not null) a left join "
					+ "amadeusit.bys_project_comment b using(co_gen_project) "
					+ "group by  a.co_gen_project, na_comment order by a.co_gen_project) "
					+ "a inner join (select min(id_project) id_project, co_gen_project "
					+ "from amadeusit.art_d_project group by 2) b "
					+ "on a.co_gen_project = b.co_gen_project group by 1,2,3;";
			st = connection.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				Project project = new Project();
				project.setId(rs.getInt("id_project"));
				project.setComments(rs.getString("na_comment"));
				project.setCode(rs.getString("co_gen_project"));
				// project.setComments(rs.getString("ds_comments"));
				// Falta incluir campos
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
	public void updateProjectComment(Integer id_project, String comment)
			throws SQLException {
		Connection connection = null;
		PreparedStatement st = null;
		String sql = "";
		try {
			connection = ds.getConnection();
			sql = "update amadeusit.bys_project_comment set na_comment = ? where id = ?";
			st = connection.prepareStatement(sql);
			st.setString(1, comment);
			st.setInt(2, id_project);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			DatabaseUtil.close(st);
			DatabaseUtil.close(connection);
		}

	}

	@Override
	public User login(String user, String password) {

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		User usu = null;
		try {
			conn = ds.getConnection();
			// String sql;
			/*
			 * sql = "SELECT * FROM usuario WHERE nombre='" + usuario +
			 * "' AND contraseña='" + contrasena + "';"; st =
			 * conn.prepareStatement(sql); rs = st.executeQuery(); if
			 * (rs.next()) { usu = new Usuario(); usu.setId(rs.getString("id"));
			 * usu.setTipo(rs.getString("tipo").charAt(0));
			 * usu.setNombre(rs.getString("nombre"));
			 * usu.setEmail(rs.getString("email"));
			 * usu.setContrasena(rs.getString("contraseña")); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(conn, st, rs);
		}
		return usu;

	}

	@Override
	public List<ProjectComment> getProjectComments() throws SQLException {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		List<ProjectComment> projectCommentList = new ArrayList<ProjectComment>();
		try {
			connection = ds.getConnection();
			String sql;
			sql = "select * from amadeusit.bys_project_comment"
					+ " where co_gen_project is not null";
			st = connection.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				ProjectComment project = new ProjectComment();
				project.setId(rs.getInt("id"));
				project.setComment(rs.getString("na_comment"));
				project.setProjectCode(rs.getString("co_gen_project"));
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
	public void addProjectComment(ProjectComment projectComment)
			throws SQLException {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = ds.getConnection();
			String sql;
			sql = "INSERT INTO amadeusit.bys_project_comment(co_gen_project, na_comment) VALUES(?, ?);";
			st = conn.prepareStatement(sql);
			st.setString(1, projectComment.getProjectCode());
			st.setString(2, projectComment.getComment());
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

}
