package model;

import java.sql.Timestamp;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-12-04T14:54:03.263+0100")
@StaticMetamodel(Stundenplaneintrag.class)
public class Stundenplaneintrag_ {
	public static volatile SingularAttribute<Stundenplaneintrag, Integer> spid;
	public static volatile SingularAttribute<Stundenplaneintrag, Date> SPEDatZeit;
	public static volatile SingularAttribute<Stundenplaneintrag, String> SPSemester;
	public static volatile SingularAttribute<Stundenplaneintrag, Integer> SPTermin;
	public static volatile SingularAttribute<Stundenplaneintrag, Integer> status;
	public static volatile SingularAttribute<Stundenplaneintrag, Integer> studierendenzahl;
	public static volatile SingularAttribute<Stundenplaneintrag, Timestamp> zeitStempel;
	public static volatile SingularAttribute<Stundenplaneintrag, Lehrveranstaltungsart> lehrveranstaltungsart;
	public static volatile SingularAttribute<Stundenplaneintrag, Raum> raum;
	public static volatile SingularAttribute<Stundenplaneintrag, Sgmodul> sgmodul;
}
