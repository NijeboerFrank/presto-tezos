package nl.utwente.presto.tezos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Pair<T, U> {
    public final T first;
    public final U second;

    @JsonCreator
    public Pair(@JsonProperty("first") T first, @JsonProperty("second") U second) {
        this.second = second;
        this.first = first;
    }

    @JsonProperty
    public T getFirst() {
        return first;
    }

    @JsonProperty
    public U getSecond() {
        return second;
    }
}

