package freenet.winterface.web;

import freenet.winterface.core.VelocityBase;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;

/**
 * Test page for Velocity templates in Jetty.
 *
 * TODO: Also look into VelocityViewServlet? How to handle
 */
public class VelocityTest extends VelocityBase {

	public VelocityTest() {
		super("test.vm");
	}

	@Override
	protected void fillContext(Context context, HttpServletRequest request) {
		context.put("test", "Do ya like waffles?");
	}
}
