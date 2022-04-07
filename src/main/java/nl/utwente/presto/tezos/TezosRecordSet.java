package nl.utwente.presto.tezos;

import com.facebook.presto.common.type.Type;
import com.facebook.presto.spi.RecordCursor;
import com.facebook.presto.spi.RecordSet;
import io.airlift.log.Logger;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.tezos.Block;
import nl.utwente.presto.tezos.tezos.Contract;
import nl.utwente.presto.tezos.tezos.Election;
import nl.utwente.presto.tezos.tezos.TezosClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class TezosRecordSet implements RecordSet {
    private static final Logger log = Logger.get(TezosRecordSet.class);

    private final TezosSplit split;
    private final TezosClient tezosClient;

    private final List<TezosColumnHandle> columnHandles;
    private final List<Type> columnTypes;

    TezosRecordSet(TezosClient tezosClient, List<TezosColumnHandle> columnHandles, TezosSplit split) {
        this.split = requireNonNull(split, "split is null");
        this.tezosClient = requireNonNull(tezosClient, "TezosClient is null");

        this.columnHandles = requireNonNull(columnHandles, "columnHandles is null");

        this.columnTypes = columnHandles.stream()
                .map(TezosColumnHandle::getType)
                .collect(Collectors.toList());
    }

    @Override
    public List<Type> getColumnTypes() {
        return columnTypes;
    }

    @Override
    public RecordCursor cursor() {

        switch (split.getTable()) {
            case BLOCK:
                Block block = null;
                try {
                    block = tezosClient.getBlock(split.getBlockId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new TezosBlockRecordCursor(columnHandles, block, split.getTable(), tezosClient);
            case ELECTION:
                Election election = null;
                try {
                    // TODO Also get election by proposal ID
                    election = tezosClient.getElection(split.getElectionId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new TezosElectionRecordCursor(columnHandles, election, split.getTable(), tezosClient);
            case CONTRACT:
                Contract contract = null;
                try {
                    contract = tezosClient.getContract(split.getBlockId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new TezosContractRecordCursor(columnHandles, contract, split.getTable(), tezosClient);
            default:
                return null;
        }
    }
}
