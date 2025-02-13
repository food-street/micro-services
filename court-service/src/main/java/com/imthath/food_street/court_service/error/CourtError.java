package com.imthath.food_street.court_service.error;

import com.imthath.utils.guardrail.CommonError;
import lombok.Getter;

@Getter
public enum CourtError implements CommonError {
    COURT_NOT_FOUND(901);

    private final int code;

    CourtError(int code) {
        this.code = code;
    }
}
