package freenet.winterface.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.keys.FreenetURI;
import freenet.support.api.Bucket;
import freenet.support.io.BucketTools;
import freenet.winterface.freenet.FreenetInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Fetch USK page.
 */
public class Root extends HttpServlet {

	public Root() {
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("modal_key") != null) {
			response.sendRedirect("/" + request.getParameter("modal_key"));
		} else {
			response.sendRedirect("/");
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getRequestURI().equals("/")) {
			response.sendRedirect("/dashboard");
		} else if (request.getRequestURI().startsWith("/USK@")) {
			FreenetInterface freenetInterface = (FreenetInterface) getServletContext().getAttribute("freenet-interface");
			FetchResult result = null;
			try {
				// Remove "/" from the beginning of the requested URI, fetch the document
				result = freenetInterface.fetchURI(new FreenetURI(request.getRequestURI().substring(1)));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FetchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (result != null) {
				response.setContentType(result.getMimeType());
		        response.setStatus(HttpServletResponse.SC_OK);
				OutputStream resOutStream = response.getOutputStream();

				Bucket resultBucket = result.asBucket();
				try {
					BucketTools.copyTo(resultBucket, resOutStream, Long.MAX_VALUE);
					resOutStream.flush();
					resOutStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resultBucket.free();
			}
		} else {
			response.sendRedirect("/invalidkey");
		}
		
	}

}