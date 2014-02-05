package com.bayesforecast.beans;



import java.io.Serializable;
import java.util.ArrayList;  
import java.util.List;  
import com.bayesforecast.converter.ProjectConverter;
import com.bayesforecsast.model.Project;

  
public class AutoCompleteBean implements Serializable{  
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Project selectedProject;  
    
    


	private List<Project> projects;
  
    public AutoCompleteBean() {  
        projects = ProjectConverter.projectDB;  
    }  
  
    public Project getSelectedProject() {  
        return selectedProject;  
    }  
  
    public void setSelectedProject(Project selectedProject1) {  
        this.selectedProject = selectedProject1;  
    }  
  
  
    public List<Project> completeProject(String query) { 
    	//busca indistintamente códigos sin importar si están en MAYUS/MINUS
    	query = query.toUpperCase(); 
        List<Project> suggestions = new ArrayList<Project>();  
          
        for(Project p : projects) {  
            if(p.getCode().startsWith(query))  
                suggestions.add(p);  
        }  
          
        return suggestions;  
    }  
    

}  