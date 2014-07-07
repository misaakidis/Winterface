package freenet.winterface.freenet;

import java.util.ArrayList;
import java.util.List;

import freenet.clients.http.bookmark.BookmarkCategory;
import freenet.clients.http.bookmark.BookmarkItem;
import freenet.clients.http.bookmark.BookmarkManager;
import freenet.node.Node;
import com.google.common.annotations.VisibleForTesting;

public class BookmarkFreenetInterface{
		
	private BookmarkManager bookmarkManager;
	private Node node;
	
	public BookmarkFreenetInterface(Node node) {
		this(node, new BookmarkManager(node.clientCore, false));
	}
	
	@VisibleForTesting
	BookmarkFreenetInterface(Node node, BookmarkManager bookmarkManager) {
		this.node = node;
		this.bookmarkManager = bookmarkManager;
	}
	
	public List<BookmarkCategory> getBookmarkCategories() {
		List<BookmarkCategory> catList = new ArrayList<BookmarkCategory>();
		catList.add(BookmarkManager.MAIN_CATEGORY);
		catList.addAll(BookmarkManager.MAIN_CATEGORY.getSubCategories());
		return catList;
	}
	
	public List<BookmarkItem> getBookmarksFromCat(BookmarkCategory cat) {
		List<BookmarkItem> items = cat.getItems();
		return items;
	}

}