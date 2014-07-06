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
		k.put(Root.class, new ServletContext("/*", ""));
		k.put(Dashboard.class, new ServletContext("/dashboard/*", "dashboard.vm"));
		k.put(Plugins.class, new ServletContext("/plugins/*", "plugins.vm"));
		k.put(InvalidKey.class, new ServletContext("/invalidkey/*", "invalidkey.vm"));
		k.put(VelocityTest.class, new ServletContext("/test", "test.vm"));
	}
	
	public ArrayList<String> getPathsList() {
		ArrayList<String> routesList = new ArrayList<String>();
		for (Entry<Class<?>, ServletContext> routeEntry : k.entrySet()) {
			routesList.add(routeEntry.getValue().path);
		}
		return routesList;
	}
	
	public ArrayList<String> getTemplatesList() {
		ArrayList<String> routesList = new ArrayList<String>();
		for (Entry<Class<?>, ServletContext> routeEntry : k.entrySet()) {
			routesList.add(routeEntry.getValue().template);
		}
		return routesList;
	}
	
	public ArrayList<String> getRoutesList() {
		ArrayList<String> routesList = new ArrayList<String>();
		for (Entry<Class<?>, ServletContext> routeEntry : k.entrySet()) {
			routesList.add(routeEntry.getValue().template);
		}
		return routesList;
	}
	
	public Set<Class<?>> getServletClasses() {
		return k.keySet();
	}
	
	public static String getPathFor(Class<?> servletClass) {
		return k.get(servletClass).path;
	}
	
	public static String getTemplateFor(Class<?> servletClass) {
		String template = k.get(servletClass).template;
		if (template == null) {
			return "invalidkey.vm";
		}
		return template;
	}
	
	private class ServletContext {
		String path;
		String template;
		
		public ServletContext(String path, String template) {
			this.path = path;
			this.template = template;
		}
	}

}
