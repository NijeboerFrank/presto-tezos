package nl.utwente.presto.tezos.tezos;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TezosClientTest {
    @Test
    public void testBlock() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Block block = tezosClient.getBlock(1);
        assertEquals(1, block.getHeight());
        assertEquals("BLSqrcLvFtqVCx8WSqkVJypW2kAVRM3eEj2BHgBsB6kb24NqYev", block.getHash());
    }
}
