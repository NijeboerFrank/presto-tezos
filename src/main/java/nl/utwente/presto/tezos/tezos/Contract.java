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
    private long account_id;
    private String address;
    private String creator;

    public Contract getContract() {
        return this;
    }

    public Number getAccount_ID() {
        return this.account_id;
    }

    public String getAddress() {return this.address;}
}
