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

	/********
	 * MAIN *
	 ********/

	public static void main(String[] args) throws Exception {

		String rentalManagerName = "Manager";

		// An example reservation scenario on car rental company 'Hertz' would
		// be...
		Client client = new Client("simpleTrips", rentalManagerName);
		client.run();
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/

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
		return (ReservationSession) manager.getNewSession("reservation");
	}

	@Override
	protected ManagerSession getNewManagerSession(String name) throws Exception {
		return (ManagerSession) manager.getNewSession("manager");
	}

	@Override
	protected void checkForAvailableCarTypes(ReservationSession session,
			Date start, Date end) throws Exception {
		// TODO Auto-generated method stub
		// printen of wat?
	}

	@Override
	protected String getCheapestCarType(ReservationSession session, Date start,
			Date end) throws Exception {
		// TODO
		return null;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Set<String> getBestClients(ManagerSession ms) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getNumberOfReservationsForCarType(ManagerSession ms,
			String carRentalCompanyName, String carType) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected CarType getMostPopularCarTypeIn(ManagerSession ms,
			String carRentalCompanyName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
