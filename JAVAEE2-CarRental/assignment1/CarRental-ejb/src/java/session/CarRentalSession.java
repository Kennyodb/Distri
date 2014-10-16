package session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateful;
import rental.RentalStore;
import rental.ReservationConstraints;
import rental.Quote;

@Stateful
public class CarRentalSession implements CarRentalSessionRemote {
    
    private String selectedRentalCompany;
    private String carRenter;
    private List<Quote> quotes;
    
    public CarRentalSession()
    {
        this.carRenter = "testRenter";
        this.selectedRentalCompany = (String) this.getAllRentalCompanies().toArray()[0];
        this.quotes = new ArrayList<Quote>();
    }
    
    @Override
    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(RentalStore.getRentals().keySet());
    }

    @Override
    public Quote  createQuote(ReservationConstraints reservationConstraints)
    {        
       /* Quote quote = new Quote(carRenter,
                                reservationConstraints.getStartDate(),
                                reservationConstraints.getEndDate(),
                                selectedRentalCompany,
                                reservationConstraints.getCarType(),
                                50);
         
        this.quotes.add(quote);
        
        return quote;*/
        return null;
    }
    
}
