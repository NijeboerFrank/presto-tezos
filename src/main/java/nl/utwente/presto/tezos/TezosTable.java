package nl.utwente.presto.tezos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the various tables of the database.
 */
@AllArgsConstructor
public enum TezosTable {
    BLOCK("block"),
    OPERATION("operation");

    @Getter
    private final String name;
}
