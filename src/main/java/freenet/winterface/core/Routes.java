package freenet.winterface.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import freenet.winterface.web.Dashboard;
import freenet.winterface.web.InvalidKey;
import freenet.winterface.web.Plugins;
import freenet.winterface.web.Root;
import freenet.winterface.web.VelocityTest;

public class Routes {
	private static Hashtable<Class<?>, ServletContext> k = new Hashtable<Class<?>, Routes.ServletContext>();
	
	public void initRoutes() {
		k.put(Root.class, new ServletContext("/*", "/", "Root", ""));
		k.put(Dashboard.class, new ServletContext("/dashboard/*", "/dashboard", "Dashboard", "dashboard.vm"));
		k.put(Plugins.class, new ServletContext("/plugins/*", "/plugins", "Plugins", "plugins.vm"));
		k.put(InvalidKey.class, new ServletContext("/invalidkey/*", "/invalidkey", "InvalidKey", "invalidkey.vm"));
		k.put(VelocityTest.class, new ServletContext("/test", "/test", "VelocityTest", "test.vm"));
	}
	
	public Set<Class<?>> getServletClasses() {
		return k.keySet();
	}
	
	public ArrayList<String> getRoutesList() {
		ArrayList<String> routesList = new ArrayList<String>();
		for (Entry<Class<?>, ServletContext> routeEntry : k.entrySet()) {
			routesList.add(routeEntry.getValue().match);
		}
		return routesList;
	}
	
	public ArrayList<String> getPathsList() {
		ArrayList<String> pathsList = new ArrayList<String>();
		for (Entry<Class<?>, ServletContext> routeEntry : k.entrySet()) {
			pathsList.add(routeEntry.getValue().path);
		}
		return pathsList;
	}
	
	public ArrayList<String> getTemplatesList() {
		ArrayList<String> templatesList = new ArrayList<String>();
		for (Entry<Class<?>, ServletContext> routeEntry : k.entrySet()) {
			templatesList.add(routeEntry.getValue().template);
		}
		return templatesList;
	}
	
	public static String getMatchFor(Class<?> servletClass) {
		return k.get(servletClass).match;
	}
	
	public static String getPathFor(Class<?> servletClass) {
		return k.get(servletClass).path;
	}
	
	public static String getPathForClassName(String className) {
		for (Entry<Class<?>, ServletContext> routeEntry : k.entrySet()) {
			if (routeEntry.getValue().classShortName.equals(className))
				return routeEntry.getValue().path;
		}
		return getPathFor(InvalidKey.class);
	}
	
	public static String getTemplateFor(Class<?> servletClass) {
		String template = k.get(servletClass).template;
		if (template == null) {
			return "invalidkey.vm";
		}
		return template;
	}
	
	private class ServletContext {
		String match;
		String path;
		String classShortName;
		String template;
		
		public ServletContext(String match, String path, String classShortName, String template) {
			this.match = match;
			this.path = path;
			this.classShortName = classShortName;
			this.template = template;
		}
	}

}
