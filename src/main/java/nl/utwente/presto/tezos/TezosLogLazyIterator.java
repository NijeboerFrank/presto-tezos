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
    }

    @Override
    public Log next() {
        return logIter.next();
    }
}
