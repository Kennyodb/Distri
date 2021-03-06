package rental;

import java.rmi.Remote;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IRentalManager extends Remote {

	public Session getNewSession(String type, String username);

	public List<String> getAvailableCarTypes(Date start, Date end);

	public List<String> getRentalCompanies();

	// public Quote getCheapestCarQuoteForConstraint(ReservationConstraints
	// constraints);

	// public Quote createQuoteForSession(ReservationConstraints constraints,
	// int sessionID);

	// public List<Quote> getCurrentQuotesForSession(int sessionID);

	// public boolean confirmQuotesForSession(int sessionID);

	public Reservation confirmQuote(String companyName, Quote q)
			throws ReservationException;

	public void cancelReservation(String companyName, Reservation res);

	public void registerCompany(CarRentalCompany crc);

	public void unregisterCompany(CarRentalCompany crc);

	public int getNumberOfReservationsForCarType(String companyName, String type);

	public Set<String> getTopCustomers();
}