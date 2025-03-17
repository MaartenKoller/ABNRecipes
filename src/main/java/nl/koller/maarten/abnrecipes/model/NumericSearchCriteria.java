package nl.koller.maarten.abnrecipes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NumericSearchCriteria {
    public enum ComparisonType {
        EXACT,      // ==
        MINIMUM,    // >=
        MAXIMUM,    // <=
        GREATER,    // >
        LESS        // <
    }

    private Integer value;
    private ComparisonType comparisonType;
}
