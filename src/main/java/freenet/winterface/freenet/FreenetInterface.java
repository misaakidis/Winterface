package freenet.winterface.freenet;

import java.util.List;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.clients.http.bookmark.BookmarkCategory;
import freenet.clients.http.bookmark.BookmarkItem;
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
	
	List<BookmarkCategory> getBookmarkCategories();
	List<BookmarkItem> getBookmarksFromCat(BookmarkCategory cat);

}
