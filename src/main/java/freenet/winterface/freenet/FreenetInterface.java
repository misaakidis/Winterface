package freenet.winterface.freenet;

import java.util.List;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.clients.http.bookmark.BookmarkCategory;
import freenet.clients.http.bookmark.BookmarkItem;
import freenet.keys.FreenetURI;
import freenet.node.useralerts.UserAlert;
import freenet.winterface.freenet.BookmarkFreenetInterface.BookmarkCategoryWithPath;
import freenet.winterface.freenet.BookmarkFreenetInterface.BookmarkItemWinterface;

/**
 * Defines the interface between Winterface and the Freenet node. All methods
 * in this interface should return as fast as possible.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface FreenetInterface {

	String publicVersion();
	int buildNumber();
	String cvsRevision();
	String extRevisionNumber();
	
	ConnectionOverview getConnections();

	FetchResult fetchURI(FreenetURI uri) throws FetchException;
	
	List<BookmarkCategoryWithPath> getBookmarkCategories();
	int getBookmarkCategoriesCount();
	List<BookmarkItemWinterface> getBookmarksFromCat(BookmarkCategory cat);
	int getBookmarksFromCatCount(BookmarkCategory cat);
	BookmarkCategory getCategoryByPath(String path);
	BookmarkItem getItemByPath(String path);
	void removeBookmark(String path);
	void moveBookmarkUp(String path, boolean store);
	void moveBookmarkDown(String path, boolean store);
	void storeBookmarks();
	
	UserAlert[] getAlerts();
	int alertClass(UserAlert alert);
	void dismissAlert(int alertHashCode);
	int getValidAlertCount();
	boolean getUpdatedStatus(String bookmarkTitle);
	
}
