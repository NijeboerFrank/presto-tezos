package nl.utwente.presto.tezos;

import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.HostAddress;
import com.facebook.presto.spi.NodeProvider;
import com.facebook.presto.spi.schedule.NodeSelectionStrategy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class TezosElectionSplit implements BaseTezosSplit {
    private final long proposalId;

    private final TezosTable table;

    @JsonCreator
    public TezosElectionSplit(
            @JsonProperty("proposalId") long proposalId,
            @JsonProperty("table") TezosTable table
    ) {
        this.proposalId = proposalId;
        this.table = table;
    }

    @JsonProperty
    public long getProposalId() {
        return proposalId;
    }

    @JsonProperty
    public TezosTable getTable() {
        return table;
    }

    @Override
    public NodeSelectionStrategy getNodeSelectionStrategy() {
        return NodeSelectionStrategy.NO_PREFERENCE;
    }

    @Override
    public List<HostAddress> getPreferredNodes(NodeProvider nodeProvider) {
        return Collections.emptyList();
    }

    @Override
    public Object getInfo() {
        return this;
    }
}
