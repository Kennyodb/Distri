package rental;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import rental.CarType;
import rental.Reservation;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-18T17:00:46")
@StaticMetamodel(Car.class)
public class Car_ { 

    public static volatile SingularAttribute<Car, Integer> id;
    public static volatile SingularAttribute<Car, CarType> type;
    public static volatile SetAttribute<Car, Reservation> reservations;

}