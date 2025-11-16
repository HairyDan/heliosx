package com.heliosx.genovia.service;

import com.heliosx.genovia.dto.EligibilityRule;
import com.heliosx.genovia.dto.Question;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.heliosx.genovia.dto.EligibilityRule.EligibilityOperator.*;

@Service
@Getter
public class QuestionService {

    List<Question> questions;

    // In a real system this would probably hit a questionRepository stored in a DB where questions could be set up without needing a redeploy
    public QuestionService() {

        questions = List.of(
                // disqualify if age < 18
                Question.of(
                        "age",
                        "How old are you?",
                        "number",
                        true,
                        null,
                        new EligibilityRule(
                                LT,
                                "18",
                                "Patient under 18"
                        )
                ),

                // disqualify if they’ve had an adverse reaction
                Question.of(
                        "adverse_reaction",
                        "Have you had an adverse reaction to pears or pear-related substances",
                        "boolean",
                        true,
                        null,
                        new EligibilityRule(
                                BOOLEAN_TRUE,
                                null,
                                "Previous adverse reaction to pears"
                        )
                ),

                // and if currently pear overdosing
                Question.of(
                        "current_symptoms",
                        "Do you currently have symptoms of pear overdose (high pear levels, pear-based vision, thoughts of pears)?",
                        "boolean",
                        true,
                        null,
                        new EligibilityRule(
                                BOOLEAN_TRUE,
                                null,
                                "Current pear overdose symptoms"
                        )
                ),

                Question.of(
                        "other_conditions",
                        "Do you have any other medical conditions we should be aware of?",
                        "string",
                        false,
                        null,
                        null
                ),

                Question.of(
                        "pear_intake_frequency",
                        "How often do you typically consume pears?",
                        "choice",
                        true,
                        List.of("Never", "Once a month", "Once a week", "Several times a week", "Daily"),
                        null
                ),

                Question.of(
                        "preferred_pear_form",
                        "What is your preferred form of pear consumption?",
                        "choice",
                        false,
                        List.of("Fresh pears", "Pear juice", "Pear jam", "Pear Powder"),
                        null
                )
        );


    }
}
