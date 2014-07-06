package freenet.winterface.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import freenet.winterface.web.Dashboard;
import freenet.winterface.web.InvalidKey;
import freenet.winterface.web.Plugins;
import freenet.winterface.web.Root;
import freenet.winterface.web.VelocityTest;

public class Routes {
	private final Class<? extends HttpServlet> errorPage = InvalidKey.class;
	private final Class<? extends HttpServlet> dashboardPage = Dashboard.class;
	private final String notfoundTemplate = "invalidkey.vm";
	
	private Hashtable<Class<? extends HttpServlet>, ServletContext> k = new Hashtable<Class<? extends HttpServlet>, Routes.ServletContext>();
	
	public void initRoutes() {
		addRoute(Root.class, "", true, null);
		addRoute(Dashboard.class, "/dashboard", true, "dashboard.vm");
		addRoute(Plugins.class, "/plugins", true, "plugins.vm");
		addRoute(InvalidKey.class, "/invalidkey", true, "invalidkey.vm");
		addRoute(VelocityTest.class, "/test", false, "test.vm");
	}
	
	/**
	 * Add an entry in the Routing table of the Winterface Server
	 * 
	 * @param servletClass	Subclass of HttpServlet that will handle the request
	 * @param path			pathSpec that the Servlet will be listening
	 * @param matchWildcard true if Servlet should handle requests that match path/*
	 * @param template		Template to be used by VelocityBase subclasses constructors
	 */
	private void addRoute(Class<? extends HttpServlet> servletClass, String path, boolean matchWildcard, String template) {
		k.put(servletClass, new ServletContext(path, matchWildcard, template));
	}
	
	public Set<Class<? extends HttpServlet>> getServletClasses() {
		return k.keySet();
	}
	
	public ArrayList<String> getRoutesList() {
		ArrayList<String> routesList = new ArrayList<String>();
		for (Entry<Class<? extends HttpServlet>, ServletContext> routeEntry : k.entrySet()) {
			routesList.add(routeEntry.getValue().path);
		}
		return routesList;
	}
	
	public ArrayList<String> getPathsList() {
		ArrayList<String> pathsList = new ArrayList<String>();
		for (Entry<Class<? extends HttpServlet>, ServletContext> routeEntry : k.entrySet()) {
			pathsList.add(routeEntry.getValue().path);
		}
		return pathsList;
	}
	
	public ArrayList<String> getTemplatesList() {
		ArrayList<String> templatesList = new ArrayList<String>();
		for (Entry<Class<? extends HttpServlet>, ServletContext> routeEntry : k.entrySet()) {
			templatesList.add(routeEntry.getValue().template);
		}
		return templatesList;
	}
	
	public String getMatchFor(Class<?> servletClass) {
		ServletContext svlCtx = k.get(servletClass);
		if (svlCtx.matchWildcard) {
			return svlCtx.path.concat("/*");
		} else {
			return svlCtx.path;
		}
	}
	
	public String getPathFor(Class<?> servletClass) {
		return k.get(servletClass).path;
	}
	
	public String getPathFor(String className) {
		for (Entry<Class<? extends HttpServlet>, ServletContext> routeEntry : k.entrySet()) {
			if (routeEntry.getKey().getSimpleName().equals(className)) {
				return routeEntry.getValue().path;
			}
		}
		return getPathFor(errorPage);
	}
	
	public String getPathForErrorPage() {
		return getPathFor(errorPage);
	}
	
	public String getPathForDashboard() {
		return getPathFor(dashboardPage);
	}
	
	public String getTemplateFor(Class<?> servletClass) {
		String template = k.get(servletClass).template;
		if (template == null) {
			return notfoundTemplate;
		}
		return template;
	}
	
	private class ServletContext {
		String path;
		boolean matchWildcard;
		String template;
		
		public ServletContext(String path, boolean matchWildcard, String template) {
			this.path = path;
			this.matchWildcard = matchWildcard;
			this.template = template;
		}
	}

}
