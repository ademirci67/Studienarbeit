package controller;


import model.Stundenplansemester;
import model.Stundenplanstatus;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
import javax.ejb.EJB;
import EJB.StundenplansemesterFacadeLocal;
import EJB.StundenplanstatusFacadeLocal;

/**
*
* @author Anil
*/

@Named(value="stundenplansemesterController")
@SessionScoped
public class StundenplansemesterController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	@Inject 
	private Stundenplansemester scheduleSemester;
	private Stundenplanstatus scheduleStatus;
	
	@EJB
	private StundenplansemesterFacadeLocal stundenplansemesterFacadeLocal;
	@EJB
	private StundenplanstatusFacadeLocal scheduleStatusEJB;

	/**
	 * Initialisierung
	 */
	@PostConstruct
    public void init() {
		scheduleSemesterList = getStundenplansemesterList();
		scheduleStatusList = getScheduleStatusList();
    }
 
    List<Stundenplanstatus> scheduleStatusList ;

    private int scheduleYear;
	private Integer scheduleCalendarWeek;
	private String scheduleSemesterSection;
	private Date startDate;
	private Date endDate;
	private boolean scheduleCalendarWeekOk = false;
	private boolean scheduleSemesterSectionOk = false;
	private boolean startDateOk = false;
	private boolean endDateOk = false;
	
	List<Stundenplansemester> scheduleSemesterList;
	private int scheduleSemesterId;
	
	private Stundenplansemester scheduleSemesterSelected;
	
	// Getter  und Setter
	public int getScheduleSemesterId() {
		return scheduleSemesterId;
	}

	public void setScheduleSemesterId(int scheduleSemesterId) {
		this.scheduleSemesterId = scheduleSemesterId;
	}

	public Stundenplansemester getScheduleSemester() {
		return scheduleSemester;
	}

	public void setScheduleSemester(Stundenplansemester scheduleSemester) {
		this.scheduleSemester = scheduleSemester;
	}

	public Stundenplanstatus getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(Stundenplanstatus scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public int getScheduleYear() {
		return scheduleYear;
	}

	public void setScheduleYear(int scheduleYear) {
		this.scheduleYear = scheduleYear;
	}

	public Integer getScheduleCalendarWeek() {
		return scheduleCalendarWeek;
	}

	public void setScheduleCalendarWeek(Integer scheduleCalendarWeek) {
		if(scheduleCalendarWeek!=null){
			this.scheduleCalendarWeek = scheduleCalendarWeek;
			scheduleCalendarWeekOk=true;
	    }
	}

	public String getScheduleSemesterSection() {
		return scheduleSemesterSection;
	}

	public void setScheduleSemesterSection(String scheduleSemesterSection) {
		if(scheduleSemesterSection!=null){
			this.scheduleSemesterSection = scheduleSemesterSection;
			scheduleSemesterSectionOk=true;
	    }
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		if(startDate!=null){
			this.startDate = startDate;
			startDateOk=true;
	    }
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		if(endDate!=null){
			this.endDate = endDate;
			endDateOk=true;
	    }
	}

	public List<Stundenplansemester> getScheduleSemesterList() {
		return scheduleSemesterList;
	}

	public void setScheduleSemesterList(List<Stundenplansemester> scheduleSemesterList) {
		this.scheduleSemesterList = scheduleSemesterList;
		
	}

	public Stundenplansemester getScheduleSemesterSelected() {
		return scheduleSemesterSelected;
	}

	public void setScheduleSemesterSelected(Stundenplansemester scheduleSemesterSelected) {
		this.scheduleSemesterSelected = scheduleSemesterSelected;
	}
	
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;  
	
	/**
	 * Erstellt einen Stundenplansemestereintrag
	 */
	public void createStundenplansemester() {
		String msg;
		Stundenplansemester sps = new Stundenplansemester();
		sps.setSPSemester(scheduleSemesterSection);
		sps.setSPJahr(scheduleYear);
		sps.setSPKw(scheduleCalendarWeek);
		sps.setStartDatum(startDate);
		sps.setEndDatum(endDate);
		sps.setStundenplanstatus(findSps(scheduleSemesterId));
		try {
			stundenplansemesterFacadeLocal.create(sps);
			msg = "entry";
           // addMessage("messages", msg);
            addInfoMessage(msg);
            
		}catch(Exception e) {
			msg = "notEntry";
            //addMessage("messages", msg);
            addInfoMessage(msg);
		}
		
		
	}
	
	/**
	 * Schaut ob StundenplanSemester, Stundenplankalenderwoche, Startdatum und EndDatum gesetzt worden ist, danach wird der Eintrag erstellt und zum Schluß wird die Liste aktualisiert.
	 * @throws SecurityException
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws Exception
	 */
	public void createDoStundenplansemester() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(scheduleSemesterSectionOk == true && scheduleCalendarWeekOk == true && startDateOk && endDateOk) {
			createStundenplansemester();
			scheduleSemesterList = getStundenplansemesterList();
		}	
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Liste des Stundenplansemesters laden
	 * @return
	 */
	public List<Stundenplansemester> getStundenplansemesterList(){
		return stundenplansemesterFacadeLocal.findAll();
	}
	
	/**
	 * Liste des Stundenplanstatus laden
	 * @return
	 */
	public List<Stundenplanstatus> getScheduleStatusList(){
		return scheduleStatusEJB.findAll();
	}
	
	/**
	 * Ausgewählte Zeile in scheduleSemeseterSelected speichern
	 * @param e
	 */
	public void onRowSelect(SelectEvent<Stundenplansemester> e) {
		String msg = "courseSemester";
        addInfoMessage(msg);
        
        scheduleSemesterSelected = e.getObject();
        
        scheduleSemesterId = scheduleSemesterSelected.getStundenplanstatus().getSpstid();
        
    }
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Stundenplansemester Eintrag löschen
     */
    public void deleteStundenplansemester() {
    	String msg;
    	scheduleSemesterList.remove(scheduleSemesterSelected);        
        EntityManager em = emf.createEntityManager();
        TypedQuery<Stundenplansemester> q = em.createNamedQuery("Stundenplansemester.findBySpsid",Stundenplansemester.class);
        q.setParameter("spsid", scheduleSemesterSelected.getSpsid());
        scheduleSemester = (Stundenplansemester)q.getSingleResult();
        try {
        	stundenplansemesterFacadeLocal.remove(scheduleSemester);
        	msg = "delete";
            //addMessage("messages", msg);
            addInfoMessage(msg);
        }catch(Exception e) {
        	msg = "notDelete";
            //addMessage("messages", msg);
            addInfoMessage(msg);
        }
        
		em.close();
    }
    
    /**
     * Finden des Stundenplanstatus anhand der Stundenplanstatusid
     * @param spsId
     * @return
     */
    private Stundenplanstatus findSps(int spsId) {
        try{
            EntityManager em = emf.createEntityManager(); 
            TypedQuery<Stundenplanstatus> query
                = em.createNamedQuery("Stundenplanstatus.findBySpsid",Stundenplanstatus.class);
            query.setParameter("spstid", spsId);
            scheduleStatus = (Stundenplanstatus)query.getSingleResult();
        }
        catch(Exception e){   
        }
        return scheduleStatus;
    }
    
    /**
     * Bearbeiten des Stundenplansemester
     */
    public void addStundenPlanSemester(){
    	String msg;
        EntityManager em = emf.createEntityManager();
        em.find(Stundenplansemester.class, scheduleSemesterSelected.getSpsid());
        scheduleSemester.setSpsid(scheduleSemesterSelected.getSpsid());
        scheduleSemester.setSPSemester(scheduleSemesterSelected.getSPSemester());
        scheduleSemester.setSPJahr(scheduleSemesterSelected.getSPJahr());
        scheduleSemester.setSPKw(scheduleSemesterSelected.getSPKw());
        scheduleSemester.setStartDatum(scheduleSemesterSelected.getStartDatum());
        scheduleSemester.setEndDatum(scheduleSemesterSelected.getEndDatum());
        scheduleSemester.setStundenplanstatus(findSps(scheduleSemesterId));
        try {
        	stundenplansemesterFacadeLocal.edit(scheduleSemester);
        	msg = "edit";
            //addMessage("messages", msg);
            addInfoMessage(msg);
        }catch(Exception e) {
        	msg = "notEdit";
            //addMessage("messages", msg);
            addInfoMessage(msg);
        }
        
      	scheduleSemesterList = getStundenplansemesterList();
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