package freenet.winterface.freenet;

import freenet.client.FetchContext;
import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.client.FetchWaiter;
import freenet.client.HighLevelSimpleClient;
import freenet.keys.FreenetURI;
import freenet.node.Node;
import freenet.node.NodeClientCore;
import freenet.node.RequestClient;
import freenet.node.RequestStarter;

import com.db4o.ObjectContainer; // TODO remove after purge-db4o

/**
 * Fetch URIs through a {@link HighLevelSimpleClient} instance with interactive realtime priority.
 *
 * @author bertm
 */
public class InteractiveHLSCFreenetURIFetcher implements FreenetURIFetcher {
    private static final short REQUEST_PRIORITY = RequestStarter.INTERACTIVE_PRIORITY_CLASS;
    private static final boolean REQUEST_REALTIME = true;
    private static final RequestClient REQUEST_CLIENT = new RequestClient() {
        @Override
        public boolean persistent() {
            return false;
        }
        @Override
        public void removeFrom(ObjectContainer container) { // TODO remove after purge-db4o
            throw new UnsupportedOperationException();
        }
        @Override
        public boolean realTimeFlag() {
            return REQUEST_REALTIME;
        }
    };
    
    private final HighLevelSimpleClient client;
    
    /**
     * Constructs a URI fetcher from the given {@link HighLevelSimpleClient}.
     */
    public InteractiveHLSCFreenetURIFetcher(HighLevelSimpleClient client) {
        if (client == null) {
            throw new NullPointerException();
        }
        this.client = client;
    }

    /**
     * Constructs a URI fetcher from a new {@link HighLevelSimpleClient} from the
     * {@link NodeClientCore}.
     */
    public InteractiveHLSCFreenetURIFetcher(NodeClientCore clientCore) {
        this(clientCore.makeClient(REQUEST_PRIORITY, true, REQUEST_REALTIME));
    }

    /**
     * Constructs a URI fetcher from a new {@link HighLevelSimpleClient} from the
     * {@link Node}'s {@link Node#clientCore clientCore}.
     */
    public InteractiveHLSCFreenetURIFetcher(Node node) {
        this(node.clientCore);
    }

    @Override
    public FetchResult fetchURI(FreenetURI uri) throws FetchException {
        return internalFetchURI(uri, false);
    }

    @Override    
    public FetchResult filteredFetchURI(FreenetURI uri) throws FetchException {
        return internalFetchURI(uri, true);
    }
    
    private FetchResult internalFetchURI(FreenetURI uri, boolean filterData)
            throws FetchException {
        FetchWaiter waiter = new FetchWaiter();
        FetchContext ctx = client.getFetchContext();
        ctx.filterData = filterData;
        client.fetch(uri, REQUEST_CLIENT, waiter, ctx, REQUEST_PRIORITY);
        return waiter.waitForCompletion();
    }
}

