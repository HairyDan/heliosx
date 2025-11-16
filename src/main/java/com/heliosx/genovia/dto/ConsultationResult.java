package com.heliosx.genovia.dto;

import lombok.Data;

@Data
public class ConsultationResult {

    private boolean eligible;
    private String reason;

    public ConsultationResult() {
    }

    public ConsultationResult(boolean eligible, String reason) {
        this.eligible = eligible;
        this.reason = reason;
    }

}
