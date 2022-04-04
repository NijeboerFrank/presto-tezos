package nl.utwente.presto.tezos.tezos;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TezosClientTest {
    private TezosClient tezosClient;

    @BeforeEach
    public void before() throws IOException {
        tezosClient = new TezosClient("https://api.tzstats.com");
    }
    @Test
    public void testBlock() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Block block = tezosClient.getBlock(1);
        assertEquals(1, block.getHeight());
        assertEquals("BLSqrcLvFtqVCx8WSqkVJypW2kAVRM3eEj2BHgBsB6kb24NqYev", block.getHash());
    }

    @Test
    public void testContract() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Contract contract = tezosClient.getContract("KT1Puc9St8wdNoGtLiD2WXaHbWU7styaxYhD");
        assertEquals(818425L, contract.getAccount_ID());
    }

//    @Test
//    public void testBlocks() throws IOException {
//        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
//        List<Block> block = tezosClient.getBlocks(new long[] { 1, 2 });
//        assertTrue(block.stream().anyMatch(b -> b.getHeight() == 1));
//        assertTrue(block.stream().anyMatch(b -> b.getHeight() == 2));
//    }
}
