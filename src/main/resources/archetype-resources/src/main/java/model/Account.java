package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the accounts database table.
 * 
 */
@Entity
@Table(name = "accounts")
@NamedQueries({
	@NamedQuery(name="Account.findAll", query="SELECT a FROM Account a"),
	@NamedQuery(name="Account.findByAccName", query = "SELECT a FROM Account a WHERE a.accName = :accName"),
	@NamedQuery(name="Account.findByAccID", query = "SELECT a FROM Account a WHERE a.accID = :accID"),
	@NamedQuery(name="Account.findByAccEmail", query = "SELECT a FROM Account a WHERE a.accEmail = :accEmail"),
	@NamedQuery(name="Account.findByAccPwd", query = "SELECT a FROM Account a WHERE a.accPwd = :accPwd")})

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int accID;

	private String accEmail;

	private String accName;

	private String accPwd;

	//bi-directional many-to-one association to Benutzergruppe
	@ManyToOne
	@JoinColumn(name="FK_GroupID")
	private Benutzergruppe benutzergruppe;

	//bi-directional many-to-one association to Faculty
	@ManyToOne
	@JoinColumn(name="FK_FBID")
	private Faculty faculty;

	public Account() {
	}

	public int getAccID() {
		return this.accID;
	}

	public void setAccID(int accID) {
		this.accID = accID;
	}

	public String getAccEmail() {
		return this.accEmail;
	}

	public void setAccEmail(String accEmail) {
		this.accEmail = accEmail;
	}

	public String getAccName() {
		return this.accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccPwd() {
		return this.accPwd;
	}

	public void setAccPwd(String accPwd) {
		this.accPwd = accPwd;
	}

	public Benutzergruppe getBenutzergruppe() {
		return this.benutzergruppe;
	}

	public void setBenutzergruppe(Benutzergruppe benutzergruppe) {
		this.benutzergruppe = benutzergruppe;
	}

	public Faculty getFaculty() {
		return this.faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

}