package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;

public class ContractTableDeserializer extends JsonDeserializer<Contract> {
    @Override
    public Contract deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Iterator<JsonNode> iterator = node.elements();
        return new Contract(
                iterator.next().asLong(), // row_id
                iterator.next().asText(), // address
                iterator.next().asLong(), // account_id
                iterator.next().asLong(), // creator_id
                iterator.next().asLong(), // first_seen
                iterator.next().asLong(), // last_seen
                iterator.next().asLong(), // storage_size
                iterator.next().asLong(), // storage_paid
                iterator.next().asText(), // script
                iterator.next().asText(), // storage
                iterator.next().asText(), // iface_hash
                iterator.next().asText(), // code_hash
                iterator.next().asText(), // storage_hash
                iterator.next().asText(), // call_stats
                iterator.next().asText(), // features
                iterator.next().asText(), // interfacesa
                iterator.next().asText() // creator
        );
    }
}
