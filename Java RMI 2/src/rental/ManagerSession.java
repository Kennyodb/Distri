package rental;

public class ManagerSession extends Session
{

	public ManagerSession(IRentalManager iRentalManager) 
	{
		super(iRentalManager);
	}
	
	public void registerCompany(CarRentalCompany crc)
	{
		this.iRentalManager.registerCompany(crc);
	}
	
	public void unregisterCompany(CarRentalCompany crc)
	{
		this.iRentalManager.unregisterCompany(crc);
	}
	
	public int getNumberOfReservationsForCarType(String type)
	{
		return this.iRentalManager.getNumberOfReservationsForCarType(type);
	}
	
	public String getTopCustomer()
	{
		return this.iRentalManager.getTopCustomer();
	}
	
}