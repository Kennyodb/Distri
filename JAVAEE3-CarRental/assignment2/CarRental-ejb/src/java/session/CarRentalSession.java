package session;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

@Stateful
public class CarRentalSession implements CarRentalSessionRemote {

    @PersistenceContext
    private EntityManager em;
    
    private static boolean persisted = false;
    
    private String renter;
    private List<Quote> quotes = new LinkedList<Quote>();


    @Override
    public Set<String> getAllRentalCompanies() {
       // return new HashSet<String>(RentalStore.getRentals().keySet());
       HashSet<String> toreturn = new HashSet<String>();
       for(Object c : em.createQuery("SELECT crc FROM CarRentalCompany crc").getResultList())
       {
           toreturn.add(((CarRentalCompany)c).getName());
       }
       return toreturn;
    }
    
    @Override
    public List<CarType> getAvailableCarTypes(Date start, Date end) {
        /*List<CarType> availableCarTypes = new LinkedList<CarType>();
        for(String crc : getAllRentalCompanies()) {
            for(Object c :  em.createQuery("SELECT crc FROM CarRentalCompany crc").getResultList())
            {
               for(CarType ct : ((CarRentalCompany)c).getAvailableCarTypes(start, end)) 
               {
                if(!availableCarTypes.contains(ct) && ct != null)
                    availableCarTypes.add(ct);
               }
            }
        }
        return availableCarTypes;*/
        
       /* Query q = em.createQuery("select cartype from CarType where exists ("
                + "select distinct car.carTypeId from \n" +
"Car  car left join Reservation reservation \n" +
"inner join Quote quote on reservation.reservationId = quote.reservationId\n" +
"on car.id = reservation.carId \n" +
"where not exists\n" +
"( select reservationquote from\n" +
"Car left join Reservation \n" +
"inner join Quote on Reservation.reservationId = Quote.reservationId\n" +
"on Car.id = Reservation.carId \n" +
"where (:startDate > Quote.startDate and :startDate < Quote.endDate)\n" +
"or (:endDate > Quote.startDate and :endDate < Quote.endDate)\n" +
"))", CarType.class);*/
       /* Query q = em.createQuery("select cartype from CarType cartype where exists ("
                + "select distinct car.type.carTypeId from \n" +
"Car  car left join Reservation reservation \n" +
"on car.id = reservation.carId \n" +
"where not exists\n" +
"( select reservation from\n" +
"Car  car left join Reservation reservation \n" +
"on car.id = reservation.carId \n" +
"where (:startDate > reservation.startDate and :startDate < reservation.endDate)\n" +
"or (:endDate > reservation.startDate and :endDate < reservation.endDate)\n" +
"))", CarType.class);*/
         Query q = em.createQuery("select cartype from CarType cartype where exists ("
                + "select car from \n" +
"Car  car" +
")", CarType.class);
       // q.setParameter("startDate", start);
       // q.setParameter("endDate", end);
        List<CarType> list = q.getResultList();
        return list;
    }
    
    public void persistRentalStore()
    {
        if(CarRentalSession.persisted == true)
            return;
        Collection<CarRentalCompany> stores = RentalStore.getRentals().values();
        for(CarRentalCompany c : stores)
        {
            for(Car car : c.getCars())
            {
                System.out.println("Car " + car + " has id " + car.getId());
                System.out.println("Car.cartypeid = " + car.getType().getCarTypeId() + " cartype address " + car.getType());
                if(em.contains(car.getType()) == false)
                { em.persist(car.getType());}
                else
                    System.out.println("cartype already in DB");
                if(em.contains(car) == false)
                { em.persist(car);}
                else
                    System.out.println("car already in DB");
            }
            em.persist(c);
            em.flush();
           
        }
        
        System.out.println("PERSISTED");
        
        List<CarRentalCompany> li = em.createQuery("select a from CarRentalCompany a").getResultList();
        
        System.out.println("number of CRCS: " + li.size() + "%%%%%%%%%%%%%%");
        
        for(CarRentalCompany cc : li)
            System.out.println(cc.toString());
        
        CarRentalSession.persisted = true;
    }

    @Override
    public Quote createQuote(String company, ReservationConstraints constraints) throws ReservationException {
        this.persistRentalStore();
        
        try {
            Quote out = RentalStore.getRental(company).createQuote(constraints, renter);
            quotes.add(out);
            return out;
        } catch(Exception e) {
            throw new ReservationException(e);
        }
    }

    @Override
    public List<Quote> getCurrentQuotes() {
        return quotes;
    }

    @Override
    public List<Reservation> confirmQuotes() throws ReservationException {
        List<Reservation> done = new LinkedList<Reservation>();
        try {
            for (Quote quote : quotes) {
                done.add(RentalStore.getRental(quote.getRentalCompany()).confirmQuote(quote));
            }
        } catch (Exception e) {
            for(Reservation r:done)
                RentalStore.getRental(r.getRentalCompany()).cancelReservation(r);
            throw new ReservationException(e);
        }
        return done;
    }

    @Override
    public void setRenterName(String name) {
        if (renter != null) {
            throw new IllegalStateException("name already set");
        }
        renter = name;
    }
}