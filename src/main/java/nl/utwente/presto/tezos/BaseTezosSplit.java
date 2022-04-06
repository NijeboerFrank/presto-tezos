package nl.utwente.presto.tezos;

import com.facebook.presto.spi.ConnectorSplit;

public interface BaseTezosSplit extends ConnectorSplit {
    public TezosTable getTable();
}
