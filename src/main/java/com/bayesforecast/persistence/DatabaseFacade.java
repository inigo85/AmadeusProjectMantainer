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
import com.bayesforecsast.model.User;

public class DatabaseFacade implements IDatabaseFacade {

	private static DatabaseFacade dbFacade;
	private DataSource ds; /* ?¿? */

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
		// para ejecutar en Tomcat
		// ds = (DataSource)
		// initialContext.lookup("java:/comp/env/jdbc/pmantainer");
		Context envCtx = (Context) initialContext.lookup("java:comp/env");
		ds = (DataSource) envCtx.lookup("jdbc/pmantainer");
		// para ejecutar en GlassFish
		// ds = (DataSource) contextoInicial.lookup("jdbc/sisdis");

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
			sql = "select * from amadeusit.art_d_project_borrar"
					+ " where co_gen_project is not null";
			st = connection.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				Project project = new Project();
				project.setId(rs.getInt("id_project"));
				project.setComments(rs.getString("ds_comments"));
				project.setCode(rs.getString("co_project"));
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
			sql = "update amadeusit.art_d_project_borrar set ds_comments = ? where id_project = ?";
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
			//String sql;
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

}
