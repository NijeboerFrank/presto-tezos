package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public Number getSize() {
        return this.storagePaid;
    }

    public Number getTimestamp() {
        return Instant.parse(this.time).getEpochSecond();
    }

    public Number getTimestampMillis() {
        return Instant.parse(this.time).getEpochSecond() * 1000;
    }
}