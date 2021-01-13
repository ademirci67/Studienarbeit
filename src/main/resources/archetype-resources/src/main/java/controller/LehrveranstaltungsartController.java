package controller;


import model.Lehrveranstaltungsart;
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
import javax.faces.bean.ManagedBean;
import controller.MessageForPrimefaces;

/**
*
* @author Anil
*/

//@ManagedBean(name="LehrveranstaltungsartController")
@Named(value="lehrveranstaltungsartController")
@SessionScoped
public class LehrveranstaltungsartController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	@Inject 
	private Lehrveranstaltungsart lehrveranstaltungsart;
	
	@PostConstruct
    public void init() {
        lvaList = getLehrveranstaltungsartList();
    }
 
	private String lvaDauer;
	private String lvaKurz;
	private String lvaName;
	private boolean lvaDauerOk = false;
	private boolean lvaKurzOk = false;
	private boolean lvaNameOk = false;
	
	List<Lehrveranstaltungsart> lvaList;
	
	private Lehrveranstaltungsart lvaSelected;

	public Lehrveranstaltungsart getLehrveranstaltungsart() {
		return lehrveranstaltungsart;
	}

	public void setLehrveranstaltungsart(Lehrveranstaltungsart lehrveranstaltungsart) {
		this.lehrveranstaltungsart = lehrveranstaltungsart;
	}

	public String getLvaDauer() {
		return lvaDauer;
	}

	public void setLvaDauer(String lvaDauer) {
		if(lvaDauer!=null){
			this.lvaDauer = lvaDauer;
			lvaDauerOk=true;
	    }
	    else{
	    	FacesMessage message = new FacesMessage("Lehrveranstaltungsdauer konnte nicht gesetzt werden.");
            FacesContext.getCurrentInstance().addMessage("LehrveranstaltungsartForm:lvdauer_reg", message);
	    }
	}

	public String getLvaKurz() {
		return lvaKurz;
	}

	public void setLvaKurz(String lvaKurz) {
		if(lvaKurz!=null){
			this.lvaKurz = lvaKurz;
			lvaKurzOk=true;
	    }
	    else{
	    	FacesMessage message = new FacesMessage("Lehrveranstaltungskurzform konnte nicht gesetzt werden.");
            FacesContext.getCurrentInstance().addMessage("LehrveranstaltungsartForm:lvkurz_reg", message);
	    }
	}

	public String getLvaName() {
		return lvaName;
	}

	public void setLvaName(String lvaName) {
		if(lvaName!=null){
			this.lvaName = lvaName;
			lvaNameOk=true;
	    }
	    else{
	    	FacesMessage message = new FacesMessage("Lehrveranstaltungsname konnte nicht gesetzt werden.");
            FacesContext.getCurrentInstance().addMessage("LehrveranstaltungsartForm:lvname_reg", message);
	    }
	}

	public List<Lehrveranstaltungsart> getLvaList() {
		return lvaList;
	}

	public void setLvaList(List<Lehrveranstaltungsart> lvaList) {
		this.lvaList = lvaList;
	}

	public Lehrveranstaltungsart getLvaSelected() {
		return lvaSelected;
	}

	public void setLvaSelected(Lehrveranstaltungsart lvaSelected) {
		this.lvaSelected = lvaSelected;
	}
	
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;  
	public void createLehrveranstaltungsart() throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception  {
		EntityManager em = emf.createEntityManager();
		Lehrveranstaltungsart lva = new Lehrveranstaltungsart();  
		lva.setLvname(lvaName);
		lva.setLvdauer(lvaDauer);     
		lva.setLvkurz(lvaKurz);
		try {
	        ut.begin();   
	        em.joinTransaction();  
	        em.persist(lva);  
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
	
	public String createDoLehrveranstaltungsart() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(lvaNameOk == true && lvaDauerOk == true && lvaKurzOk == true) {
			createLehrveranstaltungsart();
			return "showlehrveranstaltungsart.xhtml";
		}
		else{
			return "createlehrveranstaltungsart.xhtml";
		}
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	public List<Lehrveranstaltungsart> getLehrveranstaltungsartList(){
		EntityManager em = emf.createEntityManager();
		TypedQuery<Lehrveranstaltungsart> query = em.createNamedQuery("Lehrveranstaltungsart.findAll", Lehrveranstaltungsart.class);
		lvaList = query.getResultList();
		return query.getResultList();
	}
	
	
	
	public void onRowEdit(RowEditEvent<Lehrveranstaltungsart> event) {
        FacesMessage msg = new FacesMessage("Lehrveranstaltungsart Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        Lehrveranstaltungsart newLva = new Lehrveranstaltungsart();
        newLva = event.getObject();
        
        try {
	        ut.begin();
	        EntityManager em = emf.createEntityManager();
	        em.find(Lehrveranstaltungsart.class, newLva.getLvid());	        
	        lehrveranstaltungsart.setLvid(newLva.getLvid());
	        lehrveranstaltungsart.setLvname(newLva.getLvname());
	        lehrveranstaltungsart.setLvdauer(newLva.getLvdauer());
	        lehrveranstaltungsart.setLvkurz(newLva.getLvkurz());
	        em.merge(lehrveranstaltungsart);
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
     
    public void onRowCancel(RowEditEvent<Lehrveranstaltungsart> event) {
    	FacesMessage msg = new FacesMessage("Lehrveranstaltungsart Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    public void deleteLehrveranstaltungsart() throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception {
        lvaList.remove(lvaSelected);        
        EntityManager em = emf.createEntityManager();
        TypedQuery<Lehrveranstaltungsart> q = em.createNamedQuery("Lehrveranstaltungsart.findByLvid",Lehrveranstaltungsart.class);
        q.setParameter("lvid", lvaSelected.getLvid());
        lehrveranstaltungsart = (Lehrveranstaltungsart)q.getSingleResult();
        
        try {
	        ut.begin();   
	        em.joinTransaction();  
	        em.remove(lehrveranstaltungsart);
	        ut.commit(); 
	    }
	    catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
	        try {
	            ut.rollback();
	        } 
	        catch (IllegalStateException | SecurityException | SystemException ex) {
	        }
	    }
        lvaSelected = null;
		em.close();
    }
	
	
}
