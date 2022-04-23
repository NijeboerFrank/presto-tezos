package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an Account data object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    private long rowId;
    private String address;
    private String addressType;
    private String pubkey;
    private long counter;
    private long firstIn;
    private long firstOut;
    private long lastIn;
    private long lastOut;
    private long firstSeen;
    private long lastSeen;
    private String firstInTime;
    private String firstOutTime;
    private String lastInTime;
    private String lastOutTime;
    private String firstSeenTime;
    private String lastSeenTime;
    private double totalReceived;
    private double totalSent;
    private double totalBurned;
    private double totalFeesPaid;
    private double spendableBalance;
    @JsonProperty("is_funded")
    private boolean isFunded;
    @JsonProperty("is_activated")
    private boolean isActivated;
    @JsonProperty("is_delegated")
    private boolean isDelegated;
    @JsonProperty("is_revealed")
    private boolean isRevealed;
    @JsonProperty("is_baker")
    private boolean isBaker;
    @JsonProperty("is_contract")
    private boolean isContract;
    @JsonProperty("n_ops")
    private long nOps;
    @JsonProperty("n_ops_failed")
    private long nOpsFailed;
    @JsonProperty("n_tx")
    private long nTx;
    @JsonProperty("n_delegation")
    private long nDelegation;
    @JsonProperty("n_origination")
    private long nOrigination;
    @JsonProperty("n_constants")
    private long nConstants;
    private long tokenGenMin;
    private long tokenGenMax;
}
