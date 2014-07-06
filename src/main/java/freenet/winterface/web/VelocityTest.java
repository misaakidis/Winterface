package freenet.winterface.web;

import freenet.winterface.core.VelocityBase;

import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;

/**
 * Test page for Velocity templates in Jetty.
 */
public class VelocityTest extends VelocityBase {

	@Override
	protected void subFillContext(Context context, HttpServletRequest request) {
		context.put("test", "Do ya like waffles?");
	}
}
