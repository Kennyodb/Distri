package rental;

import java.util.Date;
import java.util.List;

public class ReservationSession extends Session
{
	public ReservationSession(IRentalManager iRentalManager) 
	{
		super(iRentalManager);
	}
	
	public List<String> getAvailableCarTypes(Date start, Date end)
	{
		return this.iRentalManager.getAvailableCarTypes(start, end);
	}
	
	public String getCheapestCarTypeForConstraint(ReservationConstraints constraint)
	{
		return this.getCheapestCarQuoteForConstraint(constraint).getCarType();
	}
	
	
	public Quote getCheapestCarQuoteForConstraint(ReservationConstraints constraint)
	{
		return this.getCheapestCarQuoteForConstraint(constraint);
	}
	
	public Quote createQuote(ReservationConstraints constraints)
	{
		return this.iRentalManager.createQuoteForSession(constraints, this.getID());
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