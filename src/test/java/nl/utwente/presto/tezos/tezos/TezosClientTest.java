package nl.utwente.presto.tezos.tezos;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TezosClientTest {
    @Test
    public void testBlock() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Block block = tezosClient.getBlock(1);
        assertEquals(1, block.getHeight());
        assertEquals("BLSqrcLvFtqVCx8WSqkVJypW2kAVRM3eEj2BHgBsB6kb24NqYev", block.getHash());
    }

//    @Test
//    public void testBlocks() throws IOException {
//        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
//        List<Block> block = tezosClient.getBlocks(new long[] { 1, 2 });
//        assertTrue(block.stream().anyMatch(b -> b.getHeight() == 1));
//        assertTrue(block.stream().anyMatch(b -> b.getHeight() == 2));
//    }
}
