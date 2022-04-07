package nl.utwente.presto.tezos;

import com.facebook.presto.common.predicate.Domain;
import com.facebook.presto.common.predicate.Marker;
import com.facebook.presto.common.predicate.Range;
import com.facebook.presto.common.type.*;
import com.facebook.presto.spi.*;
import com.google.common.collect.ImmutableList;
import io.airlift.slice.Slice;
import nl.utwente.presto.tezos.handle.TezosColumnHandle;
import nl.utwente.presto.tezos.handle.TezosTableHandle;
import nl.utwente.presto.tezos.handle.TezosTableLayoutHandle;
import nl.utwente.presto.tezos.tezos.TezosClient;
import nl.utwente.presto.tezos.tezos.TezosClientProvider;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static nl.utwente.presto.tezos.handle.TezosHandleResolver.convertTableHandle;

public class TezosMetadata extends BaseTezosMetadata {
    private final TezosClient tezosClient;

    @Inject
    public TezosMetadata(
            TezosClientProvider provider) {
        this.tezosClient = requireNonNull(provider, "provider is null").getTezosClient();
    }

    /**
     * Get block ranges to search in based on query
     * 
     * @param session
     * @param table          queried table
     * @param constraint     query constraints
     * @param desiredColumns
     * @return
     */
    @Override
    public List<ConnectorTableLayoutResult> getTableLayouts(
            ConnectorSession session,
            ConnectorTableHandle table,
            Constraint<ColumnHandle> constraint,
            Optional<Set<ColumnHandle>> desiredColumns) {
        ImmutableList.Builder<TezosRange> builder = ImmutableList.builder();

        Optional<Map<ColumnHandle, Domain>> domains = constraint.getSummary().getDomains();
        if (domains.isPresent()) {
            Map<ColumnHandle, Domain> columnHandleDomainMap = domains.get();
            for (Map.Entry<ColumnHandle, Domain> entry : columnHandleDomainMap.entrySet()) {
                if (!(entry.getKey() instanceof TezosColumnHandle))
                    continue;

                String columnName = ((TezosColumnHandle) entry.getKey()).getName();
                List<Range> orderedRanges = entry.getValue().getValues().getRanges().getOrderedRanges();

                switch (columnName) {
                    case "block_height":
                    case "election_id":
                    case "proposal_id":
                    case "operation_height":
                        // Limit query to block number range
                        orderedRanges.forEach(r -> {
                            Marker low = r.getLow();
                            Marker high = r.getHigh();
                            builder.add(TezosRange.fromMarkers(columnName, low, high));
                        });
                        break;
                    case "block_hash":
                    case "tx_blockHash":
                    case "operation_hash":
                        // Limit query to block hash range
                        orderedRanges.stream()
                                .filter(Range::isSingleValue).forEach(r -> {
                                    String blockHash = ((Slice) r.getSingleValue()).toStringUtf8();
                                    try {
                                        long blockNumber = tezosClient.getBlock(blockHash).getNumber().longValue();
                                        builder.add(new TezosRange(columnName, blockNumber, blockNumber));
                                    } catch (IOException e) {
                                        throw new IllegalStateException("Unable to getting block by hash " + blockHash);
                                    }
                                });
                        log.info(entry.getValue().getValues().toString(null));
                        break;
                    case "block_time":
                        // Limit query to block timestamp range
                        orderedRanges.forEach(r -> {
                            Marker low = r.getLow();
                            Marker high = r.getHigh();
                            try {
                                long startBlock = low.isLowerUnbounded() ? 1L
                                        : findBlockByTimestamp((Long) low.getValue(), -1L);
                                long endBlock = high.isUpperUnbounded() ? -1L
                                        : findBlockByTimestamp((Long) high.getValue(), 1L);
                                builder.add(new TezosRange(columnName, startBlock, endBlock));
                            } catch (IOException e) {
                                throw new IllegalStateException("Unable to find block by timestamp");
                            }
                        });
                        log.info(entry.getValue().getValues().toString(null));
                        break;
                }
            }
        }

        TezosTableHandle handle = convertTableHandle(table);
        ConnectorTableLayout layout = new ConnectorTableLayout(new TezosTableLayoutHandle(handle, builder.build()));
        return ImmutableList.of(new ConnectorTableLayoutResult(layout, constraint.getSummary()));
    }

