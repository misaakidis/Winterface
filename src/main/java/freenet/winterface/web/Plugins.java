package freenet.winterface.web;

import freenet.winterface.core.VelocityBase;
import freenet.winterface.core.WinterfacePlugin;

import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;

/**
 * Plugins page.
 */
public class Plugins extends VelocityBase {

	@Override
	protected void subFillContext(final Context context, HttpServletRequest request) {
		if (request.getMethod().equals("POST")) {
			if (request.getParameter("reload") != null) {
				//TODO use FreenetInterface for accessing winterface plugin instance
				(new Thread() {
					public void run() {
						((WinterfacePlugin) context.get("winterface-plugin")).reload();
					}
				}).start();

			}
		}
	}

}