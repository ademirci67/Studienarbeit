package controller;

import model.Modul;
import model.Pruefcode;
import model.Dozenten;
import model.Studiengang;
import model.Sgmodul;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.primefaces.event.SelectEvent;

import EJB.DozentenFacadeLocal;
import EJB.ModulFacadeLocal;
import EJB.SgModulFacadeLocal;
import EJB.StudiengangFacadeLocal;


/**
*
* @author Anil
*/

@Named(value="sgmodulController")
@SessionScoped
public class SgmodulController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	@Inject 
	private Sgmodul sgmodul;
	private Studiengang course;
	private Modul module;
	private Dozenten professor;
	
	@EJB
	private SgModulFacadeLocal sgModulFacadeLocal;
	@EJB
	private ModulFacadeLocal modulEJB;
	@EJB
	private StudiengangFacadeLocal courseEJB;
	@EJB
	private DozentenFacadeLocal professorEJB;
	
	/**
	 * Initilisierung
	 */
	@PostConstruct
    public void init() {
		sgmodulList = getSgmodulListAll();
		moduleList = getModulListAll();
		courseList = getCourseListAll();
		professorList = getProfessorListAll();
	 }
  
	
    private int moduleSemester;
	private String sgmodulNote;
	
    private int courseId;
	private int professorId;
	private int moduleId;
	
	
	List<Sgmodul> sgmodulList;
	List<Studiengang> courseList;
	List<Modul> moduleList;
	List<Dozenten> professorList;
	
	private List<Studiengang> courseSort = null;
	private List<Modul> modulSort = null;
	private List<Dozenten> professorSort = null;
	
	
	private Sgmodul sgmodulSelected;
	
	public List<Studiengang> getCourseSort() {
		return courseSort;
	}

	public void setCourseSort(List<Studiengang> courseSort) {
		this.courseSort = courseSort;
	}

	public List<Modul> getModulSort() {
		return modulSort;
	}

	public void setModulSort(List<Modul> modulSort) {
		this.modulSort = modulSort;
	}

	public List<Dozenten> getProfessorSort() {
		return professorSort;
	}

	public void setProfessorSort(List<Dozenten> professorSort) {
		this.professorSort = professorSort;
	}

	public List<Studiengang> getCourseList() {
		return courseList;
	}
	
	public void setCourseList(List<Studiengang> courseList) {
		this.courseList = courseList;
	}

	public List<Modul> getModuleList() {
		return moduleList;
	}
	
	public void setModuleList(List<Modul> moduleList) {
		this.moduleList = moduleList;
	}

	public List<Dozenten> getProfessorList() {
		return professorList;
	}
	
	public void setProfessorList(List<Dozenten> professorList) {
		this.professorList = professorList;
	}

	// Getter und Setter
	public String getSgmodulNote() {
		return sgmodulNote;
	}

	public void setSgmodulNote(String sgmodulNote) {
		this.sgmodulNote = sgmodulNote;
	}

	public Studiengang getCourse() {
		return course;
	}

	public void setCourse(Studiengang course) {
		this.course = course;
	}
	
	public Modul getModule() {
		return module;
	}

	public void setModule(Modul module) {
		this.module = module;
	}
	
	public Dozenten getProfessor() {
		return professor;
	}

	public void setProfessor(Dozenten professor) {
		this.professor = professor;
	}
	
	public Sgmodul getSgmodul() {
		return sgmodul;
	}

	public void setSgmodul(Sgmodul sgmodul) {
		this.sgmodul = sgmodul;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
			this.courseId = courseId;
	}			

	public int getProfessorId() {
		return professorId;
	}

	public void setProfessorId(int professorId) {
			this.professorId = professorId;
	}
	
	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
			this.moduleId = moduleId;
	}

	public int getModuleSemester() {
		return moduleSemester;
	}

	public void setModuleSemester(int moduleSemester) {
		this.moduleSemester = moduleSemester;
	}

	public List<Sgmodul> getSgmodulList() {
		return sgmodulList;
	}
	
	public void setSgmodulList(List<Sgmodul> sgmodulList) {
		this.sgmodulList = sgmodulList;
		
	}

	
	public Sgmodul getSgmodulSelected() {
		return sgmodulSelected;
	}

	public void setSgmodulSelected(Sgmodul sgmodulSelected) {
		this.sgmodulSelected = sgmodulSelected;
	}
	
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;
	
	/**
	 * Erstellen eines Studiengangmoduls
	 * @throws Exception
	 */
	public void createSgmodul() throws Exception  {
		String msg;
		EntityManager em = emf.createEntityManager();
		Sgmodul sgm = new Sgmodul();  
		sgm.setSGMNotiz(sgmodulNote);
		sgm.setModSemester(moduleSemester);
		sgm.setModul(findMod(moduleId));
		sgm.setDozenten(findDoz(professorId));
		sgm.setStudiengang(findSg(courseId));
		try {
			sgModulFacadeLocal.create(sgm);
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
	 * Erstellt ein Studiengangsmodul und aktualisiert dann die Liste
	 * @throws SecurityException
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws Exception
	 */
	public void createDoSgmodul() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
			createSgmodul();
			sgmodulList = getSgmodulListAll();
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Laden der Studiengangmodulliste
	 * @return
	 */
	public List<Sgmodul> getSgmodulListAll(){
		return sgModulFacadeLocal.findAll();
	}
	
	/**
	 * Laden der sortierten Modulliste
	 * @return
	 */
	public List<Modul> getModulListAll(){
		
		if (modulSort==null) {
			modulSort = modulEJB.findAll();
			if (modulSort != null) {
				Collections.sort(modulSort, (a,b) -> {
					return a.getModName().compareToIgnoreCase(b.getModName());
				});
				}
			}
		
		return  modulSort;
	}
	
	/**
	 * Laden der sortierten Studiengangliste
	 * @return
	 */
	public List<Studiengang> getCourseListAll(){
		
		if (courseSort==null) {
			courseSort = courseEJB.findAll();
			if (courseSort != null) {
				Collections.sort(courseSort, (a,b) -> {
					return a.getSGName().compareToIgnoreCase(b.getSGName());
				});
				}
			}
		
		return  courseSort;
	}
	
	/**
	 * Laden der sortierten Dozentenliste
	 * @return
	 */
	public List<Dozenten> getProfessorListAll(){
		
		if (professorSort==null) {
			professorSort = professorEJB.findAll();
			if (professorSort != null) {
				Collections.sort(professorSort, (a,b) -> {
					return a.getDName().compareToIgnoreCase(b.getDName());
				});
				}
			}
		
		return  professorSort;
	}
	
	 
	/**
	 * Ausgewählte Zeile in sgmodulSelected speichern mit den ganzen Fremdschlüsseln
	 * @param e
	 */
	public void onRowSelect(SelectEvent<Sgmodul> e) {
		String msg = "sgmodule";
        addInfoMessage(msg);
        
        sgmodulSelected = e.getObject();
        
        professorId = sgmodulSelected.getDozenten().getDid();
        moduleId = sgmodulSelected.getModul().getModID();
        courseId = sgmodulSelected.getStudiengang().getSgid();
        
    }
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Löschen eines Studiengangmoduls
     * @throws Exception
     */
    public void deleteSgmodul() throws Exception {
    	String msg;
    	sgmodulList.remove(sgmodulSelected);        
        //EntityManager em = emf.createEntityManager();
        //TypedQuery<Sgmodul> q = em.createNamedQuery("Sgmodul.findBySgmid",Sgmodul.class);
        //q.setParameter("sgmid", sgmodulSelected.getSgmid());
        //sgmodul = (Sgmodul)q.getSingleResult();
        sgmodul = sgModulFacadeLocal.find(sgmodulSelected.getSgmid());
        try {
        	sgModulFacadeLocal.remove(sgmodul);
        	msg = "delete";
            //addMessage("messages", msg);
            addInfoMessage(msg);
	    }
	    catch (Exception e) {
	    	msg = "notDelete";
            //addMessage("messages", msg);
            addInfoMessage(msg);
	    	
	    }
		//em.close();
    }
    
   //----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Finden eines Moduls anhand der ID
     * @param mod
     * @return
     */
    private Modul findMod(int mod) {
    	/*
        try{
            EntityManager em = emf.createEntityManager(); 
            TypedQuery<Modul> query
                = em.createNamedQuery("Modul.findByModID",Modul.class);
            query.setParameter("modID", mod);
            module = (Modul)query.getSingleResult();
        }
        catch(Exception e){   
        }
        */
        return module = modulEJB.find(mod);
    }
    
    /**
     * Finden eines Studiengangs anhand der ID
     * @param sg
     * @return
     */
    private Studiengang findSg(int sg) {
        /*
    	try{
            EntityManager em = emf.createEntityManager(); 
            TypedQuery<Studiengang> query
                = em.createNamedQuery("Studiengang.findBySgid",Studiengang.class);
            query.setParameter("sgid", sg);
            course = (Studiengang)query.getSingleResult();
        }
        catch(Exception e){   
        }
        */
        return course = courseEJB.find(sg);
    }
    
    /**
     * Finden eines Dozenten anhand der ID
     * @param doz
     * @return
     */
    private Dozenten findDoz(int doz) {
        /*
    	try{
            EntityManager em = emf.createEntityManager(); 
            TypedQuery<Dozenten> query
                = em.createNamedQuery("Dozenten.findByDid",Dozenten.class);
            query.setParameter("did", doz);
            professor = (Dozenten)query.getSingleResult();
        }
        catch(Exception e){   
        }
        */
        return professor =  professorEJB.find(doz) ;
    }
    
   //----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Bearbeiten eines Studiengangmoduls
     * 
     */
    public void addSgmodul(){
    	EntityManager em = emf.createEntityManager();
    	String msg;
      	 try {
	        em.find(Sgmodul.class, sgmodulSelected.getSgmid());
	        sgmodul.setSgmid(sgmodulSelected.getSgmid());
	        sgmodul.setModSemester(sgmodulSelected.getModSemester());
	        sgmodul.setSGMNotiz(sgmodulSelected.getSGMNotiz());
	        sgmodul.setModul(findMod(moduleId));
	        sgmodul.setDozenten(findDoz(professorId));
	        sgmodul.setStudiengang(findSg(courseId));
	        sgModulFacadeLocal.edit(sgmodul);
	        msg = "edit";
            //addMessage("messages", msg);
            addInfoMessage(msg);
   	    }
   	    catch (Exception e) {
   	    	msg = "notEdit";
            //addMessage("messages", msg);
            addInfoMessage(msg);
   	    }
      	sgmodulList = getSgmodulListAll();
      	em.close();
      }
    
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
