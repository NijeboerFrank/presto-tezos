package nl.utwente.presto.tezos;

import com.facebook.presto.common.type.Type;
import com.facebook.presto.spi.RecordCursor;
import com.facebook.presto.spi.RecordSet;
import com.google.common.collect.ImmutableList;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.recordCursor.TezosBlockRecordCursor;
import nl.utwente.presto.tezos.recordCursor.TezosElectionRecordCursor;
import nl.utwente.presto.tezos.recordCursor.TezosProposalRecordCursor;
import nl.utwente.presto.tezos.tezos.Block;
import nl.utwente.presto.tezos.tezos.Election;
import nl.utwente.presto.tezos.tezos.Proposal;
import nl.utwente.presto.tezos.tezos.TezosClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class TezosRecordSet implements RecordSet {
    private final TezosSplit split;
    private final TezosClient tezosClient;

    private final List<TezosColumnHandle> columnHandles;
    private final List<Type> columnTypes;

    TezosRecordSet(TezosClient tezosClient, List<TezosColumnHandle> columnHandles, TezosSplit split) {
        this.split = requireNonNull(split, "split is null");
        this.tezosClient = requireNonNull(tezosClient, "TezosClient is null");

        this.columnHandles = requireNonNull(columnHandles, "columnHandles is null");

        this.columnTypes = columnHandles.stream()
                .map(TezosColumnHandle::getType)
                .collect(Collectors.toList());
    }

    @Override
    public List<Type> getColumnTypes() {
        return columnTypes;
    }

    @Override
    public RecordCursor cursor() {

        switch (split.getTable()) {
            case BLOCK:
                List<Block> blocks = null;
                try {
                    switch (split.getType()) {
                        case BLOCK:
                            blocks = ImmutableList.of(tezosClient.getBlock(split.getBlockId()));
                            break;
                        case BLOCK_RANGE:
                            blocks = tezosClient.getBlocks(split.getBlockStartId(), split.getBlockEndId());
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new TezosBlockRecordCursor(columnHandles, blocks, split.getTable());
            case ELECTION:
                List<Election> elections = null;
                try {
                    switch (split.getType()) {
                        case ELECTION:
                            elections = ImmutableList.of(tezosClient.getElection(split.getElectionId()));
                            break;
                        case ELECTION_RANGE:
                            elections = tezosClient.getElections(split.getElectionStartId(), split.getElectionEndId());
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new TezosElectionRecordCursor(columnHandles, elections, split.getTable());
            case PROPOSAL:
                List<Proposal> proposals = null;
                try {
                    switch (split.getType()) {
                        case PROPOSAL:
                            proposals = ImmutableList.of(tezosClient.getProposal(split.getProposalId()));
                            break;
                        case PROPOSAL_RANGE:
                            proposals = tezosClient.getProposals(split.getProposalStartId(), split.getProposalEndId());
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new TezosProposalRecordCursor(columnHandles, proposals, split.getTable());
            default:
                return null;
        }
    }
}
