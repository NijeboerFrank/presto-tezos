package nl.utwente.presto.tezos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TezosTable {
    BLOCK("block"),
    TRANSACTION("transaction");

    @Getter
    private final String name;
}
