package freenet.winterface.web;

import freenet.winterface.core.VelocityBase;

import freenet.client.FetchException;

import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Invalid key page.
 */
public class InvalidKey extends VelocityBase {

	@Override
	protected void subFillContext(Context context, HttpServletRequest request) {
	    Map params = request.getParameterMap();
	    context.put("params", params);
	}
	
	/**
	 * Gives the URI fragment part for the given exception, or {@code null} if unknown.
	 * @param e the exception
	 * @param fproxyUri the URI we can give to FProxy to retry, or either {@code null} or the empty
	 * String if unknown
	 */
	public static String getErrorFragment(Exception e, String fproxyUri)
	        throws UnsupportedEncodingException {
	    if ("".equals(fproxyUri)) {
	        fproxyUri = null;
	    }
	    if (e instanceof FetchException) {
	        String msg = URLEncoder.encode(((FetchException)e).toUserFriendlyString(), "UTF-8");
	        String uri = fproxyUri == null ? null : URLEncoder.encode(fproxyUri, "UTF-8");
	        if (uri != null) {
	            return "error=fetch&errmsg=" + msg + "&rediruri=" + uri;
	        }
	        return "errmsg=" + msg;
	    }
	    return null;
	}
}

