package nl.utwente.presto.tezos.tezos;

import nl.utwente.presto.tezos.connector.TezosConnectorConfig;

public class TezosClientProvider {

    private final TezosClient tezosClient;

    public TezosClientProvider(TezosConnectorConfig config) {

        tezosClient = new TezosClient(config.getTezosEndpoint());
    }

    public TezosClient getTezosClient() {

        return this.tezosClient;
    }
}
