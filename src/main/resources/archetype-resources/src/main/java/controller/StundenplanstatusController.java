package controller;


import model.Stundenplanstatus;
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

//@ManagedBean(name="StundenplanstatusController")
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
	
	@PostConstruct
    public void init() {
		statusList = getStundenplanstatusList();
    }
 
	
	private String color;
	private String statusBezeichnung;
	private String statusHint;
	private boolean colorOk = false;
	private boolean statusBezeichnungOk = false;
	private boolean statusHintOk = false;
	
	List<Stundenplanstatus> statusList;
	
	private Stundenplanstatus statusSelected;

	public Stundenplanstatus getStundenplanstatus() {
		return stundenplanstatus;
	}

	public void setStundenplanstatus(Stundenplanstatus stundenplanstatus) {
		this.stundenplanstatus = stundenplanstatus;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if(color!=null){
			this.color = color;
			colorOk = true;
		}
		/*else{
			FacesMessage message = new FacesMessage("Farbe bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("StundenplanstatusForm:PColor_reg", message);
	    }*/
	}

	public String getStatusBezeichnung() {
		return statusBezeichnung;
	}

	public void setStatusBezeichnung(String statusBezeichnung) {
		if(statusBezeichnung!=null){
			this.statusBezeichnung = statusBezeichnung;
			statusBezeichnungOk = true;
		}
		/*else{
			FacesMessage message = new FacesMessage("Bezeichnung bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("StundenplanstatusForm:SPSTBezeichnung_reg", message);
	    }*/
	}

	public String getStatusHint() {
		return statusHint;
	}

	public void setStatusHint(String statusHint) {
		if(statusHint!=null){
			this.statusHint = statusHint;
			statusHintOk = true;
		}
		/*else{
			FacesMessage message = new FacesMessage("Hinweis bereits vorhanden.");
            FacesContext.getCurrentInstance().addMessage("StundenplanstatusForm:SPSTHint_reg", message);
	    }*/
	}

	public Stundenplanstatus getStatusSelected() {
		return statusSelected;
	}

	public void setStatusSelected(Stundenplanstatus statusSelected) {
		this.statusSelected = statusSelected;
	}

	public List<Stundenplanstatus> getStatusList() {
		return statusList;
	}
	
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;  
	public void createStundenplanstatus() throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception  {
		EntityManager em = emf.createEntityManager();
		Stundenplanstatus sps = new Stundenplanstatus();  
		sps.setSPSTBezeichnung(statusBezeichnung);
		sps.setSPSTHint(statusHint);
		sps.setPColor(color);
		try {
	        ut.begin();   
	        em.joinTransaction();  
	        em.persist(sps);  
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
	
	public String createDoStundenplanstatus() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(statusBezeichnungOk == true && statusHintOk == true && colorOk == true) {
			createStundenplanstatus();
			return "showstundenplanstatus.xhtml";
		}
		else{
			return "createstundenplanstatus.xhtml";
		}
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	public List<Stundenplanstatus> getStundenplanstatusList(){
		EntityManager em = emf.createEntityManager();
		TypedQuery<Stundenplanstatus> query = em.createNamedQuery("Stundenplanstatus.findAll", Stundenplanstatus.class);
		statusList = query.getResultList();
		return query.getResultList();
	}
	
	
	
	public void onRowEdit(RowEditEvent<Stundenplanstatus> event) {
        FacesMessage msg = new FacesMessage("Stundenplanstatus Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        Stundenplanstatus newStatus = new Stundenplanstatus();
        newStatus = event.getObject();
        
        try {
	        ut.begin();
	        EntityManager em = emf.createEntityManager();
	        em.find(Stundenplanstatus.class, newStatus.getSpstid());
	        stundenplanstatus.setSpstid(newStatus.getSpstid());
	        stundenplanstatus.setPColor(newStatus.getPColor());
	        stundenplanstatus.setSPSTBezeichnung(newStatus.getSPSTBezeichnung());
	        stundenplanstatus.setSPSTHint(newStatus.getSPSTHint());
	        em.merge(stundenplanstatus);
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
     
    public void onRowCancel(RowEditEvent<Stundenplanstatus> event) {
    	FacesMessage msg = new FacesMessage("Stundenplanstatus Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    public void deleteStundenplanstatus() throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception {
        statusList.remove(statusSelected);        
        EntityManager em = emf.createEntityManager();
        TypedQuery<Stundenplanstatus> q = em.createNamedQuery("Stundenplanstatus.findBySpsid",Stundenplanstatus.class);
        q.setParameter("spstid", statusSelected.getSpstid());
        stundenplanstatus = (Stundenplanstatus)q.getSingleResult();
        
        try {
	        ut.begin();   
	        em.joinTransaction();  
	        em.remove(stundenplanstatus);
	        ut.commit(); 
	    }
	    catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
	        try {
	            ut.rollback();
	        } 
	        catch (IllegalStateException | SecurityException | SystemException ex) {
	        }
	    }
        statusSelected = null;
		em.close();
    }

}
