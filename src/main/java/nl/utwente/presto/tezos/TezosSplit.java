package nl.utwente.presto.tezos;

import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.HostAddress;
import com.facebook.presto.spi.NodeProvider;
import com.facebook.presto.spi.schedule.NodeSelectionStrategy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class TezosSplit implements ConnectorSplit {

    private final TezosTable table;
    private final long blockId;
    private final long electionId;
    private final long proposalId;


    @JsonCreator
    public TezosSplit(
            @JsonProperty("table") TezosTable table,
            @JsonProperty("blockId") long blockId,
            @JsonProperty("electionId") long electionId,
            @JsonProperty("proposalId") long proposalId)
    {
        this.table = table;
        this.blockId = blockId;
        this.electionId = electionId;
        this.proposalId = proposalId;
    }

    public static TezosSplit forBlock(long blockId) {
        return new TezosSplit(TezosTable.BLOCK, blockId, 0, 0);
    }

    public static TezosSplit forProposal(long proposalId) {
        return new TezosSplit(TezosTable.ELECTION, 0, 0, proposalId);
    }

    public static TezosSplit forElection(long electionId) {
        return new TezosSplit(TezosTable.ELECTION, 0, electionId, 0);
    }
    public
    static TezosSplit forContract(long block_height) { //TODO slaat nog nergens op
        return new TezosSplit(TezosTable.CONTRACT, block_height, 0, 0);
    }

    @JsonProperty
    public TezosTable getTable() {
        return table;
    }

    @JsonProperty
    public long getBlockId() {
        return blockId;
    }

    @JsonProperty
    public long getElectionId() {
        return electionId;
    }

    @JsonProperty
    public long getProposalId() {
        return proposalId;
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
