package com.bayesforecast.converter;

import java.sql.SQLException;
import java.util.ArrayList;  
import java.util.List;  

import javax.faces.application.FacesMessage;  
  
import javax.faces.component.UIComponent;  
import javax.faces.context.FacesContext;  
import javax.faces.convert.Converter;  
import javax.faces.convert.ConverterException;  
import javax.faces.convert.FacesConverter;

import com.bayesforecast.persistence.DatabaseFacade;
import com.bayesforecsast.model.Project;
  
  
@FacesConverter(forClass=Project.class,value="projectConverter")
public class ProjectConverter implements Converter {  
  
    public static List<Project> projectDB;
    static DatabaseFacade db = null;
  
    static {  
        projectDB = new ArrayList<Project>();  
        db = DatabaseFacade.getInstance();
        try {
			projectDB = db.getProjects();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
  
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.trim().equals("")) {  
            return null;  
        } else {  
            try {  
                int number = Integer.parseInt(submittedValue);  
  
                for (Project p : projectDB) {  
                    if (p.getId() == number) {  
                        return p;  
                    }  
                }  
  
            } catch(NumberFormatException exception) {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid project"));  
            }  
        }  
  
        return null;  
    }  
  
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((Project) value).getId());  
        }  
    }  
}  