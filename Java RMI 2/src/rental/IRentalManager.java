package rental;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRentalManager extends Remote
{
	public Session getNewSession(String type);
	
	public List<String> getAvailableCarTypes();
	
	public List<String> getAvailableRentalCompanies();
	
	public Quote getCheapestCarQuoteForConstraint(ReservationConstraints constraint);
	
	public void createQuoteForSession(Quote quote, int sessionID);
	
	public List<Quote> getCurrentQuotesForSession(int sessionID);
	
	public boolean confirmQuotesForSession(int sessionID);
	
	public void registerCompany(CarRentalCompany crc);
	
	public void unregisterCompany(CarRentalCompany crc);
	
	public int getNumberOfReservationsForCarType(String type);
	
	public String getTopCustomer();
} 