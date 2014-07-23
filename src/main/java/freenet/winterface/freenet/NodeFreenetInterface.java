package freenet.winterface.freenet;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.FluentIterable.from;
import static java.util.Arrays.asList;

import java.util.List;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.clients.http.bookmark.BookmarkCategory;
import freenet.clients.http.bookmark.BookmarkItem;
import freenet.keys.FreenetURI;
import freenet.node.DarknetPeerNode;
import freenet.node.Node;
import freenet.node.NodeStarter;
import freenet.node.PeerManager;
import freenet.node.Version;
import freenet.node.useralerts.UserAlert;
import freenet.winterface.core.HighLevelSimpleClientInterface;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;

/**
 * {@link FreenetInterface} implementation that uses a {@link Node} instance.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NodeFreenetInterface implements FreenetInterface {

	private final Node node;
	private final PeerManager peerManager;

	private final UserAlertManagerInterface uamInterface;
	private final BookmarkFreenetInterface bmInterface;

	public NodeFreenetInterface(Node node) {
		this(node, node.peers, new BookmarkFreenetInterface(node), new UserAlertManagerInterface(node.clientCore.alerts));
	}

	@VisibleForTesting
	NodeFreenetInterface(Node node, PeerManager peerManager, BookmarkFreenetInterface bmInterface, UserAlertManagerInterface uamInterface) {
		//TODO get winterface plugin instance, substitute the one put in context
		this.node = node;
		this.peerManager = peerManager;
		this.bmInterface = bmInterface;
		this.uamInterface = uamInterface;
	}
	
	@Override
	public String publicVersion() {
		return Version.publicVersion();
	}
	
	@Override
	public int buildNumber() {
		return Version.buildNumber();
	}
	
	@Override
	public String cvsRevision() {
		return Version.cvsRevision();
	}
	
	@Override
	public String extRevisionNumber() {
		return NodeStarter.extRevisionNumber;
	}

	@Override
	public ConnectionOverview getConnections() {
		int numberOfCurrentConnections = getNumberOfCurrentConnections();
		int numberOfMaximumConnections = getNumberOfMaximumConnections();
		return new ConnectionOverview(numberOfCurrentConnections, numberOfMaximumConnections);
	}

	private int getNumberOfCurrentConnections() {
		return peerManager.countConnectedPeers();
	}

	private int getNumberOfMaximumConnections() {
		if (node.isOpennetEnabled()) {
			return node.getOpennet().getNumberOfConnectedPeersToAimIncludingDarknet();
		}
		return from(asList(peerManager.getDarknetPeers())).filter(notNull()).filter(enabledPeers()).size();
	}

	private Predicate<? super DarknetPeerNode> enabledPeers() {
		return new Predicate<DarknetPeerNode>() {
			@Override
			public boolean apply(DarknetPeerNode darknetPeerNode) {
				return !darknetPeerNode.isDisabled();
			}
		};
	}

	@Override
	public FetchResult fetchURI(FreenetURI uri) throws FetchException {
		return HighLevelSimpleClientInterface.fetchURI(uri);
	}

	@Override
	public List<BookmarkCategory> getBookmarkCategories() {
		return bmInterface.getBookmarkCategories();
	}

	@Override
	public List<BookmarkItem> getBookmarksFromCat(BookmarkCategory cat) {
		return bmInterface.getBookmarksFromCat(cat);
	}
	
	@Override
	public UserAlert[] getAlerts() {
		return uamInterface.getAlerts();
	}
	
	@Override
	public int getValidAlertCount() {
		return uamInterface.getValidAlertCount();
	}
	
	@Override
	public int alertClass(UserAlert alert) {
		return alert.getPriorityClass();
	}
	
	@Override
	public void dismissAlert(int alertHashCode) {
		uamInterface.dismissAlert(alertHashCode);
	}
	
}
