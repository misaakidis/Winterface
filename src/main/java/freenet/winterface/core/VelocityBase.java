package freenet.winterface.core;

import java.io.IOException;
import java.io.StringReader;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.tools.view.VelocityViewServlet;

import freenet.winterface.freenet.FreenetInterface;
import freenet.winterface.freenet.WinterfaceConstants;

import javax.servlet.ServletException;
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
	private final WinterfaceConstants constants;

	/**
	 * @param templateName path to the template for this page. It is relative to the /templates/ resources directory.
	 */
	public VelocityBase(String templateName) {
		this.templateName = templateName;
		i18n = new I18n();
		constants = new WinterfaceConstants();
	}
	
	public VelocityBase() {
		i18n = new I18n();
		constants = new WinterfaceConstants();
	}

	/**
	 * Fill the context with template information for the outer content.
	 */
	@Override
	protected void fillContext(Context context, HttpServletRequest request) {
		this.templateName = getTemplateFromRoutes(getClass());
		context.put("freenet", context.get(ServerManager.FREENET_INTERFACE));
		context.put("winterface-routes", context.get(ServerManager.WINTERFACE_ROUTES));
		context.put("fproxy", "http://127.0.0.1:" + ((FreenetInterface) context.get(ServerManager.FREENET_INTERFACE)).getFproxyPort());
		context.put("requestedPage", templateFor(templateName));
		context.put("request", request);
		// TODO: Support for Wizard nav bar pages too - set navbar to wizard_navbar.vm
		context.put("navbar", templateFor("navbar.vm"));
		context.put("i18n", i18n);
		constants.addConstantsToContext(context);
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
	
	protected Routes getRoutes() {
		return (Routes) getServletContext().getAttribute(ServerManager.WINTERFACE_ROUTES);
	}
	
	// Decoupling paths from Servlets, use this when you need to reference a Servlet
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
	
	/**
	 * Safely get a parameter value from a request
	 * 
	 * @param request the HttpServletRequest containing the parameter
	 * @param name the name of the parameter
	 * @param maxlength the maximum length accepted of the parameter value
	 * @param defaultValue the default value to return in case parameter is not set or exceeds maxlength (can be null)
	 * @return the parameter value, or the default value if parameter is not set or exceeds maxlength
	 */
	protected String getParamSafe(HttpServletRequest request, String name, int maxlength, String defaultValue) {
		if (isParamSet(request, name)) {
			String safeResult = request.getParameter(name);
			if (safeResult.length() <= maxlength) {
				return safeResult;
			}
		}
		return defaultValue;
	}
	
	protected boolean getParamBooleanSafe(HttpServletRequest request, String name) {
		if (isParamSet(request, name)) {
			String paramString = getParamSafe(request, name, 5, "");
			if (paramString.equals("on") || paramString.equals("true")) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isParamSet(HttpServletRequest request, String name) {
		if (request.getParameter(name) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	protected void postReqReturnErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(getRoutes().getPathForErrorPage());
		super.doPost(request, response);
		return;
	}
}
