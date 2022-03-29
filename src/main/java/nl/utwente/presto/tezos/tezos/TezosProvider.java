package nl.utwente.presto.tezos.tezos;

import nl.utwente.presto.tezos.connector.TezosConnectorConfig;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.infura.InfuraHttpService;
import org.web3j.protocol.ipc.UnixIpcService;

public class TezosProvider {

    private final TezosClient tezosClient;

    public TezosProvider(TezosConnectorConfig config) {

        tezosClient = new TezosClient(config.getTezosEnpoint());
    }

    public TezosClient getTezosClient() {

        return this.tezosClient;
    }
}
