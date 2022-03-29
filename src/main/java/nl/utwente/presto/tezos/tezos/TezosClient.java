package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class TezosClient {
    private final String endpoint;

    public TezosClient(String endpoint) {
        this.endpoint = endpoint;
    }

    public Block getHeadBlock() throws IOException {
        return getBlock("head");
    }

    public Block getBlock(long height) throws IOException {
        return getBlock(String.valueOf(height));
    }

    public Block getBlock(String hash) throws IOException {
        try {
            String json = doGetRequest(endpoint+"/explorer/block/"+hash);
            return new ObjectMapper()
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .readValue(json,  Block.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get block "+hash);
        }
    }

    private String doGetRequest(String url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) (new URL(url)).openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        return new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
