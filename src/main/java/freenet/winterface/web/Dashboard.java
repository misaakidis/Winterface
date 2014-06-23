package freenet.winterface.web;

import java.net.MalformedURLException;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.keys.FreenetURI;
import freenet.winterface.core.HighLevelSimpleClientInterface;
import freenet.winterface.core.VelocityBase;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Test page for Velocity templates in Jetty.
 */
public class Dashboard extends VelocityBase {

	public Dashboard() {
		super("dashboard.vm");
	}

	@Override
	protected void subFillContext(Context context, HttpServletRequest request) {
	}
	
	@Override
	protected void fillContext(Context context, HttpServletRequest request) {
		if (request.getRequestURI().startsWith("/USK@")) {
			FetchResult result = null;
			try {
				result = HighLevelSimpleClientInterface.fetchURI(new FreenetURI(request.getRequestURI().substring(1)));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FetchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (result != null && result.getMimeType().equals("text/html")) {
				System.out.println("HTML successfully fetched");
			}
			return;
		} else {
			super.fillContext(context, request);
		}
		
	}
		
	@Override
	protected Template getTemplate(HttpServletRequest request, HttpServletResponse response) {
		if (request.getRequestURI().startsWith("/USK@")) {	
			return Velocity.getTemplate(templateFor("plain.vm"));
		} else {
			return Velocity.getTemplate(templateFor("index.vm"));
		}
	}
		
}