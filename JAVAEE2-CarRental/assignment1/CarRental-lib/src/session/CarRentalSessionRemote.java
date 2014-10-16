package session;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import rental.ReservationConstraints;
import rental.Quote;


@Remote
public interface CarRentalSessionRemote {

    Set<String> getAllRentalCompanies();
    
    Quote createQuote(ReservationConstraints reservationContraints);
    
    List<Quote> getCurrentQuotes();
    
    boolean confirmQuotes();
}
