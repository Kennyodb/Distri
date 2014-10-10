package rental;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface ICarRentalCompany extends Remote {
	
	public List<CarType> getFreeCarTypes(Date from, Date end)
			throws RemoteException;

	public Quote createQuote(ReservationConstraints constraints, String client)
			throws RemoteException;

	public Reservation confirmQuote(Quote quote)
			throws RemoteException;
	
}
