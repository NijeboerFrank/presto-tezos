package nl.utwente.presto.tezos.connector;

import io.airlift.configuration.Config;

public class TezosConnectorConfig {
    private String endpoint;

    @Config("tezos.endpoint")
    public TezosConnectorConfig setTezosEndpoint(String tezosEndpoint) {
        this.endpoint = tezosEndpoint;
        return this;
    }

    public String getTezosEnpoint() {return endpoint;}
}
