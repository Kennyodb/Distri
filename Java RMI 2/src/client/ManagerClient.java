package client;

import java.util.Date;
import java.util.List;

import rental.IRentalManager;
import rental.ManagerSession;
import rental.Quote;
import rental.Reservation;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ManagerClient extends AbstractScriptedSimpleTest {

	private IRentalManager manager;
	private ManagerSession session;

	/********
	 * MAIN *
	 ********/

	public static void main(String[] args) throws Exception {

		String carRentalCompanyName = "Hertz";

		// An example reservation scenario on car rental company 'Hertz' would
		// be...
		ReservationClient client = new ReservationClient("simpleTrips", "manager");
		client.run();
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public ManagerClient(String scriptFile, String managerName) {
		super(scriptFile);
		try {
			System.setSecurityManager(null);

			Registry registry = LocateRegistry.getRegistry();
			manager = (IRentalManager) registry.lookup(managerName);
			session = (ManagerSession)manager.getNewSession("manager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check which car types are available in the given period and print this
	 * list of car types.
	 * 
	 * @param start
	 *            start time of the period
	 * @param end
	 *            end time of the period
	 * @throws Exception
	 *             if things go wrong, throw exception
	 */
	@Override
	protected void checkForAvailableCarTypes(Date start, Date end)
			throws Exception {
		/*List<String> types = session.getAvailableCarTypes(start, end);
		for (String type : types) {
			System.out.println(type);
		}*/
	}

	/**
	 * Retrieve a quote for a given car type (tentative reservation).
	 * 
	 * @param clientName
	 *            name of the client
	 * @param start
	 *            start time for the quote
	 * @param end
	 *            end time for the quote
	 * @param carType
	 *            type of car to be reserved
	 * @return the newly created quote
	 * 
	 * @throws Exception
	 *             if things go wrong, throw exception
	 */
	@Override
	protected Quote createQuote(String clientName, Date start, Date end,
			String carType) throws Exception {
		return null;//this.session.createQuote(new ReservationConstraints(start, end, carType));
	}

	/**
	 * Confirm the given quote to receive a final reservation of a car.
	 * 
	 * @param quote
	 *            the quote to be confirmed
	 * @return the final reservation of a car
	 * 
	 * @throws Exception
	 *             if things go wrong, throw exception
	 */
	@Override
	protected Reservation confirmQuote(Quote quote) throws Exception {
		//return this.session.confirmQuote(quote);
		return null;
	}

	/**
	 * Get all reservations made by the given client.
	 * 
	 * @param clientName
	 *            name of the client
	 * @return the list of reservations of the given client
	 * 
	 * @throws Exception
	 *             if things go wrong, throw exception
	 */
	@Override
	protected List<Reservation> getReservationsBy(String clientName)
			throws Exception {
		//return crc.getReservationsBy(clientName);
		return null;
	}

	/**
	 * Get the number of reservations for a particular car type.
	 * 
	 * @param carType
	 *            name of the car type
	 * @return number of reservations for the given car type
	 * 
	 * @throws Exception
	 *             if things go wrong, throw exception
	 */
	@Override
	protected int getNumberOfReservationsForCarType(String carType)
			throws Exception {
		return this.session.getNumberOfReservationsForCarType(carType);
	}
}
