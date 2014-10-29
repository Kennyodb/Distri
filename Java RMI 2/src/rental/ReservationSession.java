package rental;

import java.util.List;

public class ReservationSession extends Session
{
	public ReservationSession(IRentalManager iRentalManager) 
	{
		super(iRentalManager);
	}
	
	public List<String> getAvailableCarTypes()
	{
		return this.iRentalManager.getAvailableCarTypes();
	}
	
	public String getCheapestCarTypeForConstraint(ReservationConstraints constraint)
	{
		return this.getCheapestCarQuoteForConstraint(constraint).getCarType();
	}
	
	
	public Quote getCheapestCarQuoteForConstraint(ReservationConstraints constraint)
	{
		return this.getCheapestCarQuoteForConstraint(constraint);
	}
	
	public void createQuote(Quote quote)
	{
		this.iRentalManager.createQuoteForSession(quote, this.getID());
	}
	
	public List<Quote> getCurrentQuotes()
	{
		return this.iRentalManager.getCurrentQuotesForSession(this.getID());
	}
	
	public boolean confirmQuotesForSession()
	{
		return this.iRentalManager.confirmQuotesForSession(this.getID());
	}
	
}