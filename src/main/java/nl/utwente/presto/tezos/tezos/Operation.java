package nl.utwente.presto.tezos.tezos;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Operation data object, which can be transactions, as well as
 * other types of Tezos operations.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Operation {
    long rowId;
    private String type;
    private String hash;
    private long height;
    private long cycle;
    private long time;
    @JsonProperty("op_n")
    private long opN;
    @JsonProperty("op_p")
    private long opP;
    private String status;
    @JsonProperty("is_success")
    private boolean isSuccess;
    @JsonProperty("is_contract")
    private boolean isContract;
    @JsonProperty("is_event")
    private boolean isEvent;
    @JsonProperty("is_internal")
    private boolean isInternal;
    private long counter;
    private long gasLimit;
    private long gasUsed;
    private long storageLimit;
    private long storagePaid;
    private double volume;
    private double fee;
    private double reward;
    private double deposit;
    private double burned;
    private long senderId;
    private long receiverId;
    private long managerId;
    private long bakerId;
    private String data;
    private String parameters;
    private String storage;
    private String bigMapDiff;
    private String errors;
    private double daysDestroyed;
    private String sender;
    private String receiver;
    private String creator;
    private String baker;
    private String block;
    private String entrypoint;

    /**
     * Returns this Operation object
     * 
     * @return this Operation object
     */
    public Operation getOperation() {
        return this;
    }

}