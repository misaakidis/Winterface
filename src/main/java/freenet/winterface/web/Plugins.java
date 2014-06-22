package freenet.winterface.web;

import freenet.winterface.core.VelocityBase;
import freenet.winterface.core.WinterfacePlugin;

import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;

/**
 * Test page for Velocity templates in Jetty.
 */
public class Plugins extends VelocityBase {

	public Plugins() {
		super("plugins.vm");
	}

	@Override
	protected void subFillContext(final Context context, HttpServletRequest request) {
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