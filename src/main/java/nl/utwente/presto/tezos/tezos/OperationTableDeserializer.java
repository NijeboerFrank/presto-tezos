package nl.utwente.presto.tezos.tezos;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

public class OperationTableDeserializer extends JsonDeserializer<Operation> {
    @Override
    public Operation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Iterator<JsonNode> iterator = node.elements();
        return new Operation(
                iterator.next().asLong(), // id
                iterator.next().asText(), // hash
                iterator.next().asText(), // type
                iterator.next().asText(), // block
                iterator.next().asText()); // time
        // TODO for operation; ingoring quite some fields, not sure if that is ok
    }

}