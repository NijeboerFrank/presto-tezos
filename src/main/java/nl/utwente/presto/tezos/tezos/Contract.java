package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Contract {
    private long row_id;
    private String address;
    private long account_id;
    private long creator_id;
    private long first_seen;
    private long last_seen;
    private long storage_size;
    private long storage_paid;
    private String script;
    private String storage;
    private String iface_hash;
    private String code_hash;
    private String storage_hash;
    private String call_stats;
    private String features;
    private String interfaces;
    private String creator;

    public Contract getContract() {
        return this;
    }
}
