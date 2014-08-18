package freenet.winterface.web;

import java.io.IOException;

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

	@Override
	protected void subFillContext(Context context, HttpServletRequest request) {
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getQueryString() != null && request.getQueryString().startsWith("action") && request.getParameter("bookmark").length() > 0) {
			FreenetInterface freenetInterface = (FreenetInterface) getServletContext().getAttribute(ServerManager.FREENET_INTERFACE);
			
			String bookmarkPath = request.getParameter("bookmark");
			String bookmarkPathDecoded = "";
			try {
				bookmarkPathDecoded = URLDecoder.decode(bookmarkPath, false);
			} catch(URLEncodedFormatException e) {
				response.sendRedirect(getRoutes().getPathForErrorPage());
			}
			
			String action = request.getParameter("action");
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
		
		if (request.getParameter("AddDefaultBookmarks") != null) {
			freenetInterface.reAddDefaultBookmarks();
		}
		
		if (request.getQueryString() != null && request.getQueryString().startsWith("action") && request.getParameter("bookmark").length() > 0) {
			
			String bookmarkPath = request.getParameter("bookmark");
			String bookmarkPathDecoded = "";
			try {
				bookmarkPathDecoded = URLDecoder.decode(bookmarkPath, false);
			} catch(URLEncodedFormatException e) {
				response.sendRedirect(getRoutes().getPathForErrorPage());
			}
			
			String action = request.getParameter("action");
			
			if (action.equals("edit")) {
				//TODO Check/sanitize request parameters
				if(bookmarkPathDecoded.endsWith("/")) {
					// The bookmark path corresponds to a category
					freenetInterface.editBookmark(bookmarkPathDecoded, (String) request.getParameter("name"), null, null, null, false);
				} else {
					// The bookmark path corresponds to a bookmarkItem
					freenetInterface.editBookmark(bookmarkPathDecoded, (String) request.getParameter("name"), new FreenetURI((String) request.getParameter("key")), (String) request.getParameter("descB"), (String) request.getParameter("explain"), (String) request.getParameter("hasAnActivelink") != null && ((String) request.getParameter("hasAnActivelink")).equals("on"));
				}
			} else if (action.equals("addCat")) {
				freenetInterface.addCategory(bookmarkPathDecoded, (String) request.getParameter("name"));
			} else if (action.equals("addItem")) {
				freenetInterface.addBookmarkItem(bookmarkPathDecoded, (String) request.getParameter("name"), new FreenetURI((String) request.getParameter("key")), (String) request.getParameter("descB"), (String) request.getParameter("explain"), request.getParameter("hasAnActivelink") != null && ((String) request.getParameter("hasAnActivelink")).equals("on"));
			}
			
			response.sendRedirect(getRoutes().getPathFor("Bookmarks"));
		}
		super.doPost(request, response);
	}
}