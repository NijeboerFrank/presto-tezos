package nl.utwente.presto.tezos;

import com.facebook.presto.common.type.Type;
import com.facebook.presto.spi.RecordCursor;
import com.facebook.presto.spi.RecordSet;
import com.google.common.collect.ImmutableList;
import io.airlift.log.Logger;
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
import java.util.stream.LongStream;

import static java.util.Objects.requireNonNull;

public class TezosRecordSet implements RecordSet {
    private static final Logger log = Logger.get(TezosRecordSet.class);

    private final TezosSplit split;
    private final TezosClient tezosClient;

    private final List<TezosColumnHandle> columnHandles;
    private final List<Type> columnTypes;

    private long index = 0;

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
                return new TezosBlockRecordCursor(columnHandles, blocks, split.getTable(), tezosClient);
            case ELECTION:
                Election election = null;
                try {
                    election = tezosClient.getElection(split.getElectionId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new TezosElectionRecordCursor(columnHandles, election, split.getTable(), tezosClient);
            case PROPOSAL:
                Proposal proposal = null;
                try {
                    proposal = tezosClient.getProposal(split.getProposalId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new TezosProposalRecordCursor(columnHandles, proposal, split.getTable(), tezosClient);
            default:
                return null;
        }
    }
}
