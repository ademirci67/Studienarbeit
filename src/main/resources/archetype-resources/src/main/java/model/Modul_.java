package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-01-12T12:20:54.167+0100")
@StaticMetamodel(Modul.class)
public class Modul_ {
	public static volatile SingularAttribute<Modul, Integer> modID;
	public static volatile SingularAttribute<Modul, String> modKuerzel;
	public static volatile SingularAttribute<Modul, String> modName;
	public static volatile SingularAttribute<Modul, Integer> pcid;
	public static volatile ListAttribute<Modul, Sgmodul> sgmoduls;
}