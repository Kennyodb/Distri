package session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

//@TransactionAttribute(NOT_SUPPORTED)
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
       HashSet<String> result = new HashSet<String>();
       for(CarRentalCompany crc : em.createQuery("SELECT crc FROM CarRentalCompany crc",
               CarRentalCompany.class).getResultList())
       {
           result.add(crc.getName());
       }
       return result;
    }
    
    @Override
    public List<CarType> getAvailableCarTypes(Date start, Date end) {
        // get a list with all cars
        Query qCars = em.createQuery("select car from Car car", Car.class);
        List<Car> carlist = qCars.getResultList();
        
        // get a list with all reservations that fall between the selected period
        Query qReservations = em.createQuery("select reservation from" + 
                            " Reservation reservation where " + 
                            "((:start >= reservation.startDate and :start <= reservation.endDate)" +
                            " or " +
                            "(:end >= reservation.startDate and :end <= reservation.endDate))",
                             Reservation.class);
        qReservations.setParameter("start", start);
        qReservations.setParameter("end", end);
        List<Reservation> reservationlist = qReservations.getResultList();
        
        List<Car> tCarList = new ArrayList();
        for(Reservation r : reservationlist)
        {
            for(Car c : carlist)
            {
                if(r.getCarId() == c.getId())
                {
                    tCarList.add(c);
                }
            }
        }
        for(Car c : tCarList)
            carlist.remove(c);
        
        // now carlist contains only available cars
        List<CarType> list = new ArrayList();
        for(Car c : carlist)
        {
            if(list.contains(c.getType()) == false)
                list.add(c.getType());
        }
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
                if(em.contains(car.getType()) == false)
                { em.persist(car.getType());}
                if(em.contains(car) == false)
                { em.persist(car);}
            }
            em.persist(c);
            em.flush();
           
        }
        
        CarRentalSession.persisted = true;
    }

    @Override
    public Quote createQuote(String company, ReservationConstraints constraints) throws ReservationException {
        this.persistRentalStore();
        
        try {
            Query crcQuery = em.createQuery("select c from CarRentalCompany c where c.name like :cname", CarRentalCompany.class);
            crcQuery.setParameter("cname", company);
            List<CarRentalCompany> crclist = crcQuery.getResultList();
            if(crclist.size() == 0)
                throw new ReservationException("CarRentalCompany " + company + " not found");
            CarRentalCompany crc = crclist.get(0);
            
            //Quote out = RentalStore.getRental(company).createQuote(constraints, renter);
            Quote out = crc.createQuote(constraints, renter);
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

    //@TransactionAttribute(REQUIRES_NEW)
    @Override
    //@TransactionManagement(TransactionManagementType.CONTAINER) 
    //@Transactional
    public List<Reservation> confirmQuotes() throws ReservationException {
        List<Reservation> done = new LinkedList<Reservation>();
        //EntityTransaction trans = em.getTransaction();
        try {
        //userTrans.begin();
            for (Quote quote : quotes) {
                //done.add(RentalStore.getRental(quote.getRentalCompany()).confirmQuote(quote));
                Query crcQ = em.createQuery("select c from CarRentalCompany c where c.name like :cname", CarRentalCompany.class);
                crcQ.setParameter("cname", quote.getRentalCompany());
                List<CarRentalCompany> crclist = crcQ.getResultList();
                if(crclist.isEmpty())
                    throw new ReservationException("Confirm quotes failed, cannot find carrentalcompany "+
                                quote.getRentalCompany());
                CarRentalCompany crc = crclist.get(0);
                em.persist(crc.confirmQuote(quote));
                //done.add(crc.confirmQuote(quote));
            }
            //userTrans.commit();
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