package controller;



import model.Lehrveranstaltungsart;
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


import EJB.LehrveranstaltungsartFacadeLocal;

/**
*
* @author Anil
*/

@Named(value="lehrveranstaltungsartController")
@SessionScoped
public class LehrveranstaltungsartController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	@Inject 
	private Lehrveranstaltungsart teachingEvent;
	
	@EJB
	private LehrveranstaltungsartFacadeLocal teachingEventFacadeLocal;
	
	/**
	 * Initialisierung
	 */
	@PostConstruct
    public void init() {
        teachingEventList = getLehrveranstaltungsartList();
    }
 
	private String teachingEventLength;
	private String teachingEventShort;
	private String teachingEventName;
	private boolean teachingEventLengthOk = false;
	private boolean teachingEventShortOk = false;
	private boolean teachingEventNameOk = false;
	
	List<Lehrveranstaltungsart> teachingEventList;
	
	private Lehrveranstaltungsart teachingEventSelected;

	// Getter und Setter
	public Lehrveranstaltungsart getTeachingEvent() {
		return teachingEvent;
	}

	public void setTeachingEvent(Lehrveranstaltungsart teachingEvent) {
		this.teachingEvent = teachingEvent;
	}

	public String getTeachingEventLength() {
		return teachingEventLength;
	}

	public void setTeachingEventLength(String teachingEventLength) {
		if(teachingEventLength!=null){
			this.teachingEventLength = teachingEventLength;
			teachingEventLengthOk=true;
	    }
	}

	public String getTeachingEventShort() {
		return teachingEventShort;
	}

	public void setTeachingEventShort(String teachingEventShort) {
		if(teachingEventShort!=null){
			this.teachingEventShort = teachingEventShort;
			teachingEventShortOk=true;
	    }
	}

	public String getTeachingEventName() {
		return teachingEventName;
	}

	public void setTeachingEventName(String teachingEventName) {
		if(teachingEventName!=null){
			this.teachingEventName = teachingEventName;
			teachingEventNameOk=true;
	    }
	}

	public List<Lehrveranstaltungsart> getTeachingEventList() {
		return teachingEventList;
	}

	public void setTeachingEventList(List<Lehrveranstaltungsart> teachingEventList) {
		this.teachingEventList = teachingEventList;
	}

	public Lehrveranstaltungsart getTeachingEventSelected() {
		return teachingEventSelected;
	}

	public void setTeachingEventSelected(Lehrveranstaltungsart teachingEventSelected) {
		this.teachingEventSelected = teachingEventSelected;
	}
	
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;
	
	/**
	 * Erstellen eines Lehrveranstaltungseintrags
	 * @throws Exception
	 */
	public void createLehrveranstaltungsart() throws Exception  {
		String msg;
		EntityManager em = emf.createEntityManager();
		Lehrveranstaltungsart lva = new Lehrveranstaltungsart();  
		lva.setLvname(teachingEventName);
		lva.setLvdauer(teachingEventLength);     
		lva.setLvkurz(teachingEventShort);
		try {
			teachingEventFacadeLocal.create(lva);
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
	 * Erstellen des Lehrveranstaltungseintrags und danach wird die Liste aktualisiert.
	 * @throws SecurityException
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws Exception
	 */
	public void createDoLehrveranstaltungsart() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		//if(teachingEventNameOk == true && teachingEventLengthOk == true && teachingEventShortOk == true) {
			createLehrveranstaltungsart();
			teachingEventList = getLehrveranstaltungsartList();
		//}
		
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	// 
	/**
	 * Lehrveranstaltungsliste wird geladen
	 * @return
	 */
	public List<Lehrveranstaltungsart> getLehrveranstaltungsartList(){
		return teachingEventFacadeLocal.findAll();
	}
	
	/**
	 * Ausgewählte Zeile wird in teachingEventSelected gespeichert
	 * @param e
	 */
	public void onRowSelect(SelectEvent<Lehrveranstaltungsart> e) {
		String msg = "teachingEvent";
        addInfoMessage(msg);
        
        teachingEventSelected = e.getObject();
        
    }
    
	// 
    /**
     * Bearbeiten eines Lehrveranstaltungseintrags
     */
    public void addLehrveranstaltungsart(){
    	String msg;
    	EntityManager em = emf.createEntityManager();
    	 try {
 	       em.find(Lehrveranstaltungsart.class, teachingEventSelected.getLvid());
 	       teachingEvent.setLvid(teachingEventSelected.getLvid());
 	       teachingEvent.setLvname(teachingEventSelected.getLvname());
 	       teachingEvent.setLvdauer(teachingEventSelected.getLvdauer());
 	       teachingEvent.setLvkurz(teachingEventSelected.getLvkurz());
 	       teachingEventFacadeLocal.edit(teachingEvent);
 	      msg = "edit";
          //addMessage("messages", msg);
          addInfoMessage(msg);
	    }
	    catch (Exception e) {
	    	msg = "notEdit";
           //addMessage("messages", msg);
           addInfoMessage(msg);
	    }
    	teachingEventList = getLehrveranstaltungsartList();
    	em.close();
    }
	
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Löschen eines Lehrveranstaltungseintrags
     * @throws Exception
     */
    public void deleteLehrveranstaltungsart() throws Exception {
    	String msg;
        teachingEventList.remove(teachingEventSelected);        
        EntityManager em = emf.createEntityManager();
        TypedQuery<Lehrveranstaltungsart> q = em.createNamedQuery("Lehrveranstaltungsart.findByLvid",Lehrveranstaltungsart.class);
        q.setParameter("lvid", teachingEventSelected.getLvid());
        teachingEvent = (Lehrveranstaltungsart)q.getSingleResult();
        
        try {
        	teachingEventFacadeLocal.remove(teachingEvent);
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
