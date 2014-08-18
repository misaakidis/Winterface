package freenet.winterface.web;

import java.io.IOException;

import freenet.clients.http.BookmarkEditorToadlet;
import freenet.keys.FreenetURI;
import freenet.support.URLDecoder;
import freenet.support.URLEncodedFormatException;
import freenet.winterface.core.ServerManager;
import freenet.winterface.core.VelocityBase;
import freenet.winterface.freenet.FreenetInterface;

import org.apache.velocity.context.Context;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Bookmarks page
 */
public class Bookmarks extends VelocityBase {
	
	/** Values from {@link BookmarkEditorToadlet} */
	/** Max. action (edit, addCat etc.) length */
	private static final int MAX_ACTION_LENGTH = 20;
	/** Max. bookmark name length */
	private static final int MAX_NAME_LENGTH = 500;
	/** Max. bookmark path length (e.g. <code>Freenet related software and documentation/Freenet Message System</code> ) */
	private static final int MAX_BOOKMARK_PATH_LENGTH = 10 * MAX_NAME_LENGTH;
	private static final int MAX_EXPLANATION_LENGTH = 1024;

	@Override
	protected void subFillContext(Context context, HttpServletRequest request) {
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getQueryString() != null && request.getQueryString().startsWith("action") && request.getParameter("bookmark").length() > 0) {
			FreenetInterface freenetInterface = (FreenetInterface) getServletContext().getAttribute(ServerManager.FREENET_INTERFACE);
			
			String bookmarkPath = getParamSafe(request, "bookmark", MAX_BOOKMARK_PATH_LENGTH, null);
			String bookmarkPathDecoded = "";
			try {
				bookmarkPathDecoded = URLDecoder.decode(bookmarkPath, false);
			} catch(URLEncodedFormatException e) {
				response.sendRedirect(getRoutes().getPathForErrorPage());
			}
			
			String action = getParamSafe(request, "action", MAX_ACTION_LENGTH, "");
			if (action.equals("confirmdelete")) {
				freenetInterface.removeBookmark(bookmarkPathDecoded);
			} else if (action.equals("up")) {
				freenetInterface.moveBookmarkUp(bookmarkPathDecoded, true);
			} else if (action.equals("down")) {
				freenetInterface.moveBookmarkDown(bookmarkPathDecoded, true);
			}
			
			// Redirect to bookmarks editor page, so that action is removed from the URL
			response.sendRedirect(getRoutes().getPathFor("Bookmarks"));
		}
		super.doGet(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FreenetInterface freenetInterface = (FreenetInterface) getServletContext().getAttribute(ServerManager.FREENET_INTERFACE);
		
		if (getParamSafe(request, "AddDefaultBookmarks", 100, null) != null) {
			freenetInterface.reAddDefaultBookmarks();
		}
		
		if (request.getQueryString() != null && request.getQueryString().startsWith("action") && getParamSafe(request, "bookmark", MAX_BOOKMARK_PATH_LENGTH, "").length() > 0) {
			
			String bookmarkPath = getParamSafe(request, "bookmark", MAX_BOOKMARK_PATH_LENGTH, "");
			String bookmarkPathDecoded = "";
			try {
				bookmarkPathDecoded = URLDecoder.decode(bookmarkPath, false);
			} catch(URLEncodedFormatException e) {
				response.sendRedirect(getRoutes().getPathForErrorPage());
			}
			
			String action = getParamSafe(request, "action", MAX_ACTION_LENGTH, "");
			
			if (action.equals("edit")) {
				//TODO Check/sanitize request parameters
				if(bookmarkPathDecoded.endsWith("/")) {
					// The bookmark path corresponds to a category
					freenetInterface.editBookmark(bookmarkPathDecoded, getParamSafe(request, "name", MAX_NAME_LENGTH, null), null, null, null, false);
				} else {
					// The bookmark path corresponds to a bookmarkItem
					freenetInterface.editBookmark(bookmarkPathDecoded, getParamSafe(request, "name", MAX_NAME_LENGTH, null), new FreenetURI((String) request.getParameter("key")), getParamSafe(request, "descB", MAX_EXPLANATION_LENGTH, ""), getParamSafe(request, "explain", MAX_EXPLANATION_LENGTH, ""), getParamBooleanSafe(request, "hasAnActivelink"));
				}
			} else if (action.equals("addCat")) {
				freenetInterface.addCategory(bookmarkPathDecoded, getParamSafe(request, "name", MAX_NAME_LENGTH, null));
			} else if (action.equals("addItem")) {
				freenetInterface.addBookmarkItem(bookmarkPathDecoded, getParamSafe(request, "name", MAX_NAME_LENGTH, null), new FreenetURI((String) request.getParameter("key")), getParamSafe(request, "descB", MAX_EXPLANATION_LENGTH, ""), getParamSafe(request, "explain", MAX_EXPLANATION_LENGTH, ""), getParamBooleanSafe(request, "hasAnActivelink"));
			}
			
			response.sendRedirect(getRoutes().getPathFor("Bookmarks"));
		}
		super.doPost(request, response);
	}
}