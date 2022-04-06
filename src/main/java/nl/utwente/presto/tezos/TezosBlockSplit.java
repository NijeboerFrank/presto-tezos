package nl.utwente.presto.tezos;

import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.HostAddress;
import com.facebook.presto.spi.NodeProvider;
import com.facebook.presto.spi.schedule.NodeSelectionStrategy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class TezosBlockSplit extends BaseTezosSplit {
    private final long blockId;
    private final String blockHash;

    @JsonCreator
    public TezosBlockSplit(
            @JsonProperty("blockId") long blockId,
            @JsonProperty("table") TezosTable table
    ) {
        super(table);
        this.blockId = blockId;
        this.blockHash = null;
    }

    @JsonProperty
    public long getBlockId() {
        return blockId;
    }

    @JsonProperty
    public String getBlockHash() {
        return blockHash;
    }

}
