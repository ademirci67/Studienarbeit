package controller;

import model.Location;
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
import EJB.LocationFacadeLocal;

/**
*
* @author Anil
*/

@Named(value="locationController")
@SessionScoped
public class LocationController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	@Inject 
	private Location location;
	
	@EJB
	private LocationFacadeLocal locationFacadeLocal;
	
	/**
	 * Initialisierung
	 */
	@PostConstruct
    public void init() {
        locationList = getLocationListAll();
    }
	
	private String locationCity;
	private String locationStreet;
	private boolean locationCityOk = false;
	private boolean locationStreetOk = false;
	
	private Location locationSelected;
	
	List<Location> locationList;
	
	// Getter und Setter
    public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public List<Location> getLocationList() {
		return locationList;
	}

	public Location getLocationSelected() {
		return locationSelected;
	}

	public void setLocationSelected(Location locationSelected) {
		this.locationSelected = locationSelected;
	}

	public String getLocationCity() {
		return this.locationCity;
	}

	public void setLocationCity(String locationCity) {
		if(locationCity != null){
			this.locationCity = locationCity;
			locationCityOk = true;
		}
	}
	public String getLocationStreet() {
		return this.locationStreet;
	}

	public void setLocationStreet(String locationStreet) {
		if(locationStreet != null){
			this.locationStreet = locationStreet;
			locationStreetOk = true;
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
	 * Fügt einen neuen Standort hinzu.
	 * @throws Exception
	 */
	public void createLocation() throws Exception  {
		String msg;
		EntityManager em = emf.createEntityManager();
		Location loc = new Location();  
		loc.setLCity(locationCity);    
		loc.setLStreet(locationStreet);      
		try {
			locationFacadeLocal.create(loc);
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
	 * Schaut, ob die Variabeln Stadt und Straße gesetzt worden sind, fügt dann den Eintrag in die Datenbank hinzu und zum Schluß wird die die Liste aktualisiert.
	 * @throws SecurityException
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws Exception
	 */
	public void createDoLocation() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(locationCityOk == true && locationStreetOk == true) {
			createLocation();
			locationList = getLocationListAll();
			
		}
	}
	
	/**
	 * Standortliste wird geladen
	 * @return
	 */
	public List<Location> getLocationListAll(){
		return locationFacadeLocal.findAll();
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Löscht einen Standorteintrag
     * @throws Exception
     */
    public void deleteLocation() throws Exception {
    	String msg;
    	locationList.remove(locationSelected);        
        EntityManager em = emf.createEntityManager();
        TypedQuery<Location> q = em.createNamedQuery("Location.findByLid",Location.class);
        q.setParameter("lid", locationSelected.getLid());
        location = (Location)q.getSingleResult();
        
        try {
        	locationFacadeLocal.remove(location);
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
    
    //----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Die ausgewählte Zeile wird in locationSelected gespeichert.
     * @param e
     */
    public void onRowSelect(SelectEvent<Location> e) {
    	String msg = "location";
        addInfoMessage(msg);
        
        locationSelected = e.getObject();
        
    }
    
    // 
    /**
     * Bearbeitet ein Standorteintrag
     */
    public void addLocation(){
    	String msg;
    	EntityManager em = emf.createEntityManager();
    	 try {
 	        em.find(Location.class, locationSelected.getLid());
 	        location.setLid(locationSelected.getLid());
 	        location.setLCity(locationSelected.getLCity());
 	        location.setLStreet(locationSelected.getLStreet());
 	        locationFacadeLocal.edit(location);
 	       msg = "edit";
           //addMessage("messages", msg);
           addInfoMessage(msg);
	    }
	    catch (Exception e) {
           msg = "notEdit";
           //addMessage("messages", msg);
           addInfoMessage(msg);
	    }
    	locationList = getLocationListAll();
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
