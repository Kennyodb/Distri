package session;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import rental.ReservationConstraints;
import rental.Quote;


@Remote
public interface CarRentalSessionRemote {

    Set<String> getAllRentalCompanies();
    
    String createQuote(String startDate, String endDate, String carType);
    
    List<String> getCurrentQuotes();
    
    boolean confirmQuotes();
}
