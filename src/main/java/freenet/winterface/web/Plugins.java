package freenet.winterface.web;

import java.io.IOException;

import freenet.winterface.core.ServerManager;
import freenet.winterface.core.VelocityBase;
import freenet.winterface.core.WinterfacePlugin;
import freenet.winterface.freenet.FreenetInterface;

import org.apache.velocity.context.Context;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Plugins page.
 */
public class Plugins extends VelocityBase {

	@Override
	protected void subFillContext(final Context context, HttpServletRequest request) {
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("reload") != null) {
			//TODO use FreenetInterface for accessing winterface plugin instance
			(new Thread() {
				public void run() {
					WinterfacePlugin winterfacePlugin = (WinterfacePlugin) getServletContext().getAttribute(ServerManager.WINTERFACE_PLUGIN);
					winterfacePlugin.reload();
				}
			}).start();

		}
		super.doPost(request, response);
	}

}