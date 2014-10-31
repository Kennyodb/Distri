package rental;

import java.io.Serializable;

public abstract class Session implements Serializable {
	
	private int id;
	private String username;
	private boolean active;
	protected IRentalManager iRentalManager;

	private static int nextUID = 0;

	public Session(IRentalManager iRentalManager, String username) {
		this.id = nextUniqueID();
		this.username = username;
		this.active = false;
		this.iRentalManager = iRentalManager;
	}

	public int getID() {
		return this.id;
	}

	public String getUsername() {
		return username;
	}
	
	public boolean isActive() {
		return this.active;
	}

	public void start() {
		this.active = true;
	}

	public void end() {
		this.active = false;
	}

	private static synchronized int nextUniqueID() {
		return Session.nextUID++;
	}
}