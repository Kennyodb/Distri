package ds.gae;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ds.gae.entities.Car;
import ds.gae.entities.CarRentalCompany;
import ds.gae.entities.CarType;
import ds.gae.entities.Quote;
import ds.gae.entities.Reservation;
import ds.gae.entities.ReservationConstraints;

public class CarRentalModel {

	public Map<String, CarRentalCompany> CRCS = new HashMap<String, CarRentalCompany>();

	private static CarRentalModel instance;

	public static CarRentalModel get() {
		if (instance == null)
			instance = new CarRentalModel();
		return instance;
	}

	/**
	 * Get the car types available in the given car rental company.
	 * 
	 * @param crcName
	 *            the car rental company
	 * @return The list of car types (i.e. name of car type), available in the
	 *         given car rental company.
	 */
	public Set<String> getCarTypesNames(String crcName) {
		TypedQuery<String> query = EMF.get().createEntityManager()
				.createQuery("select ct.name from CarType ct", String.class);
		return new HashSet<String>(query.getResultList());
	}

	/**
	 * Get all registered car rental companies
	 * 
	 * @return the list of car rental companies
	 */
	public Collection<String> getAllRentalCompanyNames() {
		TypedQuery<String> query = EMF
				.get()
				.createEntityManager()
				.createQuery("select crc.name from CarRentalCompany crc",
						String.class);
		return new HashSet<String>(query.getResultList());
	}

	/**
	 * Create a quote according to the given reservation constraints (tentative
	 * reservation).
	 * 
	 * @param company
	 *            name of the car renter company
	 * @param renterName
	 *            name of the car renter
	 * @param constraints
	 *            reservation constraints for the quote
	 * @return The newly created quote.
	 * 
	 * @throws ReservationException
	 *             No car available that fits the given constraints.
	 */
	public Quote createQuote(String company, String renterName,
			ReservationConstraints cts) throws ReservationException {
		CarRentalCompany crc = EMF.get().createEntityManager()
				.find(CarRentalCompany.class, company);
		Query query = EMF.get().createEntityManager().createQuery(
				"SELECT c FROM Car c WHERE c MEMBER OF :crc.cars AND c.type = :ct " +
				"AND NOT EXISTS (SELECT r FROM Reservation r WHERE r.car = c AND " +
				"(r.startDate BETWEEN :start AND :end) " +
				" OR (r.endDate BETWEEN :start AND :end))");
		query.setParameter("crc", crc);
		query.setParameter("ct", cts.getCarType());
		query.setParameter("start", cts.getStartDate());
		query.setParameter("end", cts.getEndDate());
		if(query.getResultList().isEmpty()) {
			throw new ReservationException("CarType is no longer available.");
		}
		return new Quote(renterName, cts.getStartDate(),
				cts.getEndDate(), company, cts.getCarType(), 0); //TODO calculate rentalprice
	}

	/**
	 * Confirm the given quote.
	 * 
	 * @param q
	 *            Quote to confirm
	 * 
	 * @throws ReservationException
	 *             Confirmation of given quote failed.
	 */
	public void confirmQuote(Quote q) throws ReservationException {
		EMF.get().createEntityManager()
				.find(CarRentalCompany.class, q.getRentalCompany())
				.confirmQuote(q);
	}

	/**
	 * Confirm the given list of quotes
	 * 
	 * @param quotes
	 *            the quotes to confirm
	 * @return The list of reservations, resulting from confirming all given
	 *         quotes.
	 * 
	 * @throws ReservationException
	 *             One of the quotes cannot be confirmed. Therefore none of the
	 *             given quotes is confirmed.
	 */
	public List<Reservation> confirmQuotes(List<Quote> quotes)
			throws ReservationException {

		EntityManager em = EMF.get().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		for (Quote q : quotes) {
			CarRentalCompany crc = em.find(CarRentalCompany.class,
					q.getRentalCompany());
			try {
				crc.confirmQuote(q);
				em.persist(crc);
			} catch (ReservationException r) {
				r.printStackTrace();
				tx.rollback();
				em.close();
			}
		}
		tx.commit();

		List<Reservation> result = new ArrayList<>();

		for (Quote q : quotes) {
			Query query = em
					.createQuery(
							"select r from reservation where r.carType = :ct and r.startDate = :sd",
							Reservation.class);
			query.setParameter("ct", q.getCarType());
			query.setParameter("sd", q.getStartDate());
			result.add((Reservation) query.getResultList().get(0));
		}

		em.close();

		return result;
	}

	/**
	 * Get all reservations made by the given car renter.
	 * 
	 * @param renter
	 *            name of the car renter
	 * @return the list of reservations of the given car renter
	 */
	public List<Reservation> getReservations(String renter) {
		TypedQuery<Reservation> query = EMF
				.get()
				.createEntityManager()
				.createQuery(
						""
								+ "SELECT r FROM Reservation r WHERE r.carRenter = :renter",
						Reservation.class);
		query.setParameter("renter", renter);
		return query.getResultList();
	}

	/**
	 * Get the car types available in the given car rental company.
	 * 
	 * @param crcName
	 *            the given car rental company
	 * @return The list of car types in the given car rental company.
	 */
	public Collection<CarType> getCarTypesOfCarRentalCompany(String crcName) {
		TypedQuery<CarType> query = EMF
				.get()
				.createEntityManager()
				.createQuery(
						"SELECT ct FROM CarType ct WHERE EXISTS "
								+ "(SELECT c FROM Car c WHERE c MEMBER OF :crc.cars AND c.type = ct)",
						CarType.class);
		query.setParameter("crc", crcName);
		return query.getResultList();
	}

	/**
	 * Get the list of cars of the given car type in the given car rental
	 * company.
	 * 
	 * @param crcName
	 *            name of the car rental company
	 * @param carType
	 *            the given car type
	 * @return A list of car IDs of cars with the given car type.
	 */
	public Collection<Integer> getCarIdsByCarType(String crcName,
			CarType carType) {
		Collection<Integer> out = new ArrayList<Integer>();
		for (Car c : getCarsByCarType(crcName, carType)) {
			out.add(c.getId());
		}
		return out;
	}

	/**
	 * Get the amount of cars of the given car type in the given car rental
	 * company.
	 * 
	 * @param crcName
	 *            name of the car rental company
	 * @param carType
	 *            the given car type
	 * @return A number, representing the amount of cars of the given car type.
	 */
	public int getAmountOfCarsByCarType(String crcName, CarType carType) {
		return this.getCarsByCarType(crcName, carType).size();
	}

	/**
	 * Get the list of cars of the given car type in the given car rental
	 * company.
	 * 
	 * @param crcName
	 *            name of the car rental company
	 * @param carType
	 *            the given car type
	 * @return List of cars of the given car type
	 */
	private List<Car> getCarsByCarType(String crcName, CarType carType) {
		EntityManager em = EMF.get().createEntityManager();
		CarRentalCompany crc = em.find(CarRentalCompany.class, crcName);
		TypedQuery<Car> query = em
				.createQuery(
						"SELECT c FROM Car c WHERE c MEMBER OF :crc.cars AND c.type = :ct",
						Car.class);
		query.setParameter("crc", crc);
		query.setParameter("ct", carType);
		return query.getResultList();
	}

	/**
	 * Check whether the given car renter has reservations.
	 * 
	 * @param renter
	 *            the car renter
	 * @return True if the number of reservations of the given car renter is
	 *         higher than 0. False otherwise.
	 */
	public boolean hasReservations(String renter) {
		return !this.getReservations(renter).isEmpty();
	}
}