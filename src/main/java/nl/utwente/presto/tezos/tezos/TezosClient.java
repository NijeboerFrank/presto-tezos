package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TezosClient {
    private final String endpoint;

    public TezosClient(String endpoint) {
        this.endpoint = endpoint;
    }

    public Transaction getHeadTransaction() throws IOException {
        return getTransaction("head");
    }

    public Transaction getTransaction(long height) throws IOException {
        return getTransaction(String.valueOf(height));
    }

    public Transaction getTransaction(String hash) throws IOException {
        try {
            return new ObjectMapper().readValue(endpoint+"/explorer/blocks/"+hash,  Transaction.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get block "+hash);
        }
    }
}
