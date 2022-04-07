package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;

public class ProposalTableDeserializer extends JsonDeserializer<Proposal> {
    @Override
    public Proposal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Iterator<JsonNode> iterator = node.elements();
        return new Proposal(
                iterator.next().asLong(), // row_id
                iterator.next().asText(), // hash
                iterator.next().asLong(), // height
                iterator.next().asText(), // time
                iterator.next().asLong(), // source_id
                iterator.next().asLong(), // op_id
                iterator.next().asLong(), // election_id
                iterator.next().asLong(), // voting_period
                iterator.next().asLong(), // rolls
                iterator.next().asLong(), // voters
                iterator.next().asText(), // source
                iterator.next().asText()  // op
        );
    }
}

    