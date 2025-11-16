package com.heliosx.genovia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heliosx.genovia.dto.*;
import com.heliosx.genovia.exception.BadDataException;
import com.heliosx.genovia.exception.MissingAnswerException;
import com.heliosx.genovia.exception.NoAnswersException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsultationService {

    private final QuestionService questionService;
    private final ObjectMapper objectMapper;

    public ConsultationService(QuestionService questionService, ObjectMapper objectMapper) {
        this.questionService = questionService;
        this.objectMapper = objectMapper;
    }

    public ConsultationResult evaluate(ConsultationRequest request) {
        List<Answer> answers = request.getAnswers();
        if (answers == null || answers.isEmpty()) {
            throw new NoAnswersException("No answers provided");
        }

        Map<String, String> answerMap = toAnswerMap(answers);

        validateRequiredQuestions(answerMap);
        validateAnswerTypes(answerMap);

        for (Question q : questionService.getQuestions()) {
            EligibilityRule rule = q.getRule();
            if (rule == null) {
                continue;
            }
            String rawValue = answerMap.get(q.getId());
            if (rawValue == null) {
                continue;
            }

            if (ruleTriggered(q, rule, rawValue)) {
                return new ConsultationResult(false, rule.getFailReason());
            }
        }

        return new ConsultationResult(true, "No answers are immediately disqualifying, a doctor will review and reply soon :) ");
    }

    private Map<String, String> toAnswerMap(List<Answer> answers) {
        Map<String, String> map = new HashMap<>();
        for (Answer answer : answers) {
            map.put(answer.getQuestionId(), answer.getAnswer());
        }
        return map;
    }

    private void validateRequiredQuestions(Map<String, String> answerMap) {
        for (Question q : questionService.getQuestions()) {
            if (q.isRequired() && !answerMap.containsKey(q.getId())) {
                throw new MissingAnswerException("Missing required answer for question: " + q.getId());
            }
        }
    }

    private void validateAnswerTypes(Map<String, String> answerMap) {
        for (Question q : questionService.getQuestions()) {
            String value = answerMap.get(q.getId());
            if (value == null) {
                continue;
            }

            String type = q.getType();
            switch (type) {
                case "number" -> {
                    parseInteger(value, q.getId());
                }
                case "boolean" -> {
                    parseBoolean(value, q.getId());
                }
                default -> {
                }
            }
        }
    }

    private boolean ruleTriggered(Question q, EligibilityRule rule, String rawValue) {
        String type = q.getType();
        EligibilityRule.EligibilityOperator op = rule.getOperator();

        if ("number".equals(type)) {
            int numericValue = parseInteger(rawValue, q.getId());
            int comparison = parseInteger(rule.getComparisonValue(), "rule-" + q.getId());

            return switch (op) {
                case LT -> numericValue < comparison;
                case GT -> numericValue > comparison;
                default -> false;
            };
        }

        if ("boolean".equals(type)) {
            boolean value = parseBoolean(rawValue, q.getId());

            return switch (op) {
                case BOOLEAN_TRUE  -> value;
                case BOOLEAN_FALSE -> !value;
                default -> false;
            };
        }

        // string / choice etc. we can add extra validation here for more complex consultations

        return false;
    }

    private int parseInteger(String value, String questionId) {
        try {
            return objectMapper.readValue(value.trim(), Integer.class);
        } catch (JsonProcessingException e) {
            throw new BadDataException("Question '" + questionId + "' must be a number");
        }
    }

    private boolean parseBoolean(String value, String questionId) {
        try {
            return objectMapper.readValue(value.trim(), Boolean.class);
        } catch (JsonProcessingException e) {
            throw new BadDataException(
                    "Question '" + questionId + "' must be a boolean (true/false)"
            );
        }
    }
}
