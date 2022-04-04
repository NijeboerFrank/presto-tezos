package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Election {
    @JsonProperty("row_id")
    private long rowId;
    @JsonProperty("proposal_id")
    private long proposalId;
    @JsonProperty("num_periods")
    private long numPeriods;
    @JsonProperty("num_proposals")
    private long numProposals;
    @JsonProperty("voting_period")
    private long votingPeriod;
    @JsonProperty("start_time")
    private String startTime;
    @JsonProperty("end_time")
    private String endTime;
    @JsonProperty("start_height")
    private long startHeight;
    @JsonProperty("end_height")
    private long endHeight;
    @JsonProperty("is_empty")
    private boolean isEmpty;
    @JsonProperty("is_open")
    private boolean isopen;
    @JsonProperty("is_failed")
    private boolean isfailed;
    @JsonProperty("no_quorum")
    private boolean noQuorum;
    @JsonProperty("no_majority")
    private boolean nonMajority;
    private String proposal;
    @JsonProperty("last_voting_period")
    private String lastVotingPeriod;

    public Election getElection() {
        return this;
    }
}
