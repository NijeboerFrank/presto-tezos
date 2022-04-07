package nl.utwente.presto.tezos.tezos;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TezosClientTest {
    @Test
    public void testBlock() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Block block = tezosClient.getBlock(1);
        assertEquals(1, block.getHeight());
        assertEquals("BLSqrcLvFtqVCx8WSqkVJypW2kAVRM3eEj2BHgBsB6kb24NqYev", block.getHash());
    }

    @Test
    public void testBlocks() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        List<Block> block = tezosClient.getBlocks(new long[] { 1, 2 });
        assertTrue(block.stream().anyMatch(b -> b.getHeight() == 1));
        assertTrue(block.stream().anyMatch(b -> b.getHeight() == 2));
    }
    @Test
    public void testContract() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Contract contract = tezosClient.getContract("KT1Puc9St8wdNoGtLiD2WXaHbWU7styaxYhD");
        assertEquals(818425L, contract.getAccountId());
    }

    @Test
    public void testLastContract() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Contract contract = tezosClient.getLastContract();
        assertNotNull(contract);
    }
    @Test
    public void testElections() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        List<Election> elections = tezosClient.getElections(new long[] { 1, 3 });
        assertTrue(elections.stream().anyMatch(b -> b.getRowId() == 1));
        assertTrue(elections.stream().anyMatch(b -> b.getRowId() == 3));
    }

    @Test
    public void testElection() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Election election = tezosClient.getElection(1);
        assertTrue(election.getRowId() == 1);
    }

    @Test
    public void testLastElection() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Election election = tezosClient.getLastElection();
        assertTrue(election.getRowId() > 37);
    }

    @Test
    public void testProposal() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Proposal proposal = tezosClient.getProposal(1);
        assertTrue(proposal.getRowId() == 1);
    }
}
