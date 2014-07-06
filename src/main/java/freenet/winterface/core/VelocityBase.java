package freenet.winterface.core;

import java.io.StringReader;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.tools.view.VelocityViewServlet;

import javax.servlet.http.HttpServlet;
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

	protected String templateName;
	private final I18n i18n;

	/**
	 * @param templateName path to the template for this page. It is relative to the /templates/ resources directory.
	 */
	public VelocityBase(String templateName) {
		this.templateName = templateName;
		i18n = new I18n();
	}
	
	public VelocityBase() {
		i18n = new I18n();
	}

	/**
	 * Fill the context with template information for the outer content.
	 */
	@Override
	protected void fillContext(Context context, HttpServletRequest request) {
		this.templateName = getTemplateFromRoutes(getClass());
		context.put("freenet", context.get(ServerManager.FREENET_INTERFACE));
		context.put("winterface-routes", context.get(ServerManager.WINTERFACE_ROUTES));
		context.put("requestedPage", templateFor(templateName));
		context.put("request", request);
		// TODO: Support for Wizard nav bar pages too - set navbar to wizard_navbar.vm
		context.put("navbar", templateFor("navbar.vm"));
		context.put("i18n", i18n);
		context.put("page_title", new String("Freenet"));
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

	protected String templateFor(String name) {
		return TEMPLATE_PATH + name;
	}
	
	private Routes getRoutes() {
		return (Routes) getServletContext().getAttribute(ServerManager.WINTERFACE_ROUTES);
	}
	
	protected String getPathFromRoutes(Class<? extends HttpServlet> classObject) {
		return getRoutes().getPathFor(classObject);
	}
	
	protected String getTemplateFromRoutes(Class<? extends HttpServlet> classObject) {
		return getRoutes().getTemplateFor(classObject);
	}
	
	protected Template templateFromString(String inputString) throws ParseException {
		RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
		StringReader reader = new StringReader(inputString);
		SimpleNode node = runtimeServices.parse(reader, "Template name");
		Template template = new Template();
		template.setRuntimeServices(runtimeServices);
		template.setData(node);
		template.initDocument();
		return template;
	}
}
