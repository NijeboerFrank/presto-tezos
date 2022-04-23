package nl.utwente.presto.tezos.udfs;

import com.facebook.presto.common.type.StandardTypes;
import com.facebook.presto.spi.function.Description;
import com.facebook.presto.spi.function.ScalarFunction;
import com.facebook.presto.spi.function.SqlType;
import io.airlift.configuration.ConfigurationLoader;
import io.airlift.log.Logger;
import io.airlift.slice.Slice;
import nl.utwente.presto.tezos.tezos.TezosClient;

import java.io.IOException;
import java.util.Map;

public class TezosUDFs {
    private static final Logger log = Logger.get(TezosUDFs.class);
    private static final String ENDPOINT_KEY = "tezos.endpoint";
    private static final String CONFIG_PATH = "etc/catalog/tezos.properties";
    private static final TezosClient client;

    static {
        log.info("Initializing Tezos client in UDF...");
        ConfigurationLoader configLoader = new ConfigurationLoader();
        try {
            Map<String, String> config = configLoader.loadPropertiesFrom(CONFIG_PATH);
            client = new TezosClient(config.get(ENDPOINT_KEY));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load config from " + CONFIG_PATH);
        }
    }

    @ScalarFunction("tezos_blockHeight")
    @Description("Returns current block height")
    @SqlType(StandardTypes.BIGINT)
    public static long tezosBlockHeight() throws IOException {
        return client.getLastBlock().getHeight();
    }

    @ScalarFunction("tezos_getBalance")
    @Description("Returns the balance of an address")
    @SqlType(StandardTypes.DOUBLE)
    public static double tezosGetBalance(@SqlType(StandardTypes.VARCHAR) Slice address) throws IOException {
        return client.getAccount(address.toStringUtf8()).getSpendableBalance();
    }

    @ScalarFunction("tezos_getTransactionCount")
    @Description("Returns the number of transactions from this address")
    @SqlType(StandardTypes.BIGINT)
    public static long tezosGetTransactionCount(@SqlType(StandardTypes.VARCHAR) Slice address) throws IOException {
        return client.getAccount(address.toStringUtf8()).getNTx();
    }

    @ScalarFunction("fromMutez")
    @Description("Convert a mutez value to tez")
    @SqlType(StandardTypes.DOUBLE)
    public static double fromMutez(@SqlType(StandardTypes.DOUBLE) double mutez) {
        return mutez / 1000000;
    }

    @ScalarFunction("toMutez")
    @Description("Convert a tez value to mutez")
    @SqlType(StandardTypes.DOUBLE)
    public static double toMutez(@SqlType(StandardTypes.DOUBLE) double tez) {
        return tez * 1000000;
    }
}
