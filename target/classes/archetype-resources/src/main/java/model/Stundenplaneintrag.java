package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the stundenplaneintrag database table.
 * 
 */
@Entity
@NamedQuery(name="Stundenplaneintrag.findAll", query="SELECT s FROM Stundenplaneintrag s")
public class Stundenplaneintrag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int spid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date SPEDatZeit;

	private String SPSemester;

	private int SPTermin;

	private int status;

	private int studierendenzahl;

	private Timestamp zeitStempel;

	//bi-directional many-to-one association to Lehrveranstaltungsart
	@ManyToOne
	@JoinColumn(name="FK_LVID")
	private Lehrveranstaltungsart lehrveranstaltungsart;

	//bi-directional many-to-one association to Raum
	@ManyToOne
	@JoinColumn(name="FK_RID")
	private Raum raum;

	//bi-directional many-to-one association to Sgmodul
	@ManyToOne
	@JoinColumn(name="FK_SGMID")
	private Sgmodul sgmodul;

	public Stundenplaneintrag() {
	}

	public int getSpid() {
		return this.spid;
	}

	public void setSpid(int spid) {
		this.spid = spid;
	}

	public Date getSPEDatZeit() {
		return this.SPEDatZeit;
	}

	public void setSPEDatZeit(Date SPEDatZeit) {
		this.SPEDatZeit = SPEDatZeit;
	}

	public String getSPSemester() {
		return this.SPSemester;
	}

	public void setSPSemester(String SPSemester) {
		this.SPSemester = SPSemester;
	}

	public int getSPTermin() {
		return this.SPTermin;
	}

	public void setSPTermin(int SPTermin) {
		this.SPTermin = SPTermin;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStudierendenzahl() {
		return this.studierendenzahl;
	}

	public void setStudierendenzahl(int studierendenzahl) {
		this.studierendenzahl = studierendenzahl;
	}

	public Timestamp getZeitStempel() {
		return this.zeitStempel;
	}

	public void setZeitStempel(Timestamp zeitStempel) {
		this.zeitStempel = zeitStempel;
	}

	public Lehrveranstaltungsart getLehrveranstaltungsart() {
		return this.lehrveranstaltungsart;
	}

	public void setLehrveranstaltungsart(Lehrveranstaltungsart lehrveranstaltungsart) {
		this.lehrveranstaltungsart = lehrveranstaltungsart;
	}

	public Raum getRaum() {
		return this.raum;
	}

	public void setRaum(Raum raum) {
		this.raum = raum;
	}

	public Sgmodul getSgmodul() {
		return this.sgmodul;
	}

	public void setSgmodul(Sgmodul sgmodul) {
		this.sgmodul = sgmodul;
	}

}