package controller;


import model.Modul;
import model.Pruefcode;


import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;


import org.primefaces.event.SelectEvent;
import javax.ejb.EJB;
import EJB.ModulFacadeLocal;
import EJB.PruefcodeFacadeLocal;

/**
*
* @author Anil
*/

@Named(value="modulController")
@SessionScoped
public class ModulController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;
	
	@Inject 
	private Modul modul;
	@Inject
	private Pruefcode code;
	@Inject
	private PruefcodeController pcController;
	
	@EJB
	private ModulFacadeLocal modulFacadeLocal;
	@EJB
	private PruefcodeFacadeLocal pruefCodeEJB;
	
	/**
	 * Initialisierung
	 */
	@PostConstruct
    public void init() {
        modulList = getModulListAll();
        listPC = pruefCodeEJB.findAll();
    }
	
	ArrayList<Pruefcode> codeList = new ArrayList<>();
	
	
	private String modulShort;
	private String modulName;
	private int pcId;
	private int verifyCode;
	private boolean modulShortOk = false;
	private boolean modulNameOk = false;
	private boolean pCType = false ;
	
	public boolean ispCType() {
		return pCType;
	}

	public void setpCType(boolean pCType) {
		this.pCType = pCType;
	}

	List<Modul> modulList;
	List<Pruefcode> listPC;
	
	private List<Pruefcode> Codesort = null;
	private List<Modul> Modulsort = null;
	
	private Modul modulSelected;
	
	// Getter und Setter
	public Modul getModulSelected() {
		return modulSelected;
	}
	  
	public void setModulSelected(Modul modulSelected) {
		this.modulSelected = modulSelected;
	}
	
	public ArrayList<Pruefcode> getCodeList() {
		return codeList;
	}

	public void setCodeList(ArrayList<Pruefcode> codeList) {
		this.codeList = codeList;
	}
	
	public Pruefcode getCode() {
		return code;
	}

	public void setCode(Pruefcode code) {
		this.code = code;
	}
	  
    public List<Modul> getModulList() {
        return modulList;
    }
    
	public List<Pruefcode> getListPC() {
		return listPC;
	}

	public List<Pruefcode> getCodesort() {
		return Codesort;
	}

	public void setCodesort(List<Pruefcode> codesort) {
		Codesort = codesort;
	}

	public void setListPC(List<Pruefcode> listPC) {
		this.listPC = listPC;
	}

	public Modul getModul() {
		return modul;
	}
	  
	public void setModul(Modul modul) {
		this.modul = modul;
	}
	  
	public String getModulShort() {
		return modulShort;
	}
	  
	public void setModulShort(String modulShort) {
		if(modulShort!=null){
			this.modulShort = modulShort;
			modulShortOk = true;
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
	}
	  
	public int getPcId() {
		return pcId;
	}
	  
	public void setPcId(int pcId) {
	        this.pcId = pcId;
	}
	
	public int getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(int verifyCode) {
		this.verifyCode = verifyCode;
	}

	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
	  
	private UIComponent reg;
	
	/**
	 * Hinzufügen eines Moduls
	 * @throws Exception
	 */
	public void createModul() throws Exception  {
		String msg;
		EntityManager em = emf.createEntityManager();
		Modul mod = new Modul();  
		mod.setModName(modulName);    
		mod.setModKuerzel(modulShort);      
		if(!pCType) {
		mod.setPruefcode(findCode(pcId));
		}
		if(pCType) {
		Pruefcode newCode = pcController.createPruefcode(verifyCode, 1);	
		mod.setPruefcode(newCode);
		}
		try {
			modulFacadeLocal.create(mod);
			msg = "entry";
            //addMessage("messages", msg);
			addInfoMessage(msg);
            

	    }
	    catch (Exception e) {
	    	msg = "notEntry";
            //addMessage("messages", msg);
	    	addInfoMessage(msg);
	    }
		modulList = getModulListAll();
		em.close();
	}
	
	/**
	 * Schaut ob Modulname und Modulkürzel gesetzt worden sind, danach wird der Eintrag erstellt und zum Schluß wird die Liste aktualisiert.
	 * @throws SecurityException
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws Exception
	 */
	public void createDoModul() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(modulNameOk == true && modulShortOk == true) {
			createModul();
			modulList = getModulListAll();
		}
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Laden der Modulliste
	 * @return
	 */
	public List<Modul> getModulListAll(){
		/*
		if (Modulsort==null) {
			Modulsort=modulFacadeLocal.findAll();
			if (Modulsort != null) {
				Collections.sort(Modulsort, (a,b) -> {
					return a.getModName().compareToIgnoreCase(b.getModName());
				});
				}
			}
		
		return  Modulsort; 
		*/
		return modulFacadeLocal.findAll();
	}
	/*
	public List<Pruefcode> getPruefcodeListAll(){
		
		if (Codesort==null) {
			Codesort=pruefCodeEJB.findAll();
			if (Codesort != null) {
				Collections.sort(Codesort, (a,b) -> {
					return a.getPrCode().compareToIgnoreCase(b.getPrCode());
				});
				}
			}
		
		return  pruefCodeEJB.findAll();
	}*/
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Löschen eines Moduls
     * @throws Exception
     */
    public void deleteModul() throws Exception {
    	String msg;
        modulList.remove(modulSelected);
        //EntityManager em = emf.createEntityManager();
        //TypedQuery<Modul> q = em.createNamedQuery("Modul.findByModID",Modul.class);
        //q.setParameter("modID", modulSelected.getModID());
        //modul = (Modul)q.getSingleResult();
        modul = modulFacadeLocal.find(modulSelected.getModID());
        try {
        	modulFacadeLocal.remove(modul);
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
     * Die ausgewählte Zeile wird in modulSelected gespeichert sowie der Fremdschlüssel
     * @param e
     */
    public void onRowSelect(SelectEvent<Modul> e) {
    	String msg = "module";
        addInfoMessage(msg);
        
        modulSelected = e.getObject();
        pcId = modulSelected.getPruefcode().getPcid();
        
    }
    
    /**
     * Bearbeiten eines Moduls
     */
    public void addModul(){
    	EntityManager em = emf.createEntityManager();
    	String msg;
    	 try {
 	       em.find(Modul.class, modulSelected.getModID());
 	       modul.setModID(modulSelected.getModID());
 	       modul.setModName(modulSelected.getModName());
 	       modul.setModKuerzel(modulSelected.getModKuerzel());
 	       if(!pCType) {
 	 		modul.setPruefcode(findCode(pcId));
 	 		}
 	       if(pCType) {
 	 		Pruefcode newPCode = pcController.createPruefcode(verifyCode, 1);	
 	 		modul.setPruefcode(newPCode);
 	 		}
 	       modulFacadeLocal.edit(modul);
 	      msg = "edit";
          //addMessage("messages", msg);
          addInfoMessage(msg);
	    }
	    catch (Exception e) {
	    	msg = "NotEdit";
           //addMessage("messages", msg);
           addInfoMessage(msg);
	    }
    	 modulList = getModulListAll();
    	 em.close();
    }
    
    /**
     * Finden eines Prüfcodes anhand der ID
     * @param pcid
     * @return
     */
    private Pruefcode findCode(int pcid) {
    	/*
        try{
            EntityManager em = emf.createEntityManager(); 
            TypedQuery<Pruefcode> query
                = em.createNamedQuery("Pruefcode.findByPcid",Pruefcode.class);
            query.setParameter("pcid", pcid);
            code = (Pruefcode)query.getSingleResult();
        }
        catch(Exception e){   
        }
        */
        return code = pruefCodeEJB.find(pcId);
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