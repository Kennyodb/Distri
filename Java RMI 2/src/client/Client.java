package client;

import java.util.Date;
import java.util.List;
import java.util.Set;

import rental.CarType;
import rental.IRentalManager;
import rental.ManagerSession;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationSession;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client extends
		AbstractScriptedTripTest<ReservationSession, ManagerSession> {

	private IRentalManager manager;

	public static void main(String[] args) throws Exception {

		String rentalManagerName = "Manager";

		// An example reservation scenario on car rental company 'Hertz' would
		// be...
		Client client = new Client("simpleTrips", rentalManagerName);
		client.run();
	}

	public Client(String scriptFile, String managerName) {
		super(scriptFile);
		try {
			System.setSecurityManager(null);

			Registry registry = LocateRegistry.getRegistry();
			manager = (IRentalManager) registry.lookup(managerName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected ReservationSession getNewReservationSession(String name)
			throws Exception {
		return (ReservationSession) manager.getNewSession("reservation", name);
	}

	@Override
	protected ManagerSession getNewManagerSession(String name) throws Exception {
		return (ManagerSession) manager.getNewSession("manager", name);
	}

	@Override
	protected void checkForAvailableCarTypes(ReservationSession session,
			Date start, Date end) throws Exception {
		// TODO moet dit geprint worden?
		for (String type : session.getAvailableCarTypes(start, end)) {
			System.out.println(type);
		}
	}

	@Override
	protected String getCheapestCarType(ReservationSession session, Date start,
			Date end) throws Exception {
		return session.getCheapesCarType(start, end);
	}

	@Override
	protected void addQuoteToSession(ReservationSession session, Date start,
			Date end, String carType, String carRentalName) throws Exception {
		session.createQuote(carRentalName, new ReservationConstraints(start,
				end, carType));
	}

	@Override
	protected List<Reservation> confirmQuotes(ReservationSession session)
			throws Exception {
		return session.confirmQuotes();
	}

	@Override
	protected int getNumberOfReservationsBy(ManagerSession ms, String clientName)
			throws Exception {
		return ms.getNumberOfReservationsBy(clientName);
	}

	@Override
	protected Set<String> getBestClients(ManagerSession ms) throws Exception {
		return ms.getTopCustomers();
	}

	@Override
	protected int getNumberOfReservationsForCarType(ManagerSession ms,
			String carRentalCompanyName, String carType) throws Exception {
		return ms.getNumberOfReservationsForCarType(carRentalCompanyName,
				carType);
	}

	@Override
	protected CarType getMostPopularCarTypeIn(ManagerSession ms,
			String carRentalCompanyName) throws Exception {
		return ms.getMostPopularCarTypeIn(carRentalCompanyName);
	}
}
