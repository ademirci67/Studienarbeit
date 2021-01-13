package controller;

import model.Account;
import model.Faculty;
import model.Location;
import model.Modul;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import com.sun.javafx.logging.Logger;

import org.primefaces.event.CellEditEvent;
//import org.primefaces.event.


import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import controller.MessageForPrimefaces;

/**
*
* @author Anil
*/


@Named(value="facultyController")

@SessionScoped
public class FacultyController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	//@SuppressWarnings("cdi-ambiguous-dependency")
	@Inject 
	private Faculty faculty;
	
	@PostConstruct
    public void init() {
        facultyList = getFacultyListAll();
    }
 
	
	
	private String facultyName;
	private String facultyShortName;
	
	private boolean facultyNameOk = false;
	private boolean facultyShortNameOk = false;
	
	
	List<Faculty> facultyList;
	
	//modlist.add(getModulList());
	
	private Faculty facultySelected;
	
	public Faculty getFacultySelected() {
		return facultySelected;
	}
	  
	public void setFacultySelected(Faculty facultySelected) {
		this.facultySelected = facultySelected;
	}
	
	
	  
    public List<Faculty> getFacultyList() {
        return facultyList;
    }
    
	public Faculty getFaculty() {
		return faculty;
	}
	  
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}
	  
	public String getFacultyName() {
		return facultyName;
	}
	  
	public void setFacultyName(String facultyName) {
		if(facultyName!=null){
			this.facultyName = facultyName;
			facultyNameOk = true;
		}
		else{
			FacesMessage message = new FacesMessage("Faculty bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("FacultyList:facName_reg", message);
	        //String msg = "Modulkürzel bereits vorhanden.";
	        //addMessage("modKuerzel_reg",msg);
	    }
	}
	  
	public String getFacultyShortName() {
		return facultyShortName;
	}
	  
	public void setFacultyShortName(String facultyShortName) {
	    if(facultyShortName!=null){
	        this.facultyShortName = facultyShortName;
	        facultyShortNameOk=true;
	    }
	    else{
	    	FacesMessage message = new FacesMessage("Facultykürzel bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("FacultyList:facShortName_reg", message);
	        //String msg = "Modulname bereits vorhanden.";
	        //addMessage("modName_reg",msg);
	    }
	}
	  
	
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;  
	public void createFaculty() throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception  {
		EntityManager em = emf.createEntityManager();
		Faculty fac = new Faculty();  
		fac.setFacName(facultyName);    
		fac.setFacShortName(facultyShortName);      
		try {
	        ut.begin();   
	        em.joinTransaction();  
	        em.persist(fac);  
	        ut.commit(); 
	    }
	    catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
	        try {
	            ut.rollback();
	        } 
	        catch (IllegalStateException | SecurityException | SystemException ex) {
	        }
	    }
		em.close();
	}
	
	public void createDoFaculty() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(facultyNameOk == true && facultyShortNameOk == true ) {
			createFaculty();
			facultyList = getFacultyList();
		}
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	public List<Faculty> getFacultyListAll(){
		EntityManager em = emf.createEntityManager();
		TypedQuery<Faculty> query = em.createNamedQuery("Faculty.findAll", Faculty.class);
		facultyList = query.getResultList();
		return query.getResultList();
	}
	
	
	
	
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    public void deleteFaculty() throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception {
        facultyList.remove(facultySelected);
        //selectedmodul = null;
        //updateModul(modlist);
        
        EntityManager em = emf.createEntityManager();
        TypedQuery<Faculty> q = em.createNamedQuery("Faculty.findByFbid",Faculty.class);
        q.setParameter("fbid", facultySelected.getFbid());
        faculty = (Faculty)q.getSingleResult();
        
        try {
	        ut.begin();   
	        em.joinTransaction();  
	        //em.persist(q);
	        em.remove(faculty);
	        ut.commit(); 
	    }
	    catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
	        try {
	            ut.rollback();
	        } 
	        catch (IllegalStateException | SecurityException | SystemException ex) {
	        }
	    }       
		em.close();
    }
    
    
    public void onRowSelect(SelectEvent<Faculty> e) {
    	FacesMessage msg = new FacesMessage("Fakultät ausgewählt");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        facultySelected = e.getObject();
        
    }
    
    public void addFaculty(){
    	 try {
 	        ut.begin();
 	        EntityManager em = emf.createEntityManager();
 	        em.find(Faculty.class, facultySelected.getFbid());
 	        faculty.setFbid(facultySelected.getFbid());
 	        faculty.setFacName(facultySelected.getFacName());
 	        faculty.setFacShortName(facultySelected.getFacShortName());
 	        em.merge(faculty);
 	        ut.commit(); 
 	    }
 	    catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
 	        try {
 	            ut.rollback();
 	        } 
 	        catch (IllegalStateException | SecurityException | SystemException ex) {
 	        }
 	    }
    }
    
    
    
   // ---------------------------------------------------------------------------------------------------------------------
    
	
	  
		//Nachrichten an die View senden
	private void addMessage(String loginformidName, String msg) {
	   FacesMessage message = new FacesMessage(msg);
	   FacesContext.getCurrentInstance().addMessage(loginformidName, message);     
	}
  
  
  
  
}