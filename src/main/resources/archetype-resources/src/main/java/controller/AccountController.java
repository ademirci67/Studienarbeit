package controller;
import model.Benutzergruppe;
import model.Account;
import model.Faculty;


import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
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

import javax.transaction.UserTransaction;


import org.primefaces.event.SelectEvent;

import javax.ejb.EJB;
import EJB.AccountFacadeLocal;
import EJB.BenutzergruppeFacadeLocal;
import EJB.FacultyFacadeLocal;

/**
*
* @author Anil
*/

@Named(value="accountController")
@SessionScoped
public class AccountController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;
  
	@Resource
	private UserTransaction ut;

	@Inject 
	private Account account;
	private Faculty faculty;
	private Benutzergruppe userGroup;
	
	@EJB
	private AccountFacadeLocal accFacadeLocal;
	@EJB
	private FacultyFacadeLocal facultyEJB;
	@EJB
	private BenutzergruppeFacadeLocal userGroupEJB;
	
    List<Faculty> facultyList ;
    List<Benutzergruppe> userGroupList ;
	
	/**
	 * Initialisierung
	 */
	@PostConstruct
    public void init() {
		accountList = getAccountListAll();
		facultyList = facultyEJB.findAll();
		userGroupList = userGroupEJB.findAll();
    }
	
	private String accountName;
	private String accountPassword;
	private String accountEmail;
	private int userGroupID;
	private int facultyID;
	private boolean accountEmailOk = false;
	private boolean accountNameOk = false;
	private boolean accountPasswordOk = false;
	
	List<Account> accountList;
	
	private Account accountSelected;
	
	// Getter und Setter
	public Account getAccountSelected() {
		return accountSelected;
	}
	  
	public void setAccountSelected(Account accountSelected) {
		this.accountSelected = accountSelected;
	}
	
	public int getUserGroupID() {
		return userGroupID;
	}
	
	public List<Faculty> getFacultyList() {
        return facultyList;
    }

	public void setFacultyList(List<Faculty> facultyList) {
		this.facultyList = facultyList;
	}
	
	public List<Benutzergruppe> getUserGroupList() {
        return userGroupList;
    }

	public void setUserGroupList(List<Benutzergruppe> userGroupList) {
		this.userGroupList = userGroupList;
	}

	public void setUserGroupID(int userGroupID) {
		this.userGroupID = userGroupID;
	}
	  
    public List<Account> getAccountList() {
        return accountList;
    }
    
    public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
		
	}
    
	public Account getAccount() {
		return account;
	}
	  
	public void setAccount(Account account) {
		this.account = account;
	}
  
	public String getAccountEmail() {
		return accountEmail;
	}
	  
	public void setAccountEmail(String accountEmail) {
		if(accountEmail!=null){
			this.accountEmail = accountEmail;
			accountEmailOk = true;
		}
	}
	  
	public String getAccountName() {
		return accountName;
	}
	  
	public void setAccountName(String accountName) {
		if(accountName!=null){
            if(checkName(accountName)==false){
                this.accountName = accountName;
                accountNameOk=true;
            }
		}
	}
	  
	public String getAccountPassword() {
		return accountPassword;
	}
	  
	public void setAccountPassword(String accountPassword) {
		if(accountPassword!=null){
	        this.accountPassword = accountPassword;
	        accountPasswordOk=true;
	    }
	}
	
	public int getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(int facultyID) {
        this.facultyID = facultyID;
    }
	public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }
    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }
    
    public Benutzergruppe getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Benutzergruppe userGroup) {       
        this.userGroup = userGroup;
    }
	  
	private UIComponent reg;
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	// Erstellen eines Accounteintrags
	/*public void createAccount() throws Exception  {
	 * String msg;
		EntityManager em = emf.createEntityManager();
		Account acc = new Account();  
		acc.setAccName(accountName);
		acc.setAccPwd(accountPassword);
		acc.setAccEmail(accountEmail);
		acc.setBenutzergruppe(findBG(userGroupName));
		acc.setFaculty(findFac(facultyName));
		try {
			accFacadeLocal.create(acc);
			msg = "Eintrag wurde erstellt.";
            addMessage("messages", msg);
	    }
	    catch (Exception e) {
	       msg = "Eintrag wurde nicht erstellt.";
           addMessage("messages", msg);
	    }
		em.close();
	}
	
	// Schaut ob Accountname, Accountpasswort und Accountemail gesetzt worden sind, danach wird der Eintrag erstellt und zum Schluß wird die Liste aktualisiert.
	public void createDoAccount() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
		if(accountNameOk == true && accountPasswordOk == true && accountEmailOk) {
			createAccount();
			accountList = getAccountListAll();
		}
	}*/
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	
	// 
	/**
	 * Laden der Accountliste
	 * @return
	 */
	public List<Account> getAccountListAll(){
		return accFacadeLocal.findAll();
	}
	
	/**
	 * Ausgewählte Zeile wird in accountSelected gespeichert sowie die Fremdschlüssel
	 * @param e
	 */
	public void onRowSelect(SelectEvent<Account> e) {
		String msg = "account";
        addInfoMessage(msg);
        
        accountSelected = e.getObject();
        
        userGroupID = accountSelected.getBenutzergruppe().getGroupID();
        facultyID = accountSelected.getFaculty().getFbid();
        
    }
	
	//----------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Löschen eines Accounteintrags
     * @throws Exception
     */
    public void deleteAccount() throws Exception {
    	String msg;
    	accountList.remove(accountSelected);        
        //EntityManager em = emf.createEntityManager();
        //TypedQuery<Account> q = em.createNamedQuery("Account.findByAccID",Account.class);
        //q.setParameter("accID", accountSelected.getAccID());
        //account = (Account)q.getSingleResult();
        account = accFacadeLocal.find(accountSelected.getAccID());
        try {
        	accFacadeLocal.remove(account);
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
    
   // ---------------------------------------------------------------------------------------------------------------------

    /**
     * Finden einer Benutzergruppe anhand des Benutzerguppennamens
     * @param bg
     * @return
     */
    private Benutzergruppe findBG(int bg) {
    	/*
    	try{
            EntityManager em = emf.createEntityManager(); 
            TypedQuery<Benutzergruppe> query
                = em.createNamedQuery("Benutzergruppe.findByBGName",Benutzergruppe.class);
            query.setParameter("BGName", bg);
            userGroup = (Benutzergruppe)query.getSingleResult();
        }
        catch(Exception e){   
        }
        */
        return userGroup = userGroupEJB.find(bg);
    }
    
    /**
     * Finden des Fachbereichs anhand des Fachbereichsnamens
     * @param fac
     * @return
     */
    private Faculty findFac(int fac) {
        /*
    	try{
            EntityManager em = emf.createEntityManager(); 
            TypedQuery<Faculty> query
                = em.createNamedQuery("Faculty.findByFacName",Faculty.class);
            query.setParameter("facName", fac);
            faculty = (Faculty)query.getSingleResult();
        }
        catch(Exception e){   
        }
        */
        return faculty = facultyEJB.find(fac);
    }
    
    /**
     * Bearbeiten des Accounts
     */
    public void addAccount(){
    	String msg;
    	EntityManager em = emf.createEntityManager();
      	 try {
	        em.find(Account.class, accountSelected.getAccID());
	        account.setAccID(accountSelected.getAccID());
	        account.setAccName(accountSelected.getAccName());
	        account.setAccPwd(accountSelected.getAccPwd());
	        account.setAccEmail(accountSelected.getAccEmail());
	        account.setBenutzergruppe(findBG(userGroupID));
            account.setFaculty(findFac(facultyID));
            accFacadeLocal.edit(account);
            msg = "edit";
            //addMessage("messages", msg);
            addInfoMessage(msg);
      	}
    	    catch (Exception e) {
    	    	msg = "notEdit";
             //addMessage("messages", msg);
             addInfoMessage(msg);
    	    }
       	accountList = getAccountListAll();
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
	
	//-------------------------------------------------------------------------------
	
	/**
	 * Erstellt einen Benutzer, vorher wird geschaut, ob der Benutzer schon vorhanden ist.
	 * @throws Exception
	 */
	public void registerUser2() throws Exception  {
		String msg;
        EntityManager em = emf.createEntityManager();
        List<Account> user_temp = new ArrayList<>();   
        try {
            TypedQuery<Account> queryGet = em.createNamedQuery("Account.findByAccName", Account.class).setParameter("accName", this.accountName);   
            user_temp = queryGet.getResultList();  
        } 
        catch (Exception e) {
        }
        if (user_temp.isEmpty()) {  
            Account newUser = new Account();  
            newUser.setAccName(accountName);    
            newUser.setAccPwd(accountPassword);      
            newUser.setAccEmail(accountEmail);
            newUser.setBenutzergruppe(findBG(userGroupID));
            //Dropdown Menü
            newUser.setFaculty(findFac(facultyID));
            try {
            	accFacadeLocal.create(newUser);
            	msg = "entry";
                //addMessage("messages", msg);
                addInfoMessage(msg);
            }
            catch (Exception e) {
            	msg = "notEntry";
                //addMessage("messages", msg);
                addInfoMessage(msg);
            }
        } 
        else {
            msg = "accountAvailable";
            //addMessage("messages", msg);
            addInfoMessage(msg);
        }
        accountList = getAccountListAll();
        em.close();
	}
	
    /**
     * Überprüfen ob der Name schon vergeben ist.
     * @param uName
     * @return
     */
    private boolean checkName(String uName) {
        boolean found = false;
        try{
            EntityManager em = emf.createEntityManager(); 
            TypedQuery<Account> query
                = em.createNamedQuery("Account.findByBname",Account.class);
            query.setParameter("accName", uName);
            account = (Account)query.getSingleResult();
            found=true; //Account gefunden!
        }
        catch(Exception e){   
        }
        return found;
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