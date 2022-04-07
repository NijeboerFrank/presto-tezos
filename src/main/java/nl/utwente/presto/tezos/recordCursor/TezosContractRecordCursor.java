package nl.utwente.presto.tezos.recordCursor;

import com.google.common.collect.ImmutableList;
import io.airlift.log.Logger;
import nl.utwente.presto.tezos.TezosTable;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.recordCursor.BaseTezosRecordCursor;
import nl.utwente.presto.tezos.tezos.Contract;
import nl.utwente.presto.tezos.tezos.Election;
import nl.utwente.presto.tezos.tezos.TezosClient;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class TezosContractRecordCursor extends BaseTezosRecordCursor {

    private final Iterator<Contract> contractIter;

    private final TezosTable table;

    public TezosContractRecordCursor(List<TezosColumnHandle> columnHandles, List<Contract> contracts,
            TezosTable table) {

        super(columnHandles);

        this.table = table;
        this.contractIter = requireNonNull(contracts, "contracts is null").iterator();

    }

    @Override
    public long getCompletedBytes() {
        return 0;
    }

    @Override
    public boolean advanceNextPosition() {
        if (table != TezosTable.CONTRACT && !contractIter.hasNext()) {
            return false;
        }

        ImmutableList.Builder<Supplier> builder = ImmutableList.builder();
        Contract contract = contractIter.next();
        builder.add(contract::getRowId);
        builder.add(contract::getAddress);
        builder.add(contract::getAccountId);
        builder.add(contract::getCreatorId);
        builder.add(contract::getFirstSeen);
        builder.add(contract::getLastSeen);
        builder.add(contract::getStorageSize);
        builder.add(contract::getStoragePaid);
        builder.add(contract::getScript);
        builder.add(contract::getStorage);
        builder.add(contract::getIfaceHash);
        builder.add(contract::getCodeHash);
        builder.add(contract::getStorageHash);
        builder.add(contract::getCallStats);
        builder.add(contract::getFeatures);
        builder.add(contract::getInterfaces);
        builder.add(contract::getCreator);

        this.suppliers = builder.build();
        return true;
    }
}
