package freenet.winterface.freenet;

import java.util.Arrays;

import freenet.node.useralerts.UserAlert;
import freenet.node.useralerts.UserAlertManager;

public class UserAlertManagerInterface {
	
	private final UserAlertManager uam;

	public UserAlertManagerInterface(UserAlertManager uam) {
		this.uam = uam;
	}
	
	public UserAlert[] getAlerts() {
		return uam.getAlerts();
	}

	public void dismissAlert(int alertHashCode) {
		uam.dismissAlert(alertHashCode);
	}
	
	public int countAlerts() {
		int count = 0;
		for (UserAlert ua : getAlerts()) {
			if(ua.isValid()) {
				count++;
			}
		}
		return count;
	}

}