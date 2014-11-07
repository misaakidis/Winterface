package freenet.winterface.core;

import org.apache.velocity.tools.generic.EscapeTool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Provides methods to escape user-supplied text.
 *
 * @author bertm
 */
public class Escaper {
    private static final EscapeTool ESCAPE_TOOL = new EscapeTool();

    /**
     * Constructs a new escaper. This escaper does not keep state and is thread-safe.
     */
    public Escaper() {
    }
    
    /**
     * URL-encode an url. For use in e.g. {@code <a href="…"></a>}.
     * @param url URL parts ({@code null} values are skipped)
     * @return The concatenated and URL-encoded URL, or {@code null} if all parts are {@code null}.
     */
    public String url(String... url) throws UnsupportedEncodingException {
        boolean onlyNull = true;
        StringBuilder sb = new StringBuilder();
        for (String s : url) {
            if (s != null) {
                sb.append(s);
                onlyNull = false;
            }
        }
        if (onlyNull) {
            return null;
        }
        // URLEncoder produces query strings, convert back to URL
        return URLEncoder.encode(sb.toString(), "UTF-8")
            .replaceAll("\\+", "%20")
            .replaceAll("\\%21", "!")
            .replaceAll("\\%26", "&")
            .replaceAll("\\%27", "'")
            .replaceAll("\\%28", "(")
            .replaceAll("\\%29", ")")
            .replaceAll("\\%2C", ",")
            .replaceAll("\\%2F", "/")
            .replaceAll("\\%7E", "~")
            .replaceAll("\\%3A", ":")
            .replaceAll("\\%3D", "=")
            .replaceAll("\\%3F", "?");
    }
    
    /**
     * HTML-encode the text.
     */
    public String text(String text) {
        if (text == null) {
            return null;
        }
        return ESCAPE_TOOL.html(text);
    }
}

