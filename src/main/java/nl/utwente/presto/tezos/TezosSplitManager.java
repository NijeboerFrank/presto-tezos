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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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

        long lastId;
        try {
            BiFunction<Long, Long, List<ConnectorSplit>> converter;
            switch (table) {
                case BLOCK:
                    lastId = tezosClient.getLastBlock().getHeight();
                    converter = TezosSplit::forBlockRange;
                    break;
                case ELECTION:
                    lastId = tezosClient.getLastElection().getRowId();
                    converter = TezosSplit::forElectionRange;
                    break;
                case PROPOSAL:
                    lastId = tezosClient.getLastProposal().getRowId();
                    converter = TezosSplit::forProposalRange;
                    break;
                case CONTRACT:
                    lastId = tezosClient.getLastContract().getRowId();
                    converter = TezosSplit::forContractRange;
                    break;
                case OPERATION:
                    lastId  = tezosClient.getLastOperation().getHeight();
                    converter = TezosSplit::forOperationRange;
                    break;
                default:
                    throw new RuntimeException("Cannot make a split from this range");
            }

            List<ConnectorSplit> connectorSplits;
            if (tableLayoutHandle.getRanges().isEmpty()) {
                connectorSplits = converter.apply(0L, lastId + 1);
            } else {
                connectorSplits = tableLayoutHandle.getRanges()
                        .stream()
                        .flatMap(range -> converter.apply(
                                range.getStart(),
                                range.getEnd() == -1 ? lastId : range.getEnd() + 1).stream())
                        .collect(Collectors.toList());
            }

            log.info("Built %d splits", connectorSplits.size());
            return new FixedSplitSource(connectorSplits);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get block number: ", e);
        }
    }
}
