package nl.utwente.presto.tezos.tezos;

import com.google.inject.Inject;
import nl.utwente.presto.tezos.connector.TezosConnectorConfig;

public class TezosClientProvider {

    private final TezosClient tezosClient;

    @Inject
    public TezosClientProvider(TezosConnectorConfig config) {
        tezosClient = new TezosClient(config.getTezosEndpoint());
    }

    public TezosClient getTezosClient() {
        return this.tezosClient;
    }
}
