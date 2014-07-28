package freenet.winterface.web;

import java.io.IOException;

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
			if (action.equals("del")) {
				freenetInterface.removeBookmark(bookmarkPathDecoded);
			} else if (action.equals("up")) {
				freenetInterface.moveBookmarkUp(bookmarkPathDecoded, true);
			} else if (action.equals("down")) {
				freenetInterface.moveBookmarkDown(bookmarkPathDecoded, true);
			}
		}
		super.doGet(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getAttribute("confirmDel") != null) {
			FreenetInterface freenetInterface = (FreenetInterface) getServletContext().getAttribute(ServerManager.FREENET_INTERFACE);
			freenetInterface.removeBookmark((String) request.getAttribute("confirmDel"));
		}
		super.doPost(request, response);
	}
}