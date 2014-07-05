package freenet.winterface.freenet;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.keys.FreenetURI;

/**
 * Defines the interface between Winterface and the Freenet node. All methods
 * in this interface should return as fast as possible.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface FreenetInterface {

	ConnectionOverview getConnections();

	FetchResult fetchURI(FreenetURI uri) throws FetchException;

}
