package com.bayesforecsast.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class ProjectDataModel extends ListDataModel<Project> implements
		SelectableDataModel<Project>, Serializable {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProjectDataModel() {  
	    }  
	 
	public ProjectDataModel(List<Project> data) {  
	        super(data);  
	    }  
	
	@Override
	public Project getRowData(String rowKey) {
        List<Project> projects = (List<Project>) getWrappedData();  
        
        for(Project project : projects) {  
            if(project.getCode().equals(rowKey))  
                return project;  
        }  
          
        return null;
	}

	@Override
	public Object getRowKey(Project project) {
		// TODO Auto-generated method stub
		return project.getCode();
	}

}
