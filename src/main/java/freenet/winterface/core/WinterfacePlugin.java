package freenet.winterface.core;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.app.Velocity;
import org.eclipse.jetty.server.Server;

import freenet.config.SubConfig;
import freenet.l10n.BaseL10n.LANGUAGE;
import freenet.node.Node;
import freenet.pluginmanager.FredPlugin;
import freenet.pluginmanager.FredPluginConfigurable;
import freenet.pluginmanager.FredPluginVersioned;
import freenet.pluginmanager.PluginInfoWrapper;
import freenet.pluginmanager.PluginManager;
import freenet.pluginmanager.PluginRespirator;
import freenet.winterface.freenet.NodeFreenetInterface;
import freenet.winterface.freenet.PluginFreenetInterface;

/**
 * Winterface {@link FredPlugin}
 * <p>
 * Replaces FProxy with Apache Wicket and Jetty web server.
 * </p>
 * 
 * @author pasub
 * 
 */
public class WinterfacePlugin implements FredPlugin, FredPluginVersioned, FredPluginConfigurable {

	/**
	 * {@link URL} at which {@link WinterfacePlugin} resides
	 */
	private URL plugin_path;
	
	/**
	 * {@link String} name of {@link WinterfacePlugin} main thread
	 */
	private String winterface_thread_name;
	
	/**
	 * The instance of freenet node that loaded Winterface
	 */
	private Node node;

	/**
	 * An instance of {@link ServerManager} for all {@link Server} related
	 * functionalities
	 */
	private ServerManager serverManager;

	/** Configuration */
	private final Configuration config;

	/** Current version */
	private final static String VERSION = "0.1";

	/**
	 * {@code true} if in development mode. Change to {@code false} to switch to
	 * deployment mode
	 */
	private final static boolean DEV_MODE = true;

	/**
	 * Log4j logger
	 */
	private static final Logger logger = Logger.getLogger(WinterfacePlugin.class);

	private static final long MAX_THREADED_UNLOAD_WAIT_TIME = SECONDS.toMillis(60);

	public WinterfacePlugin() {
		config = new Configuration();
	}

	@Override
	public void runPlugin(PluginRespirator pr) {
		node = pr.getNode();
		
		// Load path
		plugin_path = this.getClass().getClassLoader().getResource(".");
		// Get plugin main thread name
		winterface_thread_name = getWinterfaceThreadName();
		
		// Register logger and so on
		logger.debug("Loaded WinterFacePlugin on path " + plugin_path);

		// Templates are stored in jars on the classpath.
		Properties properties = new Properties();
		properties.setProperty("resource.loader", "class");
		properties.setProperty("class.resource.loader.class",
		                       "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		Velocity.init(properties);

		// initServer();
		serverManager = new ServerManager();
		serverManager.startServer(DEV_MODE, config, new NodeFreenetInterface(pr.getNode()), this);
	}

	@Override
	public void terminate() {
		serverManager.terminateServer();
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	/**
	 * Just for test cases if {@link Node} is not needed
	 * 
	 * @param args
	 *            start arguments
	 */
	public static void main(String[] args) {
		WinterfacePlugin p = new WinterfacePlugin();
		p.runPlugin(null);
	}

	@Override
	public String getString(String arg0) {
		// TODO Part of FredPluginL10n (which is not yet implemented)
		// So we just ignore the translation for now
		return arg0;
	}

	@Override
	public void setLanguage(LANGUAGE arg0) {
		// TODO Part of FredPluginL10n (which is not yet implemented)
		// Cannot throw exception, otherwise the plug-in wont start!
		// throw new UnsupportedOperationException("Not implemented.");
	}

	@Override
	public void setupConfig(SubConfig subconfig) {
		config.initialize(subconfig);
	}
	
	public boolean reload() {
		terminate();
		
		final PluginManager pm = node.getPluginManager();
		final String fn = plugin_path.toString();
		
		pm.startPluginAuto(fn, true);
		
		pm.killPlugin(winterface_thread_name, MAX_THREADED_UNLOAD_WAIT_TIME, true);
		//TODO Add purge option (remove from cache)
		return true;
	}
	
	private static String getWinterfaceThreadName() {
		return Thread.currentThread().getName();
	}

}
