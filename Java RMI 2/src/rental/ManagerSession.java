package rental;

import java.util.Set;

public class ManagerSession extends Session {

	public ManagerSession(IRentalManager iRentalManager, String username) {
		super(iRentalManager, username);
	}

	public void registerCompany(CarRentalCompany crc) {
		this.iRentalManager.registerCompany(crc);
	}

	public void unregisterCompany(CarRentalCompany crc) {
		this.iRentalManager.unregisterCompany(crc);
	}

	public int getNumberOfReservationsForCarType(String company, String type) {
		return this.iRentalManager.getNumberOfReservationsForCarType(company, type);
	}

	public Set<String> getTopCustomers() {
		return this.iRentalManager.getTopCustomers();
	}

	public int getNumberOfReservationsBy(String clientName) {
		return 0; //TODO
	}

	public CarType getMostPopularCarTypeIn(String carRentalCompanyName) {
		return null; //TODO
	}
}