package freenet.winterface.freenet;

/**
 * Overview of the current peer connection status.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ConnectionOverview {

	private final int numberOfCurrentConnections;
	private final int numberOfMaximumConnections;

	public ConnectionOverview(int numberOfCurrentConnections, int numberOfMaximumConnections) {
		this.numberOfCurrentConnections = numberOfCurrentConnections;
		this.numberOfMaximumConnections = numberOfMaximumConnections;
	}

	public int getCurrent() {
		return numberOfCurrentConnections;
	}

	public int getMaximum() {
		return numberOfMaximumConnections;
	}

}
