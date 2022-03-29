package nl.utwente.presto.tezos.tezos;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TezosClientTest {
    @Test
    public void test() throws IOException {
        TezosClient tezosClient = new TezosClient("http://127.0.0.1:8000");
        Transaction transaction = tezosClient.getTransaction(1);
        assertEquals(1, transaction.getHeight());
        assertEquals("BLSqrcLvFtqVCx8WSqkVJypW2kAVRM3eEj2BHgBsB6kb24NqYev", transaction.getHash());
    }
}
