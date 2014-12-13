package ds.gae.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ds.gae.EMF;
import ds.gae.entities.Car;
import ds.gae.entities.CarRentalCompany;
import ds.gae.entities.CarType;
import ds.gae.entities.Quote;
import ds.gae.entities.Reservation;

public class CarRentalServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// This will be invoked as part of a warming request,
		// or the first user request if no warming request was invoked.

		// check if dummy data is available, and add if necessary
		if (!isDummyDataAvailable()) {
			addDummyData();
		}
	}

	private boolean isDummyDataAvailable() {
		// If the Hertz car rental company is in the datastore, we assume the
		// dummy data is available
		return EMF.get().createEntityManager()
				.find(CarRentalCompany.class, "Hertz") != null;
	}

	private void addDummyData() {
		loadRental("Hertz", "hertz.csv");
		loadRental("Dockx", "dockx.csv");
		
		//TODO Delete everything below this notification
		Quote quote = new Quote("Kenny", new Date(), new Date(), "Hertz",
			"Compact", 1000);
		log("quote created");
		
		EntityManager em = EMF.get().createEntityManager();
		CarRentalCompany crc = em.find(CarRentalCompany.class, "Hertz");
		/*CarType type = em.find(CarType.class, "Compact");
		TypedQuery<Car> qCar = EMF.get().createEntityManager().createQuery(
				"SELECT c FROM CarRentalCompany crc, Car c WHERE crc = :crcparam AND c.type = :ct",
				Car.class);
		qCar.setParameter("crcparam", crc);
		qCar.setParameter("ct", type);*/
		for(Car car : crc.getCars())
		{
			if(car.getType().getName().equalsIgnoreCase("compact"))
			{
				log("Found car: " + car.getType());
				Reservation res = new Reservation(quote, car);
				car.addReservation(res);
				log("Reservation placed");
			}
		}		
	}
	
	private void log(String s) {
		Logger.getLogger(CarRentalServletContextListener.class.getName()).log(
				Level.INFO, "********* " + s);
	}
	
	//Until HERE

	private void loadRental(String name, String datafile) {
		Logger.getLogger(CarRentalServletContextListener.class.getName()).log(
				Level.INFO, "loading {0} from file {1}",
				new Object[] { name, datafile });
		try {

			Set<Car> cars = loadData(name, datafile);
			CarRentalCompany company = new CarRentalCompany(name, cars);

			EntityManager em = EMF.get().createEntityManager();
			try {
				em.persist(company);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				em.close();
			}

		} catch (NumberFormatException ex) {
			Logger.getLogger(CarRentalServletContextListener.class.getName())
					.log(Level.SEVERE, "bad file", ex);
		} catch (IOException ex) {
			Logger.getLogger(CarRentalServletContextListener.class.getName())
					.log(Level.SEVERE, null, ex);
		}
	}

	public static Set<Car> loadData(String name, String datafile)
			throws NumberFormatException, IOException {

		Set<Car> cars = new HashSet<Car>();
		int carId = 1;

		// open file from jar
		BufferedReader in = new BufferedReader(new InputStreamReader(
				CarRentalServletContextListener.class.getClassLoader()
						.getResourceAsStream(datafile)));
		// while next line exists
		while (in.ready()) {
			// read line
			String line = in.readLine();
			// if comment: skip
			if (line.startsWith("#")) {
				continue;
			}
			// tokenize on ,
			StringTokenizer csvReader = new StringTokenizer(line, ",");
			// create new car type from first 5 fields
			CarType type = new CarType(csvReader.nextToken(),
					Integer.parseInt(csvReader.nextToken()),
					Float.parseFloat(csvReader.nextToken()),
					Double.parseDouble(csvReader.nextToken()),
					Boolean.parseBoolean(csvReader.nextToken()));
			// create N new cars with given type, where N is the 5th field
			for (int i = Integer.parseInt(csvReader.nextToken()); i > 0; i--) {
				cars.add(new Car(carId++, type));
			}
		}
		/*
		 * Set<Car> cars = null;
		 * 
		 * Query query = EMF.get().createEntityManager()
		 * .createQuery("select crc from CarRentalCompany where crc.name = :crcname"
		 * , CarRentalCompany.class); query.setParameter("crcname", name);
		 * List<CarRentalCompany> rlist = query.getResultList();
		 * if(rlist.size()>0) { CarRentalCompany company = rlist.get(0); cars =
		 * company.getCars();
		 * 
		 * }
		 */
		return cars;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// App Engine does not currently invoke this method.
	}
}