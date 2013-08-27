package freenet.winterface.core;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.view.VelocityViewServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base class to simplify the code behind a page.
 */
public class VelocityBase extends VelocityViewServlet {

	/**
	 * Path within /resources/ to the base templates directory.
	 */
	public static final String TEMPLATE_PATH = "/templates/";

	protected final String templateName;

	/**
	 * @param templateName path to the template for this page. It is relative to the /templates/ resources directory.
	 */
	public VelocityBase(String templateName) {
		this.templateName = templateName;
	}

	@Override
	protected Template getTemplate(HttpServletRequest request, HttpServletResponse response) {
		return Velocity.getTemplate(TEMPLATE_PATH + templateName);
	}
}
