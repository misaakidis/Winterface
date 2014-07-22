package freenet.winterface.web;

import java.io.IOException;

import freenet.winterface.core.ServerManager;
import freenet.winterface.core.VelocityBase;
import freenet.winterface.freenet.FreenetInterface;

import org.apache.velocity.context.Context;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Alerts page
 */
public class Alerts extends VelocityBase {

	@Override
	protected void subFillContext(Context context, HttpServletRequest request) {
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
		if (request.getParameter("dismissAlert") != null) {
			FreenetInterface freenetInterface = (FreenetInterface) getServletContext().getAttribute(ServerManager.FREENET_INTERFACE);
			freenetInterface.dismissAlert(Integer.parseInt(request.getParameter("dismissAlert")));
		}
	}

}