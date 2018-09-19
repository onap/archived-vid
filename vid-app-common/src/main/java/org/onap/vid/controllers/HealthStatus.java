package org.onap.vid.controllers;

import org.springframework.http.HttpStatus;

/**
 * Model for JSON response with health-check results.
 */
public final class HealthStatus {

    private final int statusCode;
    private final String detailedMsg;
    private final String date;

    public HealthStatus(HttpStatus code, String date, String detailedMsg) {
        this.statusCode = code.value();
        this.detailedMsg = detailedMsg;
        this.date = date;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDetailedMsg() {
        return detailedMsg;
    }

    public String getDate() {
        return date;
    }
}
