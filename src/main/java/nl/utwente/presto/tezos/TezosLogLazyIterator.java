package nl.utwente.presto.tezos;

import io.airlift.log.Logger;
import nl.utwente.presto.tezos.tezos.Block;
import nl.utwente.presto.tezos.tezos.TezosClient;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

/**
 * Created by xiaoyaoqian on 2/14/18.
 */
public class TezosLogLazyIterator implements Iterator<Log> {
    private static final Logger log = Logger.get(TezosRecordCursor.class);

    //private final Iterator<Block.TransactionResult> txIter;
    private Iterator<Log> logIter;
    private final TezosClient tezosClient;

    public TezosLogLazyIterator(Block block, TezosClient web3j) {
        //this.txIter = block.getBlock().getTransactions().iterator();
        this.tezosClient = web3j;
    }

    @Override
    public boolean hasNext() {
        return true;
//        if (logIter != null && logIter.hasNext()) {
//            return true;
//        }
//
//        while (txIter.hasNext()) {
//            EthBlock.TransactionResult tr = txIter.next();
//            EthBlock.TransactionObject tx = (EthBlock.TransactionObject) tr.get();
//            try {
//                log.info("Getting tx receipts...");
//                Optional<TransactionReceipt> transactionReceiptOptional = tezosClient.getTransaction(tx.getHash()) // actual transaction
//                        .send()
//                        .getTransactionReceipt()
//                        .filter(receipt -> receipt.getLogs() != null && !receipt.getLogs().isEmpty());
//                if (!transactionReceiptOptional.isPresent()) {
//                    continue;
//                }
//
//                this.logIter = transactionReceiptOptional.get().getLogs().iterator();
//                return true;
//            } catch (IOException e) {
//                throw new IllegalStateException("Unable to get transactionReceipt");
//            }
//
//        }
//
//        return false;
    }

    @Override
    public Log next() {
        return logIter.next();
    }
}
