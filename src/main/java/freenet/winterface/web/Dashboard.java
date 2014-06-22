package freenet.winterface.web;

import freenet.winterface.core.VelocityBase;
import freenet.winterface.core.WinterfacePlugin;

import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;

/**
 * Test page for Velocity templates in Jetty.
 */
public class Dashboard extends VelocityBase {

	public Dashboard() {
		super("dashboard.vm");
	}

	@Override
	protected void subFillContext(Context context, HttpServletRequest request) {
		context.put("thread_name", context.get("winterface-thread-name"));
		if (request.getMethod().equals("POST")) {
			if (request.getParameter("reload") != null) {
				System.out.println("Reloading Winterface with thread name: " + request.getParameter("reload"));
				//TODO use FreenetInterface for accessing winterface plugin instance
				((WinterfacePlugin) context.get("winterface-plugin")).reload();
			}
		}
	}
	
}