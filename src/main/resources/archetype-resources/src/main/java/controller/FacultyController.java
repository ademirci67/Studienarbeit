package controller;


import model.Faculty;


import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import EJB.FacultyFacadeLocal;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import javax.persistence.PersistenceUnit;

import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;


import org.primefaces.event.SelectEvent;


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
	
	@Inject 
	private Faculty faculty;
	
	@EJB
	private FacultyFacadeLocal facFacadeLocal;
	
	/**
	 * Initialisierung
	 */
	@PostConstruct
    public void init() {
        facultyList = getFacultyListAll();
    }
 
	
	
	private String facultyName;
	private String facultyShortName;
	
	private boolean facultyNameOk = false;
	private boolean facultyShortNameOk = false;
	
	List<Faculty> facultyList;
	
	private Faculty facultySelected;
	
	// Getter und Setter
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
	}
	  
	public String getFacultyShortName() {
		return facultyShortName;
	}
	  
	public void setFacultyShortName(String facultyShortName) {
	    if(facultyShortName!=null){
	        this.facultyShortName = facultyShortName;
	        facultyShortNameOk=true;
	    }
	}
	  
	
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;
	
	/**
	 * Erstellen eines Fachbereichs
	 * @throws Exception
	 */
	public void createFaculty() throws Exception  {
		String msg;
		EntityManager em = emf.createEntityManager();
		Faculty fac = new Faculty();  
		fac.setFacName(facultyName);    
		fac.setFacShortName(facultyShortName);      
		try {
			facFacadeLocal.create(fac);
			msg = "entry";
            //addMessage("messages", msg);
            addInfoMessage(msg);
	    }
	    catch (Exception e) {
	    	msg = "notEntry";
            //addMessage("messages", msg);
            addInfoMessage(msg);
	       
	    }
		em.close();
	}
	
	/**
	 * Schaut ob Fachbereichsname und Fachbereichskurzform gesetzt worden sind, danach wird der Eintrag erstellt und zum Schluß wird die Liste aktualisiert.
	 * @throws SecurityException
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws Exception
	 */
	public void createDoFaculty() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(facultyNameOk == true && facultyShortNameOk == true ) {
			createFaculty();
			facultyList = getFacultyListAll();
		}
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Laden der Fachbereichsliste
	 * @return
	 */
	public List<Faculty> getFacultyListAll(){		
		List<Faculty> listFac;
		listFac = facFacadeLocal.findAll();
		return listFac;
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Löschen eines Fachbereichseintrags
     * @throws Exception
     */
    public void deleteFaculty() throws Exception {
    	String msg;
        facultyList.remove(facultySelected);        
        EntityManager em = emf.createEntityManager();
        TypedQuery<Faculty> q = em.createNamedQuery("Faculty.findByFbid",Faculty.class);
        q.setParameter("fbid", facultySelected.getFbid());
        faculty = (Faculty)q.getSingleResult();
        
        try {
        	this.facFacadeLocal.remove(faculty);
        	msg = "delete";
            //addMessage("messages", msg);
            addInfoMessage(msg);
	    }
	    catch (Exception e) {
	    	msg = "notDelete";
            //addMessage("messages", msg);
            addInfoMessage(msg);
	        
	    }       
		em.close();
    }
     
    /**
     * Ausgewählte Zeile wird in facultySelected gespeichert.
     * @param e
     */
    public void onRowSelect(SelectEvent<Faculty> e) {
    	String msg = "faculty";
        addInfoMessage(msg);
        
        facultySelected = e.getObject();
        
    }
    
    /**
     * Bearbeiten eines Fachbereichseintrags
     */
    public void addFaculty(){
    	EntityManager em = emf.createEntityManager();
    	String msg;
    	 try { 
 	       em.find(Faculty.class, facultySelected.getFbid());
 	       faculty.setFbid(facultySelected.getFbid());
 	       faculty.setFacName(facultySelected.getFacName());
 	       faculty.setFacShortName(facultySelected.getFacShortName());
 	       facFacadeLocal.edit(faculty);
 	      msg = "edit";
          //addMessage("messages", msg);
          addInfoMessage(msg);
	    }
	    catch (Exception e) {
	    	msg = "notEdit";
           //addMessage("messages", msg);
           addInfoMessage(msg);
	    }
    	facultyList = getFacultyListAll();
    	em.close();
    }
    
   // ---------------------------------------------------------------------------------------------------------------------
	  
	/**
	 * Nachrichten an die View senden
	 * @param loginformidName
	 * @param msg
	 */
	private void addMessage(String loginformidName, String msg) {
	   FacesMessage message = new FacesMessage(msg);
	   FacesContext.getCurrentInstance().addMessage(loginformidName, message);     
	}

	/**
	 * Faces messages ausgeben.
	 * @param str
	 */
	public static void addInfoMessage(String str) {
		  FacesContext context = FacesContext.getCurrentInstance();
		  ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
		  String message = bundle.getString(str);
		  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
	}
  
}