package com.heliosx.genovia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EligibilityRule {

    public enum EligibilityOperator {
        LT,
        GT,
        BOOLEAN_TRUE,
        BOOLEAN_FALSE
    }

    private EligibilityOperator operator;

    //"18" for age, "true" for boolean checks
    private String comparisonValue;

    // message if this rule fails
    private String failReason;
}
