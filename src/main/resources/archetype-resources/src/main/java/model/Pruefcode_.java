package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-03-29T13:45:44.472+0200")
@StaticMetamodel(Pruefcode.class)
public class Pruefcode_ {
	public static volatile SingularAttribute<Pruefcode, Integer> pcid;
	public static volatile SingularAttribute<Pruefcode, Studiengang> studiengang;
	public static volatile ListAttribute<Pruefcode, Modul> moduls;
	public static volatile SingularAttribute<Pruefcode, String> pflichtOderWahl;
	public static volatile SingularAttribute<Pruefcode, Integer> prCode;
	public static volatile SingularAttribute<Pruefcode, String> vertiefungsrichtungShortName;
}
