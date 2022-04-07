package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Represents a Block data object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {
    private String hash;
    private String predecessor;
    private String baker;
    private long height;
    private long cycle;
    @JsonProperty("is_cycle_snapshot")
    private boolean isCycleSnapshot;
    private String time;
    private long solvetime;
    private long version;
    private long round;
    private String nonce;
    private String votingPeriodKind;
    @JsonProperty("n_endorsed_slots")
    private long nEndorsedSlots;
    @JsonProperty("n_ops_applied")
    private long nOpsApplied;
    @JsonProperty("n_ops_failed")
    private long nOpsFailed;
    private double volume;
    private double fee;
    private double reward;
    private double deposit;
    private double activatedSupply;
    private double burnedSupply;
    @JsonProperty("n_accounts")
    private long nAccounts;
    @JsonProperty("n_new_accounts")
    private long nNewAccounts;
    @JsonProperty("n_new_contracts")
    private long nNewContracts;
    @JsonProperty("n_cleared_accounts")
    private long nClearedAccounts;
    @JsonProperty("n_funded_accounts")
    private long nFundedAccounts;
    private long gasLimit;
    private long gasUsed;
    private long storagePaid;
    private double pctAccountReuse;
    @JsonProperty("n_events")
    private long nEvents;
    private boolean lbEscVote;
    private long lbEscEma;

    /**
     * Returns this Block object
     * 
     * @return this Block object
     */
    public Block getBlock() {
        return this;
    }

    /**
     * Returns the storageSize of this block
     * 
     * @return the storageSize of this block
     */
    public Number getSize() { // TODO check if this is what they want from getSize
        return this.storagePaid;
    }

    /**
     * Returns the height of the current block
     * 
     * @return the height of the current block
     */
    public Number getNumber() {
        return this.height;
    }

    /**
     * Returns the timestamp of the current block as epochs econds
     * 
     * @return the timestamp of the current block as epochs econd
     */
    public Number getTimestamp() {
        return Instant.parse(this.time).getEpochSecond();
    }

    /**
     * The timestamp of the current block as epoch second milliseconds
     * 
     * @return the timestamp of the current block as epoch milliseconds
     */
    public Number getTimestampMillis() {
        return Instant.parse(this.time).getEpochSecond() * 1000;
    }
}