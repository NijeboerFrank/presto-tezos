package nl.utwente.presto.tezos.handle;

import com.facebook.presto.spi.ConnectorTableLayoutHandle;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import nl.utwente.presto.tezos.TezosRange;

import java.util.List;

import static java.util.Objects.requireNonNull;

@ToString
public class TezosTableLayoutHandle implements ConnectorTableLayoutHandle {
    private final TezosTableHandle table;

    private final List<TezosRange> blockRanges;

    @JsonCreator
    public TezosTableLayoutHandle(
            @JsonProperty("table") TezosTableHandle table,
            @JsonProperty("blockRanges") List<TezosRange> blockRanges
    ) {
        this.table = requireNonNull(table, "table is null");
        this.blockRanges = requireNonNull(blockRanges, "blockRanges is null");
    }

    @JsonProperty
    public TezosTableHandle getTable() {
        return table;
    }

    @JsonProperty
    public List<TezosRange> getBlockRanges() {
        return blockRanges;
    }
}
