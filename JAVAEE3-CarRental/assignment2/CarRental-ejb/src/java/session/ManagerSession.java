package session;

import java.util.HashSet;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import rental.CarRentalCompany;
import rental.CarType;

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
        query.setParameter("cartype", carType);
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
        Query query = em.createQuery(
                "SELECT res.carType, COUNT(res.carType) " +
                "FROM Reservation res " +
                "WHERE res.rentalCompany = :company " +
                "GROUP BY res.carType " +
                "ORDER BY COUNT(res.carType) DESC");
        query.setParameter("company", company);
        query.setMaxResults(1);
        return (CarType) ((Object[]) query.getSingleResult())[0];
        
    }

    @Override
    public Set<String> getBestClients() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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