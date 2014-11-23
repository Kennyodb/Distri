package session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Reservation;

@Stateless
public class ManagerSession implements ManagerSessionRemote {

    @PersistenceContext
    private EntityManager em;
    
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
    public Set<CarType> getCarTypes(String company) {

       // em.find(CarRentalCompany.class, company);
        Query query = em.createQuery("SELECT DISTINCT ct " +
            "FROM CarType ct, CarRentalCompany crc " +
            "WHERE ct MEMBER OF crc.carTypes AND crc.name = :company",
                CarType.class);
       query.setParameter("company", company);
       return new HashSet<CarType>(query.getResultList());
    }
    
    @Override
    public int getNumberOfReservations(String company, String carType) {
        Query query = em.createQuery("SELECT res FROM Reservation res "
                + "WHERE res.carType = :carType AND res.rentalCompany = :company");
        query.setParameter("carType", carType);
        query.setParameter("company", company);
        return query.getResultList().size();
    }

    @Override
    public int getNumberOfReservationsBy(String renter) {
        Query query = em.createQuery("SELECT res FROM Reservation res "
                + "WHERE res.carRenter = :renter");
        query.setParameter("renter", renter);
        return query.getResultList().size();
    }

    @Override   
    public CarType getMostPopularCarTypeIn(String company) {
        
        Query resQuery = em.createQuery("select res from Reservation res where" +
                " res.rentalCompany like :cname", Reservation.class);
        resQuery.setParameter("cname", company);
        List<Reservation> resList = resQuery.getResultList();
        List<String> types = new ArrayList();
        for(Reservation r : resList)
        {
            if(!types.contains(r.getCarType()))
                types.add(r.getCarType());
        }
        int mpCount = 0;
        String mp = "";
        for(String type : types)
        {
            int currentCount = 0;
            for(Reservation r : resList)
            {
                if(r.getCarType().equals(type))
                    currentCount++;
            }
            if(currentCount > mpCount)
            {
                mpCount = currentCount;
                mp = type;
            }
        }
        
        Query ctQuery = em.createQuery("select ct from CarType ct where ct.name like :ctname", CarType.class);
        ctQuery.setParameter("ctname", mp);
        List<CarType> ctlist = ctQuery.getResultList();
        if(ctlist.isEmpty())
            throw new IllegalArgumentException("Cartype " + mp + " cannot be found");
        return ctlist.get(0);
    }

    @Override
    public Set<String> getBestClients() {
        Set<String> result = new HashSet<String>();
        for(String company : this.getAllRentalCompanies()) {
            result.add(this.getBestClient(company));
        }
        
        return result;
    }
    
    public String getBestClient(String company) {
        Query bestClientQuery = em.createQuery(
                "SELECT r.carRenter AS name, COUNT(r) AS total FROM Reservation r"+
                        " WHERE r.rentalCompany like :cname " +
                        " GROUP BY name ORDER BY total ASC");
        bestClientQuery.setParameter("cname", company);
        List<Object[]> results = bestClientQuery.getResultList();
        int bcCount = 0;
        String bestClient = "";
        if(results.isEmpty())
            throw new IllegalArgumentException("no clients found");
        
        for (Object[] result : results) 
        {
            String name = (String) result[0];
            int count = ((Number) result[1]).intValue();
            if(count > bcCount)
            {
                bcCount = count;
                bestClient = name;
            }
        }
        
        return bestClient;
    }

    /*  @Override
    public Set<Integer> getCarIds(String company, String type) {
        Set<Integer> out = new HashSet<Integer>();
        try {
            for(Car c: RentalStore.getRental(company).getCars(type)){
                out.add(c.getId());
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ManagerSession.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return out;
    }

   @Override
    public int getNumberOfReservations(String company, String type, int id) {
        try {
            return RentalStore.getRental(company).getCar(id).getReservations().size();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ManagerSession.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }*/

}