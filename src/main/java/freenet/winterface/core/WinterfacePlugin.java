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
import freenet.pluginmanager.FredPluginHTTP;
import freenet.pluginmanager.FredPluginVersioned;
import freenet.pluginmanager.PluginHTTPException;
import freenet.pluginmanager.PluginManager;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.api.HTTPRequest;
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
public class WinterfacePlugin implements FredPlugin, FredPluginVersioned, FredPluginConfigurable, FredPluginHTTP {

	/**
	 * {@link URL} at which {@link WinterfacePlugin} resides
	 */
	private URL plugin_path;
	
	/**
	 * {@link String} name of {@link WinterfacePlugin} main thread
	 */
	private static String winterface_thread_name;
	
	/**
	 * The instance of freenet node that loaded Winterface
	 */
	private Node node;
	
	/**
	 * Interface for HighLevelSimpleClient
	 */
	private HighLevelSimpleClientInterface client;

	/**
	 * An instance of {@link ServerManager} for all {@link Server} related
	 * functionalities
	 */
	private ServerManager serverManager;

	/** Configuration */
	private final Configuration config;
	
	/** Response to Visit in fproxy plugins page (for FredPluginHTTP) */
	private String redirectToWinterface;

	/** Current version */
	private final static String VERSION = "0.2";
	
	/** PluginL10n localization */
	private final I18n i18n = new I18n();

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
		winterface_thread_name = Thread.currentThread().getName();
		
		// Register logger and so on
		logger.debug("Loaded WinterFacePlugin on path " + plugin_path);

		// Templates are stored in jars on the classpath.
		Properties properties = new Properties();
		properties.setProperty("resource.loader", "class");
		properties.setProperty("class.resource.loader.class",
		                       "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		Velocity.init(properties);
		
		client = new HighLevelSimpleClientInterface(pr.getHLSimpleClient());

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
	public String getString(String i18nString) {
		return i18n.getString(i18nString);
	}

	@Override
	public void setLanguage(LANGUAGE language) {
		i18n.setLanguage(language);
	}

	@Override
	public void setupConfig(SubConfig subconfig) {
		config.initialize(subconfig);
		redirectToWinterface = "<head><meta http-equiv=\"refresh\" content=\"0; url=http://127.0.0.1:" + config.getPort() + "\" /></head>Redirecting to Winterface... " + "http://127.0.0.1:" + config.getPort();
	}
	
	public boolean reload() {
		//FIXME Use FCP messaging instead of the pluginManager directly
		try {
			Thread.sleep(SECONDS.toMillis(2));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final PluginManager pm = node.getPluginManager();
		final String fn = PluginFreenetInterface.getPluginSpecification(pm, winterface_thread_name);
		
		terminate();
		
		pm.startPluginAuto(fn, true);
		
		pm.killPlugin(winterface_thread_name, MAX_THREADED_UNLOAD_WAIT_TIME, true);
		//TODO Add purge option (remove from cache)
		
		return true;
	}

	public static String getWinterfaceThreadName() {
		return winterface_thread_name;
	}

	@Override
	public String handleHTTPGet(HTTPRequest request) throws PluginHTTPException {
		return redirectToWinterface;
	}

	@Override
	public String handleHTTPPost(HTTPRequest request) throws PluginHTTPException {
		return redirectToWinterface;
	}

}
