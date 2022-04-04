package nl.utwente.presto.tezos.tezos;

import java.time.Instant;

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
public class Operation {
    private long id;
    private String hash;
    private String type;
    private String block;
    private String time;

    public Operation getOperation() {
        return this;
    }

    public Number getTimestamp() {
        return Instant.parse(this.time).getEpochSecond();
    }

    public Number getTimestampMillis() {
        return Instant.parse(this.time).getEpochSecond() * 1000;
    }

}