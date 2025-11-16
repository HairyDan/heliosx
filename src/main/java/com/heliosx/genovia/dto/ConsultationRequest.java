package com.heliosx.genovia.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConsultationRequest {

    private List<Answer> answers;

    public ConsultationRequest() {
    }

    public ConsultationRequest(List<Answer> answers) {
        this.answers = answers;
    }


}
