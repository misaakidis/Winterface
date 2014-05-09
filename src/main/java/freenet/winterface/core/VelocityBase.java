package freenet.winterface.core;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base class to simplify the code behind a page by handling templates.
 */
public abstract class VelocityBase extends VelocityViewServlet {

	/**
	 * Path within /resources/ to the base templates directory.
	 */
	public static final String TEMPLATE_PATH = "/templates/";

	protected final String templateName;
	private final I18n i18n;

	/**
	 * @param templateName path to the template for this page. It is relative to the /templates/ resources directory.
	 */
	public VelocityBase(String templateName) {
		this.templateName = templateName;
		i18n = new I18n();
	}

	/**
	 * Fill the context with template information for the outer content.
	 */
	@Override
	protected void fillContext(Context context, HttpServletRequest request) {
		context.put("freenet", context.get("freenet-interface"));
		context.put("requestedPage", templateFor(templateName));
		// TODO: Support for Wizard nav bar pages too - set navbar to wizard_navbar.vm
		context.put("navbar", templateFor("navbar.vm"));
		context.put("i18n", i18n);
		subFillContext(context, request);
	}

	/**
	 * Fill context with information for the content template.
	 * TODO: Better name?
	 */
	protected abstract void subFillContext(Context context, HttpServletRequest request);

	@Override
	protected Template getTemplate(HttpServletRequest request, HttpServletResponse response) {
		return Velocity.getTemplate(templateFor("index.vm"));
	}

	private String templateFor(String name) {
		return TEMPLATE_PATH + name;
	}
}
