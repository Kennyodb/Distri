package rental;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RentalManager implements IRentalManager {

	Set<CarRentalCompany> companies;

	public RentalManager() {
		this.companies = new HashSet<>();
	}

	@Override
	public Session getNewSession(String type, String username) { // String-based
		if (type.equalsIgnoreCase("manager"))
			return new ManagerSession(this, username);

		if (type.equalsIgnoreCase("reservation"))
			return new ReservationSession(this, username);

		return null;
	}

	@Override
	public List<String> getRentalCompanies() {
		List<String> result = new ArrayList<>();
		for (CarRentalCompany company : companies) {
			result.add(company.getName());
		}
		return result;
	}

	private CarRentalCompany getCarRentalCompany(String name) {
		for (CarRentalCompany company : companies) {
			if (company.getName().equals(name)) {
				return company;
			}
		}
		throw new IllegalArgumentException("No company with name: " + name);
	}

	public Reservation confirmQuote(String companyName, Quote q)
			throws ReservationException {
		CarRentalCompany company = this.getCarRentalCompany(companyName);
		return company.confirmQuote(q);
	}

	public void cancelReservation(String companyName, Reservation res) {
		this.getCarRentalCompany(companyName).cancelReservation(res);
	}

	@Override
	public void registerCompany(CarRentalCompany crc) {
		this.companies.add(crc);
	}

	@Override
	public void unregisterCompany(CarRentalCompany crc) {
		this.companies.remove(crc);
	}

	@Override
	public int getNumberOfReservationsForCarType(String companyName, String type) {
		return getCarRentalCompany(companyName)
				.getNumberOfReservationsForCarType(type);
	}

	@Override
	public Set<String> getTopCustomers() {
		Set<String> result = new HashSet<>();
		for (CarRentalCompany company : companies) {
			result.add(company.getTopCustomer());
		}
		return result;
	}

	@Override
	public List<String> getAvailableCarTypes(Date start, Date end) {
		Set<String> available = new HashSet<>();
		for (CarRentalCompany company : companies) {
			for (CarType type : company.getAvailableCarTypes(start, end)) {
				available.add(type.getName());
			}
		}
		return new ArrayList<>(available);
	}
}