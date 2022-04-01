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
            Block block = this.block;
            builder.add(block::getHash);
            builder.add(block::getPredecessor);
            builder.add(block::getBaker);
            builder.add(block::getHeight);
            builder.add(block::getCycle);
            builder.add(block::isCycleSnapshot);
            builder.add(block::getTimestampMillis);
            builder.add(block::getSolvetime);
            builder.add(block::getVersion);
            builder.add(block::getFitness);
            builder.add(block::getPriority);
            builder.add(block::getNonce);
            builder.add(block::getVotingPeriodKind);
            builder.add(block::getSlotMask);
            builder.add(block::getNEndorsedSlots);
            builder.add(block::getNOps);
            builder.add(block::getNOpsFailed);
            builder.add(block::getNOpsContract);
            builder.add(block::getNContractCalls);
            builder.add(block::getNTx);
            builder.add(block::getNActivation);
            builder.add(block::getNSeedNonceRevelations);
            builder.add(block::getNDoubleBakingEvidences);
            builder.add(block::getNDoubleEndorsementEvidences);
            builder.add(block::getNEndorsement);
            builder.add(block::getNDelegation);
            builder.add(block::getNReveal);
            builder.add(block::getNOrigination);
            builder.add(block::getNProposal);
            builder.add(block::getNBallot);
            builder.add(block::getNRegisterConstant);
            builder.add(block::getVolume);
            builder.add(block::getFee);
            builder.add(block::getReward);
            builder.add(block::getDeposit);
            builder.add(block::getActivatedSupply);
            builder.add(block::getBurnedSupply);
            builder.add(block::getNAccounts);
            builder.add(block::getNNewAccounts);
            builder.add(block::getNNewContracts);
            builder.add(block::getNClearedAccounts);
            builder.add(block::getNFundedAccounts);
            builder.add(block::getGasLimit);
            builder.add(block::getGasUsed);
            builder.add(block::getGasPrice);
            builder.add(block::getStorageSize);
            builder.add(block::getDaysDestroyed);
            builder.add(block::getPctAccountReuse);
            builder.add(block::getNOpsImplicit);
            builder.add(block::isLbEscVote);
            builder.add(block::getLbEscEma);

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
