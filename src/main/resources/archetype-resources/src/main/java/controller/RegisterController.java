package controller;

import model.Account;

import model.Benutzergruppe;
import model.Faculty;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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


import javax.ejb.EJB;
import EJB.AccountFacadeLocal;
import EJB.BenutzergruppeFacadeLocal;
import EJB.FacultyFacadeLocal;

/**
 *
 * @author Anil
 */
@Named(value="registerController")
@SessionScoped
public class RegisterController implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction ut;
    
    @Inject
    private Account account;
    private Faculty faculty;
    private Benutzergruppe userGroup;
    
    List<Faculty> facultyList ;
    List<Benutzergruppe> userGroupList ;
    
    @EJB
	private AccountFacadeLocal accFacadeLocal;
    @EJB
	private FacultyFacadeLocal facultyEJB;
	@EJB
	private BenutzergruppeFacadeLocal userGroupEJB;
    
    /**
     * Initialisiere bei Anwendungsstart
     */
    @PostConstruct
    public void init(){
    	facultyList = facultyEJB.findAll();
		userGroupList = userGroupEJB.findAll();
    }

    private String accountPassword;
    private String accountName;
    private int facultyID;
    private String accountEmail;
    private int userGroupID;
    private boolean accountNameOk=false;
    private boolean accountPasswordOk = false;
    private boolean accountEmailOk = false;
    private boolean registerOk = false;
    
   
    
    


    public RegisterController() {

    }

    
    //getter und setter - Methoden 
 
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
	
    public String getAccountName() {
        return accountName;
    }

    public boolean isRegisterOk() {
        return registerOk;
    }

    public void setRegisterOk(boolean registerOk) {
        this.registerOk = registerOk;
    }

    public void setAccountName(String accountName) {
        if(accountName!=null){
            if(checkName(accountName)==false){
                this.accountName = accountName;
                accountNameOk=true;
            }
            else{
                String msg = "Account bereits vorhanden";
                addMessage("idName_reg",msg);
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
        else{
        	String msg="Bitte ein Passwort vergeben";
            addMessage("idPwd_reg",msg);
        }
    }

    public int getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(int facultyID) {
        this.facultyID = facultyID;
    }
    

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        if(accountEmail!=null){
            this.accountEmail = accountEmail;
            accountEmailOk=true;
        }
        else{
        	String msg="Bitte eine Email Adresse Angeben";
            addMessage("idEmail_reg",msg);
        }
    }
    
    public UIComponent getReg() {
        return reg;
    }

    public void setReg(UIComponent reg) {
        this.reg = reg;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

	public int getUserGroupID() {
		return userGroupID;
	}


	public void setUserGroupID(int userGroupID) {
		this.userGroupID = userGroupID;
	}
    
    //weitere Methoden
	
   /**
    * Beim drücken des Register Buttons auf registerUser leiten wenn alle Eingaben IO
    * @return
    * @throws SecurityException
    * @throws SystemException
    * @throws NotSupportedException
    * @throws RollbackException
    * @throws HeuristicMixedException
    * @throws HeuristicRollbackException
    * @throws Exception
    */
	public String register_button() throws SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception{
       if(accountNameOk==true && accountPasswordOk==true && accountEmailOk==true){
           registerUser();
           accountName="";
           accountEmail="";
           return "login.xhtml";
       }
       else{
    	   accountNameOk=false;
    	   accountPasswordOk = false;
    	   accountEmailOk = false;
       }
       accountNameOk=false;
       accountPasswordOk = false;
       accountEmailOk = false;
        return"register.xhtml";
   }


    private UIComponent reg;   
    
    /**
     * Eingegebene Daten an die Datenbank übermitteln
     * @throws Exception
     */
    public void registerUser() throws Exception  {
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
                //Alle User sind als erstes Nobodys
                newUser.setBenutzergruppe(findBGID(2));
                //Dropdown Menü
                newUser.setFaculty(findFac(facultyID));
                try {
                	accFacadeLocal.create(newUser);
                }
                catch (Exception e) {
                    try {
                        ut.rollback();
                    } 
                    catch (IllegalStateException | SecurityException | SystemException ex) {
                    }
                }
            } 
            else {
                FacesMessage message = new FacesMessage("Account ist bereits vorhanden");
                FacesContext.getCurrentInstance().addMessage("registerform:idName_reg", message);
            }
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
     * Setzen einer Benutzergruppe auf Nobody
     * @return
     */
    private Benutzergruppe findBGID(int bg) {
        /*
         * try{
        
    	
        	EntityManager em;
        	String query;
        	Query q;
            em = emf.createEntityManager();
            query = "SELECT b FROM Benutzergruppe b WHERE b.groupID = 2";
            q = em.createQuery(query);
            userGroup = (Benutzergruppe)q.getSingleResult();
        }
        catch(Exception e){   
        }
        */
        return userGroup = userGroupEJB.find(bg);
    }
    
    
    /**
     * Finden eines Fachbereichs anhand des Fachbereichsnamen
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
      * Nachrichten an die View senden
     * @param loginformidName
     * @param msg
     */
    private void addMessage(String loginformidName, String msg) {
        FacesMessage message = new FacesMessage(msg);
        FacesContext.getCurrentInstance().addMessage(loginformidName, message);     
    }
    
}

