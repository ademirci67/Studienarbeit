package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-03-29T13:45:44.492+0200")
@StaticMetamodel(Raum.class)
public class Raum_ {
	public static volatile SingularAttribute<Raum, Integer> rid;
	public static volatile SingularAttribute<Raum, Integer> kapazitaet;
	public static volatile SingularAttribute<Raum, String> nachbarRaum;
	public static volatile SingularAttribute<Raum, String> RName;
	public static volatile SingularAttribute<Raum, Location> location;
	public static volatile ListAttribute<Raum, Stundenplaneintrag> stundenplaneintrags;
}
