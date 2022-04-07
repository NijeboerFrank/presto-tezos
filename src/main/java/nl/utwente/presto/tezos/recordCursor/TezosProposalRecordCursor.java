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

    private final Proposal proposal;
    private final Iterator<Proposal> proposalIter;

    private final TezosTable table;

    public TezosProposalRecordCursor(List<TezosColumnHandle> columnHandles, Proposal proposal, TezosTable table,
            TezosClient tezosClient) {

        super(columnHandles);

        this.table = table;

        this.proposal = proposal;
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
        if (table == TezosTable.PROPOSAL && !proposalIter.hasNext()) {
            return false;
        }

        ImmutableList.Builder<Supplier> builder = ImmutableList.builder();
        if (table == TezosTable.PROPOSAL) {
            proposalIter.next();
            Proposal proposal = this.proposal;
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
        } else {
            return false;
        }

        this.suppliers = builder.build();
        return true;
    }

    private static String h32ToH20(String h32) {
        return "0x" + h32
                .substring(TezosMetadata.H32_BYTE_HASH_STRING_LENGTH - TezosMetadata.H20_BYTE_HASH_STRING_LENGTH + 2);
    }
}
