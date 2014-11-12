package freenet.winterface.freenet;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.keys.FreenetURI;

/**
 * Fetcher for Freenet URIs.
 *
 * @author bertm
 */
public interface FreenetURIFetcher {

    /*
     * Fetch a raw file from Freenet, waiting for the fetch to complete. This does not run the file
     * through any content filter.
     * @param uri the URI of the file
     * @throws FetchException When the fetch did not succeed.
     */
    public FetchResult fetchURI(FreenetURI uri) throws FetchException;
    
    /*
     * Fetch a file from Freenet, waiting for the fetch to complete, and filter its contents
     * according to its MIME type.
     * @param uri the URI of the file
     * @throws FetchException When the fetch did not succeed, or the file could not be filtered.
     */
    public FetchResult filteredFetchURI(FreenetURI uri) throws FetchException;
}

