package nl.utwente.presto.tezos;

import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.HostAddress;
import com.facebook.presto.spi.NodeProvider;
import com.facebook.presto.spi.schedule.NodeSelectionStrategy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class TezosElectionSplit extends BaseTezosSplit {
    private final long proposalId;

    @JsonCreator
    public TezosElectionSplit(
            @JsonProperty("proposalId") long proposalId,
            @JsonProperty("table") TezosTable table
    ) {
        super(table);
        this.proposalId = proposalId;
    }

    @JsonProperty
    public long getProposalId() {
        return proposalId;
    }


}
