package nl.utwente.presto.tezos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TezosTable {
    BLOCK("block"),
    OPERATION("operation");

    @Getter
    private final String name;
}
