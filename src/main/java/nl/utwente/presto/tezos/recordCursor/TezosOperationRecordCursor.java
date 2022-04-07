package nl.utwente.presto.tezos.recordCursor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import nl.utwente.presto.tezos.TezosTable;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.tezos.Operation;

import static java.util.Objects.requireNonNull;

public class TezosOperationRecordCursor extends BaseTezosRecordCursor {

    private final Iterator<Operation> operationIter;

    private final TezosTable table;

    public TezosOperationRecordCursor(List<TezosColumnHandle> columnHandles, List<Operation> operations,
            TezosTable table) {

        super(columnHandles);

        this.table = table;

        this.operationIter = requireNonNull(operations, "operations is null").iterator();
    }

    @Override
    public long getCompletedBytes() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean advanceNextPosition() {
        if (table == TezosTable.OPERATION && !operationIter.hasNext()) {
            return false;
        }

        ImmutableList.Builder<Supplier> builder = ImmutableList.builder();
        Operation operation = operationIter.next();
        builder.add(operation::getRowId);
        builder.add(operation::getType);
        builder.add(operation::getHash);
        builder.add(operation::getHeight);
        builder.add(operation::getCycle);
        builder.add(operation::getTime);
        builder.add(operation::getOpN);
        builder.add(operation::getOpP);
        builder.add(operation::getStatus);
        builder.add(operation::isSuccess);
        builder.add(operation::isContract);
        builder.add(operation::isEvent);
        builder.add(operation::isInternal);
        builder.add(operation::getCounter);
        builder.add(operation::getGasLimit);
        builder.add(operation::getGasUsed);
        builder.add(operation::getStorageLimit);
        builder.add(operation::getStoragePaid);
        builder.add(operation::getVolume);
        builder.add(operation::getFee);
        builder.add(operation::getReward);
        builder.add(operation::getDeposit);
        builder.add(operation::getBurned);
        builder.add(operation::getSenderId);
        builder.add(operation::getReceiverId);
        builder.add(operation::getManagerId);
        builder.add(operation::getBakerId);
        builder.add(operation::getData);
        builder.add(operation::getParameters);
        builder.add(operation::getStorage);
        builder.add(operation::getBigMapDiff);
        builder.add(operation::getErrors);
        builder.add(operation::getDaysDestroyed);
        builder.add(operation::getSender);
        builder.add(operation::getReceiver);
        builder.add(operation::getCreator);
        builder.add(operation::getBaker);
        builder.add(operation::getBlock);
        builder.add(operation::getEntrypoint);

        this.suppliers = builder.build();
        return true;
    }

}
