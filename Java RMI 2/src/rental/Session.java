package rental;

public abstract class Session
{
	private int id;
	private boolean active;
	protected IRentalManager iRentalManager;
	
	private static int nextUID = 0;
	
	public Session(IRentalManager iRentalManager)
	{
		this.id = nextUniqueID();
		this.active = false;
		this.iRentalManager = iRentalManager;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	
	public boolean isActive()
	{
		return this.active;
	}
	
	public void start()
	{
		this.active = true;
	}
	
	public void end()
	{
		this.active = false;
	}
	
	private static synchronized int nextUniqueID()
	{
		return Session.nextUID++;
	}
}