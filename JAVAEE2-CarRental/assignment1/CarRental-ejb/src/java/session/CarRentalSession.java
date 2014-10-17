package session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateful;
import rental.RentalStore;
import rental.ReservationConstraints;
import rental.Quote;

@Stateful
public class CarRentalSession implements CarRentalSessionRemote {
    
    private String selectedRentalCompany = "testCompany";
    private String carRenter = "testRenter";;
    private List<Quote> quotes = new ArrayList<Quote>();
    
    @Override
    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(RentalStore.getRentals().keySet());
    }

    @Override
    public String  createQuote(String startDate, String endDate, String carType)
    {   
        Quote quote = new Quote(carRenter,
                                new Date(),
                                new Date(),
                                selectedRentalCompany,
                                carType,
                                50);
         
        this.quotes.add(quote);
        
        return quote.toString();
    }
    
    @Override
    public List<String> getCurrentQuotes()
    {
        List<String> toreturn = new ArrayList<String>();
        for(Quote q:this.quotes)
            toreturn.add(q.toString());
        
        return toreturn;
    }
    
    @Override
    public boolean confirmQuotes()
    {
        this.quotes = new ArrayList<Quote>();
        
        return true;
    }
    
}
