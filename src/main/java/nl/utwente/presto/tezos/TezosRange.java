package nl.utwente.presto.tezos;

import com.facebook.presto.common.predicate.Marker;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TezosRange {
    private final String column;
    private final long start;
    private final long end;

    public static TezosRange fromMarkers(String column, Marker low, Marker high) {
        long start;
        long end;
        if (low.isLowerUnbounded()) {
            start = 0L;
        } else if (low.getBound() == Marker.Bound.EXACTLY) {
            start = (long) low.getValue();
        } else if (low.getBound() == Marker.Bound.ABOVE) {
            start = (long) low.getValue() + 1L;
        } else {
            throw new IllegalArgumentException("Low bound cannot be BELOW");
        }

        if (high.isUpperUnbounded()) {
            end = -1L;
        } else if (high.getBound() == Marker.Bound.EXACTLY) {
            end = (long) high.getValue();
        } else if (high.getBound() == Marker.Bound.BELOW) {
            end = (long) high.getValue() - 1L;
        } else {
            throw new IllegalArgumentException("High bound cannot be ABOVE");
        }

        if (start > end && end != -1L) {
            throw new IllegalArgumentException("Low bound is greater than high bound");
        }

        return new TezosRange(column, start, end);
    }

    @JsonCreator
    public TezosRange(
            @JsonProperty("column") String column,
            @JsonProperty("start") long start,
            @JsonProperty("end") long end
    ) {
        this.column = column;
        this.start = start;
        this.end = end;
    }

    @JsonProperty
    public long getStart() {
        return start;
    }

    @JsonProperty
    public long getEnd() {
        return end;
    }

    @JsonProperty
    public String getColumn() {
        return column;
    }
}
