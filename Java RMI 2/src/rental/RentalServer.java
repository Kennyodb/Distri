package rental;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RentalServer {

	public static void main(String[] args) throws ReservationException,
			NumberFormatException, IOException {
		System.setSecurityManager(null);
		
		
		List<Car> cars = loadData("hertz.csv");
		CarRentalCompany company1 = new CarRentalCompany("Hertz1", cars);
		cars = loadData("hertz.csv");
		CarRentalCompany company2 = new CarRentalCompany("Hertz2", cars);
		cars = loadData("hertz.csv");
		CarRentalCompany company3 = new CarRentalCompany("Hertz3", cars);
		
		RentalManager manager = new RentalManager();
		
		manager.registerCompany(company1);
		manager.registerCompany(company2);
		manager.registerCompany(company3);
		
		try {
			IRentalManager stub = (IRentalManager) UnicastRemoteObject
					.exportObject(manager, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("Manager", stub);
			System.out.println("Manager bound...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Car> loadData(String datafile)
			throws ReservationException, NumberFormatException, IOException {

		List<Car> cars = new LinkedList<Car>();

		int nextuid = 0;

		// open file
		BufferedReader in = new BufferedReader(new FileReader(datafile));
		// while next line exists
		while (in.ready()) {
			// read line
			String line = in.readLine();
			// if comment: skip
			if (line.startsWith("#"))
				continue;
			// tokenize on ,
			StringTokenizer csvReader = new StringTokenizer(line, ",");
			// create new car type from first 5 fields
			CarType type = new CarType(csvReader.nextToken(),
					Integer.parseInt(csvReader.nextToken()),
					Float.parseFloat(csvReader.nextToken()),
					Double.parseDouble(csvReader.nextToken()),
					Boolean.parseBoolean(csvReader.nextToken()));
			System.out.println(type);
			// create N new cars with given type, where N is the 5th field
			for (int i = Integer.parseInt(csvReader.nextToken()); i > 0; i--) {
				cars.add(new Car(nextuid++, type));
			}
		}
		in.close();

		return cars;
	}
}
