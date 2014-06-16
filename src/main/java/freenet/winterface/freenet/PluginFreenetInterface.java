package freenet.winterface.freenet;

import static java.util.concurrent.TimeUnit.SECONDS;

import freenet.node.Node;
import freenet.node.PeerManager;
import freenet.pluginmanager.PluginInfoWrapper;
import freenet.pluginmanager.PluginManager;

import com.google.common.annotations.VisibleForTesting;

//TODO Implements FreenetInterface?
public class PluginFreenetInterface{
	
	private static final long MAX_THREADED_UNLOAD_WAIT_TIME = SECONDS.toMillis(60);
	
	private final Node node;
	private final PluginManager pluginManager;
	
	public PluginFreenetInterface(Node node) {
		this(node, node.pluginManager);
	}
	
	@VisibleForTesting
	PluginFreenetInterface(Node node, PluginManager pluginManager) {
		this.node = node;
		this.pluginManager = pluginManager;
	}
	
	public boolean reloadPlugin(String pluginThreadName) {
		/* Expect thread name to be something like:
		 * "pfreenet.winterface.core.WinterfacePlugin_154014970";
		 */

		String tmpfn = null;
		
		for(PluginInfoWrapper pi: pluginManager.getPlugins()) {
			if (pi.getThreadName().equals(pluginThreadName)) {
				tmpfn = pi.getFilename();
			}
		}
		//TODO Show error message if plugin not found (fn==null)
		if (tmpfn==null) {
			return false;
		}
		
		final String fn = tmpfn;
		
		pluginManager.killPlugin(pluginThreadName, MAX_THREADED_UNLOAD_WAIT_TIME, true);
		//TODO Add purge option (remove from cache)

		node.executor.execute(new Runnable() {

			@Override
			public void run() {
				// FIXME
				pluginManager.startPluginAuto(fn, true);
			}

		});
		return true;
	}

}
