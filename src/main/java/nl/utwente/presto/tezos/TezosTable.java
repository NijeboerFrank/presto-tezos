package nl.utwente.presto.tezos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TezosTable {
    BLOCK("block"),
    CONTRACT("contract"),
    PROPOSAL("proposal"),
    ELECTION("election");

    @Getter
    private final String name;
}
