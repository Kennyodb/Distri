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
	public Session getNewSession(String type) {
		if (type.equalsIgnoreCase("manager"))
			return new ManagerSession(this);

		if (type.equalsIgnoreCase("reservation"))
			return new ReservationSession(this);

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
	public int getNumberOfReservationsForCarType(String type) {
		int nReservations = 0;
		for (CarRentalCompany c : companies) {
			c.getNumberOfReservationsForCarType(type);
		}
		return nReservations;
	}

	@Override
	public String getTopCustomer() {
		String topCustomer = "";
		for (CarRentalCompany crc : companies) {
			// TODO
		}
		return topCustomer;
	}

	@Override
	public List<String> getAvailableCarTypes(Date start, Date end) {
		Set<String> available = new HashSet<>();
		for(CarRentalCompany company : companies) {
			for(CarType type : company.getAvailableCarTypes(start, end)) {
				available.add(type.getName());
			}
		}
		return new ArrayList<>(available);
	}
}