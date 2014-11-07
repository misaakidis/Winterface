package freenet.winterface.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import freenet.winterface.web.Alerts;
import freenet.winterface.web.Bookmarks;
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
	
	// Initialize routes and define the corresponding Servlets
	public void initRoutes() {
		addRoute(Root.class, "", true, null);
		addRoute(Dashboard.class, "/dashboard", true, "dashboard.vm");
		addRoute(Plugins.class, "/plugins", true, "plugins.vm");
		addRoute(InvalidKey.class, "/invalidkey", true, "invalidkey.vm");
		addRoute(Alerts.class, "/alerts", true, "alerts.vm");
		addRoute(Bookmarks.class, "/bookmarkEditor", true, "bookmarks.vm");
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
		if (k.containsKey(servletClass)) {
			return k.get(servletClass).path;
		} else
			return getPathForErrorPage();
	}
	
	public String getPathFor(String className) {
		return getPathFor(getClassFor(className));
	}
	
	public String getPathForErrorPage() {
		return getPathFor(errorPage);
	}
	
	/**
	 * Gives the relative URI for the given exception, the default error page if unknown.
	 * @param e the exception
	 * @param fproxyUri the URI we can give to FProxy to retry, or either {@code null} or the empty
	 * String if unknown
	 * @return A relative URI (i.e. the path possibly including a fragment part)
	 */
	public String getPathForErrorPage(Exception e, String fproxyUri) {
	    String errorPath = getPathFor(errorPage);
        try {
            Method m = errorPage.getDeclaredMethod("getErrorFragment", Exception.class,
                String.class);
            Object o = m.invoke(null, e, fproxyUri);
            if (o instanceof String) {
                return errorPath + "?" + o;
            }
            return errorPath;
        } catch (Exception ignored) {
            // Uh-oh, our error page did not know that exception. Return the plain error page.
            return errorPath;
        }
	}
	
	/**
	 * Shorthand for {@code getPathForErrorPage(e, null)}.
	 * @see Routes#getPathForErrorPage(Exception, String)
	 */
	public String getPathForErrorPage(Exception e) {
	    return getPathForErrorPage(e, null);
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
	
	public String getTemplateFor(String className) {
		return getTemplateFor(getClassFor(className));
	}
	
	public String getFullPathTemplateFor(String className) {
		return VelocityBase.TEMPLATE_PATH + getTemplateFor(className);
	}
	
	private Class<? extends HttpServlet> getClassFor(String className) {
		for (Entry<Class<? extends HttpServlet>, ServletContext> routeEntry : k.entrySet()) {
			if (routeEntry.getKey().getSimpleName().equals(className)) {
				return routeEntry.getKey();
			}
		}
		return errorPage;
	}
	
	// Inner class to keep all servlet-related info in one place
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
