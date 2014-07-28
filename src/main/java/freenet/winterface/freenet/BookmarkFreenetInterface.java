package freenet.winterface.freenet;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.db4o.ObjectContainer;
import com.google.common.annotations.VisibleForTesting;

import freenet.client.async.ClientContext;
import freenet.client.async.USKCallback;
import freenet.clients.http.bookmark.BookmarkCategory;
import freenet.clients.http.bookmark.BookmarkItem;
import freenet.clients.http.bookmark.BookmarkManager;
import freenet.keys.USK;
import freenet.l10n.NodeL10n;
import freenet.node.FSParseException;
import freenet.node.Node;
import freenet.node.RequestClient;
import freenet.node.useralerts.UserAlertManager;
import freenet.support.SimpleFieldSet;
import freenet.support.URLEncoder;

public class BookmarkFreenetInterface{

	private BookmarkManager bookmarkManager;
	private Node node;
	private UserAlertManager uam;

	public BookmarkFreenetInterface(Node node) {
		this(node, new BookmarkManager(node.clientCore, false));
	}

	@VisibleForTesting
	BookmarkFreenetInterface(Node node, BookmarkManager bookmarkManager) {
		this.node = node;
		this.bookmarkManager = bookmarkManager;
		this.uam = node.clientCore.alerts;
	}

	public List<BookmarkCategoryWithPath> getBookmarkCategories() {
		List<BookmarkCategoryWithPath> catList = new ArrayList<BookmarkCategoryWithPath>();
		recursivelyAddSubCategoriesWithPath(catList, BookmarkManager.MAIN_CATEGORY, "");
		return catList;
	}
	
	public int getBookmarkCategoriesCount() {
		return getBookmarkCategories().size();
	}

	private void recursivelyAddSubCategoriesWithPath(List<BookmarkCategoryWithPath> catList, BookmarkCategory bc, String path) {
		try {
			catList.add(new BookmarkCategoryWithPath(bc, path));
		} catch (FSParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (BookmarkCategory bcChild : bc.getSubCategories()) {
			recursivelyAddSubCategoriesWithPath(catList, bcChild, path + bc.getName());
		}
	}

	public List<BookmarkItemWinterface> getBookmarksFromCat(BookmarkCategory cat) {
		List<BookmarkItemWinterface> items = new ArrayList<BookmarkFreenetInterface.BookmarkItemWinterface>();
		fillWithBookmarkItems(items, cat.getItems());
		return items;
	}
	
	public int getBookmarksFromCatCount(BookmarkCategory cat) {
		return getBookmarksFromCat(cat).size();
	}
	
	private void fillWithBookmarkItems(List<BookmarkItemWinterface> biwItems, List<BookmarkItem> biItems) {
		for (BookmarkItem bookmarkItem : biItems) {
			try {
				biwItems.add(new BookmarkItemWinterface(bookmarkItem));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FSParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
	}

	public BookmarkCategory getCategoryByPath(String path) {
		return bookmarkManager.getCategoryByPath(path);
	}

	public BookmarkItemWinterface getItemByPath(String path) {
		try {
			return new BookmarkItemWinterface(bookmarkManager.getItemByPath(path));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FSParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void removeBookmark(String path) {
		bookmarkManager.removeBookmark(path);
		bookmarkManager.storeBookmarks();
	}
	
	public void moveBookmarkUp(String path, boolean store) {
		bookmarkManager.moveBookmarkUp(path, store);
		bookmarkManager.storeBookmarks();
	}
	
	public void moveBookmarkDown(String path, boolean store) {
		bookmarkManager.moveBookmarkDown(path, store);
		bookmarkManager.storeBookmarks();
	}
	
	public void storeBookmarks() {
		bookmarkManager.storeBookmarks();
	}
	

	public class BookmarkCategoryWithPath extends BookmarkCategory {
		
		private final static String mainCategoryPath = "/";
		private String path;
		private List<BookmarkItem> bookmarkItems = new ArrayList<BookmarkItem>();

		public BookmarkCategoryWithPath(BookmarkCategory bc, String path) throws FSParseException {
			super(bc.getSimpleFieldSet());
			bookmarkItems = bc.getItems();
			if(path.isEmpty()) {
				this.path = mainCategoryPath;
			} else {
				this.path = path + getName() + '/';
			}
		}

		@Override
		public String getVisibleName() {
			if (path.equals(mainCategoryPath)) {
				//TODO Add localization for Main Cateogry
				return "Main Category";
			} else if (name.toLowerCase().startsWith("l10n:")) {
				return NodeL10n.getBase().getString("Bookmarks.Defaults.Name."+name.substring("l10n:".length()));
			} else {
				return name;
			}
		}
		
		public String getCatPath() {
			return path;
		}

		public String getCatPathEncoded() {
			return URLEncoder.encode(getCatPath(), false);
		}
		
		public List<BookmarkItem> getItems() {
			return bookmarkItems;
		}
		
	}
	
	public class BookmarkItemWinterface extends BookmarkItem implements RequestClient{
		public boolean updated = false;
		private final USKUpdatedCallback uskCBWin = new USKUpdatedCallback();
		
		public BookmarkItemWinterface(SimpleFieldSet sfs, UserAlertManager uam) throws FSParseException, MalformedURLException {
			super(sfs, uam);
			subscribeToUSK();
		}
		
		public BookmarkItemWinterface(BookmarkItem bi) throws MalformedURLException, FSParseException {
			super(bi.getSimpleFieldSet(), uam);
			subscribeToUSK();
		}
		
		public String getPath(String parentPath) {
			return parentPath + getName();
		}
		
		public String getPathEncoded(String parentPath) {
			return URLEncoder.encode(getPath(parentPath), false);
		}
		
		public boolean getUpdatedStatus() {
			return updated;
		}
		
		public void setUpdatedStatus(boolean status) {
			updated = status;
		}
		
		private void subscribeToUSK() {
			if("USK".equals(getKeyType()))
				try {
					USK u = getUSK();
					node.clientCore.uskManager.subscribe(u, uskCBWin, true, this);
				} catch(MalformedURLException mue) {}
		}
		
		private class USKUpdatedCallback implements USKCallback {

			@Override
			public void onFoundEdition(long l, USK key, ObjectContainer container, ClientContext context, boolean metadata, short codec, byte[] data, boolean newKnownGood, boolean newSlotToo) {
				if (BookmarkItemWinterface.this.getKey().equals(key)) {
					BookmarkItemWinterface.this.updated = true;
				}
			}

			@Override
			public short getPollingPriorityNormal() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public short getPollingPriorityProgress() {
				// TODO Auto-generated method stub
				return 0;
			}
			
		}

		@Override
		public boolean persistent() {
			return false;
		}

		@Override
		public void removeFrom(ObjectContainer container) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean realTimeFlag() {
			return false;
		}
	
	}
	
}