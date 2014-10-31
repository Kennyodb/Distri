package client;

import java.util.Date;
import java.util.List;
import java.util.Set;

import rental.CarType;
import rental.IRentalManager;
import rental.ManagerSession;
import rental.Quote;
import rental.Reservation;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client<ReservationSession, ManagerSession> extends AbstractScriptedTripTest {

	private IRentalManager manager;

	/********
	 * MAIN *
	 ********/

	public static void main(String[] args) throws Exception {

		String carRentalCompanyName = "Hertz";

		// An example reservation scenario on car rental company 'Hertz' would
		// be...
		Client client = new Client("simpleTrips", "manager");
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

	/**
	 * returns the number of new reservation for the given client
	 * 
	 * @param name
	 * 			the clients name
	 * @return
	 * 			a list with new reservations
	 * @throws Exception
	 * 			client does not exist exception
	 */
	@Override
	protected Object getNewReservationSession(String name) throws Exception {
		// TODO Auto-generated method stub
		return manager.getNewSession("reservation");;
	}

	/**
	 * returns a new manager session
	 * 
	 * @param name
	 * 		the user's name (in this case the managers name)
	 * 
	 * @return
	 * 		a new manager session
	 * 
	 * @throws Exception
	 * 		if things go wrong....
	 */
	@Override
	protected Object getNewManagerSession(String name) throws Exception {
		// TODO Auto-generated method stub
		return manager.getNewSession("manager");;
	}

	/**
	 * prints all available car types for the period
	 * 
	 * @param session
	 * 		the current users active session
	 * 
	 * @param start
	 * 		date indicating the start of the period
	 * 
	 * @param end
	 * 		date indicating the end of the period
	 * 
	 * @throws Exception
	 * 		throws an exception if the session is not active
	 */
	@Override
	protected void checkForAvailableCarTypes(Object session, Date start,
			Date end) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * returns the cheapest car type available for the given period
	 * 
	 * @param session
	 * 		the current users active session
	 * 
	 * @param start
	 * 		date indicating the start of the period
	 * 
	 * @param end
	 * 		date indicating the end of the period
	 * 
	 * @return
	 * 		a string with the cheapest car type
	 * 
	 * @throws Exception
	 * 		throws an exception if the session is not active or if no car type is available
	 */
	@Override
	protected String getCheapestCarType(Object session, Date start, Date end)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * adds a new quote with the passed info to the session
	 * 
	 * @param session
	 * 		the current users active session
	 * 
	 * @param start
	 * 		date indicating the start of the period
	 * 
	 * @param end
	 * 		date indicating the end of the period
	 * 
	 * @param carType
	 * 		cartype for the quote
	 * 
	 * @param carRentalName
	 * 		the name of the car
	 * 
	 * @throws Exception
	 * 		throws exception if: session not active or if the quote isnt valid
	 */
	@Override
	protected void addQuoteToSession(Object session, Date start, Date end,
			String carType, String carRentalName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * confirms all quotes linked to the session
	 * 
	 * @param session
	 * 		the session for which the quotes should be confirmed
	 * 
	 * @return
	 * 		a list with the confirmed quotes
	 * 
	 * @throws Exception
	 * 		throws an exception if: the session is not active or
	 * 								not all quotes could be confirmed
	 */
	@Override
	protected List confirmQuotes(Object session) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * returns the number of reservations for a given client
	 * 
	 * @param ms
	 * 		the manager session
	 * 
	 * @param clientName
	 * 		the name of the client for which we want to get the number of reservations
	 * 
	 * @return
	 * 		the number of reservations for the given client
	 * 
	 * @throws Exception
	 * 		session not active
	 */
	@Override
	protected int getNumberOfReservationsBy(Object ms, String clientName)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * returns a list with the best clients
	 * 
	 * @param ms
	 * 		an active manager session
	 * 
	 * @return
	 * 		a set with the best clients
	 * 
	 * @throws Exception
	 * 		if session not active
	 */
	@Override
	protected Set getBestClients(Object ms) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * returns the number of reservations for a certain cartype
	 * 
	 * @param ms
	 * 		an active manager session
	 * 
	 * @param carRentalCompanyName
	 * 		a name for a carrentalcompany
	 * 
	 * @param carType
	 * 		the cartype for which we want to know the reservations
	 * 
	 * @return
	 * 		the number of reservations for the cartype at the given car rental company
	 * 
	 * @throws Exception
	 * 		session not active/cartype doesnt exist/carrentalcompany not found
	 */
	@Override
	protected int getNumberOfReservationsForCarType(Object ms,
			String carRentalCompanyName, String carType) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * returns the most popular car type
	 * 
	 * @param ms
	 * 		an active manager session
	 * 
	 * @param carRentalCompanyName
	 * 		a name for a carrentalcompany
	 * 
	 * @return
	 * 		the most popular car type at a certain carrental company
	 * 
	 * @throws Exception
	 * 		throws an exception if: the session is not active or the carrental company does not exist
	 */
	@Override
	protected CarType getMostPopularCarTypeIn(Object ms,
			String carRentalCompanyName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
