package freenet.winterface.web;

import java.util.Map;

import freenet.winterface.core.VelocityBase;
import freenet.winterface.core.WinterfacePlugin;
import freenet.winterface.freenet.FreenetInterface;
import freenet.winterface.freenet.NodeFreenetInterface;
import freenet.winterface.freenet.PluginFreenetInterface;

import org.apache.velocity.context.Context;

import com.sun.corba.se.spi.activation.ServerManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
				((PluginFreenetInterface) context.get("plugin-freenet-interface")).reloadPlugin(request.getParameter("reload"));
			}
		}
	}
	
}