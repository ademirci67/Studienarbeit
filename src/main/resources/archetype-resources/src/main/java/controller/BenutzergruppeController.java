package controller;

import model.Account;
import model.Faculty;
import model.Modul;
import model.Benutzergruppe;
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

//@ManagedBean(name="BenutzergruppeController")
@Named(value="benutzergruppeController")
//@SessionScoped
@SessionScoped
public class BenutzergruppeController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	//@SuppressWarnings("cdi-ambiguous-dependency")
	@Inject 
	private Benutzergruppe benutzergruppe;
	
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
	
	//modlist.add(getModulList());
	
	private Benutzergruppe userGroupSelected;
	
	public Benutzergruppe getUserGroupSelected() {
		return userGroupSelected;
	}
	  
	public void setUserGroupSelected(Benutzergruppe userGroupSelected) {
		this.userGroupSelected = userGroupSelected;
	}
	
	
	  
    public List<Benutzergruppe> getUserGroupList() {
        return userGroupList;
    }
    
	public Benutzergruppe getBenutzergruppe() {
		return benutzergruppe;
	}
	  
	public void setBenutzergruppe(Benutzergruppe benutzergruppe) {
		this.benutzergruppe = benutzergruppe;
	}
	  
	public String getUserGroupName() {
		return userGroupName;
	}
	  
	public void setUserGroupName(String userGroupName) {
		if(userGroupName!=null){
			this.userGroupName = userGroupName;
			userGroupNameOk = true;
		}
		else{
			FacesMessage message = new FacesMessage("Benutzergruppekürzel bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("BenutzergruppeForm:BGName_reg", message);
	        //String msg = "Modulkürzel bereits vorhanden.";
	        //addMessage("modKuerzel_reg",msg);
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
	    else{
	    	FacesMessage message = new FacesMessage("Benutzergruppe bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("BenutzergruppeForm:BGShortName_reg", message);
	        //String msg = "Modulname bereits vorhanden.";
	        //addMessage("modName_reg",msg);
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
	    else{
	    	FacesMessage message = new FacesMessage("BGRechte bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("BenutzergruppeForm:BGRechte_reg", message);
	        //String msg = "Prüfcodeid bereits vorhanden.";
	        //addMessage("pcid_reg",msg);
	    }
	}
	
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;  
	public void createBenutzergruppe() throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception  {
		EntityManager em = emf.createEntityManager();
		Benutzergruppe bg = new Benutzergruppe();  
		bg.setBGName(userGroupName);    
		bg.setBGShortName(userGroupShortName);      
		bg.setBGRechte(userGroupRights);
		try {
	        ut.begin();   
	        em.joinTransaction();  
	        em.persist(bg);  
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
	
	public void createDoBenutzergruppe() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(userGroupNameOk == true && userGroupShortNameOk == true && userGroupRightsOk == true) {
			createBenutzergruppe();
			userGroupList = getBenutzergruppeList();
		}
		
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	public List<Benutzergruppe> getBenutzergruppeList(){
		EntityManager em = emf.createEntityManager();
		TypedQuery<Benutzergruppe> query = em.createNamedQuery("Benutzergruppe.findAll", Benutzergruppe.class);
		userGroupList = query.getResultList();
		return query.getResultList();
	}
	
	
	
	
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    public void deleteBenutzergruppe() throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception {
        userGroupList.remove(userGroupSelected);
        //selectedmodul = null;
        //updateModul(modlist);
        
        EntityManager em = emf.createEntityManager();
        TypedQuery<Benutzergruppe> q = em.createNamedQuery("Benutzergruppe.findByID",Benutzergruppe.class);
        q.setParameter("groupID", userGroupSelected.getGroupID());
        benutzergruppe = (Benutzergruppe)q.getSingleResult();
        
        try {
	        ut.begin();   
	        em.joinTransaction();  
	        //em.persist(q);
	        em.remove(benutzergruppe);
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
    
    public void onRowSelect(SelectEvent<Benutzergruppe> e) {
    	FacesMessage msg = new FacesMessage("Benutzergruppe ausgewählt");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        userGroupSelected = e.getObject();
        
    }
    
    public void addBenutzergruppe(){
    	 try {
 	        ut.begin();
 	        EntityManager em = emf.createEntityManager();
 	        em.find(Benutzergruppe.class, userGroupSelected.getGroupID());
 	        benutzergruppe.setGroupID(userGroupSelected.getGroupID());
 	        benutzergruppe.setBGName(userGroupSelected.getBGName());
 	        benutzergruppe.setBGShortName(userGroupSelected.getBGShortName());
 	       benutzergruppe.setBGRechte(userGroupSelected.getBGRechte());
 	        em.merge(benutzergruppe);
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