package nl.utwente.presto.tezos.recordCursor;

import com.google.common.collect.ImmutableList;
import io.airlift.log.Logger;
import nl.utwente.presto.tezos.TezosMetadata;
import nl.utwente.presto.tezos.TezosTable;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.tezos.Election;
import nl.utwente.presto.tezos.tezos.TezosClient;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class TezosElectionRecordCursor extends BaseTezosRecordCursor {
    private static final Logger log = Logger.get(TezosBlockRecordCursor.class);

    private final Iterator<Election> electionIter;

    private final TezosTable table;

    public TezosElectionRecordCursor(List<TezosColumnHandle> columnHandles, Election election, TezosTable table,
            TezosClient tezosClient) {

        super(columnHandles);

        this.table = table;

        if (election != null) {
            this.electionIter = ImmutableList.of(election).iterator();
        } else {
            this.electionIter = Collections.emptyIterator();
        }
    }

    @Override
    public long getCompletedBytes() {
        return 0;
    }

    @Override
    public boolean advanceNextPosition() {
        if (table != TezosTable.ELECTION || !electionIter.hasNext()) {
            return false;
        }

        ImmutableList.Builder<Supplier> builder = ImmutableList.builder();
        Election election = electionIter.next();
        builder.add(election::getRowId);
        builder.add(election::getProposalId);
        builder.add(election::getNumPeriods);
        builder.add(election::getNumProposals);
        builder.add(election::getVotingPeriod);
        builder.add(election::getStartTime);
        builder.add(election::getEndTime);
        builder.add(election::getStartHeight);
        builder.add(election::getEndHeight);
        builder.add(election::isEmpty);
        builder.add(election::isOpen);
        builder.add(election::isFailed);
        builder.add(election::isNoQuorum);
        builder.add(election::isNonMajority);
        builder.add(election::getProposal);
        builder.add(election::getLastVotingPeriod);

        this.suppliers = builder.build();
        return true;
    }
}
