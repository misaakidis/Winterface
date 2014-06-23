package freenet.winterface.web;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.keys.FreenetURI;
import freenet.support.api.Bucket;
import freenet.support.io.BucketTools;
import freenet.winterface.core.HighLevelSimpleClientInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Fetch USK page.
 */
public class Fetch extends HttpServlet {

	public Fetch() {
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
		
		if (request.getRequestURI().startsWith("/fetch/USK@")) {
			FetchResult result = null;
			try {
				// Remove "/" from the beginning of the requested URI
				result = HighLevelSimpleClientInterface.fetchURI(new FreenetURI(request.getRequestURI().substring(7)));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FetchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (result != null && result.getMimeType().equals("text/html")) {
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
		}
		
	}

}