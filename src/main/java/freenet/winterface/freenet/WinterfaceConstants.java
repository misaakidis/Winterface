package freenet.winterface.freenet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.context.Context;

import freenet.node.SecurityLevels.NETWORK_THREAT_LEVEL;
import freenet.node.SecurityLevels.PHYSICAL_THREAT_LEVEL;
import freenet.node.useralerts.UserAlert;

public class WinterfaceConstants {
	
	private static final Map<String, String> constants = new HashMap<String,String>();
	
	public WinterfaceConstants() {
		initConstants();
	}
	
	// Constants that need to be accessed by Velocity templates
	// When possible refer to the corresponding value of fred
	private void initConstants() {
		/** Alert classes codes */
		constants.put("ALERT_CRITICAL_ERROR", String.valueOf(UserAlert.CRITICAL_ERROR));
		constants.put("ALERT_ERROR", String.valueOf(UserAlert.ERROR));
		constants.put("ALERT_WARNING", String.valueOf(UserAlert.WARNING));
		constants.put("ALERT_MINOR", String.valueOf(UserAlert.MINOR));
		
		/** Security levels */
		constants.put("NETWORK_THREAT_LEVEL_LOW", String.valueOf(NETWORK_THREAT_LEVEL.LOW));
		constants.put("NETWORK_THREAT_LEVEL_NORMAL", String.valueOf(NETWORK_THREAT_LEVEL.NORMAL));
		constants.put("NETWORK_THREAT_LEVEL_HIGH", String.valueOf(NETWORK_THREAT_LEVEL.HIGH));
		constants.put("NETWORK_THREAT_LEVEL_MAXIMUM", String.valueOf(NETWORK_THREAT_LEVEL.MAXIMUM));
		constants.put("PHYSICAL_THREAT_LEVEL_LOW", String.valueOf(PHYSICAL_THREAT_LEVEL.LOW));
		constants.put("PHYSICAL_THREAT_LEVEL_NORMAL", String.valueOf(PHYSICAL_THREAT_LEVEL.NORMAL));
		constants.put("PHYSICAL_THREAT_LEVEL_HIGH", String.valueOf(PHYSICAL_THREAT_LEVEL.HIGH));
		constants.put("PHYSICAL_THREAT_LEVEL_MAXIMUM", String.valueOf(PHYSICAL_THREAT_LEVEL.MAXIMUM));
	}
	
	public void addConstantsToContext(Context ctx) {
		// Adds constants to context so they can be accessible by Velocity templates as $win_{constant}
		// e.g. $win_ALERT_WARNING
		for (Entry<String, String> constant : constants.entrySet()) {
			ctx.put("win_" + constant.getKey(), constant.getValue());
		}
	}

}
