package controller;

import model.Location;
import model.Raum;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Collections;
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
import EJB.RaumFacadeLocal;

/**
*
* @author Anil
*/

@Named(value="raumController")
@SessionScoped
public class RaumController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	@Inject 
	private Raum room;
	private Location location;
	
	@EJB
	private RaumFacadeLocal raumFacadeLocal;
	@EJB
	private LocationFacadeLocal locationEJB;
	
	/**
	 * Initialisierung
	 */
	@PostConstruct
    public void init() {
        roomList = getRaumList();
        locationList = getLocationList();
    }
 
    List<Location> locationList;
    private List<Location> locationSort = null;
    private int locationId;

	private Integer capacity;
	private String roomNeighbor;
	private String roomName;
	private boolean capacityOk = false;
	private boolean roomNameOk = false;
	
	List<Raum> roomList;
	
	private Raum roomSelected;
	
	// Getter und Setter
	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public Raum getRoom() {
		return room;
	}

	public void setRoom(Raum room) {
		this.room = room;
	}

	public Location getLocation() {
		return location;
	}

	public List<Location> getLocationSort() {
		return locationSort;
	}

	public void setLocationSort(List<Location> locationSort) {
		this.locationSort = locationSort;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		if(capacity!=null){
			this.capacity = capacity;
			capacityOk=true;
	    }
	}

	public String getRoomNeighbor() {
		return roomNeighbor;
	}

	public void setRoomNeighbor(String roomNeighbor) {
			this.roomNeighbor = roomNeighbor;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		if(roomName!=null){
			this.roomName = roomName;
	        roomNameOk=true;
	    }
	}

	public List<Raum> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<Raum> roomList) {
		this.roomList = roomList;
	}

	public Raum getRoomSelected() {
		return roomSelected;
	}

	public void setRoomSelected(Raum roomSelected) {
		this.roomSelected = roomSelected;
	}
	
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;
	
	/**
	 * Erstellen eines Raumes
	 * @throws Exception
	 */
	public void createRoom() throws Exception  {
		String msg;
		EntityManager em = emf.createEntityManager();
		Raum rau = new Raum();  
		rau.setRName(roomName);
		rau.setKapazitaet(capacity);
		rau.setNachbarRaum(roomNeighbor);
		rau.setLocation(findLoc(locationId));
		try {
	        raumFacadeLocal.create(rau);
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
	 * Schaut ob Raumname und Kapazität gesetzt worden ist, danach wird der Raum erstellt und zum Schluß wird die Liste aktualisiert.
	 * @throws SecurityException
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws Exception
	 */
	public void createDoRoom() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(roomNameOk == true && capacityOk == true) {
			createRoom();
			roomList = getRaumList();
		}
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Laden der Raumliste
	 * @return
	 */
	public List<Raum> getRaumList(){
		return raumFacadeLocal.findAll();
	}
	
	/**
	 * Laden der sortierten Standortsliste
	 * @return
	 */
	public List<Location> getLocationList(){
		if (locationSort==null) {
			locationSort = locationEJB.findAll();
			if (locationSort != null) {
				Collections.sort(locationSort, (a,b) -> {
					return a.getLCity().compareToIgnoreCase(b.getLCity());
				});
				}
			}
		
		return  locationSort;
	}
	
	/**
	 * Ausgewählte Zeile wird in roomSelected gespeichert, sowie der Fremdschlüssel
	 * @param e
	 */
	public void onRowSelect(SelectEvent<Raum> e) {
		String msg = "room";
        addInfoMessage(msg);
        
        roomSelected = e.getObject();
        
        locationId = roomSelected.getLocation().getLid();
        
    }
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Löscht einen Raum
     * @throws Exception
     */
    public void deleteRoom() throws Exception {
    	String msg;
        roomList.remove(roomSelected);        
        //EntityManager em = emf.createEntityManager();
        //TypedQuery<Raum> q = em.createNamedQuery("Raum.findByRid",Raum.class);
        //q.setParameter("rid", roomSelected.getRid());
        //room = (Raum)q.getSingleResult();
        room = raumFacadeLocal.find(roomSelected.getRid());
        try {
        	this.raumFacadeLocal.remove(room);
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
     * Finden eines Standortes anhand der ID
     * @param locationId
     * @return
     */
    private Location findLoc(int locationId) {
    	/*
        try{
            EntityManager em = emf.createEntityManager(); 
            TypedQuery<Location> query
                = em.createNamedQuery("Location.findByLid",Location.class);
            query.setParameter("lid", locationId);
            location = (Location)query.getSingleResult();
        }
        catch(Exception e){   
        }
        */
        return location = locationEJB.find(locationId);
    }
    
    /**
     * Bearbeiten eines Raumes
     */
    public void addRoom(){
    	EntityManager em = emf.createEntityManager();
    	String msg;
	   	 try {
	        em.find(Raum.class, roomSelected.getRid());
	        room.setRid(roomSelected.getRid());
	        room.setRName(roomSelected.getRName());
	        room.setKapazitaet(roomSelected.getKapazitaet());
	        room.setNachbarRaum(roomSelected.getNachbarRaum());
	        room.setLocation(findLoc(locationId));
	        raumFacadeLocal.edit(room);
	        msg = "edit";
	        //addMessage("messages", msg);
	        addInfoMessage(msg);
		    }
		    catch (Exception e) {
		    	msg = "notEdit";
	            //addMessage("messages", msg);
	            addInfoMessage(msg);
		    }
	   	roomList = getRaumList();
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
