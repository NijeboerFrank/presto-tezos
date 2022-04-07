package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Proposal {
    long rowId;
    String hash;
    long height;
    String time;
    long sourceId;
    long opId;
    long electionId;
    long votingPeriod;
    long rolls;
    long voters;
    String source;
    String op;

    public Proposal getProposal() {
        return this;
    }
}
