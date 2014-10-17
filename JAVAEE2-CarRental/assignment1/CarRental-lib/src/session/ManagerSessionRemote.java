package session;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import rental.ReservationConstraints;
import rental.Quote;


@Remote
public interface ManagerSessionRemote 
{
    List<String> getSupportedCarTypes();
    
    String getNumberOfReservationsForCarType(String type);
    
    String getNumberOfReservationsForClient(String clientID);
}
