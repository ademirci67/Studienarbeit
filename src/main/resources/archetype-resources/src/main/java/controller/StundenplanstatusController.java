package controller;


import model.Stundenplanstatus;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

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
import EJB.StundenplanstatusFacadeLocal;

/**
*
* @author Anil
*/

@Named(value="stundenplanstatusController")
@SessionScoped
public class StundenplanstatusController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	@Inject 
	private Stundenplanstatus stundenplanstatus;
	
	@EJB
	private StundenplanstatusFacadeLocal stundenplanstatusFacadeLocal;
	
	/**
	 * Initialisierung
	 */
	@PostConstruct
    public void init() {
		scheduleStatusList = getStundenplanstatusList();
    }
 
	
	private String statusColor;
	private String statusDescription;
	private String statusHint;
	private boolean statusColorOk = false;
	private boolean statusDescriptionOk = false;
	private boolean statusHintOk = false;
	
	List<Stundenplanstatus> scheduleStatusList;
	
	private Stundenplanstatus statusSelected;

	// Getter und Setter
	public Stundenplanstatus getStundenplanstatus() {
		return stundenplanstatus;
	}

	public void setStundenplanstatus(Stundenplanstatus stundenplanstatus) {
		this.stundenplanstatus = stundenplanstatus;
	}

	public String getStatusColor() {
		return statusColor;
	}

	public void setStatusColor(String statusColor) {
		if(statusColor!=null){
			this.statusColor = statusColor;
			statusColorOk = true;
		}
	}
	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		if(statusDescription!=null){
			this.statusDescription = statusDescription;
			statusDescriptionOk = true;
		}
	}

	public String getStatusHint() {
		return statusHint;
	}

	public void setStatusHint(String statusHint) {
		if(statusHint!=null){
			this.statusHint = statusHint;
			statusHintOk = true;
		}
	}

	public Stundenplanstatus getStatusSelected() {
		return statusSelected;
	}

	public void setStatusSelected(Stundenplanstatus statusSelected) {
		this.statusSelected = statusSelected;
	}

	public List<Stundenplanstatus> getScheduleStatusList() {
		return scheduleStatusList;
	}
	
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;
	
	/**
	 * Erstellen eines Stundenplansemesterstatus Eintrag
	 * @throws Exception
	 */
	public void createStundenplanstatus() throws Exception  {
		String msg;
		EntityManager em = emf.createEntityManager();
		Stundenplanstatus sps = new Stundenplanstatus();  
		sps.setSPSTBezeichnung(statusDescription);
		sps.setSPSTHint(statusHint);
		sps.setPColor(statusColor);
		try {
			stundenplanstatusFacadeLocal.create(sps);
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
	 * Schaut ob Studenplanstatusbezeichnung, StundenplanstatusHinweis und Stundenplanstausfarbe gesetzt ist, danach wird der Eintrag erstellt und zum Schluß die Liste aktualisiert.
	 * @throws SecurityException
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws Exception
	 */
	public void createDoStundenplanstatus() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(statusDescriptionOk == true && statusHintOk == true && statusColorOk == true) {
			createStundenplanstatus();
			scheduleStatusList = getStundenplanstatusList();
		}
		
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Laden der Stundenplanstatusliste
	 * @return
	 */
	public List<Stundenplanstatus> getStundenplanstatusList(){
		return stundenplanstatusFacadeLocal.findAll();
	}
	
	 /**
	  * Ausgewählte Zeile in statusSelected speichern
	 * @param e
	 */
	public void onRowSelect(SelectEvent<Stundenplanstatus> e) {
		String msg = "courseStatus";
        addInfoMessage(msg);
	        
	    statusSelected = e.getObject();
	        
	    }
	    
	    /**
	     * Bearbeiten eines Stundenplanstatuseintrag
	     */
	    public void addStundenplanstatus(){
	    	String msg;
 	        EntityManager em = emf.createEntityManager();
 	        em.find(Stundenplanstatus.class, statusSelected.getSpstid());
 	        stundenplanstatus.setSpstid(statusSelected.getSpstid());
 	        stundenplanstatus.setPColor(statusSelected.getPColor());
 	        stundenplanstatus.setSPSTBezeichnung(statusSelected.getSPSTBezeichnung());
 	        stundenplanstatus.setSPSTHint(statusSelected.getSPSTHint());
 	       try {
	        	stundenplanstatusFacadeLocal.edit(stundenplanstatus);
	        	msg = "edit";
	            //addMessage("messages", msg);
	            addInfoMessage(msg);
	        }catch(Exception e) {
	        	msg = "notEdit";
	            //addMessage("messages", msg);
	            addInfoMessage(msg);
	        	
	        }
 	       scheduleStatusList = getStundenplanstatusList();
 	        em.close();
	 	    
	    }
	
	//----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Löschen eines Stundenplanstatuseintrag
     * @throws Exception
     */
    public void deleteStundenplanstatus() throws Exception {
    	String msg;
        scheduleStatusList.remove(statusSelected);        
        EntityManager em = emf.createEntityManager();
        TypedQuery<Stundenplanstatus> q = em.createNamedQuery("Stundenplanstatus.findBySpsid",Stundenplanstatus.class);
        q.setParameter("spstid", statusSelected.getSpstid());
        stundenplanstatus = (Stundenplanstatus)q.getSingleResult();
        try {
        	stundenplanstatusFacadeLocal.remove(stundenplanstatus);
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
