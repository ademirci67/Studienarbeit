package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-01-12T12:20:54.106+0100")
@StaticMetamodel(Benutzergruppe.class)
public class Benutzergruppe_ {
	public static volatile SingularAttribute<Benutzergruppe, Integer> groupID;
	public static volatile SingularAttribute<Benutzergruppe, String> BGName;
	public static volatile SingularAttribute<Benutzergruppe, Integer> BGRechte;
	public static volatile SingularAttribute<Benutzergruppe, String> BGShortName;
	public static volatile ListAttribute<Benutzergruppe, Account> accounts;
}
