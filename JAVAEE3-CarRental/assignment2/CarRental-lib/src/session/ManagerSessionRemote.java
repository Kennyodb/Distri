package session;

import java.util.Set;
import javax.ejb.Remote;
import rental.CarType;

@Remote
public interface ManagerSessionRemote {
    
    public Set<String> getAllRentalCompanies();
    
    public Set<CarType> getCarTypes(String company);
 
    public int getNumberOfReservations(String company, String type);
      
    public int getNumberOfReservationsBy(String renter);
    
    public CarType getMostPopularCarTypeIn(String company);
    
    public Set<String> getBestClients();
    
}