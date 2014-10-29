package rental;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class RentalManager implements IRentalManager
{
	Dictionary<String, CarRentalCompany> companies;
	Dictionary<Integer, List<Quote>> sessionQuotes;
	
	public RentalManager()
	{
		this.companies = new Hashtable<String, CarRentalCompany>();
		this.sessionQuotes = new Hashtable<Integer, List<Quote>>();
	}
	
	@Override
	public Session getNewSession(String type) {
		// TODO Auto-generated method stub
		if(type.equalsIgnoreCase("manager") == true)
			return new ManagerSession(this);
		
		if(type.equalsIgnoreCase("reservation") == true)
			return new ReservationSession(this);
		
		return null;
	}

	@Override
	public List<String> getAvailableCarTypes() {
		List<String> toreturn = new ArrayList<String>();
		
		while(this.companies.elements().hasMoreElements())
		{
			CarRentalCompany c = this.companies.elements().nextElement();
			String [] allTypes = c.getAllCarTypes().toArray(new String[0]);
			for(String type : allTypes)
			{
				if(toreturn.contains(type) == false)
				{
					toreturn.add(type);
				}
			}
		}
		
		return toreturn;
	}

	@Override
	public List<String> getAvailableRentalCompanies() {
		List<String> toreturn = new ArrayList<String>();
		
		while(this.companies.elements().hasMoreElements())
			toreturn.add(this.companies.elements().nextElement().getName());
		
		return toreturn;
	}

	@Override
	public Quote getCheapestCarQuoteForConstraint(
			ReservationConstraints constraint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createQuoteForSession(Quote quote, int sessionID) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Quote> getCurrentQuotesForSession(int sessionID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean confirmQuotesForSession(int sessionID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerCompany(CarRentalCompany crc) {
		// TODO Auto-generated method stub
		this.companies.put(crc.getName(), crc);
	}

	@Override
	public void unregisterCompany(CarRentalCompany crc) {
		// TODO Auto-generated method stub
		this.companies.remove(crc);
	}

	@Override
	public int getNumberOfReservationsForCarType(String type) {
		
		int nReservations = 0;
		
		while(this.companies.elements().hasMoreElements())
		{
			CarRentalCompany c = this.companies.elements().nextElement();
			
			c.getNumberOfReservationsForCarType(type);
		}
		
		return nReservations;
	}

	@Override
	public String getTopCustomer(){
		
		String topCustomer = "";
		
		while(this.companies.elements().hasMoreElements())
		{
			CarRentalCompany c = this.companies.elements().nextElement();
		}
		
		return topCustomer;
	}
	
}