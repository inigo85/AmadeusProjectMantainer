package com.bayesforecast.persistence;

import java.sql.SQLException;
import java.util.List;

import com.bayesforecsast.modelo.*;

public interface IDatabaseFacade {

	public List<Project> getProjects() throws SQLException;

}
