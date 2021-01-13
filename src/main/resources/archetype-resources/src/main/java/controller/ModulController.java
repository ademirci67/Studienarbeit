package controller;

import model.Account;
import model.Faculty;
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

//@ManagedBean(name="ModulController")
@Named(value="modulController")
//@SessionScoped
@SessionScoped
public class ModulController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	//@SuppressWarnings("cdi-ambiguous-dependency")
	@Inject 
	private Modul modul;
	
	@PostConstruct
    public void init() {
        modList = getModulList();
    }
 
	
	
	private String modulKuerzel;
	private String modulName;
	private Integer pcId;
	private boolean modulKuerzelOk = false;
	private boolean modulNameOk = false;
	private boolean pcIdOk = false;
	
	List<Modul> modList;
	
	//modlist.add(getModulList());
	
	private Modul modulSelected;
	
	public Modul getModulSelected() {
		return modulSelected;
	}
	  
	public void setModulSelected(Modul modulSelected) {
		this.modulSelected = modulSelected;
	}
	
	
	  
    public List<Modul> getModList() {
        return modList;
    }
    
	public Modul getModul() {
		return modul;
	}
	  
	public void setModul(Modul modul) {
		this.modul = modul;
	}
	  
	public String getModulKuerzel() {
		return modulKuerzel;
	}
	  
	public void setModulKuerzel(String modulKuerzel) {
		if(modulKuerzel!=null){
			this.modulKuerzel = modulKuerzel;
			modulKuerzelOk = true;
		}
		else{
			FacesMessage message = new FacesMessage("Modulkürzel bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("ModulForm:modKuerzel_reg", message);
	        //String msg = "Modulkürzel bereits vorhanden.";
	        //addMessage("modKuerzel_reg",msg);
	    }
	}
	  
	public String getModulName() {
		return modulName;
	}
	  
	public void setModulName(String modulName) {
	    if(modulName!=null){
	        this.modulName = modulName;
	        modulNameOk=true;
	    }
	    else{
	    	FacesMessage message = new FacesMessage("Modulname bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("ModulForm:modName_reg", message);
	        //String msg = "Modulname bereits vorhanden.";
	        //addMessage("modName_reg",msg);
	    }
	}
	  
	public Integer getPcId() {
		return pcId;
	}
	  
	public void setPcId(Integer pcId) {
		if(pcId!=null){
	        this.pcId = pcId;
	        pcIdOk=true;
	    }
	    else{
	    	FacesMessage message = new FacesMessage("Prüfcodeid bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("ModulForm:pcid_reg", message);
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
	public void createModul() throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception  {
		EntityManager em = emf.createEntityManager();
		Modul mod = new Modul();  
		mod.setModName(modulName);    
		mod.setModKuerzel(modulKuerzel);      
		mod.setPcid(pcId);
		try {
	        ut.begin();   
	        em.joinTransaction();  
	        em.persist(mod);  
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
	
	public String createDoModul() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(modulNameOk == true && modulKuerzelOk == true && pcIdOk == true) {
			createModul();
			return "showmodul.xhtml";
		}
		else{
			return "createmodul.xhtml";
		}
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	public List<Modul> getModulList(){
		EntityManager em = emf.createEntityManager();
		TypedQuery<Modul> query = em.createNamedQuery("Modul.findAll", Modul.class);
		modList = query.getResultList();
		return query.getResultList();
	}
	
	
	
	public void onRowEdit(RowEditEvent<Modul> event) {
        //MessageForPrimefaces msg = new MessageForPrimefaces("Modul Edited", event.getObject().getModID());
        //FacesMessage msg = new FacesMessage("Modul Edited", event.getObject().getModID());
        FacesMessage msg = new FacesMessage("Modul Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        Modul newMod = new Modul();
        newMod = event.getObject();
        
        try {
	        ut.begin();
	        EntityManager em = emf.createEntityManager();
	        //em.joinTransaction();
	        em.find(Modul.class, 279);
	        //em.persist(q)
	        
	        modul.setModID(newMod.getModID());
	        modul.setModName(newMod.getModName());
	        modul.setModKuerzel(newMod.getModKuerzel());
	        modul.setPcid(newMod.getPcid());
	        
	        
	        em.merge(modul);
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
     
    public void onRowCancel(RowEditEvent<Modul> event) {
        //MessageForPrimefaces msg = new MessageForPrimefaces("Modul Cancelled", event.getObject().getModID());
        //FacesMessage msg = new FacesMessage("Modul Cancelled", event.getObject().getModID());
    	FacesMessage msg = new FacesMessage("Modul Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    public void deleteModul() throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception {
        modList.remove(modulSelected);
        //selectedmodul = null;
        //updateModul(modlist);
        
        EntityManager em = emf.createEntityManager();
        TypedQuery<Modul> q = em.createNamedQuery("Modul.findByModID",Modul.class);
        q.setParameter("modID", modulSelected.getModID());
        modul = (Modul)q.getSingleResult();
        
        try {
	        ut.begin();   
	        em.joinTransaction();  
	        //em.persist(q);
	        em.remove(modul);
	        ut.commit(); 
	    }
	    catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
	        try {
	            ut.rollback();
	        } 
	        catch (IllegalStateException | SecurityException | SystemException ex) {
	        }
	    }
        modulSelected = null;
		em.close();
    }
    
   // ---------------------------------------------------------------------------------------------------------------------
    
	
	  
		//Nachrichten an die View senden
	private void addMessage(String loginformidName, String msg) {
	   FacesMessage message = new FacesMessage(msg);
	   FacesContext.getCurrentInstance().addMessage(loginformidName, message);     
	}
  
  
  
  
}