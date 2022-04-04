package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;

public class ElectionTableDeserializer extends JsonDeserializer<Election> {
    @Override
    public Election deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Iterator<JsonNode> iterator = node.elements();
        return new Election(
                iterator.next().asLong(), // row_id
                iterator.next().asLong(), // proposal_id
                iterator.next().asLong(), // num_periods
                iterator.next().asLong(), // num_proposals
                iterator.next().asLong(), // voting_period
                iterator.next().asText(), // start_time
                iterator.next().asText(), // end_time
                iterator.next().asLong(), // start_height
                iterator.next().asLong(), // end_height
                iterator.next().asBoolean(), // is_empty
                iterator.next().asBoolean(), // is_open
                iterator.next().asBoolean(), // is_failed
                iterator.next().asBoolean(), // no_quorum
                iterator.next().asBoolean(), // no_majority
                iterator.next().asText(), // proposal
                iterator.next().asText() // last_voting_period
        );
    }
}
