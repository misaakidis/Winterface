package freenet.winterface.web.markup;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import freenet.node.NodeStarter;
import freenet.node.Version;

/**
 * {@link DashboardPanel} showing node's version information
 * 
 * @author pausb
 */
@SuppressWarnings("serial")
public class VersionPanel extends DashboardPanel {

	/**
	 * Constructs
	 * 
	 * @param id
	 *            {@link Component} markup ID
	 */
	public VersionPanel(String id) {
		super(id);
	}
	
	@Override
	protected void onInitialize() {
		// TODO Add localization support
		super.onInitialize();
		// Freenet Version info
		String freenet = "Freenet " + Version.publicVersion + " Build #" + Version.buildNumber() + " " + Version.cvsRevision();
		add(new Label("freenet-ver", Model.of(freenet)));
		// Freenet-ext version info
		add(new Label("freenet-ext-ver", Model.of("Freenet-ext Build #" + NodeStarter.extBuildNumber + " " + NodeStarter.extRevisionNumber)));
	}

	@Override
	public String getName() {
		return "Version";
	}

}
