package ds.gae.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.persistence.Query;

import ds.gae.CarRentalModel;
import ds.gae.EMF;
import ds.gae.entities.Car;
import ds.gae.entities.CarRentalCompany;
import ds.gae.entities.CarType;

public class CarRentalServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// This will be invoked as part of a warming request, 
		// or the first user request if no warming request was invoked.
						
		// check if dummy data is available, and add if necessary
		if(!isDummyDataAvailable()) {
			addDummyData();
		}
	}
	
	private boolean isDummyDataAvailable() {
		// If the Hertz car rental company is in the datastore, we assume the dummy data is available
		//  FIXME generates the following error: FROM clause of query has class ds.gae.entities.CarRentalCompany but no alias
		/*Query query = EMF.get().createEntityManager()
						.createQuery("select crc from CarRentalCompany where crc.name like :crcname");
		query.setParameter("crcname", "Hertz");
		return query.getResultList().size() > 0;*/return false;

	}
	
	private void addDummyData() {
		loadRental("Hertz","hertz.csv");
        loadRental("Dockx","dockx.csv");
	}
	
	private void loadRental(String name, String datafile) {
		Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.INFO, "loading {0} from file {1}", new Object[]{name, datafile});
        try {
        	
            Set<Car> cars = loadData(name, datafile);
            CarRentalCompany company = new CarRentalCompany(name, cars);
            
    		// FIXME: use persistence instead
            CarRentalModel.get().CRCS.put(name, company);
            /*EntityManager em = EMF.get().createEntityManager();
            try
            {
            	em.persist(company);
            }
            finally
            {
            	em.close();
            }*/
            

        } catch (NumberFormatException ex) {
            Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.SEVERE, "bad file", ex);
        } catch (IOException ex) {
            Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public static Set<Car> loadData(String name, String datafile) throws NumberFormatException, IOException {
		
		Set<Car> cars = new HashSet<Car>();
		int carId = 1;

		//open file from jar
		BufferedReader in = new BufferedReader(new InputStreamReader(CarRentalServletContextListener.class.getClassLoader().getResourceAsStream(datafile)));
		//while next line exists
		while (in.ready()) {
			//read line
			String line = in.readLine();
			//if comment: skip
			if (line.startsWith("#")) {
				continue;
			}
			//tokenize on ,
			StringTokenizer csvReader = new StringTokenizer(line, ",");
			//create new car type from first 5 fields
			CarType type = new CarType(csvReader.nextToken(),
					Integer.parseInt(csvReader.nextToken()),
					Float.parseFloat(csvReader.nextToken()),
					Double.parseDouble(csvReader.nextToken()),
					Boolean.parseBoolean(csvReader.nextToken()));
			//create N new cars with given type, where N is the 5th field
			for (int i = Integer.parseInt(csvReader.nextToken()); i > 0; i--) {
				cars.add(new Car(carId++, type));
			}
		}
		/*
		Set<Car> cars = null;
		
		Query query = EMF.get().createEntityManager()
				.createQuery("select crc from CarRentalCompany where crc.name = :crcname",
								CarRentalCompany.class);
		query.setParameter("crcname", name);
		List<CarRentalCompany> rlist = query.getResultList();
		if(rlist.size()>0)
		{
			CarRentalCompany company = rlist.get(0);
			cars = company.getCars();
			
		}*/
		return cars;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// App Engine does not currently invoke this method.
	}
}