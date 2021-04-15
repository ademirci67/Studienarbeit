package controller;

import model.Benutzergruppe;
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
import EJB.BenutzergruppeFacadeLocal;


/**
*
* @author Anil
*/

@Named(value="benutzergruppeController")
@SessionScoped
public class BenutzergruppeController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	@Inject 
	private Benutzergruppe userGroup;
	
	@EJB
	private BenutzergruppeFacadeLocal userGroupFacadeLocal;
	
	/**
	 * Initialisierung
	 */
	@PostConstruct
    public void init() {
        userGroupList = getBenutzergruppeList();
    }
 
	
	
	private String userGroupName;
	private String userGroupShortName;
	private Integer userGroupRights;
	private boolean userGroupNameOk = false;
	private boolean userGroupShortNameOk = false;
	private boolean userGroupRightsOk = false;
	
	List<Benutzergruppe> userGroupList;
	
	private Benutzergruppe userGroupSelected;
	
	// Getter und Setter
	public Benutzergruppe getUserGroupSelected() {
		return userGroupSelected;
	}
	  
	public void setUserGroupSelected(Benutzergruppe userGroupSelected) {
		this.userGroupSelected = userGroupSelected;
	}
	  
    public List<Benutzergruppe> getUserGroupList() {
        return userGroupList;
    }
    
	public Benutzergruppe getUserGroup() {
		return userGroup;
	}
	  
	public void setUserGroup(Benutzergruppe userGroup) {
		this.userGroup = userGroup;
	}
	  
	public String getUserGroupName() {
		return userGroupName;
	}
	  
	public void setUserGroupName(String userGroupName) {
		if(userGroupName!=null){
			this.userGroupName = userGroupName;
			userGroupNameOk = true;
		}
	}
	  
	public String getUserGroupShortName() {
		return userGroupShortName;
	}
	  
	public void setUserGroupShortName(String userGroupShortName) {
	    if(userGroupShortName!=null){
	        this.userGroupShortName = userGroupShortName;
	        userGroupShortNameOk=true;
	    }
	}
	  
	public Integer getUserGroupRights() {
		return userGroupRights;
	}
	  
	public void setUserGroupRights(Integer userGroupRights) {
		if(userGroupRights!=null){
	        this.userGroupRights = userGroupRights;
	        userGroupRightsOk=true;
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
	 * Erstellen eines Benutzergruppeneintrags
	 * @throws Exception
	 */
	public void createBenutzergruppe() throws Exception  {
		String msg;
		EntityManager em = emf.createEntityManager();
		Benutzergruppe bg = new Benutzergruppe();  
		bg.setBGName(userGroupName);    
		bg.setBGShortName(userGroupShortName);      
		bg.setBGRechte(userGroupRights);
		try {
			userGroupFacadeLocal.create(bg);
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
	 * Schaut ob Benutzergruppenname, Benutzergruppenkurzname und Benutzergruppenrechte gesetzt worden ist, danach wird der Benutzergruppeneintrag erstellt und zum Schluß wird die Liste aktualisiert.
	 * @throws SecurityException
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws Exception
	 */
	public void createDoBenutzergruppe() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(userGroupNameOk == true && userGroupShortNameOk == true && userGroupRightsOk == true) {
			createBenutzergruppe();
			userGroupList = getBenutzergruppeList();
		}
		
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Laden der Benutzergruppenliste
	 * @return
	 */
	public List<Benutzergruppe> getBenutzergruppeList(){
		return userGroupFacadeLocal.findAll();
	}
	
	
	
	
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Löschen eines Benutzergruppeneintrags
     * @throws Exception
     */
    public void deleteBenutzergruppe() throws Exception {
    	String msg;
        userGroupList.remove(userGroupSelected);        
        //EntityManager em = emf.createEntityManager();
        //TypedQuery<Benutzergruppe> q = em.createNamedQuery("Benutzergruppe.findByID",Benutzergruppe.class);
        //q.setParameter("groupID", userGroupSelected.getGroupID());
        //userGroup = (Benutzergruppe)q.getSingleResult();
        userGroup = userGroupFacadeLocal.find(userGroupSelected.getGroupID());
        
        try {
        	userGroupFacadeLocal.remove(userGroup);
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
    
    /**
     * Ausgewählte Zeile wird in userGroupSelected gespeichert
     * @param e
     */
    public void onRowSelect(SelectEvent<Benutzergruppe> e) {
    	String msg = "userGroup";
        addInfoMessage(msg);
        
        userGroupSelected = e.getObject();
        
    }
    
    /**
     * Bearbeiten eines Benutzergruppeneintrags
     */
    public void addBenutzergruppe(){
    	EntityManager em = emf.createEntityManager();
    	String msg;
    	try {
 	       em.find(Benutzergruppe.class, userGroupSelected.getGroupID());
 	       userGroup.setGroupID(userGroupSelected.getGroupID());
 	       userGroup.setBGName(userGroupSelected.getBGName());
 	       userGroup.setBGShortName(userGroupSelected.getBGShortName());
 	       userGroup.setBGRechte(userGroupSelected.getBGRechte());
 	       userGroupFacadeLocal.edit(userGroup);
 	      msg = "edit";
          //addMessage("messages", msg);
          addInfoMessage(msg);
	    }
	    catch (Exception e) {
	    	msg = "notEdit";
           //addMessage("messages", msg);
           addInfoMessage(msg);
	    }
    	userGroupList = getBenutzergruppeList();
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