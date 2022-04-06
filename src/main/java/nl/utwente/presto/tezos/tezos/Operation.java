package nl.utwente.presto.tezos.tezos;

import java.time.Instant;

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
public class Operation {
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
    private long gas_limit;
    private long gas_used;
    private long storage_limit;
    private long storage_paid;
    private double volume;
    private double fee;
    private double reward;
    private double deposit;
    private double burned;
    private long sender_id;
    private long receiver_id;
    private long manager_id;
    private long baker_id;
    private String data;
    private String parameters;
    private String storage;
    private String big_map_diff;
    private String errors;
    private double days_destroyed;
    private String sender;
    private String receiver;
    private String creator;
    private String baker;
    private String block;
    private String entrypoint;

    public Operation getOperation() {
        return this;
    }

}