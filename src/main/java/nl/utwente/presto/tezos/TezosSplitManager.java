package nl.utwente.presto.tezos;

import com.facebook.presto.spi.*;
import com.facebook.presto.spi.connector.ConnectorSplitManager;
import com.facebook.presto.spi.connector.ConnectorTransactionHandle;
import io.airlift.log.Logger;
import nl.utwente.presto.tezos.connector.TezosConnectorConfig;
import nl.utwente.presto.tezos.handle.TezosTableLayoutHandle;
import nl.utwente.presto.tezos.tezos.TezosClient;
import nl.utwente.presto.tezos.tezos.TezosClientProvider;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.util.Objects.requireNonNull;
import static nl.utwente.presto.tezos.handle.TezosHandleResolver.convertLayout;

public class TezosSplitManager implements ConnectorSplitManager {
    private static final Logger log = Logger.get(TezosSplitManager.class);

    private final TezosClient tezosClient;

    @Inject
    public TezosSplitManager(
            TezosConnectorConfig config,
            TezosClientProvider tezosClientProvider) {
        requireNonNull(tezosClientProvider, "tezos client provider is null");
        requireNonNull(config, "config is null");
        this.tezosClient = tezosClientProvider.getTezosClient();
    }

    /**
     * Convert list of block ranges to a list of splits
     * 
     * @param transaction
     * @param session
     * @param layout                 table layout and block ranges
     * @param splitSchedulingContext
     * @return
     */
    @Override
    public ConnectorSplitSource getSplits(
            ConnectorTransactionHandle transaction,
            ConnectorSession session,
            ConnectorTableLayoutHandle layout,
            SplitSchedulingContext splitSchedulingContext) {
        TezosTableLayoutHandle tableLayoutHandle = convertLayout(layout);
        TezosTable table = TezosTable.valueOf(tableLayoutHandle.getTable().getTableName().toUpperCase());

        try {
            long lastBlockNumber = tezosClient.getLastBlockNumber();
            log.info("current block number: " + lastBlockNumber);

            List<ConnectorSplit> connectorSplits;
            switch (table) {
                case BLOCK:
                    if (tableLayoutHandle.getRanges().isEmpty()) {
                        connectorSplits = LongStream.range(0, lastBlockNumber + 1)
                                .mapToObj(TezosSplit::forBlock)
                                .collect(Collectors.toList());
                    } else {
                        connectorSplits = tableLayoutHandle.getRanges()
                                .stream()
                                .flatMap(blockRange -> LongStream.range(
                                        blockRange.getStart(),
                                        blockRange.getEnd() == -1 ? lastBlockNumber : blockRange.getEnd() + 1).boxed())
                                .map(TezosSplit::forBlock)
                                .collect(Collectors.toList());
                    }

                    log.info("Built %d splits", connectorSplits.size());
                    return new FixedSplitSource(connectorSplits);
                case ELECTION:
                    long lastElection = tezosClient.getLastElection().getRowId();
                    if (tableLayoutHandle.getRanges().isEmpty()) {
                        connectorSplits = LongStream.range(0, lastElection + 1)
                                .mapToObj(TezosSplit::forElection)
                                .collect(Collectors.toList());
                    } else {
                        connectorSplits = tableLayoutHandle.getRanges()
                                .stream()
                                .flatMap(blockRange -> LongStream.range(
                                        blockRange.getStart(),
                                        blockRange.getEnd() == -1 ? lastElection : blockRange.getEnd() + 1).boxed())
                                .map(TezosSplit::forElection)
                                .collect(Collectors.toList());
                    }
                    log.info("Built %d splits", connectorSplits.size());
                    return new FixedSplitSource(connectorSplits);
                case PROPOSAL:
                    long lastProposal = 50;
                    if (tableLayoutHandle.getRanges().isEmpty()) {
                        connectorSplits = LongStream.range(0, lastProposal + 1)
                                .mapToObj(TezosSplit::forProposal)
                                .collect(Collectors.toList());
                    } else {
                        connectorSplits = tableLayoutHandle.getRanges()
                                .stream()
                                .flatMap(blockRange -> LongStream.range(
                                        blockRange.getStart(),
                                        blockRange.getEnd() == -1 ? lastProposal : blockRange.getEnd() + 1).boxed())
                                .map(TezosSplit::forProposal)
                                .collect(Collectors.toList());
                    }
                    log.info("Built %d splits", connectorSplits.size());
                    return new FixedSplitSource(connectorSplits);
                default:
                    throw new RuntimeException("Cannot make a split from this range");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get block number: ", e);
        }
    }
}
