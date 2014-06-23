package freenet.winterface.web;

import freenet.winterface.core.VelocityBase;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.parser.ParseException;

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
	}
	
	@Override
	protected void fillContext(Context context, HttpServletRequest request) {
		if (request.getRequestURI().startsWith("/USK@")) {
			return;
		} else {
			super.fillContext(context, request);
		}
		
	}
		
	@Override
	protected Template getTemplate(HttpServletRequest request, HttpServletResponse response) {
		if (request.getRequestURI().startsWith("/USK@")) {	
			return Velocity.getTemplate(templateFor("plain.vm"));
		} else {
			return Velocity.getTemplate(templateFor("index.vm"));
		}
	}
		
}