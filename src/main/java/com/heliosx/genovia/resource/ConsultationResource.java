package com.heliosx.genovia.resource;

import com.heliosx.genovia.dto.ConsultationRequest;
import com.heliosx.genovia.dto.ConsultationResult;
import com.heliosx.genovia.dto.Question;
import com.heliosx.genovia.service.ConsultationService;
import com.heliosx.genovia.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConsultationResource {

    private final ConsultationService consultationService;
    private final QuestionService questionService;

    @Autowired
    public ConsultationResource(ConsultationService consultationService, QuestionService questionService) {
        this.consultationService = consultationService;
        this.questionService = questionService;
    }

    @GetMapping("/consultation/questions")
    public List<Question> getQuestions() {
        return questionService.getQuestions();
    }

    @PostMapping("/consultation/evaluate")
    public ConsultationResult evaluate(@RequestBody ConsultationRequest request) {
        // here we just respond with the preliminary evaluation, finished service would also save the answer for a doctor's follow-up
        return consultationService.evaluate(request);
    }
}
