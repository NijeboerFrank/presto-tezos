package nl.utwente.presto.tezos;

import com.google.common.collect.ImmutableList;
import io.airlift.log.Logger;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.tezos.Election;
import nl.utwente.presto.tezos.tezos.TezosClient;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class TezosElectionRecordCursor extends BaseTezosRecordCursor {
    private static final Logger log = Logger.get(TezosBlockRecordCursor.class);

    private final Election election;
    private final Iterator<Election> electionIter;

    private final TezosTable table;

    public TezosElectionRecordCursor(List<TezosColumnHandle> columnHandles, Election election, TezosTable table,
            TezosClient tezosClient) {

        super(columnHandles);

        this.table = table;

        this.election = election;
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
        if (table == TezosTable.ELECTION && !electionIter.hasNext()) {
            return false;
        }

        ImmutableList.Builder<Supplier> builder = ImmutableList.builder();
        if (table == TezosTable.ELECTION) {
            electionIter.next();
            Election election = this.election;
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
