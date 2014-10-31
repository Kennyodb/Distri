package rental;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ReservationSession extends Session {

	Map<String, Set<Quote>> quotes; // maps crc name to quotes

	public ReservationSession(IRentalManager iRentalManager, String username) {
		super(iRentalManager, username);
		quotes = new HashMap<>();
	}

	public List<String> getAvailableCarTypes(Date start, Date end) {
		return iRentalManager.getAvailableCarTypes(start, end);
	}

	public List<String> getCarRentalCompanies() {
		return iRentalManager.getRentalCompanies();
	}

	public String getCheapesCarType(Date start, Date end) {
		return null; // TODO
	}

	public Quote getCheapestCarQuoteForConstraint(
			ReservationConstraints constraint) {
		return null;// TODO
	}

	public Quote createQuote(String company, ReservationConstraints constraints) {
		Quote quote = this.getCheapestCarQuoteForConstraint(constraints);
		if (quotes.containsKey(company)) {
			quotes.get(company).add(quote);
		} else {
			Set<Quote> set = new HashSet<>();
			set.add(quote);
			quotes.put(company, set);
		}
		return quote;
	}

	public List<Quote> getCurrentQuotes() {
		List<Quote> result = new ArrayList<>();
		for (Set<Quote> set : quotes.values()) {
			for (Quote quote : set) {
				result.add(quote);
			}
		}
		return result;
	}

	public List<Reservation> confirmQuotes() throws ReservationException {

		// keep track of confirmed reservations in case of rollback
		Map<String, Set<Reservation>> confirmed = new HashMap<>();

		for (Entry<String, Set<Quote>> entry : quotes.entrySet()) {
			for (Quote quote : entry.getValue()) {
				try {
					Reservation res = iRentalManager.confirmQuote(
							entry.getKey(), quote);
					if (confirmed.containsKey(entry.getKey())) {
						confirmed.get(entry.getKey()).add(res);
					} else {
						Set<Reservation> set = new HashSet<>();
						set.add(res);
						confirmed.put(entry.getKey(), set);
					}
				} catch (ReservationException e) { // rollback
					System.err.println(e.getMessage());
					for (Entry<String, Set<Reservation>> confirmedEntry : confirmed
							.entrySet()) {
						for (Reservation res : confirmedEntry.getValue()) {
							iRentalManager.cancelReservation(
									confirmedEntry.getKey(), res);
						}
					}
					throw e;
				}
			}
		}

		List<Reservation> result = new ArrayList<>();
		for (Entry<String, Set<Reservation>> entry : confirmed.entrySet()) {
			result.addAll(entry.getValue());
		}
		return result;
	}
}