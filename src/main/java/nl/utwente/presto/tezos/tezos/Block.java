package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.web3j.protocol.core.methods.response.EthBlock;

@Getter
@Setter
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
    private long fitness;
    private long priority;
    private String nonce;
    private String votingPeriodKind;
    private String slotMask;
    @JsonProperty("n_endorsed_slots")
    private long nEndorsedSlots;
    @JsonProperty("n_ops")
    private long nOps;
    @JsonProperty("n_ops_failed")
    private long nOpsFailed;
    @JsonProperty("n_ops_contract")
    private long nOpsContract;
    @JsonProperty("n_contract_calls")
    private long nContractCalls;
    @JsonProperty("n_tx")
    private long nTx;
    @JsonProperty("n_activation")
    private long nActivation;
    @JsonProperty("n_seed_nonce_revelations")
    private long nSeedNonceRevelations;
    @JsonProperty("n_double_baking_evidences")
    private long nDoubleBakingEvidences;
    @JsonProperty("n_double_endorsement_evidences")
    private long nDoubleEndorsementEvidences;
    @JsonProperty("n_endorsement")
    private long nEndorsement;
    @JsonProperty("n_delegation")
    private long nDelegation;
    @JsonProperty("n_reveal")
    private long nReveal;
    @JsonProperty("n_origination")
    private long nOrigination;
    @JsonProperty("n_proposal")
    private long nProposal;
    @JsonProperty("n_ballot")
    private long nBallot;
    @JsonProperty("n_register_constant")
    private long nRegisterConstant;
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
    private double gasPrice;
    private long storageSize;
    private double daysDestroyed;
    private double pctAccountReuse;
    @JsonProperty("n_ops_implicit")
    private long nOpsImplicit;
    private boolean lbEscVote;
    private long lbEscEma;

    public Block getBlock() {
        return this;
    }

    public Number getSize() { // TODO check if this is what they want from getSize
        return this.storageSize;
    }

    public Number getNumber() {
        return this.height;
    }

    public Number getTimestamp() {
        //return this.time;
        return 1; //TODO make time to number example time = "2022-03-29T09:12:24Z"
    }
}