    /**
     * Return the columns and their types
     * 
     * @param table table to get columns of
     * @return list of columns
     */
    @Override
    protected List<Pair<String, Type>> getColumnsWithTypes(String table) {
        ImmutableList.Builder<Pair<String, Type>> builder = ImmutableList.builder();

        if (TezosTable.BLOCK.getName().equals(table)) {
            builder.add(new Pair<>("block_hash", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("block_predecessor", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("block_baker", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("block_height", BigintType.BIGINT));
            builder.add(new Pair<>("block_cycle", BigintType.BIGINT));
            builder.add(new Pair<>("block_isCycleSnapshot", BooleanType.BOOLEAN));
            builder.add(new Pair<>("block_time", TimestampType.TIMESTAMP));
            builder.add(new Pair<>("block_solvetime", BigintType.BIGINT));
            builder.add(new Pair<>("block_version", BigintType.BIGINT));
            builder.add(new Pair<>("block_round", BigintType.BIGINT));
            builder.add(new Pair<>("block_nonce", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("block_votingPeriodKind", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("block_nEndorsedSlots", BigintType.BIGINT));
            builder.add(new Pair<>("block_nOpsApplied", BigintType.BIGINT));
            builder.add(new Pair<>("block_nOpsFailed", BigintType.BIGINT));
            builder.add(new Pair<>("block_volume", DoubleType.DOUBLE));
            builder.add(new Pair<>("block_fee", DoubleType.DOUBLE));
            builder.add(new Pair<>("block_reward", DoubleType.DOUBLE));
            builder.add(new Pair<>("block_deposit", DoubleType.DOUBLE));
            builder.add(new Pair<>("block_activatedSupply", DoubleType.DOUBLE));
            builder.add(new Pair<>("block_burnedSupply", DoubleType.DOUBLE));
            builder.add(new Pair<>("block_nAccounts", BigintType.BIGINT));
            builder.add(new Pair<>("block_nNewAccounts", BigintType.BIGINT));
            builder.add(new Pair<>("block_nNewContracts", BigintType.BIGINT));
            builder.add(new Pair<>("block_nClearedAccounts", BigintType.BIGINT));
            builder.add(new Pair<>("block_nFundedAccounts", BigintType.BIGINT));
            builder.add(new Pair<>("block_gasLimit", BigintType.BIGINT));
            builder.add(new Pair<>("block_gasUsed", BigintType.BIGINT));
            builder.add(new Pair<>("block_storagePaid", BigintType.BIGINT));
            builder.add(new Pair<>("block_pctAccountReuse", DoubleType.DOUBLE));
            builder.add(new Pair<>("block_nEvents", BigintType.BIGINT));
            builder.add(new Pair<>("block_lbEscVote", BooleanType.BOOLEAN));
            builder.add(new Pair<>("block_lbEscEma", BigintType.BIGINT));
        } else if (TezosTable.OPERATION.getName().equals(table)) {
            builder.add(new Pair<>("operation_id", BigintType.BIGINT));
            builder.add(new Pair<>("operation_type", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_hash", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_height", BigintType.BIGINT));
            builder.add(new Pair<>("operation_cycle", BigintType.BIGINT));
            builder.add(new Pair<>("operation_time", TimestampType.TIMESTAMP));
            builder.add(new Pair<>("operation_opN", BigintType.BIGINT));
            builder.add(new Pair<>("operation_opP", BigintType.BIGINT));
            builder.add(new Pair<>("operation_status", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_isSuccess", BooleanType.BOOLEAN));
            builder.add(new Pair<>("operation_isContract", BooleanType.BOOLEAN));
            builder.add(new Pair<>("operation_isEvent", BooleanType.BOOLEAN));
            builder.add(new Pair<>("operation_isInternal", BooleanType.BOOLEAN));
            builder.add(new Pair<>("operation_counter", BigintType.BIGINT));
            builder.add(new Pair<>("operation_gasLimit", BigintType.BIGINT));
            builder.add(new Pair<>("operation_gasUsed", BigintType.BIGINT));
            builder.add(new Pair<>("operation_storageLimit", BigintType.BIGINT));
            builder.add(new Pair<>("operation_storagePaid", BigintType.BIGINT));
            builder.add(new Pair<>("operation_volume", DoubleType.DOUBLE));
            builder.add(new Pair<>("operation_fee", DoubleType.DOUBLE));
            builder.add(new Pair<>("operation_reward", DoubleType.DOUBLE));
            builder.add(new Pair<>("operation_deposit", DoubleType.DOUBLE));
            builder.add(new Pair<>("operation_burned", DoubleType.DOUBLE));
            builder.add(new Pair<>("operation_senderId", BigintType.BIGINT));
            builder.add(new Pair<>("operation_receiverId", BigintType.BIGINT));
            builder.add(new Pair<>("operation_managerId", BigintType.BIGINT));
            builder.add(new Pair<>("operation_bakerId", BigintType.BIGINT));
            builder.add(new Pair<>("operation_data", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_parameters", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_storage", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_bigMapDiff", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_errors", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_daysDestroyed", DoubleType.DOUBLE));
            builder.add(new Pair<>("operation_sender", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_receiver", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_creator", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_baker", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_block", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("operation_entrypoint", VarcharType.createUnboundedVarcharType()));
        } else if (TezosTable.ELECTION.getName().equals(table)) {
            builder.add(new Pair<>("election_id", BigintType.BIGINT));
            builder.add(new Pair<>("election_proposalId", BigintType.BIGINT));
            builder.add(new Pair<>("election_numPeriods", BigintType.BIGINT));
            builder.add(new Pair<>("election_numProposals", BigintType.BIGINT));
            builder.add(new Pair<>("election_votingPeriod", BigintType.BIGINT));
            builder.add(new Pair<>("election_startTime", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("election_endTime", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("election_startHeight", BigintType.BIGINT));
            builder.add(new Pair<>("election_endHeight", BigintType.BIGINT));
            builder.add(new Pair<>("election_isEmpty", BooleanType.BOOLEAN));
            builder.add(new Pair<>("election_isOpen", BooleanType.BOOLEAN));
            builder.add(new Pair<>("election_isFailed", BooleanType.BOOLEAN));
            builder.add(new Pair<>("election_noQuorum", BooleanType.BOOLEAN));
            builder.add(new Pair<>("election_noMajority", BooleanType.BOOLEAN));
            builder.add(new Pair<>("election_proposal", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("election_lastVotingPeriod", VarcharType.createUnboundedVarcharType()));
        } else if (TezosTable.PROPOSAL.getName().equals(table)) {
            builder.add(new Pair<>("proposal_id", BigintType.BIGINT));
            builder.add(new Pair<>("proposal_hash", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("proposal_height", BigintType.BIGINT));
            builder.add(new Pair<>("proposal_time", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("proposal_sourceId", BigintType.BIGINT));
            builder.add(new Pair<>("proposal_opId", BigintType.BIGINT));
            builder.add(new Pair<>("proposal_electionId", BigintType.BIGINT));
            builder.add(new Pair<>("proposal_votingPeriod", BigintType.BIGINT));
            builder.add(new Pair<>("proposal_rolls", BigintType.BIGINT));
            builder.add(new Pair<>("proposal_voters", BigintType.BIGINT));
            builder.add(new Pair<>("proposal_source", VarcharType.createUnboundedVarcharType()));
            builder.add(new Pair<>("proposal_op", VarcharType.createUnboundedVarcharType()));
        } else {
            throw new IllegalArgumentException("Unknown Table Name " + table);
        }

        return builder.build();
    }

    /**
     * Get the ID of a block that was added at a given timestamp
     * 
     * @param timestamp timestamp of block
     * @param offset    offset to middle block
     * @return ID of block
     */
    private long findBlockByTimestamp(long timestamp, long offset) throws IOException {
        long startBlock = 1L;
        long currentBlock = 0;
        try {
            currentBlock = tezosClient.getLastBlock().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (currentBlock <= 1) {
            return currentBlock;
        }

        long low = startBlock;
        long high = currentBlock;
        long middle = low + (high - low) / 2;

        while (low <= high) {
            middle = low + (high - low) / 2;
            long ts = tezosClient.getBlock(middle).getTimestamp().longValue();

            if (ts < timestamp) {
                low = middle + 1;
            } else if (ts > timestamp) {
                high = middle - 1;
            } else {
                return middle;
            }
        }
        return middle + offset;
    }
}
