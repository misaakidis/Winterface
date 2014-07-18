package freenet.winterface.web;

import freenet.winterface.core.ServerManager;
import freenet.winterface.core.VelocityBase;
import freenet.winterface.freenet.FreenetInterface;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;

/**
 * Alerts page
 */
public class Alerts extends VelocityBase {

	@Override
	protected void subFillContext(Context context, HttpServletRequest request) {
		if (request.getMethod().equals("POST")) {
			if (request.getParameter("dismissAlert") != null) {
				((FreenetInterface) context.get(ServerManager.FREENET_INTERFACE)).dismissAlert(Integer.parseInt(request.getParameter("dismissAlert")));
			}
		}
	}

}