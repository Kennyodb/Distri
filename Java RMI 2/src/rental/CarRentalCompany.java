package rental;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarRentalCompany implements Serializable {

	private static final long serialVersionUID = -5594330145422239039L;

	private static Logger logger = Logger.getLogger(CarRentalCompany.class
			.getName());

	private String name;
	private List<Car> cars;
	private Map<String, CarType> carTypes = new HashMap<String, CarType>();

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public CarRentalCompany(String name, List<Car> cars) {
		logger.log(Level.INFO, "<{0}> Car Rental Company {0} starting up...",
				name);
		setName(name);
		this.cars = cars;
		for (Car car : cars)
			carTypes.put(car.getType().getName(), car.getType());
	}

	/********
	 * NAME *
	 ********/

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	/*************
	 * CAR TYPES *
	 *************/

	public Collection<CarType> getAllCarTypes() {
		return carTypes.values();
	}

	public CarType getCarType(String carTypeName) {
		if (carTypes.containsKey(carTypeName))
			return carTypes.get(carTypeName);
		throw new IllegalArgumentException("<" + carTypeName
				+ "> No car type of name " + carTypeName);
	}

	public boolean isAvailable(String carTypeName, Date start, Date end) {
		logger.log(Level.INFO, "<{0}> Checking availability for car type {1}",
				new Object[] { name, carTypeName });
		if (carTypes.containsKey(carTypeName))
			return getAvailableCarTypes(start, end).contains(
					carTypes.get(carTypeName));
		throw new IllegalArgumentException("<" + carTypeName
				+ "> No car type of name " + carTypeName);
	}

	public Set<CarType> getAvailableCarTypes(Date start, Date end) {
		Set<CarType> availableCarTypes = new HashSet<CarType>();
		for (Car car : cars) {
			if (car.isAvailable(start, end)) {
				availableCarTypes.add(car.getType());
			}
		}
		return availableCarTypes;
	}

	public int getNumberOfReservationsForCarType(String carType) {
		int nb = 0;
		for (Car car : cars) {
			for (Reservation res : car.getReservations()) {
				if (res.getCarType().equals(carType)) {
					nb++;
				}
			}
		}
		return nb;
	}

	/*********
	 * CARS *
	 *********/

	private Car getCar(int uid) {
		for (Car car : cars) {
			if (car.getId() == uid)
				return car;
		}
		throw new IllegalArgumentException("<" + name + "> No car with uid "
				+ uid);
	}

	private List<Car> getAvailableCars(String carType, Date start, Date end) {
		List<Car> availableCars = new LinkedList<Car>();
		for (Car car : cars) {
			if (car.getType().getName().equals(carType)
					&& car.isAvailable(start, end)) {
				availableCars.add(car);
			}
		}
		return availableCars;
	}

	/****************
	 * RESERVATIONS *
	 ****************/

	public Quote createQuote(ReservationConstraints constraints, String client)
			throws ReservationException {
		logger.log(
				Level.INFO,
				"<{0}> Creating tentative reservation for {1} with constraints {2}",
				new Object[] { name, client, constraints.toString() });

		CarType type = getCarType(constraints.getCarType());

		if (!isAvailable(constraints.getCarType(), constraints.getStartDate(),
				constraints.getEndDate()))
			throw new ReservationException("<" + name
					+ "> No cars available to satisfy the given constraints.");

		double price = calculateRentalPrice(type.getRentalPricePerDay(),
				constraints.getStartDate(), constraints.getEndDate());

		return new Quote(client, constraints.getStartDate(),
				constraints.getEndDate(), getName(), constraints.getCarType(),
				price);
	}

	// Implementation can be subject to different pricing strategies
	private double calculateRentalPrice(double rentalPricePerDay, Date start,
			Date end) {
		return rentalPricePerDay
				* Math.ceil((end.getTime() - start.getTime())
						/ (1000 * 60 * 60 * 24D));
	}

	public synchronized Reservation confirmQuote(Quote quote)
			throws ReservationException {
		logger.log(Level.INFO, "<{0}> Reservation of {1}", new Object[] { name,
				quote.toString() });
		List<Car> availableCars = getAvailableCars(quote.getCarType(),
				quote.getStartDate(), quote.getEndDate());
		if (availableCars.isEmpty())
			throw new ReservationException(
					"Reservation failed, all cars of type "
							+ quote.getCarType() + " are unavailable from "
							+ quote.getStartDate() + " to "
							+ quote.getEndDate());
		Car car = availableCars
				.get((int) (Math.random() * availableCars.size()));

		Reservation res = new Reservation(quote, car.getId());
		car.addReservation(res);
		return res;
	}

	public void cancelReservation(Reservation res) {
		logger.log(Level.INFO, "<{0}> Cancelling reservation {1}",
				new Object[] { name, res.toString() });
		getCar(res.getCarId()).removeReservation(res);
	}

	public List<Reservation> getReservationsBy(String client) {
		List<Reservation> list = new ArrayList<>();
		for (Car car : cars) {
			for (Reservation res : car.getReservations()) {
				if (res.getCarRenter().equals(client)) {
					list.add(res);
				}
			}
		}
		return list;
	}

	public String getTopCustomer() { // inefficient
		List<String> list = new ArrayList<>();
		for (Car car : cars) {
			for (Reservation res : car.getReservations()) {
				list.add(res.getCarRenter());
			}
		}

		String result = null;
		int nbReservations = -1;
		for (String client : list) {
			int frequency = Collections.frequency(list, client);
			if (frequency > nbReservations) {
				result = client;
				nbReservations = frequency;
			}
		}
		return result;
	}

	public List<CarType> getFreeCarTypes(Date from, Date end)
	/* throws RemoteException */{
		return new ArrayList<>(this.getAvailableCarTypes(from, end));
	}

	/*************
	 * TO STRING *
	 *************/
	private String carsAsString() {
		String toret = "";

		for (Car c : this.cars)
			toret += c.toString() + ", ";

		if (this.cars.size() > 0)
			toret = toret.substring(0, toret.length() - 2);

		return toret;
	}

	private String carTypesAsString() {
		String toret = "";

		for (CarType t : this.getAllCarTypes())
			toret += t.toString() + ", ";

		if (this.getAllCarTypes().size() > 0)
			toret = toret.substring(0, toret.length() - 2);

		return toret;
	}

	@Override
	public String toString() {
		return String
				.format("Car Rental Company: %s\n\nsupported cartypes\n----------------\n%s\n\navailable cars\n--------------\n%s",
						this.name, this.carTypesAsString(), this.carsAsString());
	}

	/***************
	 * HASHCODE *
	 **************/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/***************
	 * EQUALS *
	 **************/
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarRentalCompany other = (CarRentalCompany) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
