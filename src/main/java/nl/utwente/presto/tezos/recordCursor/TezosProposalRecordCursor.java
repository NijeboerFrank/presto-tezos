package nl.utwente.presto.tezos.recordCursor;

import com.google.common.collect.ImmutableList;
import io.airlift.log.Logger;
import nl.utwente.presto.tezos.TezosMetadata;
import nl.utwente.presto.tezos.TezosTable;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.tezos.Proposal;
import nl.utwente.presto.tezos.tezos.TezosClient;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class TezosProposalRecordCursor extends BaseTezosRecordCursor {
    private static final Logger log = Logger.get(TezosBlockRecordCursor.class);

    private final Iterator<Proposal> proposalIter;

    private final TezosTable table;

    public TezosProposalRecordCursor(List<TezosColumnHandle> columnHandles, Proposal proposal, TezosTable table,
            TezosClient tezosClient) {

        super(columnHandles);

        this.table = table;

        if (proposal != null) {
            this.proposalIter = ImmutableList.of(proposal).iterator();
        } else {
            this.proposalIter = Collections.emptyIterator();
        }
    }

    @Override
    public long getCompletedBytes() {
        return 0;
    }

    @Override
    public boolean advanceNextPosition() {
        if (table != TezosTable.PROPOSAL || !proposalIter.hasNext()) {
            return false;
        }

        ImmutableList.Builder<Supplier> builder = ImmutableList.builder();
        Proposal proposal = proposalIter.next();
        builder.add(proposal::getRowId);
        builder.add(proposal::getHash);
        builder.add(proposal::getHeight);
        builder.add(proposal::getTime);
        builder.add(proposal::getSourceId);
        builder.add(proposal::getOpId);
        builder.add(proposal::getElectionId);
        builder.add(proposal::getVotingPeriod);
        builder.add(proposal::getRolls);
        builder.add(proposal::getVoters);
        builder.add(proposal::getSource);
        builder.add(proposal::getOp);

        this.suppliers = builder.build();
        return true;
    }
}
