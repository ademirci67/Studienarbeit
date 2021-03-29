package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-03-29T13:45:44.437+0200")
@StaticMetamodel(Modul.class)
public class Modul_ {
	public static volatile SingularAttribute<Modul, Integer> modID;
	public static volatile SingularAttribute<Modul, String> modKuerzel;
	public static volatile SingularAttribute<Modul, String> modName;
	public static volatile ListAttribute<Modul, Sgmodul> sgmoduls;
	public static volatile SingularAttribute<Modul, Pruefcode> pruefcode;
}
