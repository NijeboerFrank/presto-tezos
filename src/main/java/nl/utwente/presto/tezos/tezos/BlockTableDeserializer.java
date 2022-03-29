package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;

public class BlockTableDeserializer extends JsonDeserializer<Block> {
    @Override
    public Block deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
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
                iterator.next().asLong(), // fitness
                iterator.next().asLong(), // priority
                iterator.next().asText(), // nonce
                iterator.next().asText(), // voting_period_kind
                iterator.next().asText(), // slot_mask
                iterator.next().asLong(), // n_endorsed_slots
                iterator.next().asLong(), // n_ops
                iterator.next().asLong(), // n_ops_failed
                iterator.next().asLong(), // n_ops_contract
                iterator.next().asLong(), // n_contract_calls
                iterator.next().asLong(), // n_tx
                iterator.next().asLong(), // n_activation
                iterator.next().asLong(), // n_seed_nonce_revelation
                iterator.next().asLong(), // n_double_baking_evidence
                iterator.next().asLong(), // n_double_endorsement_evidence
                iterator.next().asLong(), // n_endorsement
                iterator.next().asLong(), // n_delegation
                iterator.next().asLong(), // n_reveal
                iterator.next().asLong(), // n_origination
                iterator.next().asLong(), // n_proposal
                iterator.next().asLong(), // n_ballot
                iterator.next().asLong(), // n_register_constant
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
                iterator.next().asDouble(), // gas_price
                iterator.next().asLong(), // storage_size
                iterator.next().asDouble(), // days_destroyed
                iterator.next().asDouble(), // pct_account_reuse
                iterator.next().asLong(), // n_ops_implicit
                iterator.next().asBoolean(), // lb_esc_vote
                iterator.next().asLong() // lb_esc_ema
        );
    }
}
