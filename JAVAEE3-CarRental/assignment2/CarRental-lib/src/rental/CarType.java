package rental;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CarType implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int carTypeId;
    
    @Column(name = "NAME")
    private String name;
    private int nbOfSeats;
    private boolean smokingAllowed;
    private double rentalPricePerDay;
    //trunk space in liters
    private float trunkSpace;
    
    /***************
     * CONSTRUCTOR *
     ***************/
    
    public CarType()
    {
        
    }
    
    public CarType(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed) {
        this.name = name;
        this.nbOfSeats = nbOfSeats;
        this.trunkSpace = trunkSpace;
        this.rentalPricePerDay = rentalPricePerDay;
        this.smokingAllowed = smokingAllowed;
    }

    
    public int getCarTypeId()
    {
        return this.carTypeId;
    }
    
    public void setCarTypeId(int carTypeId)
    {
        this.carTypeId = carTypeId;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public int getNbOfSeats() {
        return nbOfSeats;
    }
    
    public void setNbOfSeats(int nbOfSeats) {
        this.nbOfSeats = nbOfSeats;
    }
    
    public boolean isSmokingAllowed() {
        return smokingAllowed;
    }
    
    public void setIsSmokingAllowed(boolean smokingAllowed)
    {
        this.smokingAllowed = smokingAllowed;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }
    
    public void setRentalPricePerDay(double rentalPricePerDay)
    {
        this.rentalPricePerDay = rentalPricePerDay;
    }
    
    public float getTrunkSpace() {
    	return trunkSpace;
    }
    
    public void setTrunkSpace(float trunkSpace)
    {
        this.trunkSpace = trunkSpace;
    }
    
    /*************
     * TO STRING *
     *************/
    
    @Override
    public String toString() {
    	return String.format("Id: %s Car type: %s \t[seats: %d, price: %.2f, smoking: %b, trunk: %.0fl]" , "" + this.carTypeId,
                getName(), getNbOfSeats(), getRentalPricePerDay(), isSmokingAllowed(), getTrunkSpace());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
	if (obj == null)
            return false;
	if (getClass() != obj.getClass())
            return false;
	CarType other = (CarType) obj;
	if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
	return true;
    }
}