package nl.utwente.presto.tezos.tezos;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

public class OperationTableDeserializer extends JsonDeserializer<Operation> {
    /**
     * Deserializer for the operation JSON object from the table API.
     * Returns a new Operation object with the given data.
     */
    @Override
    public Operation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Iterator<JsonNode> iterator = node.elements();
        return new Operation(
                iterator.next().asText(), // type
                iterator.next().asText(), // hash
                iterator.next().asLong(), // heigh
                iterator.next().asLong(), // cycle
                iterator.next().asLong(), // time
                iterator.next().asLong(), // op_n
                iterator.next().asLong(), // op_p
                iterator.next().asText(), // status
                iterator.next().asBoolean(), // is_success
                iterator.next().asBoolean(), // is_contract
                iterator.next().asBoolean(), // is_event
                iterator.next().asBoolean(), // is_internal
                iterator.next().asLong(), // counter
                iterator.next().asLong(), // gas_limit
                iterator.next().asLong(), // gas_used
                iterator.next().asLong(), // storage_lmit
                iterator.next().asLong(), // storage_paid
                iterator.next().asDouble(), // volume
                iterator.next().asDouble(), // fee
                iterator.next().asDouble(), // reward
                iterator.next().asDouble(), // deposit
                iterator.next().asDouble(), // burned
                iterator.next().asLong(), // sender_id
                iterator.next().asLong(), // receiver_id
                iterator.next().asLong(), // manager_id
                iterator.next().asLong(), // baker_id
                iterator.next().asText(), // data
                iterator.next().asText(), // parameters
                iterator.next().asText(), // storage
                iterator.next().asText(), // big_map_diff
                iterator.next().asText(), // errors
                iterator.next().asDouble(), // days_destroyed
                iterator.next().asText(), // sender
                iterator.next().asText(), // receiver
                iterator.next().asText(), // creator
                iterator.next().asText(), // baker
                iterator.next().asText(), // block
                iterator.next().asText()); // entrypoint
    }

}