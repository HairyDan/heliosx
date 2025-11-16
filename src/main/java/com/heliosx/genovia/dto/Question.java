package com.heliosx.genovia.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Question {

    String id;
    String question;
    String type;
    boolean required;

    // For multiple choice answers, maps to the options
    @Nullable
    List<String> options;
    // Internal rule that immediately disqualifies
    @Nullable
    @JsonIgnore
    private EligibilityRule rule;

}
