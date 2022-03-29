package nl.utwente.presto.tezos;

import com.google.common.collect.ImmutableList;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import io.airlift.log.Logger;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class TezosRecordCursor extends BaseTezosRecordCursor {
    private static final Logger log = Logger.get(TezosRecordCursor.class);

    private final EthBlock block;
    private final Iterator<EthBlock> blockIter;

    private final TezosTable table;

    public TezosRecordCursor(List<TezosColumnHandle> columnHandles, EthBlock block, TezosTable table, Web3j web3j) {
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
            EthBlock.Block blockBlock = this.block.getBlock();
            builder.add(blockBlock::getNumber);
            builder.add(blockBlock::getHash);
            builder.add(blockBlock::getParentHash);
            builder.add(blockBlock::getNonceRaw);
            builder.add(blockBlock::getSha3Uncles);
            builder.add(blockBlock::getLogsBloom);
            builder.add(blockBlock::getTransactionsRoot);
            builder.add(blockBlock::getStateRoot);
            builder.add(blockBlock::getMiner);
            builder.add(blockBlock::getDifficulty);
            builder.add(blockBlock::getTotalDifficulty);
            builder.add(blockBlock::getSize);
            builder.add(blockBlock::getExtraData);
            builder.add(blockBlock::getGasLimit);
            builder.add(blockBlock::getGasUsed);
            builder.add(blockBlock::getTimestamp);
            builder.add(blockBlock::getUncles);

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
