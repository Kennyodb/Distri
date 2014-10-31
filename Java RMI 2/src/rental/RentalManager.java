package rental;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

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
	public List<String> getAvailableCarTypes(Date start, Date end) {
		List<String> toreturn = new ArrayList<String>();
		
		while(this.companies.elements().hasMoreElements())
		{
			CarRentalCompany c = this.companies.elements().nextElement();
			Set<CarType> availableTypes = c.getAvailableCarTypes(start, end);
			for(CarType type : availableTypes)
			{
				if(toreturn.contains(type.getName()) == false)
					toreturn.add(type.getName());
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
	
	/*******************TODO************************/

	@Override
	public Quote getCheapestCarQuoteForConstraint(
			ReservationConstraints constraints) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void addQuoteForSession(Quote quote, int sessionID)
	{
		List<Quote> quotesForSession = this.sessionQuotes.get(sessionID);
		if(quotesForSession == null)
		{
			quotesForSession = new ArrayList<Quote>();
			this.sessionQuotes.put(sessionID, quotesForSession);
		}
		quotesForSession.add(quote);
	}

	@Override
	public Quote createQuoteForSession(ReservationConstraints constraints, int sessionID) {
		// TODO Auto-generated method stub
		Quote quote = this.getCheapestCarQuoteForConstraint(constraints);
		this.addQuoteForSession(quote, sessionID);
		return quote;
	}

	@Override
	public List<Quote> getCurrentQuotesForSession(int sessionID) {
		// TODO Auto-generated method stub
		return this.sessionQuotes.get(sessionID);
	}
	
	/***** TODO: CONCURRENCY CHECKS EN ROLLBACK STUFF ***********************/
	
	private boolean confirmQuote(Quote q)
	{
		CarRentalCompany company = this.companies.get(q.getRentalCompany());
		try {
			company.confirmQuote(q);
		} catch (ReservationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean confirmQuotesForSession(int sessionID) {
		// TODO Auto-generated method stub
		List<Quote> quotes = this.getCurrentQuotesForSession(sessionID);
		for(Quote quote : quotes)
		{
			if(this.confirmQuote(quote) == false)
			{
				return false;
			}
		}
		return true;
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