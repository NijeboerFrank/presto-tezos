package nl.utwente.presto.tezos;

import com.google.common.collect.ImmutableList;
import io.airlift.log.Logger;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.tezos.Contract;
import nl.utwente.presto.tezos.tezos.Election;
import nl.utwente.presto.tezos.tezos.TezosClient;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class TezosContractRecordCursor extends BaseTezosRecordCursor {
    private static final Logger log = Logger.get(TezosBlockRecordCursor.class);

    private final Contract contract;
    private final Iterator<Contract> electionIter;

    private final TezosTable table;

    public TezosContractRecordCursor(List<TezosColumnHandle> columnHandles, Contract contract, TezosTable table,
                                     TezosClient tezosClient) {

        super(columnHandles);

        this.table = table;

        this.contract = contract;
        if (contract != null) {
            this.electionIter = ImmutableList.of(contract).iterator();
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
            Contract contract = this.contract;
            builder.add(contract::getRow_id);
            builder.add(contract::getAddress);
            builder.add(contract::getAccount_id);
            builder.add(contract::getCreator_id);
            builder.add(contract::getFirst_seen);
            builder.add(contract::getLast_seen);
            builder.add(contract::getStorage_size);
            builder.add(contract::getStorage_paid);
            builder.add(contract::getScript);
            builder.add(contract::getStorage);
            builder.add(contract::getIface_hash);
            builder.add(contract::getCode_hash);
            builder.add(contract::getStorage_hash);
            builder.add(contract::getCall_stats);
            builder.add(contract::getFeatures);
            builder.add(contract::getInterfaces);
            builder.add(contract::getCreator);
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
