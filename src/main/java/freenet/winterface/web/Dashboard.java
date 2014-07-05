package freenet.winterface.web;

import freenet.winterface.core.VelocityBase;

import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;

/**
 * Dashboard page
 */
public class Dashboard extends VelocityBase {

	public Dashboard() {
		super("dashboard.vm");
	}

	@Override
	protected void subFillContext(Context context, HttpServletRequest request) {
	}
		
}