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
    private long rowId;
    private long proposalId;
    private long numPeriods;
    private long numProposals;
    private long votingPeriod;
    private String startTime;
    private String endTime;
    private long startHeight;
    private long endHeight;
    private boolean isEmpty;
    private boolean isOpen;
    private boolean isFailed;
    private boolean noQuorum;
    private boolean nonMajority;
    private String proposal;
    private String lastVotingPeriod;

    public Election getElection() {
        return this;
    }
}
