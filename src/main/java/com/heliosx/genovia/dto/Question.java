package com.heliosx.genovia.dto;

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
    @Nullable
    private EligibilityRule rule;

}
