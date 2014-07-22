package freenet.winterface.freenet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.context.Context;

import freenet.node.useralerts.UserAlert;

public class WinterfaceConstants {
	
	private static final Map<String, String> constants = new HashMap<String,String>();
	
	public WinterfaceConstants() {
		initConstants();
	}
	
	private void initConstants() {
		/** Alert classes codes */
		constants.put("ALERT_CRITICAL_ERROR", String.valueOf(UserAlert.CRITICAL_ERROR));
		constants.put("ALERT_ERROR", String.valueOf(UserAlert.ERROR));
		constants.put("ALERT_WARNING", String.valueOf(UserAlert.WARNING));
		constants.put("ALERT_MINOR", String.valueOf(UserAlert.MINOR));
		
	}
	
	public void addConstantsToContext(Context ctx) {
		for (Entry<String, String> constant : constants.entrySet()) {
			ctx.put("win_" + constant.getKey(), constant.getValue());
		}
	}

}
