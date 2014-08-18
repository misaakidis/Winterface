package freenet.winterface.freenet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import freenet.node.DarknetPeerNode;
import freenet.node.Node;
import freenet.node.OpennetManager;
import freenet.node.PeerManager;

import org.junit.Test;

/**
 * Unit test for {@link NodeFreenetInterface}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NodeFreenetInterfaceTest {

	private final Node node = mock(Node.class);
	private final PeerManager peerManager = mock(PeerManager.class);
	private final FreenetInterface freenetInterface = new NodeFreenetInterface(node, peerManager, null, null, null);

	@Test
	public void canGetConnectionOverviewWithOpennetDisabled() {
		when(peerManager.countConnectedPeers()).thenReturn(1);
		DarknetPeerNode[] darknetPeerNodes = createDarknetNodes();
		when(peerManager.getDarknetPeers()).thenReturn(darknetPeerNodes);
		ConnectionOverview connectionOverview = freenetInterface.getConnections();
		assertThat(connectionOverview.getCurrent(), is(1));
		assertThat(connectionOverview.getMaximum(), is(3));
	}

	private DarknetPeerNode[] createDarknetNodes() {
		return new DarknetPeerNode[] {
				createEnabledDarknetPeerNode(),
				createDisabledDarknetPeerNode(),
				createEnabledDarknetPeerNode(),
				createDisabledDarknetPeerNode(),
				createEnabledDarknetPeerNode()
		};
	}

	private DarknetPeerNode createEnabledDarknetPeerNode() {
		return mock(DarknetPeerNode.class);
	}

	private DarknetPeerNode createDisabledDarknetPeerNode() {
		DarknetPeerNode darknetPeerNode = mock(DarknetPeerNode.class);
		when(darknetPeerNode.isDisabled()).thenReturn(true);
		return darknetPeerNode;
	}

	@Test
	public void canGetConnectionOverviewWithOpennetEnabled() {
		when(node.isOpennetEnabled()).thenReturn(true);
		OpennetManager opennetManager = mock(OpennetManager.class);
		when(opennetManager.getNumberOfConnectedPeersToAimIncludingDarknet()).thenReturn(7);
		when(node.getOpennet()).thenReturn(opennetManager);
		when(peerManager.countConnectedPeers()).thenReturn(2);
		ConnectionOverview connectionOverview = freenetInterface.getConnections();
		assertThat(connectionOverview.getCurrent(), is(2));
		assertThat(connectionOverview.getMaximum(), is(7));
	}

}
