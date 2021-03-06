package rental;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @OneToMany
    private Set<Reservation> reservations;
    
    @ManyToOne
    private CarType type;

    /***************
     * CONSTRUCTOR *
     ***************/
    public Car()
    {
        
    }
    
    public Car(int uid, CarType type) {
    	//this.id = uid;
        this.type = type;
        this.reservations = new HashSet<Reservation>();
    }

    /******
     * ID *
     ******/
    public int getId() {
    	return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    /************
     * CAR TYPE *
     * @return 
     ************/
    // TODO MappedBy toevoegen zie slides
    public CarType getType() {
        return type;
    }
    
    public void setType(CarType type)
    {
        this.type = type;
    }

    /****************
     * RESERVATIONS *
     ****************/

    public boolean isAvailable(Date start, Date end) {
        if(!start.before(end))
            throw new IllegalArgumentException("Illegal given period");

        for(Reservation reservation : reservations) {
            if(reservation.getEndDate().before(start) || reservation.getStartDate().after(end))
                continue;
            return false;
        }
        return true;
    }
    
    public void addReservation(Reservation res) {
        reservations.add(res);
    }
    
    public void removeReservation(Reservation reservation) {
        // equals-method for Reservation is required!
        reservations.remove(reservation);
    }

    // TODO MappedBy toevoegen zie slides
    public Set<Reservation> getReservations() {
        return reservations;
    }   
    
    public void setReservations(Set<Reservation> reservations)
    {
        this.reservations = reservations;
    }
    
    // TODO FIX
    @Override
    public int hashCode() {
        final int prime = 31;
	int result = 1;
	return result;
    }

    // TODO FIX
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
	if (obj == null)
            return false;
	if (getClass() != obj.getClass())
            return false;
        
	return true;
    }
}