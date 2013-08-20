package freenet.winterface.web;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Test page for Velocity templates in Jetty.
 *
 * TODO: Also look into VelocityViewServlet? How to handle
 */
public class VelocityTest extends VelocityViewServlet {
	@Override
	protected void fillContext(Context context, HttpServletRequest request) {
		context.put("test", "Do ya like waffles?");
	}

	// TODO: Possibly make base class that returns the template name, or perhaps gives it in a constructor to base.
	@Override
	protected Template getTemplate(HttpServletRequest request, HttpServletResponse response) {
		return Velocity.getTemplate("/templates/test.vm");
	}
}
