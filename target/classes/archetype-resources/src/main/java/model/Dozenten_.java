package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-03-29T13:47:43.790+0200")
@StaticMetamodel(Dozenten.class)
public class Dozenten_ {
	public static volatile SingularAttribute<Dozenten, Integer> did;
	public static volatile SingularAttribute<Dozenten, String> DKurz;
	public static volatile SingularAttribute<Dozenten, String> DName;
	public static volatile SingularAttribute<Dozenten, String> DTitel;
	public static volatile SingularAttribute<Dozenten, String> DVorname;
	public static volatile ListAttribute<Dozenten, Sgmodul> sgmoduls;
}
