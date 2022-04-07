package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contract {
    private long rowId;
    private String address;
    private long accountId;
    private long creatorId;
    private long firstSeen;
    private long lastSeen;
    private long storageSize;
    private long storagePaid;
    private String script;
    private String storage;
    private String ifaceHash;
    private String codeHash;
    private String storageHash;
    private String callStats;
    private String features;
    private String interfaces;
    private String creator;

    /**
     * Retrieves the contract
     * @return Contract
     */
    public Contract getContract() {
        return this;
    }
}
