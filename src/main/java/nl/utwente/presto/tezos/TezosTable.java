package nl.utwente.presto.tezos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the various tables of the database.
 */
@AllArgsConstructor
public enum TezosTable {
    BLOCK("block"),
    CONTRACT("contract"),
    OPERATION("operation"),
    PROPOSAL("proposal"),
    ELECTION("election");

    @Getter
    private final String name;
}
