package nl.utwente.presto.tezos;

import com.google.common.collect.ImmutableList;
import io.airlift.log.Logger;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.tezos.Block;
import nl.utwente.presto.tezos.tezos.TezosClient;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class TezosRecordCursor extends BaseTezosRecordCursor {
    private static final Logger log = Logger.get(TezosRecordCursor.class);

    private final Block block;
    private final Iterator<Block> blockIter;

    private final TezosTable table;

    public TezosRecordCursor(List<TezosColumnHandle> columnHandles, Block block, TezosTable table, TezosClient tezosClient) {
        super(columnHandles);

        this.table = table;

        this.block = requireNonNull(block, "block is null");
        this.blockIter = ImmutableList.of(block).iterator();
    }

    @Override
    public long getCompletedBytes() {
        return block.getBlock().getSize().longValue();
    }

    @Override
    public boolean advanceNextPosition() {
        if (table == TezosTable.BLOCK && !blockIter.hasNext()) {
            return false;
        }

        ImmutableList.Builder<Supplier> builder = ImmutableList.builder();
        if (table == TezosTable.BLOCK) {
            blockIter.next();
            Block blockBlock = this.block;
            builder.add(blockBlock::getHash);
            builder.add(blockBlock::getSize);
            //builder.add(blockBlock::getExtraData); //TODO add all the new Tezos fields that we want to use
            builder.add(blockBlock::getGasLimit);
            builder.add(blockBlock::getGasUsed);
            builder.add(blockBlock::getTimestamp);

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
