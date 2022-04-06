package nl.utwente.presto.tezos.handle;

import com.facebook.presto.spi.*;
import com.facebook.presto.spi.connector.ConnectorTransactionHandle;

import nl.utwente.presto.tezos.TezosSplit;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class TezosHandleResolver implements ConnectorHandleResolver {
    @Override
    public Class<? extends ConnectorTableHandle> getTableHandleClass() {
        return TezosTableHandle.class;
    }

    @Override
    public Class<? extends ColumnHandle> getColumnHandleClass() {
        return TezosColumnHandle.class;
    }

    @Override
    public Class<? extends ConnectorSplit> getSplitClass() {
        return TezosSplit.class;
    }

    @Override
    public Class<? extends ConnectorTableLayoutHandle> getTableLayoutHandleClass() {
        return TezosTableLayoutHandle.class;
    }

    @Override
    public Class<? extends ConnectorTransactionHandle> getTransactionHandleClass() {
        return TezosTransactionHandle.class;
    }

    public static TezosTableHandle convertTableHandle(ConnectorTableHandle tableHandle) {
        requireNonNull(tableHandle, "tableHandle is null");
        checkArgument(tableHandle instanceof TezosTableHandle, "tableHandle is not an instance of TezosTableHandle");
        return (TezosTableHandle) tableHandle;
    }

    public static TezosColumnHandle convertColumnHandle(ColumnHandle columnHandle) {
        requireNonNull(columnHandle, "columnHandle is null");
        checkArgument(columnHandle instanceof TezosColumnHandle,
                "columnHandle is not an instance of TezosColumnHandle");
        return (TezosColumnHandle) columnHandle;
    }

    public static TezosSplit convertSplit(ConnectorSplit split) {
        requireNonNull(split, "split is null");
        checkArgument(split instanceof TezosSplit, "split is not an instance of TezosSplit");
        return (TezosSplit) split;
    }

    public static TezosTableLayoutHandle convertLayout(ConnectorTableLayoutHandle layout) {
        requireNonNull(layout, "layout is null");
        checkArgument(layout instanceof TezosTableLayoutHandle, "layout is not an instance of TezosTableLayoutHandle");
        return (TezosTableLayoutHandle) layout;
    }
}
