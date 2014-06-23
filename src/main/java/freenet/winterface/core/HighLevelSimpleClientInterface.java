package freenet.winterface.core;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.client.HighLevelSimpleClient;
import freenet.keys.FreenetURI;
import freenet.node.Node;
import freenet.node.NodeClientCore;
import freenet.node.RequestStarter;

public class HighLevelSimpleClientInterface {

	static private HighLevelSimpleClient client;
	static private Node node;
	
	public HighLevelSimpleClientInterface(Node node, NodeClientCore core) {
		this.node = node;
		this.client = node.clientCore.makeClient(RequestStarter.INTERACTIVE_PRIORITY_CLASS, false, false);
	}
	
	public HighLevelSimpleClientInterface(HighLevelSimpleClient hlSimpleClient) {
		this.client = hlSimpleClient;
	}

	public static FetchResult fetchURI(FreenetURI uri) throws FetchException {
		FetchResult result = client.fetch(uri);
		return result;
	}
	
	
}
