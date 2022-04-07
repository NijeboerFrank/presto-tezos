package nl.utwente.presto.tezos.recordCursor;

import com.google.common.collect.ImmutableList;
import nl.utwente.presto.tezos.TezosTable;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.tezos.Proposal;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class TezosProposalRecordCursor extends BaseTezosRecordCursor {
    private final Iterator<Proposal> proposalIter;

    private final TezosTable table;

    public TezosProposalRecordCursor(List<TezosColumnHandle> columnHandles, List<Proposal> proposals, TezosTable table) {
        super(columnHandles);

        this.table = table;

        this.proposalIter = requireNonNull(proposals, "proposals is null").iterator();
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
