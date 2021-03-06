package rental;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.EntityType;

@Stateful
public class RentalStore {
    
    //@PersistenceContext
    private static EntityManager em;

    private static Map<String, CarRentalCompany> rentals;
    
    private static EntityManager getEM()
    {
        if(RentalStore.em == null)
        {
          
        EntityManagerFactory f = Persistence.createEntityManagerFactory("CarRental-ejbPU");
        em = f.createEntityManager();
        System.out.println("EM CREATED **********************************");  
        
        
        Set<EntityType<?>> li = em.getMetamodel().getEntities();
        System.out.println("%%% number of enities supported " + li.size());
        for(EntityType<?> et : li)
        {
            System.out.println("Entity " + et.getName() + " %%%%%%");
        }
        }
        
        return RentalStore.em;
    }

    public static CarRentalCompany getRental(String company) throws IllegalArgumentException {
        CarRentalCompany out = RentalStore.getRentals().get(company);
        if (out == null) {
            throw new IllegalArgumentException("Company doesn't exist!: " + company);
        }
        return out;
    }

    public static synchronized Map<String, CarRentalCompany> getRentals() {
        if (rentals == null) {
            rentals = new HashMap<String, CarRentalCompany>();
            loadRental("Hertz", "hertz.csv");
            loadRental("Dockx", "dockx.csv");
        }
        return rentals;
    }

    public static void loadRental(String name, String datafile) {
        Logger.getLogger(RentalStore.class.getName()).log(Level.INFO, "loading {0} from file {1}", new Object[]{name, datafile});
        try {
            List<Car> cars = loadData(datafile);
            CarRentalCompany company = new CarRentalCompany(name, cars);
            rentals.put(name, company);
          /* if(company != null)
                RentalStore.getEM().persist(company);*/
        } catch (NumberFormatException ex) {
            Logger.getLogger(RentalStore.class.getName()).log(Level.SEVERE, "bad file", ex);
        } catch (IOException ex) {
            Logger.getLogger(RentalStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<Car> loadData(String datafile)
            throws NumberFormatException, IOException {

        List<Car> cars = new LinkedList<Car>();

        int nextuid = 0;

        //open file from jar
        BufferedReader in = new BufferedReader(new InputStreamReader(RentalStore.class.getClassLoader().getResourceAsStream(datafile)));
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
                cars.add(new Car(nextuid++, type));
            }
        }

        return cars;
    }
}