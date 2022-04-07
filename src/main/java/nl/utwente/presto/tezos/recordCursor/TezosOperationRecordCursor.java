package nl.utwente.presto.tezos.recordCursor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import io.airlift.log.Logger;
import nl.utwente.presto.tezos.TezosMetadata;
import nl.utwente.presto.tezos.TezosTable;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.tezos.Operation;
import nl.utwente.presto.tezos.tezos.TezosClient;

public class TezosOperationRecordCursor extends BaseTezosRecordCursor {

    private static final Logger log = Logger.get(TezosBlockRecordCursor.class);

    private final Operation operation;
    private final Iterator<Operation> operationIter;

    private final TezosTable table;

    public TezosOperationRecordCursor(List<TezosColumnHandle> columnHandles, Operation operation, TezosTable table,
            TezosClient tezosClient) {

        super(columnHandles);

        this.table = table;

        this.operation = operation;
        if (operation != null) {
            this.operationIter = ImmutableList.of(operation).iterator();
        } else {
            this.operationIter = Collections.emptyIterator();
        }
    }

    @Override
    public long getCompletedBytes() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean advanceNextPosition() {
        if (table == TezosTable.PROPOSAL && !operationIter.hasNext()) {
            return false;
        }

        ImmutableList.Builder<Supplier> builder = ImmutableList.builder();
        if (table == TezosTable.PROPOSAL) {
            operationIter.next();
            Operation operation = this.operation;
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
