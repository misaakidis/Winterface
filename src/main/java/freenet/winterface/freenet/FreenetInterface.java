package freenet.winterface.freenet;

import java.util.List;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.clients.http.bookmark.BookmarkCategory;
import freenet.clients.http.bookmark.BookmarkItem;
import freenet.keys.FreenetURI;
import freenet.node.SecurityLevels.NETWORK_THREAT_LEVEL;
import freenet.node.SecurityLevels.PHYSICAL_THREAT_LEVEL;
import freenet.node.useralerts.UserAlert;
import freenet.winterface.freenet.BookmarkFreenetInterface.BookmarkCategoryWithPath;

/**
 * Defines the interface between Winterface and the Freenet node. All methods
 * in this interface should return as fast as possible.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface FreenetInterface {

	String publicVersion();
	int buildNumber();
	String fredMinVersionSupported();
	int fredMinBuildSupported();
	boolean isFredVersionSupported();
	String cvsRevision();
	String extRevisionNumber();
	
	ConnectionOverview getConnections();
	NETWORK_THREAT_LEVEL getNetworkThreatLevel();
	PHYSICAL_THREAT_LEVEL getPhysicalThreatLevel();
	
	int getFproxyPort();

	FetchResult fetchURI(FreenetURI uri) throws FetchException;
	
	List<BookmarkCategoryWithPath> getBookmarkCategories();
	int getBookmarkCategoriesCount();
	List<BookmarkItem> getBookmarksFromCat(BookmarkCategory cat);
	int getBookmarksFromCatCount(BookmarkCategory cat);
	BookmarkCategory getCategoryByPath(String path);
	String getBookmarkItemPathEncoded(String parentPath, BookmarkItem bmItem);
	void editBookmark(String path, String name, FreenetURI key, String descB, String explain, boolean hasAnActivelink);
	void removeBookmark(String path);
	void moveBookmarkUp(String path, boolean store);
	void moveBookmarkDown(String path, boolean store);
	void storeBookmarks();
	
	UserAlert[] getAlerts();
	UserAlert[] getValidAlerts();	
	int alertClass(UserAlert alert);
	void dismissAlert(int alertHashCode);
	int getValidAlertCount();
	int getAlertAnchorSafe(String anchorUnsafe);
	int alertsHighestClass();

}
