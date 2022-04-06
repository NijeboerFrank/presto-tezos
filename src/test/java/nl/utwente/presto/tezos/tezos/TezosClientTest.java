package nl.utwente.presto.tezos.tezos;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TezosClientTest {
    /**
     * Requests a block at height one, and checks if the requested Block object's
     * hash is equal to the hardcoded hash of that block.
     * 
     * @throws IOException
     */
    @Test
    public void testBlock() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        Block block = tezosClient.getBlock(1);
        assertEquals(1, block.getHeight());
        assertEquals("BLSqrcLvFtqVCx8WSqkVJypW2kAVRM3eEj2BHgBsB6kb24NqYev", block.getHash());
    }

    /**
     * Requests multiple blocks at height 1 and 2, and checks if the heights of the
     * returned objects actually are 1 and 2.
     * 
     * @throws IOException
     */
    @Test
    public void testBlocks() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        List<Block> block = tezosClient.getBlocks(new long[] { 1, 2 });
        assertTrue(block.stream().anyMatch(b -> b.getHeight() == 1));
        assertTrue(block.stream().anyMatch(b -> b.getHeight() == 2));
    }

    /**
     * Requests multiple operations at height 1 and 2, and checks if the hash is
     * equal to the hardcoded hash of the first operation.
     * 
     * @throws IOException
     */
    @Test
    public void testOperations() throws IOException {
        TezosClient tezosClient = new TezosClient("https://api.tzstats.com");
        List<Operation> operations = tezosClient.getOperations(new long[] { 1, 2 });
        assertTrue(operations.stream()
                .anyMatch(b -> b.getHash().equals("oneDGhZacw99EEFaYDTtWfz5QEhUW3PPVFsHa7GShnLPuDn7gSd")));
    }
}
