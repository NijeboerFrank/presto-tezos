package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;

public class BlockTableDeserializer extends JsonDeserializer<Block> {

    /**
     * Deserializer for the block JSON object from the table API.
     * Returns a new Block object with the given data.
     */
    @Override
    public Block deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Iterator<JsonNode> iterator = node.elements();
        return new Block(
                iterator.next().asText(), // hash
                iterator.next().asText(), // predecessor
                iterator.next().asText(), // baker
                iterator.next().asLong(), // height
                iterator.next().asLong(), // cycle
                iterator.next().asBoolean(), // is_cycle_snapshot
                iterator.next().asText(), // time
                iterator.next().asLong(), // solvetime
                iterator.next().asLong(), // version
                iterator.next().asLong(), // round
                iterator.next().asText(), // nonce
                iterator.next().asText(), // voting_period_kind
                iterator.next().asLong(), // n_endorsed_slots
                iterator.next().asLong(), // n_ops_applied
                iterator.next().asLong(), // n_ops_failed
                iterator.next().asDouble(), // volume
                iterator.next().asDouble(), // fee
                iterator.next().asDouble(), // reward
                iterator.next().asDouble(), // deposit
                iterator.next().asDouble(), // activated_supply
                iterator.next().asDouble(), // burned_supply
                iterator.next().asLong(), // n_accounts
                iterator.next().asLong(), // n_new_accounts
                iterator.next().asLong(), // n_new_contracts
                iterator.next().asLong(), // n_cleared_accounts
                iterator.next().asLong(), // n_funded_accounts
                iterator.next().asLong(), // gas_limit
                iterator.next().asLong(), // gas_used
                iterator.next().asLong(), // storage_paid
                iterator.next().asDouble(), // pct_account_reuse
                iterator.next().asLong(), // n_events
                iterator.next().asBoolean(), // lb_esc_vote
                iterator.next().asLong() // lb_esc_ema
        );
    }
}
